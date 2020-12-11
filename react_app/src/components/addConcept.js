import React from 'react';
import axios from 'axios';
import {config} from './constants';
import Permissions from './permissions.js'
import './form.css'
import Utils from './Utils.js'
import { Link } from 'react-router-dom';
import { FaPlus, FaTimes } from "react-icons/fa";

class BundleConceptTable extends React.Component {

    render() {
        const weeks = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        const weekOptions = weeks.map((week) => (
            <option key={"week_" + week} value={week}>
                {"startweek + " + week}
            </option>))

        const bundleOptions =
            this.props.bundles.map((bundle, index) => (
                <option key={"bundleChoice" + index} value={bundle.id}>
                    {bundle.name}
                </option>))

        return (
            <div className="col-sm-9">
                <table className="bundleTable dossier">
                    <tbody>
                        {this.props.chosenBundles.map((chosenBundle, index) =>
                            (this.props.editDisabled ?
                                <tr key={"bundleSelect_" + index}>
                                    <td>{chosenBundle.bundle.name}</td>
                                    <td>Start: week {chosenBundle.week}</td>
                                    <td></td>
                                </tr>
                                :
                                <tr className="row" key={"bundleSelect_" + index}>
                                    <td>
                                        <select className="form-control"
                                            id={"week_" + index}
                                            name="startWeek"
                                            value={chosenBundle.week}
                                            onChange={(e) => this.props.handleBundleWeekChange(e)}
                                        >
                                            <option value="-1" hidden>Kies een startweek</option>
                                            {weekOptions}
                                        </select>
                                    </td>
                                    <td>
                                        <select className="form-control"
                                            id={"bundle_" + index}
                                            name="bundle"
                                            value={chosenBundle.bundle.id ? chosenBundle.bundle.id : -1}
                                            onChange={(e) => this.props.handleBundleChange(e)}
                                        >
                                            <option value="-1" hidden>Kies een bundel</option>
                                            {bundleOptions}
                                        </select>
                                    </td>
                                    <td>
                                        <button className="btn btn-danger btn-sm" type="button" onClick={(e) => this.props.removeBundle(e, index)}>
                                            <FaTimes />
                                        </button>
                                    </td>
                                </tr>))}
                    </tbody>
                </table>
                {!this.props.editDisabled ?
                    <button className="btn btn-danger btn-sm" name="add" type="button" onClick={this.props.addBundle}>
                        <FaPlus />
                    </button> : ""}
            </div>)
    }
}

