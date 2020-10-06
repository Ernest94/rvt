import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';

import constraints from '../../constraints/loginConstraints';

import {config} from '../constants';

class Login extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            buttonDisabled: false,
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
        this.setState({buttonDisabled: true});
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/login", this.createLoginJson())
                .then(response => {
                    this.setState({buttonDisabled: false, errors: null});
                    
                    this.props.handleSuccessfulAuth(response.data);
                })
                .catch((error) => {

                     this.props.handleSuccessfulAuth({id: 1, name: "Admin", role: {name: "Admin"}, location: {id: 1, name: "Utrecht"}}); // use this line to log in without use of database
                    console.log("an error occorured " + error); 
                    const custErr = {login: ["Mislukt om in te loggen."]};
                    this.setState({
                        buttonDisabled: false,
                        errors: this.props.setErrors(custErr)
                    });

                });
        }
        else {
            this.setState({
                buttonDisabled: false,
                errors: this.props.setErrors(errors)
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
                        <input className="form-control " id="password" type="password" name="password" onChange={this.handleFormChange}/>
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

export default Login;