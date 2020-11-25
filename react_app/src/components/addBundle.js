
import React from 'react';
import axios from 'axios';
import { withRouter } from 'react-router-dom'

import {config} from './constants';
import Permissions from './permissions.js';

class addBundle extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
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
            axios.post(config.url.API_URL + "/webapi/bundle/create", this.createBundleJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    this.succesfullAdd();
                    this.props.history.push('/conceptOverview');
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
            name: this.state.name,
            creator: {"id":sessionStorage.getItem("userId")}
        }
    }

    succesfullAdd(){
        this.setState({ message:"bundel aangemaakt", 
                        name: ""});
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

        return (
            <div className="container" >
                <h2 className="text-center">Bundel aanmaken</h2>

                    <form onSubmit={this.handleSubmit}>
                    <div className="row justify-content-center">
                        <div className="form-group">
                            <label htmlFor="name">Naam:</label>
                            <input className="form-control" id="name" type="text" name="name" value={this.state.name} onChange={this.handleFormChange}/>
                        </div>
                    </div>
                        <div className="row justify-content-center m-1">
                            <div className="col-4">
                            {(this.state.loading) ? 
                                <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>:
                                <button className="btn btn-primary float-right" type="submit">maak aan</button>}
                            </div>
                        </div>
                        
                        <div className="row justify-content-center m-1">
                            <div className="col-4">
                                <button className="btn btn-primary float-right" type="submit">annuleer</button>
                            </div>
                        </div>
                    </form>
                    <h4 className="text-center text-success">{this.state.message}</h4>
            </div >

        )
    }

}


export default withRouter(addBundle);