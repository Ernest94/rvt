import React from 'react';
import axios from 'axios';

import {config} from '../constants';

class addTheme extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            description: "",
            abbreviation: "",
            loading: false
        };
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value  
        });
        console.log(name + " " + value);
    }

    validate() {
        return null;
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true}); 
        var errors = this.validate();
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/user/addTheme", this.createThemeJson())
                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    this.props.handleReturnToSettings();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    console.log(this.createThemeJson());

                    this.setErrors({login: ["Mislukt om thema toe te voegen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }
    
    createThemeJson() {
        return {
            name: this.state.name,
            description: this.state.description,
            abbreviation: this.state.abbreviation
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
                <ul className="errors">{this.state.errors}</ul>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Naam:</label>
                        <input className="form-control" id="name" type="text" name="name" onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Beschrijving:</label>
                        <input className="form-control " id="description" type="text" name="description" onChange={this.handleFormChange}/>
                    </div>

                    <div className="form-group">
                        <label htmlFor="abbreviation">Afkorting:</label>
                        <input className="form-control " id="abbreviation" type="text" name="abbreviation" onChange={this.handleFormChange} />
                    </div>

                    {(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>: 
                    <button className="btn btn-primary float-right" type="submit">Thema toevoegen</button>}
                </form>
            </div >
        )
    }
}

export default addTheme;