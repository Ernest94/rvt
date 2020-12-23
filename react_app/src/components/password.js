import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { withRouter,Link } from 'react-router-dom'

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
            message: "",
        };
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value,
           message: ""
        });
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({buttonDisabled: true});
        this.setState({message: ""});
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/password", this.createPasswordJson())
                .then(response => {
                    this.setState({buttonDisabled: false, errors: null, message: "Wachtwoord succesvol veranderd"});
                                    })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    const custErr = {password: ["Mislukt om het wachtwoord te veranderen. Mogelijk is het ingevoerde huidige wachtwoord incorrect."]}
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
        const errorsList = !!this.state.errors?<ul className="text-danger">{this.state.errors}</ul>: <span></span>;
        return (
            <div className="container">

                <h2 className="text-center">Wachtwoord veranderen</h2>

                <div className="row justify-content-center">{errorsList} </div>

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

                        <Link className="btn btn-danger btn-block" to={"/settings"}>
                            Annuleer
                        </Link>
                        
                    </form>
                </div>

                <div className="form-group">
                    <h4 className="text-center text-success">{this.state.message}</h4>
                </div>

            </div>
        )
    }
}

export default withRouter(Password);