import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import RoleAndLocation from './roleAndLocation.js';
import UserInfo from './userInfo.js';

import constraints from '../../constraints/addUserConstraints';

class AddUser extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            currentStep: 1,
            name: "",
            email: "",
            password: "",
            dateActive: "",
            role: null,
            location: null,
            roleDisplayName: "",
            locationDisplayName: "",
            roles : [],
            locations: [],
            errors: null,
            loading: false,
            pageLoading: false,
            isDocent: sessionStorage.getItem("userRole") === "Docent"
        };
        
        this.handleFormChange = this.handleFormChange.bind(this);
        this.onChangeRole = this.onChangeRole.bind(this);
        this.onChangeLocation = this.onChangeLocation.bind(this);
        this._next = this._next.bind(this);
        this._prev = this._prev.bind(this);
    }
    
    
    
    componentDidMount() {
        this.setState({pageLoading: true});
        this.getLocationsAndRoles()
            // if (this.state.isDocent) {
                // console.log("is docent");
                // this.setState({
                    // currentStep: 2,
                    // role: this.state.roles.find(role => role.name === "Trainee"),
                    // location: {id: sessionStorage.getItem("userLocationId"),
                               // name: sessionStorage.getItem("userLocation")}
                // });
            // }
    }
   
    getLocationsAndRoles() {
        
        axios.get('http://localhost:8080/J2EE/webapi/user/roles')
            .then( response => {
                    this.setState({
                        roles: response.data.roles,
                        locations: response.data.locations,
                        pageLoading: false
                    });
                })
        .catch(() => {
            this.setState({
                roles: null, //[{id: 1, name: "Trainee"}],
                locations: null, //[{id: 1, name: "Utrecht"}],
                pageLoading: false
            });
        })
        .finally(() => {
            if (this.state.isDocent) {
                this.setState({
                    currentStep: 2,
                    role: this.state.roles.find(role => role.name === "Trainee"),
                    location: {id: sessionStorage.getItem("userLocationId"),
                               name: sessionStorage.getItem("userLocation")}
                });
            }
        })
        
        
    }
    
    setErrors = (errors) => {
        const foundErrors = Object.keys(errors).map((key) =>
            <li key={key}>{errors[key][0]}</li>
        );
        this.setState({
           errors: foundErrors 
        });
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }
    
    onChangeRole= (e) => {
        this.setState({
           role: this.state.roles.find(role => role.id === parseInt(e.target.value)),
           roleDisplayName: e.target.value
        });
    }
    
    onChangeLocation= (e) => {
        this.setState({
           location: this.state.locations.find(loc => loc.id === parseInt(e.target.value)),
           locationDisplayName: e.target.value
        });
    }
    
    _next() {
        if (this.state.role != null && this.state.location != null) {
            this.setState({currentStep: 2, errors: null});
        }
        else {
            this.setErrors({roleAndLoc: ["Rol en locatie zijn verplicht."]})
        }
    }
    
    _prev() {
        this.setState({currentStep: 1, errors: null});
    }
    
    get previousButton() {
        let currentStep = this.state.currentStep;
        
        if (currentStep > 1 &&  !this.state.isDocent) {
            return (
                <button
                    className="btn btn-secondary"
                    type="button" onClick={this._prev}>
                    Vorige
                </button>
            )
        }
        return null;
    }
    
    get nextButton() {
        let currentStep = this.state.currentStep;
        
        if (currentStep <= 1 ) {
            return (
                <button
                    className="btn btn-primary float-right"
                    type="button" onClick={this._next}>
                    Volgende
                </button>
            )
        }
        return null;
    }
    
    get submitButton() {
        let currentStep = this.state.currentStep;
        
        if (currentStep === 2) {
            return (
                (this.state.loading) ? 
                <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button> : 
                <button className="btn btn-primary float-right" type="submit">Opslaan </button>
            )
        }
        return null;
    }
    
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true});
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.post("http://localhost:8080/J2EE/webapi/user/create", this.createUserJson())
                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    this.props.handleReturnToSettings();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    this.setErrors({addUser: ["Mislukt om een gebruiker toe te voegen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }
    
    createUserJson() {
        return {
            name: this.state.name,
            email: this.state.email,
            password: this.state.password,
            role: this.state.role,
            location: this.state.location,
            datumActive: this.state.date
        }
    }
    
    render() {
        const pageLoading = this.state.pageLoading;
        const errorsList = !!this.state.errors?<ul className="errors">{this.state.errors}</ul>: <span></span>;
        if (pageLoading) return <div className="container center"><span> Laden...</span></div>;
        
        return (
            <div className="container main-container">

                <h2>Voeg een gebruiker toe</h2>
                
                {errorsList}
                <form onSubmit={this.handleSubmit}>
                    
                    <RoleAndLocation
                        currentStep={this.state.currentStep}
                        getLocationsAndRoles={this.getLocationsAndRoles}
                        roles={this.state.roles}
                        locations={this.state.locations}
                        roleDisplayName={this.state.roleDisplayName}
                        locationDisplayName={this.state.locationDisplayName}
                        onChangeRole={this.onChangeRole}
                        onChangeLocation={this.onChangeLocation}
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
                    
                    {this.nextButton}
                    {this.previousButton}
                    {this.submitButton}
                    
                </form>
                
            </div>
        )
    }
}

export default AddUser;