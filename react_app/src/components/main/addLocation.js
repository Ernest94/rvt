
import React from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

import {config} from '../constants';

class addLocation extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            location: "",
            locationDisplayName: "",
            message: "",
            loading: false,
        };
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value,
        });
        console.log(name + " " + value);
    }

    validate() {
        return null;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true}); 
        var errors = this.validate();
        if (!errors) {
            console.log(this.createConceptJson());
            axios.post(config.url.API_URL + "/webapi/add_location/saveConcept", this.createConceptJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    this.succesfullAdd();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    console.log(this.createConceptJson());

                    this.setErrors({login: ["Mislukt om concept toe te voegen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }

    createConceptJson() {
        return {
            location: this.state.location
        }
    }

    succesfullAdd(){
        this.setState({ message:"locatie toegevoegd",
                        locationDisplayName: ""});
    }


    onChangeLocation = (e) => {
        var selectedLocation = this.state.location.find(location=> location.id === parseInt(e.target.value));
        this.setState({
            location: selectedLocation,
            locationDisplayName: e.target.value
        });
    }
    
    setErrors = (errors) => {
        const foundErrors = Object.keys(errors).map((key) =>
            <li key={key}>{errors[key][0]}</li>
        );
        this.setState({
           errors: foundErrors 
        });
    }

    render() {

        // const locationOptions = this.state.location.map((location) => {
        //     return (
        //         <option key={location.id} value={location.id}>{location.name}</option>
        //     )
        // });

        return (
            <div>
                <h2>Locatie toevoegen</h2>

                <div className="container main-container">
                    <form onSubmit={this.handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name">Naam van locatie:</label>
                            <input className="form-control" id="name" type="text" name="name" value={this.state.location} onChange={this.handleFormChange}/>
                        </div>
                        {(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>:
                            <button className="btn btn-primary float-right" type="submit">locatie toevoegen</button>}
                    </form>
                    <h4 className="text-center">{this.state.message}</h4>
                </div >
            </div>

        )
    }

}


export default addLocation;