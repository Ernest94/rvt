import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { withRouter } from 'react-router-dom'
import Util from './Utils.js'
import constraints from '../constraints/loginConstraints';

import {config} from './constants';

class Login extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            buttonDisabled: false,
        };
    }

    handleSuccessfulAuth(data) {
        sessionStorage.setItem("token", data);
        sessionStorage.setItem("isUserLoggedIn", true);
        var tokens = data.split(".");
        var responseData = JSON.parse(atob(tokens[1]));
        
        sessionStorage.setItem("userId", responseData.UserId);
        sessionStorage.setItem("userName", responseData.sub);
        sessionStorage.setItem("userRole", responseData.Role.name);
        sessionStorage.setItem("userLocation", JSON.stringify(responseData.currentLocations));
        this.props.handleLoginState();
        this.props.history.push('/settings');
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({buttonDisabled: true});
        var errors = validate(this.state, constraints);
        console.log(this.createLoginJson())
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/login", this.createLoginJson())
                .then(response => {
                    console.log(response)
                    this.setState({buttonDisabled: false, errors: null});
                    
                    this.handleSuccessfulAuth(response.data);
                })
                .catch((error) => {
                    // use this line to log in without use of database
                    // this.props.handleSuccessfulAuth({id: 1, name: "Admin", role: {name: "Admin"}, location: {id: 1, name: "Utrecht"}}); 
                    console.log("an error occurred " + error); 
                    const custErr = {login: ["Mislukt om in te loggen."]};
                    this.setState({
                        buttonDisabled: false,
                        errors: Util.setErrors(custErr)

                    });

                });
        }
        else {
            this.setState({
                buttonDisabled: false,
                errors: Util.setErrors(errors)
            });
        }
    }
    
    createLoginJson() {
        return {
            email: this.state.email,
            password: this.state.password
        }
    }
    
    render() {
        const {buttonDisabled} = this.state;
        return (
            <div >
                <ul className="errors">{this.state.errors}</ul>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input className="form-control" id="email" type="email" name="email" onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Wachtwoord:</label>
                        <input className="form-control" id="password" type="password" name="password" onChange={this.handleFormChange}/>
                    </div>

                    <button className="btn btn-danger float-right" 
                        disabled={buttonDisabled} 
                        type="submit">
                        {(buttonDisabled)? "Laden..." : "Log in"}
                    </button>
                </form>
            </div >
        )
    }
}

export default withRouter(Login);