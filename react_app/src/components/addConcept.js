import React from 'react';
import axios from 'axios';

import {config} from './constants';
import Permissions from './permissions.js'

class addConcept extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            description: "",
            theme: null,
            themes: [],
            date: null,
            week: 1,
            loading: false,
            message:"",
            themeDisplayName:""
        };
    }

    static hasAccess() {
        return Permissions.canAddConcept();
    }

    componentDidMount() {
        this.getThemes()
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value,
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
            console.log(this.createConceptJson());
            axios.post(config.url.API_URL + "/webapi/theme_concept/saveConcept", this.createConceptJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    
                    //this.props.handleReturnToConcepts();
                    this.succesfullAdd();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);
                    console.log(this.createConceptJson());

                    this.setErrors({login: ["Mislukt om concept toe te voegen."]}); 
                    this.setState({loading: false});
                });
        }
        else {
            this.setErrors(errors);
            this.setState({loading: false});
        }
    }
    
    createConceptJson() {
        return {
            name: this.state.name,
            description: this.state.description,
            theme: {id: this.state.theme.id},
            week: this.state.week,
            startDate: this.date
        }
    }

    succesfullAdd(){
        this.setState({ name:"",
                        description: "",
                        message:"Concept toegevoegd",
                        startDate:"",
                        week:"",
                        themeDisplayName: ""});
    }

    onChangeTheme = (e) => {
        var selectedTheme = this.state.themes.find(theme=> theme.id === parseInt(e.target.value));
        this.setState({
            theme: selectedTheme,
            themeDisplayName: e.target.value
        });
    }

    handleChangeDate = (e) => {
        var selectDate = (e.target.value).toString();
            this.setState({
                date: selectDate,
            }); console.log(selectDate);
    }

    getThemes() {
        axios.get(config.url.API_URL + '/webapi/theme_concept/themes')
            .then(response => {
                this.setState({
                    themes: response.data, 
                    pageLoading: false
                });
            })
            .catch(() => {
                this.setState({
                    themes: [{ id: 1, name: "MySQL" }, { id: 2, name: "webbasis" }, { id: 3, name: "agile/scrum" }],
                    pageLoading: false
                });
            })
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

        const themeOptions = this.state.themes.map((theme) => {
            return (
                <option key={theme.id} value={theme.id}>{theme.name}</option>
            )
        });

        return (
            <div> 
                <div className="container main-container">
                <h2 className="text-center">Concept toevoegen</h2>

                    <form onSubmit={this.handleSubmit}>
                        <div className="row">
                            <div className="col-6 ">
                                <div className="float-right">
                                    <div className="form-group">
                                        <label htmlFor="name">Naam:</label>
                                        <input className="form-control" id="name" type="text" name="name" value={this.state.name} onChange={this.handleFormChange}/>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="description">Beschrijving:</label>
                                        <input className="form-control " id="description" type="text" name="description" value={this.state.description} onChange={this.handleFormChange}/>
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="theme">Thema:</label>
                                        <select className="m-2 p-2" name="theme" id="theme"
                                            value={this.themeDisplayName}
                                            onChange={this.onChangeTheme}
                                            required>

                                            <option hidden value=''>Thema</option>
                                            {themeOptions}
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div className="col-6 ">
                                <div className="float-left">

                                    <div className="form-group col-6">
                                        <label htmlFor="week">Week:</label>
                                        <input className="form-control " id="week" type="number" name="week" min="1" value={this.state.week} onChange={this.handleFormChange} />
                                    </div>

                                    <div className="form-group">
                                        <label htmlFor="date">Startdatum:</label>
                                        <input className="form-control col-10" id="date" type="date" name="date" value={this.state.startDate} onChange={this.handleChangeDate} />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row justify-content-center">
                            <div className="col-6 ">
                                <div className="float-right">
                                    {(this.state.loading) ? <button className="btn btn-primary" type="submit" disabled> Laden...</button>:
                                    <button className="btn btn-primary" type="submit">Concept toevoegen</button>}
                                </div>
                            </div>
                        </div>

                    </form>
                    <h4 className="text-center text-success">{this.state.message}</h4>
                </div >
            </div>
        )
    }
}

export default addConcept;