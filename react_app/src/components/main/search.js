import React from 'react';
import axios from 'axios';

import {config} from '../constants';

class Search extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            locations: [],
            location: "",
            roles: [],
            role: "",
            criteria: "",
            users: [],
            loading: false
        };
    }

    componentDidMount() {
        this.setState({ pageLoading: true });
        this.getLocationsAndRoles()
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
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
                    this.fakeHandleSearchReponse();
                    this.setErrors({login: ["Mislukt om zoek actie uit te voeren."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }

    handleSearchReponse(data)
    {
        this.setState({
            users: [{ id: 1, name: "Niels", email: "niels.vanrijn@hotmail.com", role: "Trainee", location: "Utrecht" }, { id: 2, name: "Quinten", email: "quinten@hotmail.com", role: "Trainee", location: "Utrecht" }]//data.date.users;
        });
    }

    fakeHandleSearchReponse() {
        this.setState({
            users: [{ id: 1, name: "Niels", email: "niels.vanrijn@hotmail.com", role: "Trainee", location: "Utrecht" }, { id: 2, name: "Quinten", email: "quinten@hotmail.com", role: "Trainee", location: "Utrecht" }]//data.date.users;
        });
    }

    getLocationsAndRoles() {

        axios.get(config.url.API_URL + '/webapi/user/roles')
            .then(response => {
                this.setState({
                    roles: response.data.roles,
                    locations: response.data.locations,
                    pageLoading: false
                });
            })
            .catch(() => {
                this.setState({
                    roles: null, //[{id: 1, name: "Trainee"}, {id: 2, name: "Docent"}],
                    locations: null, // [{id: 1, name: "Utrecht"}],
                    pageLoading: false
                });
            })
    }
    
    createSearchJson() {
        return {
            Location: this.state.role,
            Role: this.state.location,
            Criteria: this.state.criteria
        }
    }
    
    setErrors = (errors) => {
        const foundErrors = Object.keys(errors).map((key) =>
            <li key={key}>{errors[key][0]}</li>
        );
        this.setState({
           errors: foundErrors 
        });
    }

    onChangeRole = (e) => {
        var selectedRole = this.state.roles.find(role => role.id === parseInt(e.target.value));
        var isTrainee = selectedRole.name === "Trainee";

        this.setState({
            isTrainee: isTrainee,
            role: selectedRole,
            roleDisplayName: e.target.value
        });
    }

    onChangeLocation = (e) => {
        this.setState({
            location: this.state.locations.find(loc => loc.id === parseInt(e.target.value)),
            locationDisplayName: e.target.value
        });
    }

    render() {

        const rolesOptions = this.state.roles.map((role) => {
            return (
                <option key={role.id} value={role.id}>{role.name}</option>
            )
        });
        const locationOptions = this.state.locations.map((location) => {
            return (
                <option key={location.id} value={location.id}>{location.name}</option>
            )
        });
        var userDisplay = this.state.users.map((user) => {
            return (
                <tr onClick={this.props.handleDossierRequest(user.id)} >
                    <td className="p-2 text-nowrap align-middle">
                        {user.name} 
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {user.email}
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {user.role}
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {user.location}
                    </td>
                </tr >
            )
        });


        return (

            <div className="container">
                <div >
                    <ul className="errors">{this.state.errors}</ul>
                    <form onSubmit={this.handleSubmit}>
                        <div className="w-100 mx-auto align-middle"> 
                            <label className="mr-2 p-2 align-middle" htmlFor="role">Rol:</label>
                            <select className="mr-5 p-2 align-middle" name="role" id="role"
                                value={this.props.roleDisplayName}
                                onChange={this.props.onChangeRole}
                                required>

                                <option hidden value=''>Rol</option>
                                {rolesOptions}
                            </select>
                            <label className="mr-2 p-2 align-middle" htmlFor="location">Locatie:</label>
                            <select className="mr-5 p-2 align-middle" name="location" id="location"
                                value={this.props.locationDisplayName}
                                onChange={this.props.onChangeLocation}
                                required>

                                <option hidden value=''>Locatie</option>
                                {locationOptions}
                            </select>
                            <label className="label mr-2 p-2 align-middle" htmlFor="criteria">Zoek Criteria:</label>
                            <input className="form mr-5 p-2 align-middle" id="criteria" type="criteria" name="criteria" onChange={this.handleFormChange} />
                        </div>
                        <div>
                            {(this.state.loading) ? <button className="w-30 mx-auto btn btn-primary mt-3" type="submit" disabled> Laden...</button> :
                                <button className="w-30 mx-auto btn btn-primary mt-3" type="submit">Zoek</button>}
                        </div>
                    </form>                  
                </div >

                <div>
                    <table className="w-100 mx-auto">
                        <thead>
                            <tr>
                                <th className="p-2 text-nowrap align-middle">
                                    Naam
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Email
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Rol
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Locatie
                                    </th>
                            </tr>
                        </thead>
                        {userDisplay}
                    </table>
                </div >
            </div>
        )
    }
}

export default Search;