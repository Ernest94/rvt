import React from 'react';
import axios from 'axios';

import {config} from '../constants.js';
import './search.css';

import { withRouter } from 'react-router-dom'
import Util from '../Utils';
import Permissions from '../permissions.js'
import {Select, Input, MenuItem, InputLabel} from '@material-ui/core'

class Search extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            locations: [],
            selectedLocations: [],
            roles: [],
            role: "",
            criteria: "",
            users: [],
            loading: false,
            roleDisplayName: "",
            buttonDisabled: false,
        };
    }

    static hasAccess() {
        return Permissions.canSearch();
    }

    componentDidMount() {
        this.setState({ pageLoading: true });
        this.getLocationsAndRoles()
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        console.log(value)
        this.setState({
           [name]: value
        });
    }


    setLocationAndRole()
    {
        let locationName = JSON.parse(sessionStorage.getItem("userLocation"));
        console.log(locationName);
        console.log(this.state.locations);

        const roleName = "Trainee";

        let role = this.state.roles.find(element => element.name === roleName);
        let location = this.state.locations.find(element => element.name === locationName[0].name);
        // console.log(location)
        this.setState({
            loading: true,
            selectedLocations: [location],
            // selectedLocations: [location],
            role: role,
            roleDisplayName: role.id
        });

        axios.post(config.url.API_URL + "/webapi/user/search", this.createSearchJson())

            .then(response => {
                this.setState({ loading: false, errors: null });

                this.handleSearchReponse(response.data);
                this.render();
            })
            .catch((error) => {
                console.log("an error occorured " + error);
                this.setState({ loading: false });
            });
    }

    findlocation(location) {
        return location;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true});
        var errors = null
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/search", this.createSearchJson())
                .then(response => {
                    this.setState({loading: false, errors: null});

                    this.handleSearchReponse(response.data);
                    this.render();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    Util.setErrors({login: ["Mislukt om zoekactie uit te voeren."]});
                    this.setState({loading: false});
                });
        }
        else {
            Util.setErrors(errors);
            this.setState({loading: false});
        }
    }

    handleSearchReponse(data)
    {
        this.setState({
            users: data.userSearch
        });
    }

    getLocationsAndRoles() {
        axios.get(config.url.API_URL + '/webapi/user/roles')
            .then(response => {
                console.log(response.data)
                this.setState({
                    roles: response.data.roles,
                    locations: response.data.locations,
                    pageLoading: false
                });
                this.setLocationAndRole();
            })
            .catch(() => {
                this.setState({
                    roles: null,
                    locations: null,
                    pageLoading: false
                });
            })
    }

    createSearchJson() {
        return {
            locations: this.state.selectedLocations,
            role: this.state.role,
            criteria: this.state.criteria
        }
}

    onChangeRole = (e) => {
        this.setState({
            role: this.state.roles.find(role => role.id === parseInt(e.target.value)),
        });
    }

    onChangeRole = (e) => {
        var selectedRole = this.state.roles.find(role => role.id === parseInt(e.target.value));

        this.setState({
            role: selectedRole,
            roleDisplayName: e.target.value
        });
    }

    render() {
        const {roles, locations, users, pageLoading, loading} = this.state;
        if (pageLoading) return (<span className="error-message-center">Laden...</span>)

        if (roles === null || locations === null) {
            return (<span className="error-message-center">Mislukt om pagina te laden.</span>)
        }
        const rolesOptions = roles.map((role) => {
            return (
                <option key={role.id} value={role.id}>{role.name}</option>
            )
        });
        var userDisplay = users.map((user) => {
            return (
                <tr className="row searchResult" key={user.id} onClick={(e) => {   this.props.history.push('/dossier/' + user.id)} } >
                    {/* <td className="p-2 col-sm text-nowrap align-middle">
                        {user.name}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.email}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.role.name}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.location.name}
                    </td>
                    <td className="p-2 col-sm text-nowrap align-middle">
                        {user.dateActive}
                    </td> */}
                </tr >
            )
        });


        return (
            <div className="container">

                <h2 className="text-center">Zoeken naar gebruikers</h2>

                <div className="row"> 
                    <ul className="errors">{this.state.errors}</ul>
                </div>

                    <form onSubmit={this.handleSubmit}>

                        <div className="search-bar row d-flex">

                          <div className="m-auto col-2">
                            <label className="m-1" htmlFor="role">Rol:</label>
                            <select name="role" id="role"

                                value={this.state.roleDisplayName}
                                onChange={this.onChangeRole}
                                >
                                {rolesOptions}
                            </select>
                          </div>
                          <div className="m-auto col-4">
                                <InputLabel className="m-1 text-black" shrink={false} id="location-label" >Locatie: 
                                <Select
                                    className="m-1 text-black"
                                    labelId="location-label"
                                    id="location"
                                    name="selectedLocations" 
                                    multiple
                                    value={this.state.selectedLocations}
                                    onChange={this.handleFormChange}
                                    //the MenuProps below are needed to stop the dropdown jumping around when selecting
                                    MenuProps={{
                                        variant: "menu",
                                        getContentAnchorEl: null}
                                    }
                                    input={<Input id="select-location" />}
                                    >
                                    {locations.map((location) => (
                                        <MenuItem key={location.id} value={location}>
                                            {location.name}
                                        </MenuItem>
                                    ))}
                                </Select>
                                </InputLabel>
                          </div> 
                          <div className="m-auto col-4">
                            <label className="m-1" htmlFor="criteria">Zoek Criteria:</label>
                            <input id="criteria" type="criteria" name="criteria" onChange={this.handleFormChange} />
                          </div>
                        <div className="m-auto col-2">
                            <button className="btn btn-outline-secondary m-2"
                                disabled={loading}
                                type="submit">
                                {(loading)?"Laden...": "Zoek"}
                            </button>
                        </div>
                        </div>

                    </form>

                <div className="text-center">
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