
import React from 'react';
import axios from 'axios';

import { config } from './constants';
import { validate } from 'validate.js';
import Permissions from './permissions.js';
import Util from './Utils.js'
import constraints from '../constraints/adminPasswordChangeConstraints';
import {Link} from 'react-router-dom';
import { TextField } from '@material-ui/core';

class adminPassword extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            newPassword: "",
            repeatPassword: "",
            message: "",
            errors: null,
            buttonDisabled: false
        };
    }

    async componentDidMount() {
        const { computedMatch: { params } } = this.props;
        await this.setState({ userId: params.userId });
    }

    static hasAccess() {
        return Permissions.isUserAdmin();
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value,
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({ message: "" });
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/adminPassword", this.createPasswordJson())
                .then(response => {
                    this.setState({ buttonDisabled: false, errors: null, message: "Wachtwoord succesvol veranderd",newPassword:"",repeatPassword:"" });
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    const custErr = { newPassword: ["Mislukt om het wachtwoord te veranderen."] }
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

    createPasswordJson() {
        return {
            newPassword: this.state.newPassword,
            userId: this.state.userId
        }
    }

    render() {

        const { userId, newPassword, repeatPassword } = this.state;

        return (
            <div className="container">
                
                <h2 className="text-center">Wachtwoord Veranderen</h2>
                
                <div className="row justify-content-center text-danger">{this.state.errors}</div>
                
                    <form onSubmit={this.handleSubmit}>
                        <div className="row justify-content-center">
                            <div className="form-group">
                                <label htmlFor="name">Nieuw Wachtwoord:</label>
                                <TextField className="form-control" id="newPassword" type="password" name="newPassword" value={newPassword} onChange={this.handleFormChange}/>
                            </div>
                        </div>

                        <div className="row justify-content-center">
                            <div className="form-group">
                                <label htmlFor="name">Herhaal Wachtwoord:</label>
                                <TextField className="form-control" id="repeatPassword" type="password" name="repeatPassword" value={repeatPassword} onChange={this.handleFormChange}/>
                            </div>
                        </div>

                        <div className="buttons">
                            <button className="btn btn-danger btn-block" type="submit">
                                Wachtwoord veranderen
                            </button>
                            {(this.state.loading) ? 
                            <button className="btn btn-danger btn-block" type="submit" disabled> 
                                Laden...
                            </button>: 
                            <Link className="btn btn-danger btn-block" to={"/dossier/" + userId}>
                                Terug
                            </Link>}
                        </div> 

                    </form>

                <div className="row justify-content-center m-3">
                    <h4 className="text-center text-success">{this.state.message}</h4>
                </div>
            </div>

        )
    }

}


export default adminPassword;