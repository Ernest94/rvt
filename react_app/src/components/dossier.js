import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link, withRouter } from 'react-router-dom';
import {config} from './constants';
import './form.css';
import Permissions from './permissions.js';
import constraints from '../constraints/dossierConstraints';
import Utils from './Utils';
import { FaPlus, FaTimes } from "react-icons/fa";
import {Select, Input, MenuItem} from '@material-ui/core'


class BundleTable extends React.Component {

    render(){
        const weeks = [1,2,3,4,5,6,7,8,9,10,11,12];
        const weekOptions = weeks.map((week) =>(
                            <option key={"week_"+week} value={week}>
                                {"week " + week}
                            </option>))

        const bundleOptions = 
        this.props.bundles.map((bundle,index) => (
                            <option key={"bundleChoice" + index} value={bundle.id}>
                                {bundle.name}
                            </option>))
        
    return(
    <div className="col-sm-9">
        <table  className="bundleTable dossier">
            <tbody>
                {this.props.bundlesTrainee.map((bundleTrainee, index)=> 
                    (this.props.editDisabled?
                    <tr key={"bundleSelect_" + index}>
                        <td>{bundleTrainee.bundle.name}</td>
                        <td>Start: week {bundleTrainee.startWeek}</td>
                        <td></td>
                    </tr>
                    :
                    <tr className="row" key={"bundleSelect_" + index}>
                        <td>  
                            <select className="form-control"
                            id={"bundle_" + index}
                            name="bundle"
                            value={bundleTrainee.bundle.id? bundleTrainee.bundle.id : -1}
                            onChange={(e) => this.props.handleBundleChange(e,index)}
                            >
                                <option value="-1" hidden>Kies een bundel</option>
                                {bundleOptions}
                            </select>                     
                        </td>
                        <td>              
                            <select className="form-control"
                                id={"week_" + index}
                                name="startWeek"
                                value={bundleTrainee.startWeek}
                                onChange={(e)=> this.props.handleBundleChange(e,index)}
                            >
                                <option value="-1" hidden>Kies een startweek</option>
                                {weekOptions}
                            </select>
                        </td>
                        <td>
                            <button className="btn btn-danger btn-sm" type="button" onClick={(e) => this.props.removeBundle(e,index)}>
                                <FaTimes /> 
                            </button>
                        </td>
                    </tr>))}
            </tbody>
        </table>
        {!this.props.editDisabled?
            <button className="btn btn-danger btn-sm" name="add" type="button" onClick={this.props.addBundle}>
                    <FaPlus />
                </button>:""}
    </div>)
    }
}

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
            bundlesTrainee: [],
            bundles: [],
            selected: [0, 1],
        };
    }

    componentDidMount(props) {
        Utils.dateValidation();
        this.setState({ pageLoading: true, userId: this.props.match.params.userId });    
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
        const dossierLocation = this.state.location.name;

        var isAllowedToEdit;
        var fields = [];
        switch (userRole) {
            case "Office":
            case "Docent":
                isAllowedToEdit = dossierRole==="Trainee" 
                    && dossierLocation===sessionStorage.getItem("userLocation") 
                    || this.isOwnUserId();
                fields = ["name", "email","location", "bundle"];
                break;
            case "Admin":
                isAllowedToEdit = true;
                fields = ["name", "email", "location", "role", "startDate","bundle"];
                break;
            case "Sales":
            case "Trainee":
            
            default:
                isAllowedToEdit = false;
                break;
        }
        this.setState({allowedToEdit: isAllowedToEdit, allowedToEditFields: fields});        
    }
    getAllInfo() {
        const userId =  this.props.match.params.userId;

        const userRequest = axios.get(config.url.API_URL +'/webapi/user/dossier', 
        {headers: {"userId": userId}} );
        const roleLocRequest = axios.get(config.url.API_URL + '/webapi/user/roles');
        const bundleRequest = axios.get(config.url.API_URL + '/webapi/bundle/bundles');
        axios.all([userRequest, roleLocRequest, bundleRequest]).then(axios.spread((...responses) => {
            const userResponse = responses[0]
            const roleLocResponse = responses[1]
            const bundleResponse = responses[2]
            console.log(userResponse.data)
            
           var currentLocationsIds = userResponse.data.currentLocations.map(element => element.id)
           this.setState({
                userId: userId,

                name: userResponse.data.name,
                email: userResponse.data.email,
                role: userResponse.data.role,
                currentLocations: userResponse.data.currentLocations,
                currentLocationsIds: currentLocationsIds,
                startDate: userResponse.data.startDate,

                roles: roleLocResponse.data.roles,
                locations: roleLocResponse.data.locations,

                bundles: bundleResponse.data,

                pageLoading: false,
           });
            this.getTraineeBundles();
            this.canViewUserDossier();
            this.canEditUserDossier();
            // if (!this.props.editDisabled) {this.canEditUserDossier()};
           
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

        axios.get(config.url.API_URL + "/webapi/bundle/user/" + this.state.userId)
            .then(response => {
                this.setState({
                    bundlesTrainee: response.data,
                });
                console.log(response.data.bundleCheck);
            })
            .catch((error) => {
                console.log(error);
            });
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({buttonDisabled: true});
        var errors = validate(this.state, constraints);
        var newUserLocations = this.state.locations.filter(element => this.state.currentLocationsIds.includes(element.id))       
 
        if(this.isOwnUserId()){
            sessionStorage.setItem("userName", this.state.name);
            sessionStorage.setItem("userLocation", JSON.stringify(newUserLocations));
        }
        console.log(this.createUserJson())
        const userUpdate = axios.put(config.url.API_URL + "/webapi/user/dossier", this.createUserJson());
        const bundleUpdate = axios.put(config.url.API_URL + "/webapi/bundle/user/"+this.state.userId, this.createBundleJson());
        if (!errors) {
            axios.all([userUpdate, ]) //bundleUpdate
                .then(response => {
                    this.setState({buttonDisabled: false, errors: null});
                    this.props.history.push('/dossier/' + this.state.userId);
                })
                .catch((error) => {
                    console.log("an error occurred " + error);  
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
        const {name, email, role, currentLocationsIds, startDate, userId } = this.state;
        // var userLocations = [];
        // var i;
        // for (i=0;i<currentLocationsIds.length;i++) {
        //     userLocations.push({
        //         user: {id:userId},
        //         location: {id:currentLocationsIds[i]}
        //     })
        // } 
        var locations = [];
        var i;
        for (i=0;i<currentLocationsIds.length;i++) {
            locations.push(
                {id:currentLocationsIds[i]}
            )
        }
        return {
            user:{
                id: userId,
                name: name,
                email: email,
                role: role,
                startDate: startDate
            },
            locations: locations
            // locations: [{id:1}]
        }  
    }

    createBundleJson() {
        const {bundlesTrainee} = this.state;
        return(
            bundlesTrainee
        )
    }
    
    onChangeRole = (e) => {
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

    handleLocationChange = (e) => {
        this.handleFormChange(e);
        var newUserLocations = this.state.locations.filter(element => e.target.value.includes(element.id))       

        this.setState({
            currentLocations:newUserLocations
        })
    }
    handleBundleChange = (e,index) => {

        const value = +e.target.value;

        if(e.target.name==="startWeek"){
            let bundlesTrainee = [...this.state.bundlesTrainee];
            let modBundle = {...bundlesTrainee[index], startWeek: value};
            bundlesTrainee[index]=modBundle;
            this.setState({bundlesTrainee});
        }
        else{
            var selectedBundleIndex = this.state.bundles.findIndex(bundle=>bundle.id === value);
            const newBundle = this.state.bundles[selectedBundleIndex]
            var localBundlesTrainee = this.state.bundlesTrainee.slice();
            localBundlesTrainee[index].bundle = newBundle;
            this.setState({bundlesTrainee: localBundlesTrainee});
        }
    }

    addBundle(){
        this.setState((prevState)=>({bundlesTrainee: [...prevState.bundlesTrainee, {bundle:{id:-1},startWeek:0}]}));
    }

    removeBundle(e,index){
        this.setState((prevState) => ({bundlesTrainee: [...prevState.bundlesTrainee.slice(0,index), ...prevState.bundlesTrainee.slice(index+1)]}));
    }

    render() {


        const {role, name, email,startDate, userId, pageLoading, errors,
            serverFail, locations, roles,allowedToView, allowedToEdit, 
            allowedToEditFields,currentLocationsIds} = this.state;

        const {editDisabled} = this.props;
        const traineeDossier = role.name === "Trainee";

        if (pageLoading) return <span className="error-message-center"> Laden... </span>
        if (serverFail) return <span className="error-message-center"> Mislukt om de gegevens op te halen. </span> 
        if (!allowedToView) return <span className="error-message-center"> Het is niet mogelijk om deze pagina te bekijken. </span>
        
        const rolesOptions = roles.map((role) => {
            return (
                <option key={role.id} value={role.id}>{role.name}</option>
            )
        });

        const locationOptions = locations.map((location) => {
            return (
                <MenuItem key={location.id} value={location.id}>
                    {location.name}
                </MenuItem>
                )
            });
        return (
            <div>
                <h2 className="txt-centere">Gebruikersaccount</h2>
                <ul className="errors text-center">{errors}</ul>
                <form onSubmit={this.handleSubmit} className="container col-lg-8">
                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="name">Naam:</label>
                        <input className="form-control col-sm-9" id="name" type="name" name="name" value={name} 
                            disabled={editDisabled || !allowedToEditFields.includes("name") || !allowedToEdit}
                            onChange={this.handleFormChange}/>
                    </div>
                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="email">Email:</label>
                        <input className="form-control col-sm-9" id="email" type="email" name="email" value={email} 
                        disabled={editDisabled || !allowedToEditFields.includes("email") || !allowedToEdit}
                        onChange={this.handleFormChange}/>
                    </div>

                    <div className="input row dossier">
                        <label className="label col-sm col-form-label" htmlFor="role">Rol:</label>
                        <select className="form-control col-sm-9" name="role" id="role"
                            value={role.id}
                            onChange={this.onChangeRole}
                            required
                            disabled={editDisabled || !allowedToEditFields.includes("role") || !allowedToEdit}>

                            <option hidden value=''>Rol</option>
                            {rolesOptions}
                        </select>
                    </div>
                    <div className="input row dossier">
                    <label className="label col-sm col-form-label" htmlFor="location">Locatie:</label>
                    <Select
                        className="m-1 text-black"
                        id="currentLocationsIds"
                        name="currentLocationsIds" 
                        multiple
                        value={currentLocationsIds}
                        onChange={this.handleLocationChange.bind(this)}
                        //the MenuProps below are needed to stop the dropdown jumping around when selecting
                        MenuProps={{
                            variant: "menu",
                            getContentAnchorEl: null}
                        }
                        input={<Input id="currentLocationsIds" />}
                        disabled={editDisabled || !allowedToEditFields.includes("location") || !allowedToEdit}
                        >
                        {locationOptions}

                    </Select>
                    </div>
                    <div className="input row dossier" >
                        <label className="label col col-form-label" htmlFor="startDate">Startdatum:</label>
                        <input className="form-control col-sm-9" id="startDate" type="date" name="startDate" 
                            value={startDate} 
                            disabled={editDisabled || !allowedToEditFields.includes("startDate") || !allowedToEdit}
                            onChange={this.handleFormChange}/>
                    </div>
                    <div className="input row dossier" hidden={!traineeDossier}>
                        <label className="label col col-form-label" htmlFor="bundles">Bundels:</label>
                       <BundleTable 
                       bundlesTrainee={this.state.bundlesTrainee} 
                       bundles={this.state.bundles}
                       editDisabled={editDisabled || !allowedToEditFields.includes("bundle") || !allowedToEdit} 
                        removeBundle={this.removeBundle.bind(this)}
                        handleBundleChange={this.handleBundleChange.bind(this)} 
                        addBundle ={this.addBundle.bind(this)} />
                    </div>
                    <div className="row">
                        <div className="buttons">
                            {(!editDisabled) ? <button type="submit" className="btn btn-danger btn-block">Opslaan</button> : <span></span>}
                        </div>
                    </div>
                    <div className="row">
                        <div className="buttons">
                            {(!editDisabled) ? <Link to={'/dossier/' + this.state.userId}  className="btn btn-danger btn-block">Annuleer</Link> : <span></span>}
                        </div>
                    </div>
                </form>

                {(editDisabled) ?
                <div className="buttons">
                    <div>
                        <Link 
                            className="btn btn-danger btn-block" 
                            to={"/dossier/" + userId + "/edit"}
                            hidden={!allowedToEdit}
                            role="button"
                            >                        
                            Gegevens aanpassen
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