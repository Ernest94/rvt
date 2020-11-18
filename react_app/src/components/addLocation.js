
import React from 'react';
import axios from 'axios';

import {config} from './constants';
import Permissions from './permissions.js';
import Utils from './Utils.js';

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
                    console.log("an error occorured " + error);
                    console.log(this.createLocationJson());

                    this.setState({errors: Utils.setErrors({api: ["Mislukt om locatie toe te voegen. Mogelijk bestaat deze locatie al."]})}); 
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
            <div>
                <h2>Locatie toevoegen</h2>
                <div>{this.state.errors}</div>
                <div className="container main-container">
                    <form onSubmit={this.handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name">Naam van locatie:</label>
                            <input className="form-control" id="locationName" type="text" name="locationName" value={this.state.locationName} onChange={this.handleFormChange}/>
                        </div>
                            <button className="btn btn-danger float-right" type="submit">Locatie toevoegen</button>
                    </form>
                    <h4 className="text-center">{this.state.message}</h4>
                </div >
            </div>

        )
    }

}


export default addLocation;