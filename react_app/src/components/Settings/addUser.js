import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import RoleAndLocation from './roleAndLocation.js';
import UserInfo from './userInfo.js';
import { withRouter } from 'react-router-dom';

import constraints from '../../constraints/addUserConstraints';
import {config} from '../constants';
import Util from '../main/Util.js';

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
            // teacher: null,
            roleDisplayName: "",
            locationDisplayName: "",
            // teacherDisplayName: "",
            isTrainee: null,
            roles : [],
            locations: [],
            // teachers: [{id:1 , name: "Pieter"}],
            errors: null,
            pageLoading: false,
            submitButtonDisabled: false,
            isDocent: sessionStorage.getItem("userRole") === "docent",
        };

        this.handleFormChange = this.handleFormChange.bind(this);
        this.onChangeRole = this.onChangeRole.bind(this);
        this.onChangeLocation = this.onChangeLocation.bind(this);
        // this.onChangeTeacher = this.onChangeTeacher.bind(this);
        this._next = this._next.bind(this);
        this._prev = this._prev.bind(this);
    }

    componentDidMount() {
        this.setState({pageLoading: true});
        this.getLocationsAndRoles()
    }

    getLocationsAndRoles() {

        axios.get(config.url.API_URL + '/webapi/user/roles')
            .then( response => {
                    this.setState({
                        roles: response.data.roles,
                        locations: response.data.locations,
                        pageLoading: false
                    });
                console.log("api_call succesfull");
                })
        .catch(() => {
            this.setState({
                roles: null, //[{id: 1, name: "Trainee"}, {id: 2, name: "Docent"}],
                locations: null, // [{id: 1, name: "Utrecht"}],
                pageLoading: false
            });
        })
        .finally(() => {
            if (this.state.isLocation) {
                this.setState({
                    currentStep: 2,
                    role: this.state.roles.find(role => role.name === "Trainee"),
                    location: {id: sessionStorage.getItem("userLocationId"),
                               name: sessionStorage.getItem("userLocation")}
                });
            }
        })
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value
        });
    }

    onChangeRole= (e) => {
        var selectedRole = this.state.roles.find(role => role.id === parseInt(e.target.value));
        var isTrainee = selectedRole.name === "Trainee";

        this.setState({
           isTrainee: isTrainee,
           role: selectedRole,
           roleDisplayName: e.target.value
        });
    }

    onChangeLocation= (e) => {
        this.setState({
           location: this.state.locations.find(loc => loc.id === parseInt(e.target.value)),
           locationDisplayName: e.target.value
        });
    }

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
            Util.setErrors({roleAndLoc: ["Maak voor alle velden een selectie."]});
        }
        console.log(this.state.currentStep);
    }

    _prev() {
        this.setState({currentStep: 1, errors: null});
    }

    get previousButton() {
        let currentStep = this.state.currentStep;

        if (currentStep > 1 /*&&  !this.state.isDocent*/) {
            return (
                <button
                    className="btn btn-danger"
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
                    className="btn btn-danger float-right"
                    type="button" onClick={this._next}>
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
                    <button className="btn btn-danger float-right"
                        disabled={submitButtonDisabled}
                        type="submit">
                        {(submitButtonDisabled) ? "Laden..." :"Opslaan"}
                    </button>
            )
        }
        return null;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({submitButtonDisabled: true});
        var errors = validate(this.state, constraints);
        console.log(this.createUserJson());
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/create", this.createUserJson())
                .then(response => {
                    this.setState({submitButtonDisabled: false, errors: null});
                    this.props.history.push('/settings');
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    const custErr = {addUser: ["Mislukt om een gebruiker toe te voegen."]};
                    this.setState({
                        submitButtonDisabled: false,
                        errors: Util.setErrors(custErr)
                    });
                });
        }
        else {
            this.setState({
                submitButtonDisabled: false,
                errors: Util.setErrors(errors)
            });
        }
    }

    createUserJson() {
        return {
            name: this.state.name,
            email: this.state.email,
            password: this.state.password,
            role: this.state.role,
            location: this.state.location,
            dateActive: this.state.dateActive
        }
    }

    render() {
        const pageLoading = this.state.pageLoading;
        const errorsList = !!this.state.errors?<ul className="errors">{this.state.errors}</ul>: <span></span>;
        if (pageLoading) return <div className="container center"><span> Laden...</span></div>;

        return (
            <div>
                <h2>Gebruiker toevoegen</h2>

                {errorsList}
                <form onSubmit={this.handleSubmit}>

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
                        dateValidation={this.props.dateValidation}
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

export default withRouter(AddUser);