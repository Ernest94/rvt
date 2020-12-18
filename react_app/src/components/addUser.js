import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link,withRouter } from 'react-router-dom';
import {Select, Input, MenuItem} from '@material-ui/core'

import constraints from '../constraints/addUserConstraints';
import {config} from './constants';
import Utils from './Utils.js';
import Permissions from './permissions.js'

// class LocationSelection extends React.Component {
    
//     constructor(props) {
//         super(props);
//         console.log(this.props.roleDisplayName);
//     }

//     render() {
//         const roleDisplayName = this.props.roleDisplayName;
//         const locations = this.props.locations;
//         const locationsOptions = locations.map((loc) => {
//            return (
//                 <option key={loc.id} value={loc.id}>{loc.name}</option>
//            ) 
//         });
        
//         if (this.props.isTrainee === null) {
//             return null;
//         }
//             return (
//                 <div className="my-2">
//                     <label htmlFor="location">Locatie:</label>
//                     <div>
//                     <select className="m-1" name="location" id="location" 
//                         value={this.props.locationDisplayName} 
//                         onChange={this.props.onChangeLocation}
//                         required>
                        
//                         <option hidden value=''>Locatie</option>
//                         {locationsOptions}
//                     </select>
//                     </div>
//                 </div>
//         )        
//     }
// }


// // export default RoleAndLocation;


// class RoleAndLocation extends React.Component {
    
//     render() {
        
//         const roles = this.props.roles;
//         const locations = this.props.locations;
//         if (roles === null) return <span> Problemen met laden van de pagina. </span>;
//         if (locations === null) return <span> Problemen met laden van de pagina. </span>;

//         const rolesOptions = roles.map((role) => {
//            return (
//                 <option key={role.id} value={role.id}>{role.name}</option>
//            ) 
//         });
        
        
//         if (this.props.currentStep !== 1) {
//             return null;
//         }
        
//         return (
//             <div className="m-3 p-2">

//                 <div className="">
//                     <label htmlFor="role">Rol:</label>
//                     <div>
//                     <select className="m-1" name="role" id="role" 
//                         value={this.props.roleDisplayName} 
//                         onChange={this.props.onChangeRole}
//                         required>
                        
//                         <option hidden value=''></option>
//                         {rolesOptions}
//                     </select>
//                     </div>
//                 </div>
                
//                 <LocationSelection 
//                     locations={this.props.locations}
//                     isTrainee={this.props.isTrainee}
//                     locationDisplayName= {this.props.locationDisplayName}
//                     roleDisplayName = {this.props.roleDisplayName}
//                     onChangeLocation={this.props.onChangeLocation}
//                 /> 
                
//             </div>
//         )
//     }
// }

// class UserInfo extends React.Component {
    
//     componentDidMount() {
//         Utils.dateValidation();
//     }
    
//     render() {
        
//         if (this.props.currentStep !== 2) {
//             return null;
//         }
        
        
//         return (
//             <div className="row">
//                 <div className="col-6 ">
//                     <div className="float-right">
//                         <label className="form-label" htmlFor="name">Naam:</label>
//                         <input className="form-control small-form" id="name" type="text" name="name" value={this.props.name} onChange={this.props.handleFormChange}/>
                        
//                         <label htmlFor="email">Email:</label>
//                         <input className="form-control" id="email" type="email" name="email" value={this.props.email} onChange={this.props.handleFormChange}/>
                    
//                         <label htmlFor="password">Wachtwoord:</label>
//                         <input className="form-control" id="password" type="password" name="password"  value={this.props.password} onChange={this.props.handleFormChange}/>
//                     </div>
//                 </div>
                
//                 <div className="col-6">
//                     <div className="float-left">

//                         <label htmlFor="date">Datum actief:</label>
//                         <input className="form-control" id="date" type="date" name="dateActive" value={this.props.date} onChange={this.props.handleFormChange}/>
                    
//                         <label htmlFor="role">Rol:</label>
//                         <input className="form-control" id="role" type="text" name="role" value={this.props.role.name} disabled/>
                    
//                         <label htmlFor="date">Locatie:</label>
//                         <input className="form-control" id="location" type="text" name="location" value={this.props.location.name} disabled/>
//                     </div>
//                 </div>

                
//             </div>
//         )
//     }
// }

