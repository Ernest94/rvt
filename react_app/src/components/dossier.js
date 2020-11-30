import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link, withRouter } from 'react-router-dom';
import {config} from './constants';
import './form.css';
import Permissions from './permissions.js';
import constraints from '../constraints/dossierConstraints';
import Utils from './Utils';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import InputLabel from '@material-ui/core/InputLabel';
import Input from '@material-ui/core/Input';

class Dossier extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            errors: null,
            name: "",
            email: "",
            role: null,
            roleName: "",
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
            bundleCheck: [],
            selected: [0, 1],
        };
    }

    async componentDidMount() {
        const { computedMatch: { params } } = this.props;
        Utils.dateValidation();
        // this.props.dateValidation();
        await this.setState({ pageLoading: true, userId: params.userId });
        await console.log(this.state.role);
        await this.getAllInfo();
    }
    
    static hasAccess() {
        return Permissions.canEditDossier();
    }
    
    canViewUserDossier() {
        /* JH TIP: Zie remark op main.js regel 102 om hier een eigen permissions.js van te maken, en daarnaast ook isUserAdmin etc hier te gebruiken */
        const userRole = sessionStorage.getItem("userRole");
        const roleDossierUser = this.state.role.name;
        const ownUserId = sessionStorage.getItem("userId");
        var isBlocked; /* JH TIP: Ik zou in plaats van isBlocked een isAllowedToView maken, dat maakt de logica hieronder leesbaarder 
        switch (userRole) {
            case "Trainee":
                isAllowedToView = isOwnUserId(this.state.userId);
                break;
            case "Docent":
            case "Sales":
            case "Office":
                isAllowedToView = isTraineeDossier(this.state.role) || isOwnUserId(this.state.userId);
                break;
            case "Admin":
                isAllowedToView = true;
                break;
            default:
                isAllowedToView = false;
                break;
        }
        this.setState({blocked: !isAllowedToView});
        */
        switch (userRole) {
            case "Trainee":
                isBlocked = ownUserId !== this.state.userId
                break;
            case "Docent":
                isBlocked = (roleDossierUser !== "Trainee" && ownUserId !== this.state.userId);
                break;
            case "Sales":
                isBlocked = roleDossierUser === "Admin"; /* JH: Vreemd ik had hier verwacht dat sales ook alleen trainees en zichzelf mocht zien */
                break;
            case "Office":
                isBlocked = (roleDossierUser === "Admin" || roleDossierUser === "Sales"); /* JH: Vreemd ik had hier verwacht dat office ook alleen trainees en zichzelf mocht zien */
                break;
            default:
                isBlocked = false; /* JH: Dit is gevaarlijk, het is beter om case admin: isBlocked = false te hebben en de default case op isBlocked = true te zetten */
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
                roleName: userResponse.data.role.name,
                location: userResponse.data.location,
                startDate: userResponse.data.startDate,
                roleDisplayName: userResponse.data.role.id,
                locationDisplayName: userResponse.data.location.id,
                roles: roleLocResponse.data.roles,
                locations: roleLocResponse.data.locations,
                pageLoading: false,
           });
            this.getTraineeBundles();
            this.canViewUserDossier();
           
        })).catch(errors => {
            console.log("errors occured " + errors); 
            this.setState({pageLoading:false, error: true});
        });
    }

    getTraineeBundles() {
        if (this.state.role.name !== "Trainee") {
            console.log("dossier is from a non Trainee user");
            return;
        }

        axios.get(config.url.API_URL + "/webapi/bundle/bundleTrainee/" + this.state.userId)
            .then(response => {
                this.setState({
                    bundleCheck: response.data.bundleCheck
                });
                console.log(this.state.bundleCheck);
            })
            .catch((error) => {
                console.log(error);
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
                    
                    this.props.history.push('/settings');
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

    handleSelectChange = (e) => {
        const {value} = e.target;
        console.log(value);
    }

    onChangeLocation = (e) => {
        this.setState({
            location: this.state.locations.find(loc => loc.id === parseInt(e.target.value)),
            locationDisplayName: e.target.value
        });
    }

    setSelected = (s) => {
        // const{selected} = this.state.selected;
        // this.state({
        //     [selected]: selected
        // })
        console.log(s);
    }

    render() {

        const {name, email, roleName, startDate, userId, pageLoading, errors, blocked,
            serverFail, locations, roles, roleDisplayName, locationDisplayName, bundleCheck, selected} = this.state;
        const { editDisabled } = this.props;
        const traineeDossier = roleName === "Trainee";

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

        const bundleOptions = bundleCheck.map((bundleCheck) => {
            return (
                <option key = {bundleCheck.bundle.id} value={bundleCheck.bundle.id}> {bundleCheck.bundle.name} </option>  
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
                            disabled={editDisabled}
                            onChange={this.handleFormChange}/>
                    </div>
                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="email">Email:</label>
                        <input className="form-control col-sm-9" id="email" type="email" name="email" value={email} 
                        disabled={editDisabled}
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="rol">Rol:</label>
                        <select className="form-control col-sm-9" name="role" id="role"
                            value={roleDisplayName}
                            onChange={this.onChangeRole}
                            required
                            disabled={editDisabled}>

                            <option hidden value=''>Rol</option>
                            {rolesOptions}
                        </select>
                    </div>
                    <div className="input row">
                        <label className="label col-sm col-form-label" htmlFor="location">Locatie:</label>
                        <select className="form-control col-sm-9" name="location" id="location"
                            value={locationDisplayName}
                            onChange={this.onChangeLocation}
                            required
                            disabled={editDisabled}>

                            <option hidden value=''>Locatie</option>
                            {locationOptions}
                        </select>
                    </div>
                    <div className="input row" >
                        <label className="label col col-form-label" htmlFor="startDate">Startdatum:</label>
                        <input className="form-control col-sm-9" id="startDate" type="date" name="startDate" value={startDate} 
                            disabled={editDisabled}
                            onChange={this.handleFormChange}/>
                    </div>
                    <div className="input row">
                        <label className="label col col-form-label" htmlFor="bundles">Bundels:</label>
                        {/* <InputLabel id="demo-mutiple-name-label">Bundels</InputLabel> */}
                            <Select className="form-control col-sm-9"
                            // labelId="demo-mutiple-name-label"
                            id="demo-mutiple-name"
                            multiple
                            name="bundle"
                            value={bundleOptions}
                            placeholder
                            onChange={this.handleSelectChange}
                            // input={<Input />}
                            // MenuProps={MenuProps}
                            >
                            {bundleCheck.map((bundleCheck) => (
                                <MenuItem key={bundleCheck.bundle.id} value={bundleCheck.bundle.id} /*style={getStyles()}*/>
                                {bundleCheck.bundle.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </div>
                    {(!editDisabled) ? <button type="submit" className="btn btn-danger">Opslaan</button>: <span></span>}

                </form>
                {(editDisabled) ?
                <div className="buttons">
                    <div>
                        <Link 
                            className="btn btn-danger btn-block" 
                            to={"/dossier/" + userId + "/edit"}
                            hidden={Permissions.isUserTrainee()}
                            role="button"
                            >
                            Pas gebruiker aan
                        </Link>
                    </div>
                    {/* <div>
                        <Link 
                            className="btn btn-danger btn-block" 
                            to={"/linking/" + userId}
                            hidden={isTrainee}
                            >
                            Gelinkte gebruikers
                        </Link>
                    </div> */}
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
                            to={"/docentAddReview/"  + userId}
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