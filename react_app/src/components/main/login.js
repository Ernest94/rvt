import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';

import constraints from '../../constraints/constraints';

import {config} from '../constants';

class Login extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            loading: false
        };
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true});
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/login", this.createLoginJson())

                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    this.props.handleSuccessfulAuth(response.data);
                })
                .catch((error) => {
                    this.props.handleSuccessfulAuth({id: 1, name: "test", role: {name: "Admin"}, location: {id: 1, name: "Utrecht"}}); // use this line to log in without use of database
                    console.log("an error occorured " + error);  
                    this.setErrors({login: ["Mislukt om in te loggen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }
    
    createLoginJson() {
        return {
            email: this.state.email,
            password: this.state.password
        }
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
            <div className="container main-container">
                <ul className="errors">{this.state.errors}</ul>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input className="form-control" id="email" type="email" name="email" onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Wachtwoord:</label>
                        <input className="form-control " id="password" type="password" name="password" onChange={this.handleFormChange}/>
                    </div>
                    {(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>: 
                    <button className="btn btn-primary float-right" type="submit">Log in </button>}
                </form>
            </div >
        )
    }
}

export default Login;