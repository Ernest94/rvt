import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link,withRouter } from 'react-router-dom';
import {Select, Input, MenuItem, TextField} from '@material-ui/core'

import constraints from '../constraints/addUserConstraints';
import {config} from './constants';
import Utils from './Utils.js';
import Permissions from './permissions.js';
// import BundleTable from './dossier.js';



class AddUser extends React.Component {

    static hasAccess() {
        return Permissions.canAddUser();
    }

    constructor(props) {
        super(props);
        this.state = {

            role: null,
            roleId: ((sessionStorage.getItem("userRole")==="Docent")?"3":""),
            selectedLocationsIds:[],
            name: "",
            email: "",
            password: "",
            startDate: "",
            isTrainee: null,

            roles : [],
            locations: [],
            errors: null,
            pageLoading: true,
        };
    }

    componentDidMount() {
        Utils.dateValidation();
        this.setState({pageLoading: true});
        this.getLocationsAndRoles()
    }

    getLocationsAndRoles() {
        axios.get(config.url.API_URL + '/webapi/user/roles')
            .then(response => {
                    const roleName = "Trainee";
                    let role = response.data.roles.find(element => element.name === roleName);
                    this.setState({
                        roles: response.data.roles,
                        locations: response.data.locations,
                        pageLoading: false,
                        role: role,
                        roleId: role.id
                    });
                })
            .catch(() => {
                this.setState({
                    errors: Utils.setErrors({connection: ["Momenteel kan er geen gebruiker worden toegevoegd."]}),
                    pageLoading: false
                });
        })

    }

    compareLocations(first, second) {
        first.map(o1 => {
            second.map(o2 => {
                if (o1.id === o2.id) { return true; }
            });
        });
        return false;
    }

    createUserJson() {
        const {name, email, password, roleId, startDate } = this.state;
        var {selectedLocationsIds} = this.state;
        if(Number.isInteger(this.state.selectedLocationsIds)){
            selectedLocationsIds = [this.state.selectedLocationsIds];
        }
        var locations = [];
        var i;
        for (i=0;i<selectedLocationsIds.length;i++) {
            locations.push(
                {id:selectedLocationsIds[i]}
            )
        }
        console.log(locations);
                return {
            user:{
                name: name,
                email: email,
                password:password,
                role: {id:parseInt(roleId)},
                startDate: startDate
            },
            locations: locations
        }
    }  

    handleSubmit = (event) => {
        event.preventDefault();
        var errors = validate(this.state, constraints);

        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/create", this.createUserJson())
                .then(response => {
                    console.log(response);
                    this.props.history.push('/dossier/' + response.data.id);
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    this.setState({
                        errors: Utils.setErrors({addUser: ["Mislukt om een gebruiker toe te voegen. Mogelijk bestaat er al een gebruiker met dit e-mailadres."]})
                    });
                });
        }
        else {
            this.setState({
                submitButtonDisabled: false,
                errors: Utils.setErrors(errors)
            });
        }
    }

    handleFormChange = (e) => {       
        const {name, value} = e.target;
/*         if (name==="roleId"){
            this.setState({
                selectedLocationsIds:[]
             });
            
        } */
        this.setState({
           [name]: value
        });
    }

    render() {
        const pageLoading = this.state.pageLoading;
        const errorsList = !!this.state.errors?<ul className="">{this.state.errors}</ul>: <span></span>;
        if (pageLoading) return <div className="error-message-center"><span> Laden...</span></div>;
        const userRole = sessionStorage.getItem("userRole");
        const userLocation = JSON.parse(sessionStorage.getItem("userLocation"));
        const {locations,roles,selectedLocationsIds} = this.state

        let locationsOptions;
        const rolesOptions = roles.map((role) => {
            return (
                <option key={role.id} value={role.id}>{role.name}</option>
            )
        });
            
        if (userRole === "admin") {
            locationsOptions = locations.map((loc) => {
                return (
                    <MenuItem key={loc.id} value={loc.id}>{loc.name}</MenuItem >
                )
            });
        }


        if (userRole === "admin") {
            locationsOptions = locations.map((loc) => {
                return (
                    <MenuItem key={loc.id} value={loc.id}>{loc.name}</MenuItem >
                )
            });
        }
        else {
            locationsOptions = userLocation.map((loc) => {
                return (
                    <MenuItem key={loc.id} value={loc.id}>{loc.name}</MenuItem >
                )
            });
        }

        return (
            <div className="container main-container">

                <h2 className="text-center">Gebruiker toevoegen</h2>

                <div className="text-center text-danger">{errorsList}</div>

                <form onSubmit={this.handleSubmit} className="container col-lg-8">

                    <div className="input row dossier mt-2">
                        <label className="label col-sm col-form-label" htmlFor="roleId">Rol:</label>
                        <Select 
                            className="form-control col-sm-9" 
                            name="roleId" 
                            id="roleId" 
                            disabled={!(userRole === "Admin")}
                            onChange={this.handleFormChange}
                            value={this.state.roleId}
                            required>
                                <option hidden value=''></option>
                                {rolesOptions}
                        </Select>
                    </div>

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="location">Locatie:</label>
                        
                        {(this.state.roleId===3)?
                            <Select
                            className="text-black form-control col-sm-9"
                            id="selectedLocationsIds"
                            name="selectedLocationsIds" 
                            value={selectedLocationsIds}
                            onChange={this.handleFormChange}
                            //the MenuProps below are needed to stop the dropdown jumping around when selecting
                            MenuProps={{
                                variant: "menu",
                                getContentAnchorEl: null}
                            }
                            input={<Input id="selectedLocationsIds" />}>
                                {locationsOptions}
                        </Select>:
                        <Select
                            className="text-black form-control col-sm-9"
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
                            input={<Input id="selectedLocationsIds" />}>
                                {locationsOptions}
                        </Select>}
                    </div>
            
                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="name">Naam:</label>
                        <TextField className="form-control col-sm-9" id="name" type="name" name="name"
                            onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="email">Email:</label>
                        <TextField className="form-control col-sm-9" id="email" type="email" name="email"
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="password">Wachtwoord:</label>
                        <TextField className="form-control col-sm-9" id="password" type="password" name="password"
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier" >
                        <label className="label col-sm col-form-label" htmlFor="startDate">Startdatum:</label>
                        <TextField className="form-control col-sm-9" id="startDate" type="date" name="startDate" 
                            onChange={this.handleFormChange}/>
                    </div>
                            
                    {/* leaving this here for when a bundleFeature like this is needed in the future
                    <div className="input row dossier" hidden={!traineeDossier}>
                        <label className="label col col-form-label" htmlFor="bundles">Bundels:</label>
                        <BundleTable 
                            bundlesTrainee={this.state.bundlesTrainee} 
                            bundles={this.state.bundles}
                            removeBundle={this.removeBundle.bind(this)}
                            handleBundleChange={this.handleBundleChange.bind(this)} 
                            addBundle ={this.addBundle.bind(this)} 
                        />
                    </div> */}

                    <div className="row mt-2">
                        <div className="buttons">
                            <button type="submit" className="btn btn-danger btn-block">Voeg toe</button>
                        </div>
                    </div>

                    <div className="row">
                        <div className="buttons">
                            <Link to="/settings"  className="btn btn-danger btn-block">Annuleer</Link>
                        </div>
                    </div>
                </form>
            </div>
        )
    }
}

export default withRouter(AddUser);