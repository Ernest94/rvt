import React from 'react';
import axios from 'axios';
import { withRouter,Link } from 'react-router-dom'
import {Select, Input, MenuItem, InputLabel, TextField} from '@material-ui/core'

import './search.css';
import { config } from '../MISC/constants.js';
import Util from '../MISC/Utils';
import Permissions from '../MISC/Permissions.js'

class Search extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            locations: [],
            selectedLocationsIds:[],
            selectedLocations: [],
            criteria: "",

            roles: [],
            selectedRoleId:0,

            users: [],
            pageLoading: true,
            buttonDisabled: false,
        };
    }

    static hasAccess() {
        return Permissions.canSearch();
    }

    componentDidMount() {
        this.getLocationsAndRoles() 
    }

    getLocationsAndRoles() {
        axios.all([ axios.get(config.url.API_URL + '/webapi/roles'),
                    axios.get(config.url.API_URL + '/webapi/locations')])
       
            .then(axios.spread((roleResponse, locationResponse) => {
                this.setState({
                    roles: roleResponse.data,
                    locations: locationResponse.data,
                    pageLoading: true
                });
                this.setLocationAndRole();
            }))
            .catch(() => {
                this.setState({
                    pageLoading: false
                });
            })
    }

    setLocationAndRole()
    {
        let userLocations = JSON.parse(sessionStorage.getItem("userLocation"));
        var userLocationsIds = userLocations.map(element => element.id)
        const roleName = "Trainee";
        let role = this.state.roles.find(element => element.name === roleName);
        this.setState({
            pageLoading: true,
            selectedLocations:userLocations,
            selectedLocationsIds: userLocationsIds,
            selectedRoleId: role.id,
        });
        if (sessionStorage.getItem("userRole")==="Office") {
            this.setState({
                locations:userLocations});
            }
        this.handleSubmit();
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value
        }, () => this.handleSubmit());
    }

    createSearchJson() {
        var locations = [];
        var i;
        for (i=0;i<this.state.selectedLocationsIds.length;i++) {
            locations.push(
                {id:this.state.selectedLocationsIds[i]}
            )
        }
        return {
            locations: locations,
            role: {id:parseInt(this.state.selectedRoleId)},
            criteria: this.state.criteria
        }
    }

    handleSubmit() {
        axios.post(config.url.API_URL + "/webapi/users/search", this.createSearchJson())
            .then(response => {
                this.setState({pageLoading: false, errors: null});
                this.handleSearchReponse(response.data);
                this.render();
            })
            .catch((error) => {
                console.log("an error occurred " + error);
                Util.setErrors({login: ["Mislukt om zoekactie uit te voeren."]});
                });
    }

    handleSearchReponse(data)
    {
        this.setState({
            users: data.userSearch
        });
    }

    render() {
        const {roles, locations, users, pageLoading, selectedLocationsIds} = this.state;
        if (pageLoading) return (<span className="error-message-center">Laden...</span>)

        if (roles === null || locations === null) {
            return (<span className="error-message-center">Mislukt om pagina te laden.</span>)
        }
        const rolesOptions = (sessionStorage.getItem("userRole")==="Docent"||
            sessionStorage.getItem("userRole")==="Office"||
            sessionStorage.getItem("userRole")==="Sales")?
                roles.filter(role => role.name==="Trainee").map((role) => {
                    return (
                        <MenuItem key={role.id} value={role.id}>{role.name}</MenuItem>
                    )
                }):roles.map((role) => {
                    return (
                        <MenuItem key={role.id} value={role.id}>{role.name}</MenuItem>
                    )
                });

        const emptyUsers = users.length === 0;

        var userDisplay = users.map((user) => {
            var userLocationsColumn = '';
            var i;
            for (i=0;i<user.currentUserLocations.length;i++){
                userLocationsColumn+= user.currentUserLocations[i].name + ((i>=0&&i+1<user.currentUserLocations.length) ? ", ":"")
                }

            return (
                <tr className="row searchResult" key={user.id} onClick={(e) => {this.props.history.push('/dossier/' + user.id)} } >
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.name}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.email}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.role.name}
                    </td>
                    <td className="p-2 col-sm  align-middle">
                        {userLocationsColumn}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.dateActive}
                    </td>
                </tr >
            )
        });


        return (
            <div className="container">

                <h2 className="text-center">Zoeken naar gebruikers</h2>

                    <div className="search-bar row d-flex">
                        <div className="m-auto col-1">
                        <InputLabel className="m-1" htmlFor="role">
                            <small>Rol:</small>
                        </InputLabel>
                            <Select name="selectedRoleId" id="selectedRoleId"
                                value={this.state.selectedRoleId}
                                onChange={this.handleFormChange}
                                >
                                {rolesOptions}
                            </Select>
                          </div>
                          <div className="m-auto col-3">
                                <InputLabel className="m-1" shrink={false} id="location-label" >
                                   <small> Locatie: </small>
                                </InputLabel>
                                <Select
                                    className="m-1 text-black locationInput search"
                                    labelId="location-label"
                                    id="selectedLocationsIds"
                                    name="selectedLocationsIds" 
                                    multiple
                                    value={selectedLocationsIds}
                                    onChange={this.handleFormChange}
                                    //the MenuProps below are needed to stop the dropdown jumping around when selecting
                                    MenuProps={{
                                        variant: "menu",
                                        getContentAnchorEl: null}
                                    }
                                    input={<Input id="select-location" />}
                                    >
                                    {locations.map((location) => (
                                        <MenuItem key={location.id} value={location.id}>
                                            {location.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                          </div> 
                          <div className="m-auto col-3">
                            <InputLabel className="m-1" htmlFor="criteria">
                                <small>Zoek een gebruiker: </small>
                            </InputLabel>
                            <TextField id="criteria" type="criteria" name="criteria" onChange={this.handleFormChange} />
                          </div>
                        <div className="m-auto col-1">
                        <Link className="btn btn-outline-secondary m-2" to={"/menu"}>
                            Annuleer
                        </Link>
                        </div>
                    </div>

                <div className="row justify-content-center">
                    <ul className="errors text-center" hidden={!emptyUsers}>Geen overeenkomende gebruikers gevonden</ul>
                </div>

                <div className="text-center" hidden={emptyUsers}>
                    <table className="w-100 mx-auto">
                        <thead>
                            <tr className="row" key={0}>
                                <th className="p-2 col-sm text-nowrap align-middle">
                                    Naam
                                    </th>
                                <th className="p-2 col-sm text-nowrap align-middle">
                                    Email
                                    </th>
                                <th className="p-2 col-sm text-nowrap align-middle">
                                    Rol
                                    </th>
                                <th className="p-2 col-sm text-nowrap align-middle">
                                    Locatie
                                    </th>
                                <th className="p-2 col-sm text-nowrap align-middle">
                                    Startdatum
                                    </th>
                            </tr>
                        </thead>
                        <tbody>
                        {userDisplay}
                        </tbody>
                    </table>
                </div >
            </div>
        )
    }
}

export default withRouter(Search);