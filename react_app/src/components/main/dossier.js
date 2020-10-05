import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link } from 'react-router-dom';
import {config} from '../constants';
import './form.css';

import constraints from '../../constraints/dossierConstraints';

class Dossier extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
			email: "",
			role: null,
			location: null,
			startDate: "",
			pageLoading: false,
            userId: null,
            buttonDisabled: false,
            roles: [],
            locations: [],
            roleDisplayName: "",
            locationDisplayName: "",
            blocked: false,
            serverFail: false,
        };
    }

    async componentDidMount() {
        const { computedMatch: { params } } = this.props;
        this.props.dateValidation();
        await this.setState({pageLoading: true, userId: params.userId});
        this.getAllInfo();
        
    }
    
    canViewUserDossier() {
        const userRole = sessionStorage.getItem("userRole");
        console.log(this.state.role);
        const roleDossierUser = this.state.role.name;
        const ownUserId = sessionStorage.getItem("userId");
        var isBlocked;
        switch (userRole) {
            case "Trainee":
                isBlocked = ownUserId !== this.state.userId
                break;
            case "Docent":
                isBlocked = (roleDossierUser !== "Trainee" && ownUserId !== this.state.userId);
                break;
            case "Sales":
                isBlocked = roleDossierUser === "Admin";
                break;
            case "Office":
                isBlocked = (roleDossierUser === "Admin" || roleDossierUser === "Sales");
                break;
            default:
                isBlocked = false;
                break;
        }
        this.setState({blocked: isBlocked});
    }
    
    getAllInfo() {
        const {userId} = this.state;
        const userRequest = axios.get(config.url.API_URL +'/webapi/user/dossier',  {headers: {"userId": userId}} );
        const roleLocRequest = axios.get(config.url.API_URL + '/webapi/user/roles');
        
        axios.all([userRequest, roleLocRequest]).then(axios.spread((...responses) => {
           const userResponse = responses[0]
           const roleLocResponse = responses[1]
           this.setState({
                name: userResponse.data.name,
                email: userResponse.data.email,
                role: userResponse.data.role,
                location: userResponse.data.location,
                startDate: userResponse.data.startDate,
                roleDisplayName: userResponse.data.role.id,
                locationDisplayName: userResponse.data.location.id,
                roles: roleLocResponse.data.roles,
                locations: roleLocResponse.data.locations,
                pageLoading: false,
            });
            this.canViewUserDossier();
           
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
            axios.post(config.url.API_URL + "/webapi/user/change", this.createUserJson())
                .then(response => {
                    this.setState({buttonDisabled: false, errors: null});
                    
                    this.props.handleReturnToSettings();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    const custErr = {changeUser: ["Mislukt om gebruiker te veranderen."]};
                    this.setState({
                        buttonDisabled: false,
                        errors: this.props.setErrors(custErr)
                    });
                });
        }
        else {
            this.setState({
                buttonDisabled: false,
                errors: this.props.setErrors(errors)
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
        const {name, email, startDate, userId, pageLoading, errors, blocked,
            serverFail, locations, roles, roleDisplayName, locationDisplayName} = this.state;
        const {editDisabled, isTrainee} = this.props;
        if (pageLoading) return <span className="center"> Laden... </span>
        if (serverFail) return <span className="center"> Mislukt om de gegevens op te halen. </span> 
        if (blocked) return <span className="center"> Het is niet mogelijk om deze pagina te bekijken. </span>
        
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
                <h2 className="text-center">Dossier</h2>
                <ul className="errors">{errors}</ul>
                <form onSubmit={this.handleSubmit}>
                    <div className="input">
                        <label className="label" htmlFor="name">Naam:</label>
                        <input className="form" id="name" type="name" name="name" value={name} 
                            disabled={editDisabled}
                            onChange={this.handleFormChange}/>
                    </div>

                    <div className="input">
                        <label className="label" htmlFor="email">Email:</label>
                        <input className="form" id="email" type="email" name="email" value={email} 
                        disabled={editDisabled}
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input">
                        <label className="label" htmlFor="rol">Rol:</label>
                        <select className="form" name="role" id="role"
                            value={roleDisplayName}
                            onChange={this.onChangeRole}
                            required
                            disabled={editDisabled}>

                            <option hidden value=''>Rol</option>
                            {rolesOptions}
                        </select>
                    </div>

                    <div className="input">
                        <label className="label" htmlFor="location">Locatie:</label>
                        <select className="form" name="location" id="location"
                            value={locationDisplayName}
                            onChange={this.onChangeLocation}
                            required
                            disabled={editDisabled}>

                            <option hidden value=''>Locatie</option>
                            {locationOptions}
                        </select>
                    </div>

                    <div className="input" >
                        <label className="label" htmlFor="startDate">Startdatum:</label>
                        <input className="form" id="startDate" type="date" name="startDate" value={startDate} 
                            disabled={editDisabled}
                            onChange={this.handleFormChange}/>
                    </div>
                    {(!editDisabled) ? <button type="submit" className="button">Opslaan</button>: <span></span>}

                </form>
                {(editDisabled) ?
                <div>
                    <div className="text-center">
                        <Link 
                            className="buttonLink" 
                            to={"/dossier/" + userId + "/edit"}
                            >                        
                            <button className="rvtbutton" hidden={isTrainee}>Pas gebruiker aan</button>
                        </Link>
                    </div>
                    <div className="text-center">
                        <Link 
                            className="buttonLink" 
                            to={"/linking/" + userId}>
                            <button className="rvtbutton" hidden={isTrainee}>Gelinkte gebruikers</button>
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

export default Dossier;