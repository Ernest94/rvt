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
            loading: false,
            roleDisplayName: "",
            locationDisplayName: "",
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
        axios.post(config.url.API_URL + "/webapi/user/search", this.createSearchJson())

            .then(response => {
                this.setState({loading: false, errors: null});
                
                this.handleSearchReponse(response.data);
                this.render();
            })
            .catch((error) => {
                console.log("an error occorured " + error);
                this.fakeHandleSearchReponse();
                const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({
                    loading: false,
                    errors: this.props.setErrors(custErr)
                });
            });
    }

    handleSearchReponse(data)
    {
        this.setState({
            users: [{ id: 1, name: "Niels", email: "niels.vanrijn@hotmail.com", role: "Trainee", location: "Utrecht" }, { id: 2, name: "Quinten", email: "quinten@hotmail.com", role: "Trainee", location: "Utrecht" }]//data.date.users;
        });
    }

    fakeHandleSearchReponse() {
        this.setState({
            users: [{ id: 1, name: "Jeroen", email: "jeroen@educom.nu", role: "Docent", location: "Utrecht" }]//data.date.users;
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
                    roles: [{id: 1, name: "Trainee"}, {id: 2, name: "Docent"}],
                    locations:  [{id: 1, name: "Utrecht"}],
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

    onChangeRole = (e) => {
        console.log("check");
        var selectedRole = this.state.roles.find(role => role.id === parseInt(e.target.value));
        
        this.setState({
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
                <tr onClick={(e) => {this.props.handleDossierRequest(e, user.id)}} >
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
                        <div className="w-100 mx-auto align-middle text-center"> 
                            <label className="mr-2 p-2 align-middle" htmlFor="role">Rol:</label>
                            <select className="mr-5 p-2 align-middle" name="role" id="role"
                                value={this.state.roleDisplayName}
                                onChange={this.onChangeRole}
                                required>

                                <option hidden value=''>Rol</option>
                                {rolesOptions}
                            </select>
                            <label className="mr-2 p-2 align-middle" htmlFor="location">Locatie:</label>
                            <select className="mr-5 p-2 align-middle" name="location" id="location"
                                value={this.state.locationDisplayName}
                                onChange={this.onChangeLocation}
                                required>

                                <option hidden value=''>Locatie</option>
                                {locationOptions}
                            </select>
                            <label className="mr-2 p-2 align-middle" htmlFor="criteria">Zoek Criteria:</label>
                            <input className="mr-5 p-2 align-middle" id="criteria" type="criteria" name="criteria" onChange={this.handleFormChange} />
                        </div>
                        <div className="text-center"> 
                            {(this.state.loading) ? <button className="w-30 mx-auto btn btn-primary mt-3" type="submit" disabled> Laden...</button> :
                                <button className="w-30 mx-auto btn btn-primary mt-3" type="submit">Zoek</button>}
                        </div>
                    </form>                  
                </div >

                <div className="text-center">
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
                        <tbody>
                        {userDisplay}
                        </tbody>
                    </table>
                </div >
            </div>
        )
    }
}

export default Search;