class addConcept extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            name: "",
            description: "",
            theme: null,
            themes: [],
            startDate: null,
            loading: false,
            message:"",
            themeDisplayName: "",
            userId: null,
            bundles: [],
            bundleCount: 3,
            chosenBundles: [],
            counter: 0
        };
        this.onChangeTheme= this.onChangeTheme.bind(this);
        }

    static hasAccess() {
        return Permissions.canAddConcept();
    }

    async componentDidMount() {
        this.getThemes()
        
        this.setState({
            userId: sessionStorage.getItem("userId")
        });
        this.getYourBundles()
        console.log("State:", this.state)
    }

    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
            [name]: value,
            message: ""
        });
    }
    validate() {
        if(!this.state.name.trim() || !this.state.description.trim() || this.state.themeDisplayName==="")
        {
            return {input: ["Alle velden moeten worden ingevuld"]};
        }        
    }

    handleSubmit = (event) => {
        console.log(this.createConceptJson())
        event.preventDefault();
        this.setState({loading: true}); 
        var errors = this.validate();
        if (!errors) {
            axios.post(config.url.API_URL + "/webapi/theme_concept/saveConcept", this.createConceptJson())  
                .then(response => {
                    this.setState({loading: false, errors: null});
                    this.succesfullAdd();
                })
                .catch((error) => {
                    this.setState({loading: false, 
                        errors: Utils.setErrors({input: ["Mislukt om concept toe te voegen. Mogelijk bestaat er al een concept met deze naam."]})});
                });
        }
        else {
            this.setState({
                errors: Utils.setErrors(errors),
                loading: false
            });
        }
    }
    
    createConceptJson() {
        return {
            concept:{ name: this.state.name, description: this.state.description, theme: { id: this.state.theme.id }},
            bundles: this.state.chosenBundles,
        }
    }

    succesfullAdd(){
        this.setState({ 
                        themeDisplayName:"",
                        name:"",
                        description:"",
                        message:"Concept toegevoegd",
                        startDate:"",
                        week:""
        });
    }


    onChangeTheme = (e) => {
        var selectedTheme = this.state.themes.find(theme=> theme.id === parseInt(e.target.value));
        this.setState({
            theme: selectedTheme,
            themeDisplayName: e.target.value
        });
    }

    // handleChangeDate = (e) => {
    //     var selectDate = (e.target.value).toString();
    //         this.setState({
    //             startDate: selectDate,
    //         }); 
    // }

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
                    pageLoading: false
                });
            })
    }

    getYourBundles() {

        if (Permissions.isUserAdmin()) {
            axios.get(config.url.API_URL + '/webapi/bundle/bundles')
                .then(response => {
                    console.log("Response:", response);
                    this.handleBundleResponse(response.data);
                })
                .catch(() => {
                    console.log("error");
                })
        }
        else {
            axios.get(config.url.API_URL + '/webapi/bundle/getCreatorBundles/' + this.state.userId)
                .then(response => {
                    console.log("Repsponse:", response);
                    this.handleBundleResponse(response.data);
                })
                .catch(() => {
                    this.setState({
                        pageLoading: false
                    });
                    console.log("error");
                })
        }
    }

    handleBundleResponse(data) {
        if (data === "") {
            this.upCounter();
            this.getYourBundles();
            return;
        }
        
        this.setState({
            bundles: data,
        });
    }

    upCounter() {
        let counter = this.state.counter + 1;
        this.setState({
            counter: counter,
        });
        console.log(this.state.counter);
    }
    
    // setErrors = (errors) => {
    //     const foundErrors = Object.keys(errors).map((key) =>
    //         <li key={key}>{errors[key][0]}</li>
    //     );
    //     this.setState({
    //        errors: foundErrors 
    //     });
    // }

    addBundle() {
        this.setState((prevState) => ({ chosenBundles: [...prevState.chosenBundles, { bundle: { id: -1 }, week: 0 }] }));
    }

    removeBundle(e, index) {
        this.setState((prevState) => ({ chosenBundles: [...prevState.chosenBundles.slice(0, index), ...prevState.chosenBundles.slice(index + 1)] }));
    }

    handleBundleChange(e) {

        let allBundles = this.state.bundles;
        const index = e.target.id.substring(7);
        const value = +e.target.value;

        var selectedBundleIndex = allBundles.findIndex(bundle => bundle.id === value);

        let bundles = this.state.chosenBundles;
        let bundle = bundles[index];
        bundle.bundle = allBundles[selectedBundleIndex];
        bundles[index] = bundle;
        this.setState({
            chosenBundles: bundles
        });
        console.log(this.state.chosenBundles);
    }

    handleBundleWeekChange(e) {

        const index = e.target.id.substring(5);
        const value = e.target.value;
        console.log(index);
        console.log(value);

        let bundles = this.state.chosenBundles;
        let bundle = bundles[index];
        bundle.week = value;
        bundles[index] = bundle;
        this.setState({
            chosenBundles: bundles
        });
        console.log(this.state.chosenBundles);
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

                <h2 className="text-center ">Concept toevoegen</h2>
                <div className="text-danger" >{this.state.errors}</div>

                    <form onSubmit={this.handleSubmit}>
                        
                        <div className="row m-2 mt-4 justify-content-center">
                            <label className="col-2" htmlFor="name">Naam:</label>
                            <div className="col-3">
                                <input id="name" type="text" name="name" value={this.state.name} onChange={this.handleFormChange}/>
                            </div>
                        </div>

                        <div className="row m-2 justify-content-center">
                            <label className="col-2" htmlFor="description">Beschrijving:</label>
                            <div className="col-3">
                                <input id="description" type="text" name="description" value={this.state.description} onChange={this.handleFormChange}/>
                            </div>
                        </div>

                        <div className="row m-2 justify-content-center">
                            <label className="col-2" htmlFor="theme">Thema:</label>
                            <div className="col-3">
                                <select name="theme" id="theme"

                                    value={this.state.themeDisplayName}
                                    onChange={this.onChangeTheme}
                                    required>
                                    <option hidden value=''></option>
                                    {themeOptions}
                                </select>
                            </div>
                        </div>
                        <div className="row m-2 justify-content-center">
                            <label className="label col col-form-label" htmlFor="bundles">Bundels:</label>
                            <div>
                                <BundleConceptTable
                                    chosenBundles={this.state.chosenBundles}
                                    bundles={this.state.bundles}
                                    removeBundle={this.removeBundle.bind(this)}
                                    handleBundleChange={this.handleBundleChange.bind(this)}
                                    handleBundleWeekChange={this.handleBundleWeekChange.bind(this)}
                                    addBundle={this.addBundle.bind(this)}
                                />
                            </div>
                        </div>
                        <div className="row justify-content-center">
                            <div className="col-6 m">
                                <div className="float-right">
                                    {(this.state.loading) ? <button className="btn btn-primary" type="submit" disabled> Laden...</button>:
                                    <button className="btn btn-primary" type="submit">Concept toevoegen</button>}
                                </div>
                            </div>
                        </div>
                        </form>

                        <div className="row justify-content-center">
                            <div className="col-6">
                                <div className="float-right m-1">
                                    {(this.state.loading) ? <button className="btn btn-primary" disabled> Laden...</button>:
                                    <Link to={"/settings"} className="btn btn-primary">Annuleren</Link>}
                                </div>
                            </div>
                        </div>
                        
                    <h4 className="text-center text-success">{this.state.message}</h4>
                </div >
            </div>
        )
    }
}

export default addConcept;