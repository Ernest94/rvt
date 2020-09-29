import React from 'react';
import axios from 'axios';
import { validate } from 'validate.js';
import { Link } from 'react-router-dom';
import './form.css';

class Dossier extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "Jeroen Heemskerk",
			email: "jeroen@educom.nu",
			role: "Docent",
			location:  "Utrecht",
			startDate: "",
			pageLoading: false
        };
    }

    componentDidMount() {
        this.setState({pageLoading: true});
        //    this.getUserInfo()
        // var name = this.state.name;
        // document.getElementById("name").value = name;
        // var email = this.state.email;
        // document.getElementById("email").value = email;
        // var role = this.state.role;
        // document.getElementById("rol").value = role;
        // var location = this.state.location;
        // document.getElementById("location").value = location;
        // var startDate = this.state.startDate;
        // document.getElementById("startDate").value = startDate;
    }

/*    
getUserInfo() {
        axios.get(config.url.API_URL +'/webapi/user/dossier')
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
        const {name, email, role, location, startDate} = this.state;
        return (
            <div className="container main-container">
                <h2 className="text-center">Dossier</h2>
                <ul className="errors">{this.state.errors}</ul>
                <form onSubmit={this.props.handleSubmit}>
                    <div className="input">
                        <label class="label" htmlFor="name">Naam:</label>
                        <input className="form" id="name" type="name" name="name" value={name} disabled={this.props.editDisabled}/>
                    </div>

                    <div className="input">
                        <label class="label" htmlFor="email">Email:</label>
                        <input className="form" id="email" type="email" name="email" value={email} disabled={this.props.editDisabled}/>
                    </div>

                    <div className="input">
                        <label class="label" htmlFor="rol">Rol:</label>
                        <input className="form" id="rol" type="rol" name="rol" value={role} disabled={this.props.editDisabled}/>
                    </div>

                    <div className="input">
                        <label class="label" htmlFor="location">Locatie:</label>
                        <input class="form" id="location" type="location" name="location" value={location} disabled={this.props.editDisabled}/>
                    </div>

                    <div className="input" >
                        <label class="label" htmlFor="startDate">Startdatum:</label>
                        <input className="form" id="startDate" type="date" name="startDate" value={startDate} disabled={this.props.editDisabled}/>
                    </div>
                    {(!this.props.editDisabled) ? <button type="submit" className="button">Opslaan</button>: <span></span>}

                </form>
                {(this.props.editDisabled) ?
                <div>
                    <Link className="buttonLink" to="/dossier/1/edit"><button className="button">Pas gebruiker aan</button></Link>
                    <Link className="buttonLink" to="/linking/1"><button className="button">Gelinkte gebruikers</button></Link>
                    
                    <button hidden={true} className="button" type="submit">Voortgang</button> </div>: <span></span>
                }
                
                

                
            </div >
        )
    }
}

export default Dossier;