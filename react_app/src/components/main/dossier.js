import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import './form.css';

import constraints from '../../constraints/constraints';

class Dossier extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "Leonie Schoorl",
			email: "leonie.schoorl@hotmai.com",
			rol: "Trainee",
			location:  "Utrecht",
			startDate: "7 juli 2020",
			pageLoading: false
        };

    }

    componentDidMount() {
    this.setState({pageLoading: true});
    //    this.getUserInfo()
	var name = this.state.name;
	document.getElementById("name").value = name;
	var email = this.state.email;
	document.getElementById("email").value = email;
	var rol = this.state.rol;
	document.getElementById("rol").value = rol;
	var location = this.state.location;
	document.getElementById("location").value = location;
	var startDate = this.state.startDate;
	document.getElementById("startDate").value = startDate;
    }

/*    
getUserInfo() {
        axios.get('http://localhost:8080/J2EE/webapi/user/dossier')
            .then( response => {
                    this.setState({
                        name: response.data.name,
                        email: response.data.email,
                        rol: response.data.rol,
						location: response.data.location,
						startDate: response.data.startDate
                    });
                })
        .catch(() => {
            this.setState({
	                    name: null,
                        email: null,
                        rol: null,
						location: null,
						startDate: null
            });
        })
}
  */  
    render() {
        return (
            <div className="container main-container">
                <ul className="errors">{this.state.errors}</ul>
                <form onSubmit={this.handleSubmit}>
                    <div className="input">
                        <label class="label" htmlFor="name">Naam:</label>
                        <input className="form" id="name" type="name" name="name" />
                    </div>

                    <div className="input">
                        <label class="label" htmlFor="email">Email:</label>
                        <input className="form" id="email" type="email" name="email"/>
                    </div>

                    <div className="input">
                        <label class="label" htmlFor="rol">rol:</label>
                        <input className="form" id="rol" type="rol" name="rol"/>
                    </div>

                    <div className="input">
                        <label class="label" htmlFor="location">Locatie:</label>
                        <input class="form" id="location" type="location" name="location"/>
                    </div>

                    <div className="input" >
                        <label class="label" htmlFor="startDate">Startdatum:</label>
                        <input className="form " id="startDate" type="startDate" name="startDate"/>
                    </div>

                    {(this.state.loading) ? <button className="button" type="button" disabled> Laden...</button>: 
                    <button className="button" type="submit">Gelinkte gebruikers </button>}

                    {(this.state.loading) ? <button className="button" type="button" disabled> Laden...</button>: 
                    <button className="button" type="submit">Voortgang </button>}


                    {(this.state.loading) ? <button className="button" type="submit" disabled> Laden...</button>: 
                    <button className="button" type="submit">Pas gebruiker aan </button>}
                </form>
            </div >
        )
    }
}

export default Dossier;