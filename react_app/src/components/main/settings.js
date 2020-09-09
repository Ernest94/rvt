import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';

import constraints from '../../constraints/passwordChangeConstraints';

class Settings extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            currentPassword: "",
            newPassword: "",
            repeatPassword: "",
            errors: null,
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
            axios.post("http://localhost:8080/J2EE/webapi/user/password", this.createPasswordJson())
                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    this.props.handleReturnToHome(response.data);
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    this.setErrors({password: ["Mislukt om het wachtwoord te veranderen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
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
        return (
            <div className="container main-container">
                <h2>Verander uw wachtwoord</h2>
                <ul className="errors">{this.state.errors}</ul>
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
                    
                    {(this.state.loading) ? <button type="submit" disabled> Laden...</button>: 
                    <button type="submit">Verander wachtwoord</button>}
                </form> 
            </div>
        )
    }
}

export default Settings;