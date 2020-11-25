import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link, withRouter } from 'react-router-dom';
import {config} from './constants';
import './form.css';
import Permissions from './permissions.js';
import constraints from '../constraints/dossierConstraints';
import Utils from './Utils';

class Dossier extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            errors: null,

            userId: null,
            name: "",
            email: "",
            role: {},
            location: {},
            startDate: "",

            roles: [],
            locations: [],

            allowedToView: false,
            allowedToEdit: false,
            allowedToEditFields: [],

            pageLoading: true,
            buttonDisabled: false,
            serverFail: false,
        };
    }

    componentDidMount() {
        Utils.dateValidation();
        this.getAllInfo();

    }
    
    static hasAccess(props) {
        return Permissions.canViewDossier(props.match.params.userId);
    }
    
    isOwnUserId(){
        const userId = sessionStorage.getItem("userId");
        const dossierId = this.state.userId;
        return userId===dossierId;               
    }
    canViewUserDossier() {
        const userRole = sessionStorage.getItem("userRole");
        const dossierRole = this.state.role.name;

        var isAllowedToView; 
        switch (userRole) {
            case "Trainee":
                isAllowedToView = this.isOwnUserId();
                break;
            case "Docent":
            case "Sales":
            case "Office":
                isAllowedToView = dossierRole==="Trainee" || this.isOwnUserId();
                break;
            case "Admin":
                isAllowedToView = true;
                break;
            default:
                isAllowedToView = false;
                break;
        }
        this.setState({allowedToView: isAllowedToView});
    }
    canEditUserDossier(){
        const userRole = sessionStorage.getItem("userRole");
        const dossierRole = this.state.role.name;

        var isAllowedToEdit= {name:false, email:false, role: false, location: false, startDate: false};
        var fields = [];
        switch (userRole) {
            case "Sales":
            case "Trainee":
                isAllowedToEdit = this.isOwnUserId();
                fields= ["name", "email"];
                break;
            case "Docent":
            case "Office":
                isAllowedToEdit = dossierRole==="Trainee" || this.isOwnUserId();
                fields = ["name", "email", "location", "startDate"];
                break;
            case "Admin":
                isAllowedToEdit = true;
                fields = ["name", "email", "location", "role", "startDate"];
                break;
            default:
                isAllowedToEdit = false;
                break;
        }
        this.setState({allowedToEdit: isAllowedToEdit, allowedToEditFields: fields});        
    }
    getAllInfo() {
        const userId =  this.props.match.params.userId;

        const userRequest = axios.get(config.url.API_URL +'/webapi/user/dossier',  {headers: {"userId": userId}} );
        const roleLocRequest = axios.get(config.url.API_URL + '/webapi/user/roles');
        
        axios.all([userRequest, roleLocRequest]).then(axios.spread((...responses) => {
           const userResponse = responses[0]
           const roleLocResponse = responses[1]
           this.setState({
                userId: userId,

                name: userResponse.data.name,
                email: userResponse.data.email,
                role: userResponse.data.role,
                location: userResponse.data.location,
                startDate: userResponse.data.dateActive,

                roles: roleLocResponse.data.roles,
                locations: roleLocResponse.data.locations,

                pageLoading: false,
            });
            this.canViewUserDossier();
            if (!this.props.editDisabled) {this.canEditUserDossier()};
           
        })).catch(errors => {
            console.log("errors occured " + errors); 
            this.setState({pageLoading:false, error: true});
        });
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({buttonDisabled: true});
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.put(config.url.API_URL + "/webapi/user/dossier", this.createUserJson())
                .then(response => {
                    this.setState({buttonDisabled: false, errors: null});
                    this.props.history.push('/dossier/' + this.state.userId);
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    const custErr = {changeUser: ["Mislukt om gebruiker te veranderen."]};
                    this.setState({
                        buttonDisabled: false,
                        errors: Utils.setErrors(custErr)
                    });
                });
        }
        else {
            this.setState({
                buttonDisabled: false,
                errors: Utils.setErrors(errors)
            });
        }
    }
    
    createUserJson() {
        const {name, email, role, location, startDate, userId} = this.state;
        return {
            id: userId,
            name: name,
            email: email,
            role: role,
            location: location,
            dateActive: startDate,
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
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }

    onChangeLocation = (e) => {
        this.setState({
            location: this.state.locations.find(loc => loc.id === parseInt(e.target.value)),
            locationDisplayName: e.target.value
        });
    }
  
    render() {

        const {name, email, role, location, startDate, userId, pageLoading, errors, allowedToView, allowedToEdit, allowedToEditFields,
            serverFail, locations, roles} = this.state;

        const {editDisabled} = this.props;
        const traineeDossier = role.name === "Trainee";

        if (pageLoading) return <span className="center"> Laden... </span>
        if (serverFail) return <span className="center"> Mislukt om de gegevens op te halen. </span> 
        if (!allowedToView) return <span className="center"> Het is niet mogelijk om deze pagina te bekijken. </span>
        
        const rolesOptions = roles.map((role) => {
            return (
                <option key={role.id} value={role.id}>{role.name}</option>
            )
        });
        const locationOptions = locations.map((location) => {
            return (
                <option key={location.id} value={location.id}>{location.name}</option>
            )
        });
        return (
            <div>
                <h2 className="text-center">Gebruikersaccount</h2>
                <ul className="errors text-center">{errors}</ul>
                <form onSubmit={this.handleSubmit} className="container col-lg-8">
                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="name">Naam:</label>
                        <input className="form-control col-sm-9" id="name" type="name" name="name" value={name} 
                            disabled={editDisabled || !allowedToEditFields.includes("name")}
                            onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="email">Email:</label>
                        <input className="form-control col-sm-9" id="email" type="email" name="email" value={email} 
                        disabled={editDisabled || !allowedToEditFields.includes("email")}
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="role">Rol:</label>
                        <select className="form-control col-sm-9" name="role" id="role"
                            value={role.id}
                            onChange={this.onChangeRole}
                            required
                            disabled={editDisabled || !allowedToEditFields.includes("role")}>

                            <option hidden value=''>Rol</option>
                            {rolesOptions}
                        </select>
                    </div>

                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="location">Locatie:</label>
                        <select className="form-control col-sm-9" name="location" id="location"
                            value={location.id}
                            onChange={this.onChangeLocation}
                            required
                            disabled={editDisabled || !allowedToEditFields.includes("location")}>

                            <option hidden value=''>Locatie</option>
                            {locationOptions}
                        </select>
                    </div>

                    <div className="input row" >
                        <label className="label col col-form-label" htmlFor="startDate">Startdatum:</label>
                        <input className="form-control col-sm-9" id="startDate" type="date" name="startDate" 
                            value={startDate} 
                            disabled={editDisabled || !allowedToEditFields.includes("startDate")}
                            onChange={this.handleFormChange}/>
                    </div>
                    {(!editDisabled) ? <button type="submit" className="btn btn-danger" disabled={this.state.buttonDisabled}>Opslaan</button>: <span></span>}

                </form>
                {(editDisabled) ?
                <div className="buttons">
                    <div>
                        <Link 
                            className="btn btn-danger btn-block" 
                            to={"/dossier/" + userId + "/edit"}
                            hidden={allowedToEdit}
                            role="button"
                            >                        
                            Pas gebruiker aan
                        </Link>
                    </div>
                    <div>
                        <Link
                            className="btn btn-danger btn-block"
                            to={"/curriculum/" + userId /*+ "/" + name */}
                            hidden={!traineeDossier}
                            >
                            Review
                        </Link>
                    </div>
                    <div>
                        <Link
                            className="btn btn-danger btn-block"
                            to={"/docentAddReview/"  + userId /*+ "/" + name */}
                                hidden={!traineeDossier || !Permissions.canAddReview()}
                                >
                                Review aanmaken/aanpassen
                        </Link>
                    </div>
                    <div className="text-center">
                        <button 
                            hidden={true} 
                            className="rvtbutton" 
                            type="submit">
                            Voortgang
                        </button>
                    </div>
                </div>: <span></span>
                }
            </div>
        )
    }
}

export default withRouter(Dossier);