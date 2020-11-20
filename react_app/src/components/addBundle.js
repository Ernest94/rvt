
import React from 'react';
import axios from 'axios';
import { withRouter } from 'react-router-dom'

import {config} from './constants';
import Permissions from './permissions.js';

class addBundle extends React.Component {
    
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
    }

    validate() {
        return null;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true}); 
        var errors = this.validate();
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/add_bundle", this.createBundleJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    this.succesfullAdd();
                })
                .catch((error) => {
                    this.setErrors({login: ["Mislukt om bundel aan te maken."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }

    createBundleJson() {
        return {
            bundle: this.state.bundle,
            userId: sessionStorage.getItem("userId")
        }
    }

    succesfullAdd(){
        this.setState({ message:"bundel aangemaakt", locationDisplayName: ""});
    }


    onChangeLocation = (e) => {
        var selectedBundle = this.state.bundle.find(bundle=> bundle.id === parseInt(e.target.value));
        this.setState({
            bundle: selectedBundle,
            bundleDisplayName: e.target.value
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
                <h2>Bundel aanmaken</h2>

                <div className="container main-container">
                    <form onSubmit={this.handleSubmit}>
                        <div className="form-group">
                            <label htmlFor="name">Naam:</label>
                            <input className="form-control" id="name" type="text" name="name" value={this.state.location} onChange={this.handleFormChange}/>
                        </div>
                        <div>{(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>:
                            <button className="btn btn-primary float-right" type="submit">maak aan</button>}</div>
                        <div ><button className="btn btn-primary float-right" type="submit">annuleer</button></div>
                            

                    </form>
                    <h4 className="text-center">{this.state.message}</h4>
                </div >
            </div>

        )
    }

}


export default withRouter(addBundle);