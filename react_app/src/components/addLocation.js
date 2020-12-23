
import React from 'react';
import axios from 'axios';

import {config} from './constants';
import Permissions from './permissions.js';
import Utils from './Utils.js';
import {Link} from 'react-router-dom';
import { TextField } from '@material-ui/core';

class addLocation extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            locationName: "",
            message: "",
            errors: null,
        };
    }

    static hasAccess() {
        return Permissions.canAddLocation();
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value,
        });
    }

    validate() {
        if(!this.state.locationName.trim())
        {
            this.setState({locationName:""});
            return {name: ["De locatienaam mag niet leeg zijn"]};
        }
        if(!isNaN(this.state.locationName))
        {
            return {name: ["De locatienaam moet letters bevatten"]}
        }
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({message: ""});
        var errors = this.validate();
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/locations", this.createLocationJson())  
                .then(response => {
                    this.succesfullAdd(this.state.locationName);
                    this.setState({errors: null, locationName: ""});
                })
                .catch((error) => {
                    this.setState({loading: false, 
                        errors: Utils.setErrors({input: ["Mislukt om Locatie toe te voegen. Mogelijk bestaat er al een locatie met deze naam."]})});
                });
        }
        else {
            this.setState({errors: Utils.setErrors(errors)});
        }
    }

    createLocationJson() {
        return {
            name: this.state.locationName
        }
    }

    succesfullAdd(name){
        this.setState({ message:"Locatie " + name + " is succesvol toegevoegd."});
    }
    

    render() {
        return (
            <div className="container">
                
                <h2 className="text-center">Locatie toevoegen</h2>
                
                <div className="row justify-content-center text-danger">{this.state.errors}</div>
                
                    <form onSubmit={this.handleSubmit}>
                        <div className="row justify-content-center">
                            <div className="form-group">
                                <label htmlFor="name">Naam van locatie:</label>
                                <TextField className="form-control" id="locationName" type="text" name="locationName" value={this.state.locationName} onChange={this.handleFormChange}/>
                            </div>
                        </div>

                        <div className="buttons">
                            <button className="btn btn-danger btn-block" type="submit">
                                Locatie toevoegen
                            </button>
                            {(this.state.loading) ? 
                            <button className="btn btn-danger btn-block" type="submit" disabled> 
                                Laden...
                            </button>: 
                            <Link className="btn btn-danger btn-block" to={"/settings"}>
                                Annuleren
                            </Link>}
                        </div> 

                    </form>

                <div className="row justify-content-center m-3">
                    <h4 className="text-center text-success">{this.state.message}</h4>
                </div>
            </div>

        )
    }

}


export default addLocation;