
import React from 'react';
import axios from 'axios';
import { withRouter, Link } from 'react-router-dom'

import {config} from './constants';
import Permissions from './permissions.js';
import Utils from './Utils.js';
import {TextField} from '@material-ui/core'


class addBundle extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            message: "",
            loading: false,
            errors: null,
        };
    }

    static hasAccess() {
        return Permissions.canAddBundle();
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value,
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({loading: true}); 
        var errors = this.validate();
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/bundle/create", this.createBundleJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    this.succesfullAdd();
                    this.props.history.push('/conceptOverview');
                })
                .catch((error) => {
                    this.setState({loading: false, 
                        errors: Utils.setErrors({input: ["Mislukt om bundel toe te voegen. Mogelijk bestaat er al een bundel met deze naam."]})});
                });
        }
        else {
            this.setState({errors: Utils.setErrors(errors),loading: false});
        }
    }

    validate() {
        if(!this.state.name.trim())
        {
            this.setState({name:""});
            return {name: ["De bundelnaam mag niet leeg zijn"]};
        }
        if(!isNaN(this.state.name))
        {
            return {name: ["De bundelnaam moet letters bevatten"]}
        }
    }



    createBundleJson() {
        return {
            name: this.state.name,
            creator: {"id":sessionStorage.getItem("userId")}
        }
    }

    succesfullAdd(){
        this.setState({ message:"bundel aangemaakt", 
                        name: ""});
    }

    render() {

        return (
            <div className="container" >
                <h2 className="text-center">Bundel aanmaken</h2>

                <div className="row justify-content-center text-danger">{
                    this.state.errors}
                </div>

                <div className="row justify-content-center m-4">
                    <form onSubmit={this.handleSubmit}>

                    <div className="form-group">
                        <div className="form-group">
                            <label htmlFor="name">Naam:</label>
                            <TextField className="form-control" id="name" type="text" name="name" value={this.state.name} onChange={this.handleFormChange}/>
                        </div>
                    </div>

                    <div className="buttons">
                        {(this.state.loading) ? 
                        <button className="btn btn-danger btn-block" type="submit" disabled> Laden...</button>:
                        <button className="btn btn-danger btn-block" type="submit">
                            Maak aan
                        </button>}
                    
                        <button className="btn btn-danger btn-block" to={"/conceptOverview"}>
                            Annuleer
                        </button>
                    </div>
                    </form>
                </div>
                <h4 className="text-center text-success">{this.state.message}</h4>
            </div >

        )
    }

}


export default withRouter(addBundle);