class AddUser extends React.Component {

    static hasAccess() {
        return Permissions.canAddUser();
    }

    constructor(props) {
        super(props);
        this.state = {

            role: null,
            roleId: "",
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
            isDocent: sessionStorage.getItem("userRole") === "docent",
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
                        roleDisplayName: role.id
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

<<<<<<< HEAD
    // onChangeTeacher= (e) => {
    //     this.setState({
    //        teacher: this.state.teachers.find(tea => tea.id === parseInt(e.target.value)),
    //        teacherDisplayName: e.target.value
    //     });
    // }

    _next() {
    //     console.log(this.state.currentStep);
    //     if (!this.state.isTrainee) {
    //         this.setState({teacher: null, teacherDisplayName: ""});
    // }

        if ((this.state.location != null /*|| this.state.teacher*/) && this.state.role != null) {
            this.setState({currentStep: 2, errors: null});
        }
        else {
            Utils.setErrors({roleAndLoc: ["Maak voor alle velden een selectie."]});
        }
        console.log(this.state.currentStep);
    }

    _prev() {
        this.setState({currentStep: 1, errors: null});
    }

    // get previousButton() {
    //     let currentStep = this.state.currentStep;

    //     if (currentStep > 1 /*&&  !this.state.isDocent*/) {
    //         return (
    //             <button
    //                 className="btn btn-primary float-right"
    //                 type="button" onClick={this._prev}>
    //                 Vorige
    //             </button>
    //         )
    //     }
    //     return null;
    // }

    get nextButton() {
        let currentStep = this.state.currentStep;

        if (currentStep <= 1 ) {
            return (
                <button
                    className="btn btn-danger btn-block"
                    type="button" 
                    onClick={this._next}>
                    Volgende
                </button>
            )
        }
        return null;
    }

    get submitButton() {
        const {currentStep, submitButtonDisabled} = this.state;

        if (currentStep === 2) {
            return (
                <div className="">
                    <button className="btn btn-danger btn-block"
                        disabled={submitButtonDisabled}
                        type="submit">
                        {(submitButtonDisabled) ? "Laden..." :"Opslaan"}
                    </button>
                </div>
            )
=======
    createUserJson() {
        const {name, email, password, roleId, selectedLocationsIds, startDate } = this.state;
        var locations = [];
        var i;
        for (i=0;i<selectedLocationsIds.length;i++) {
            locations.push(
                {id:selectedLocationsIds[i]}
            )
        }
        return {
            user:{
                name: name,
                email: email,
                password:password,
                role: {id:parseInt(roleId)},
                startDate: startDate
            },
            locations: locations
>>>>>>> origin/development
        }
    }  

    handleSubmit = (event) => {
        event.preventDefault();
        var errors = validate(this.state, constraints);
        console.log(this.createUserJson());
        console.log();

        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/create", this.createUserJson())
                .then(response => {
                    this.props.history.push('/settings');
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    this.setState({
                        errors: Utils.setErrors({addUser: ["Mislukt om een gebruiker toe te voegen."]})
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
        console.log(name, value)

        this.setState({
           [name]: value
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
        const pageLoading = this.state.pageLoading;
        const errorsList = !!this.state.errors?<ul className="errors">{this.state.errors}</ul>: <span></span>;
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
        else {
            console.log(userLocation);
            locationsOptions = userLocation.map((loc) => {
                return (
                    <MenuItem key={loc.id} value={loc.id}>{loc.name}</MenuItem >
                )
            });
        }

        return (
            <div className="container main-container">

                <h2 className="text-center">Gebruiker toevoegen</h2>

                <div className="row text-center">
                    {errorsList}
                </div>
                    

                    {/* <form onSubmit={this.handleSubmit} className="col-lg-8">

                        <div className="">
                        <label className="" htmlFor="role">Rol:</label>
                        <select className="" name="role" id="role" disabled={!(userRole === "Admin")}
                                value={this.state.roleDisplayName}
                                onChange={this.onChangeRole}
                                required>
                                {rolesOptions}
                            </select>
                        </div>               */}

                <form onSubmit={this.handleSubmit} className="container col-lg-8">

                    <div className="input row dossier mt-2">
                        <label className="label col-sm col-form-label" htmlFor="roleId">Rol:</label>
                        <select className="form-control col-sm-9" name="roleId" id="roleId" disabled={!(userRole === "Admin")}
                            onChange={this.handleFormChange}
                            required>
                            <option hidden value=''></option>
                            {rolesOptions}
                        </select>
                    </div>
{/* >>>>>>> origin/multiple_locations */}

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="location">Locatie:</label>
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
                            input={<Input id="selectedLocationsIds" />}
                            >
                            {locationsOptions}
                        </Select>
                    </div>
            
                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="name">Naam:</label>
                        <input className="form-control col-sm-9" id="name" type="name" name="name"
                            onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="email">Email:</label>
                        <input className="form-control col-sm-9" id="email" type="email" name="email"
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="password">Wachtwoord:</label>
                        <input className="form-control col-sm-9" id="password" type="password" name="password"
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier" >
                        <label className="label col-sm col-form-label" htmlFor="startDate">Startdatum:</label>
                        <input className="form-control col-sm-9" id="startDate" type="date" name="startDate" 
                            onChange={this.handleFormChange}/>
                    </div>
                            {/* 
                                <div className="input row dossier" hidden={!traineeDossier}>
                                    <label className="label col col-form-label" htmlFor="bundles">Bundels:</label>
                                    <BundleTable 
                                        bundlesTrainee={this.state.bundlesTrainee} 
                                        bundles={this.state.bundles}
                                        removeBundle={this.removeBundle.bind(this)}
                                        handleBundleChange={this.handleBundleChange.bind(this)} 
                                        addBundle ={this.addBundle.bind(this)} />
                            </div> */}

                    <div className="row mt-2">
                        <div className="buttons">
                            <button type="submit" className="btn btn-primary btn-block">Voeg toe</button>
                        </div>
                    </div>

                    <div className="row">
                        <div className="buttons">
                            <Link to="/settings"  className="btn btn-primary btn-block">Annuleer</Link>
                        </div>
                    </div>

                </form>


{/* 
                    <form onSubmit={this.handleSubmit}>

                        <div className="row justify-content-center">

                            <div className="col-4">

                                <RoleAndLocation
                                    currentStep={this.state.currentStep}
                                    roles={this.state.roles}
                                    locations={this.state.locations}
                                    // teachers={this.state.teachers}
                                    // teacherDisplayName={this.state.teacherDisplayName}
                                    roleDisplayName={this.state.roleDisplayName}
                                    locationDisplayName={this.state.locationDisplayName}
                                    onChangeRole={this.onChangeRole}
                                    onChangeLocation={this.onChangeLocation}
                                    // onChangeTeacher={this.onChangeTeacher}
                                    isTrainee={this.state.isTrainee}
                                />
            
                            <UserInfo
                                currentStep={this.state.currentStep}
                                name={this.state.name}
                                email={this.state.email}
                                date={this.state.dateActive}
                                role={this.state.role}
                                location={this.state.location}
                                password={this.state.password}
                                handleFormChange={this.handleFormChange}
                            />
                            </div>
                        </div>
                    
                        <div className="row justify-content-center">
                            <div className="col-4 m-1">
                                {this.nextButton}
                                {this.submitButton}
                            </div>
<<<<<<< HEAD
                        </div>

                        <div className="row justify-content-center">
                            <div className="col-4">
                                {(this.state.currentStep <= 1 ) ? 
                                    <Link className="btn btn-danger btn-block" to={"/settings"}>Annuleren</Link>: 
                                    <button
                                        className="btn btn-danger btn-block"
                                        type="button" onClick={this._prev}>
                                        Vorige
                                    </button>}
                            </div>
                        </div> 

                    </form>
            </div>
=======
                    </form>*/}

                    {/* <div className="row justify-content-center">
                        <div className="col-3">
                            {(this.state.currentStep <= 1 ) ? 
                                <Link className="btn btn-primary float-right" to={"/settings"}>Annuleren</Link>: 
                                <button
                                    className="btn btn-primary float-right"
                                    type="button" onClick={this._prev}>
                                    Vorige
                                </button>}
                        </div>
                    </div>  */}

             </div> 
>>>>>>> origin/development
        )
    }
}

export default withRouter(AddUser);