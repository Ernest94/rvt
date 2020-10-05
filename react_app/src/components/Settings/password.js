import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';

import constraints from '../../constraints/passwordChangeConstraints';
import {config} from '../constants';

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
                    
                    this.props.handleReturnToSettings();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    this.setErrors({password: ["Mislukt om het wachtwoord te veranderen."]}); 
                    this.setState({buttonDisabled: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({buttonDisabled: false});
        }
    }
    
    createPasswordJson () {
        return {
            currentPassword: this.state.currentPassword,
            newPassword: this.state.newPassword,
            userId: sessionStorage.getItem("userId")
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
        const {buttonDisabled} = this.state;
        const errorsList = !!this.state.errors?<ul className="errors">{this.state.errors}</ul>: <span></span>;
        return (
            <div>
                <h2>Verander uw wachtwoord</h2>
                {errorsList}
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="current">Huidig wachtwoord:</label>
                        <input className="form-control" id="current" type="password" name="currentPassword" onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="newPassword">Nieuw wachtwoord:</label>
                        <input className="form-control " id="password" type="password" name="newPassword" onChange={this.handleFormChange}/>
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="repeatePassword">Herhaal nieuw wachtwoord:</label>
                        <input className="form-control " id="repeatPassword" type="password" name="repeatPassword" onChange={this.handleFormChange}/>
                    </div>
                    
                    <button className="btn rvtbutton float-right" 
                        disabled={buttonDisabled} 
                        type="submit">
                        {(buttonDisabled)?"Laden...": "Verander wachtwoord"}
                    </button>
                    
                </form> 
            </div>
        )
    }
}

export default Password;