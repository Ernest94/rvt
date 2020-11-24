import React from 'react';
import axios from 'axios';

import {config} from './constants';
import Permissions from './permissions.js';
import Utils from './Utils.js'

class addTheme extends React.Component {
    
    constructor(props) {
        super(props);       
        this.state = {
            name: "",
            description: "",
            abbreviation: "",
            message: "",
            loading: false
        };
    }
    
    static hasAccess() {
        return Permissions.canAddTheme();
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value  
        });
        console.log(name + " " + value);
    }

    validate() {
        if(!this.state.name.trim() || !this.state.description.trim() || !this.state.abbreviation.trim())
        {
            this.setState({locationName:""});
            return {name: ["Alle velden moeten worden ingevuld"]};
        }        
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true, message: ""}); 
        var errors = this.validate();
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/theme_concept/saveTheme", this.createThemeJson())
                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    // this.props.handleReturnToConcepts();
                    this.succesfullAdd();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    console.log(this.createThemeJson());

                    this.setErrors({login: ["Mislukt om thema toe te voegen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setState({errors: Utils.setErrors(errors)});
            this.setState({loading: false});
        }
    }

    succesfullAdd(){
        this.setState({ name:"",
                        description: "",
                        message:"Thema toegevoegd",
                        abbreviation: ""});
    }
    
    createThemeJson() {
        return {
            name: this.state.name,
            description: this.state.description,
            abbreviation: this.state.abbreviation,
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
                <h2 className="text-center">Thema toevoegen</h2>
                <div>{this.state.errors}</div>
                    <form onSubmit={this.handleSubmit}>
                        <div className="row justify-content-center">

                                <div className="col-4">
                                    <div className="form-group">
                                        <label htmlFor="name">Naam:</label>
                                        <input className="form-control" id="name" type="text" name="name" value={this.state.name} onChange={this.handleFormChange}/>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="description">Beschrijving:</label>
                                        <input className="form-control " id="description" type="text" name="description" value={this.state.description} onChange={this.handleFormChange}/>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="abbreviation">Afkorting:</label>
                                        <input className="form-control " id="abbreviation" type="text" name="abbreviation" value={this.state.abbreviation} onChange={this.handleFormChange} />
                                    </div>
                                {(this.state.loading) ? 
                                    <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>: 
                                    <button className="btn btn-primary float-right"  type="submit">Thema toevoegen</button>}
                                </div>
                        </div>

                    </form>
                    <h4 className="text-success text-center">{this.state.message}</h4>

                </div >

        )
    }
}

export default addTheme;