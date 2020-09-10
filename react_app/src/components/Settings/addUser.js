import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';

import constraints from '../../constraints/addUserConstraints';

class AddUser extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            email: "",
            password: "",
            role: null,
            roleDisplayName: 1,
            roles : [],
            errors: null,
            loading: false,
            pageLoading: false
        };
    }
    
    componentDidMount() {
        this.setState({pageLoading: true});
        this.getRoles();
    }
    
    getRoles() {
        axios.get('http://localhost:8080/J2EE/webapi/user/roles')
            .then((response) => {console.log(response.data); this.setState({roles: response.data.roles, pageLoading: false})})
    .catch(() => {
        this.setState({roles: null, pageLoading: false});
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
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }
    
    onChangeRole= (e) => {
        this.setState({
           role: this.state.roles.find(role => role.id === e.target.value.parseInt()),
           roleDisplayName: e.target.value
        });
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true});
        var errors = validate(this.state, constraints);
        if (!errors) {
            axios.post("http://localhost:8080/J2EE/webapi/user/create", this.createUserJson())
                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    this.props.handleReturnToSettings();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    this.setErrors({addUser: ["Mislukt om een gebruiker toe te voegen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }
    
    createUserJson() {
        return {
            name: this.state.name,
            email: this.state.email,
            password: this.state.password,
            role: this.state.role
        }
    }

    render() {
        const pageLoading = this.state.pageLoading;
        const roles = this.state.roles;
        const errorsList = !!this.state.errors?<ul className="errors">{this.state.errors}</ul>: <span></span>;
        if (pageLoading) return <div className="container center"><span> Laden...</span></div>;
        if (roles === null) return <div className="container center"><span> Problemen met laden van de pagina. </span></div>;
        const rolesOptions = roles.map((role) => {
           return (
                <option key={role.id} value={role.id}>{role.name}</option>
           ) 
        });
        return (
            <div className="container main-container">

                <h2>Voeg een gebruiker toe</h2>
                
                {errorsList}
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Naam:</label>
                        <input className="form-control" id="name" type="text" name="name" value={this.state.name} onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email:</label>
                        <input className="form-control " id="email" type="email" name="email" value={this.state.email} onChange={this.handleFormChange}/>
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="password">Wachtwoord:</label>
                        <input className="form-control " id="password" type="password" name="password"  value={this.state.password} onChange={this.handleFormChange}/>
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="role">Rol:</label>
                        <select name="role" id="role" value={this.state.roleDisplayName} onChange={this.onChangeRole}>
                            {rolesOptions}
                        </select>
                    </div>
                    
                    {(this.state.loading) ? <button type="submit" disabled> Laden...</button>: 
                    <button type="submit">Opslaan </button>}
                </form>
                
            </div>
        )
    }
}

export default AddUser;