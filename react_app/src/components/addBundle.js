
import React from 'react';
import axios from 'axios';

import {config} from './constants';
import Permissions from './permissions.js';

class addLocation extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            bundle: "",
            bundleDisplayName: "",
            message: "",
            loading: false,
        };
    }

    static hasAccess() {
        return Permissions.canAddBundle();
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
            axios.post(config.url.API_URL + "/webapi/add_bundle", this.createConceptJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    this.succesfullAdd();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    console.log(this.createConceptJson());

                    this.setErrors({login: ["Mislukt om bundel toe te voegen."]}); 
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
        this.setState({ message:"bundel toegevoegd", locationDisplayName: ""});
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
                <h2>Bundel toevoegen</h2>

                <div className="container main-container">
                    <form onSubmit={this.handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name">Naam:</label>
                            <input className="form-control" id="name" type="text" name="name" value={this.state.location} onChange={this.handleFormChange}/>
                        </div>
                        {(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>:
                            <button className="btn btn-primary float-right" type="submit">bundel toevoegen</button>}
                    </form>
                    <h4 className="text-center">{this.state.message}</h4>
                </div >
            </div>

        )
    }

}


export default addLocation;