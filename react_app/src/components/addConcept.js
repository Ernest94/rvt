import React from 'react';
import axios from 'axios';
import {Select, MenuItem } from '@material-ui/core';
import {config} from './constants';
import Permissions from './permissions.js'
import './form.css'
import Utils from './Utils.js'
import {Link} from 'react-router-dom';

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
            name: this.state.name,
            description: this.state.description,
            theme: { id: this.state.theme.id },
            //bundleConcept: this.state.chosenBundles,
            // week: this.state.week,
            // startDate: this.state.startDate
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

    add = (e) => {
        let chosenBundles = this.state.chosenBundles;
        chosenBundles.push({ bundle: this.state.bundles[0], week: 0 });
        console.log(chosenBundles);

        this.setState({
            chosenBundles: chosenBundles,
        }) 
    }

    remove = (e) => {
        let chosenBundles = this.state.chosenBundles.slice(0, -1)
        console.log(chosenBundles);

        this.setState({
            chosenBundles: chosenBundles,
        })     
    }

    handleBundleChange(e) {

        const index = e.target.name.substring(7);
        const value = e.target.value;
        console.log(index);
        console.log(value);

        let bundles = this.state.chosenBundles;
        let bundle = bundles[index];
        bundle.bundle = value;
        bundles[index] = bundle;
        this.setState({
            chosenBundles: bundles
        });
        console.log(this.state.chosenBundles);
    }

    handleBundleWeekChange(e) {

        const index = e.target.name.substring(5);
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

        const chosenBundles = this.state.chosenBundles

        const themeOptions = this.state.themes.map((theme) => {
            return (
                <option key={theme.id} value={theme.id}>{theme.name}</option>
            )
        });

        const bundleOptions = this.state.bundles.map((bundle) =>(
            <MenuItem key={bundle.id} value={bundle}>
                {bundle.name}
            </MenuItem>
        ))

        const weeks = [0,1,2,3,4,5,6,7,8,9,10,11,12];
        const weekoptions = weeks.map((week) =>(
                            <MenuItem key={"week_"+week} value={week}>
                                {"week " + week}
                            </MenuItem>))

        const bundleContent = chosenBundles.map((bundle, index) => {    
            return (
                <tr>
                    <td>
                        <Select  name={"weeks" + index} id={"weeks" + index}
                            value={bundle.week}
                            onChange={(e)=>this.handleBundleWeekChange(e)}
                            >
                            {weekoptions}
                        </Select></td>
                    <td>
                        <Select  name={"bundles" + index} id={"bundles" + index}
                            value={bundle.bundle}
                            onChange={(e) => this.handleBundleChange(e)}
                            displayEmpty
                            >
                            {bundleOptions}
                        </Select>
                    </td>
                </tr>
            )
        });

        const bundleTable = (
            <table>
                <thead>
                    <tr>
                        <th>
                            Week
                        </th>
                        <th>
                            Bundle
                        </th>
                    </tr>
                    {bundleContent}
                </thead>
            </table>
        );

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

                        {/* <div className="row m-2 justify-content-center">
                            <label className="col-2" htmlFor="week">Week:</label>
                            <div className="col-3">
                                <input className="col-4" id="week" type="number" name="week" min="1" value={this.state.week} onChange={this.handleFormChange} />
                            </div>
                        </div> */}

                        {/* <div className="row m-2 mb-4 justify-content-center">
                            <label className="col-2" htmlFor="date">Startdatum:</label>
                            <div className="col-3">
                                <input className="col-9" id="date" type="date" name="date" value={this.state.startDate} onChange={this.handleChangeDate} />
                            </div>
                        </div> */}

                        <div>
                            <button type="button" onClick={this.add}>add</button>
                            <button type="button" onClick={this.remove}>remove</button>
                        </div>

                        {bundleTable}

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