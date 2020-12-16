import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { withRouter } from 'react-router-dom'

import Util from './Utils.js'
import constraints from '../constraints/passwordChangeConstraints';
import {config} from './constants';
import { TextField } from '@material-ui/core';

class Password extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            currentPassword: "",
            newPassword: "",
            repeatPassword: "",
            errors: null,
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
            axios.post(config.url.API_URL + "/webapi/user/password", this.createPasswordJson())
                .then(response => {
                    this.setState({buttonDisabled: false, errors: null});
                    
                    this.props.history.push('/settings');
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    const custErr = {password: ["Mislukt om het wachtwoord te veranderen."]}
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
    
    createPasswordJson () {
        return {
            currentPassword: this.state.currentPassword,
            newPassword: this.state.newPassword,
            userId: sessionStorage.getItem("userId")
        }
    }
    
    render() {
        const {buttonDisabled} = this.state;
        const errorsList = !!this.state.errors?<ul className="errors">{this.state.errors}</ul>: <span></span>;
        return (
            <div className="container">
                <h2 className="text-center">Wachtwoord veranderen</h2>

                <div className="row justify-content-center m-4">
                {errorsList}
                </div>

                <div className="row justify-content-center m-4">
                <form onSubmit={this.handleSubmit}>

                    <div className="form-group">
                        <label htmlFor="current">Huidig wachtwoord:</label>
                        <TextField className="form-control" id="current" type="password" name="currentPassword" onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="newPassword">Nieuw wachtwoord:</label>
                        <TextField className="form-control" id="password" type="password" name="newPassword" onChange={this.handleFormChange}/>
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="repeatePassword">Herhaal nieuw wachtwoord:</label>
                        <TextField className="form-control" id="repeatPassword" type="password" name="repeatPassword" onChange={this.handleFormChange}/>
                    </div>
                    
                    <button className="btn btn-danger btn-block" 
                        disabled={buttonDisabled} 
                        type="submit">
                        {(buttonDisabled)?"Laden...": "Verander wachtwoord"}
                    </button>
                    
                </form> 
                </div>
            </div>
        )
    }
}

export default withRouter(Password);