import React from 'react';
import axios from 'axios';

import {config} from '../constants';
import './search.css';

class conceptOverview extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            userName: "",
            userLocation: null,
            themes: [],
            theme: "",
            active: false,
            blocks: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15'],
            block: '1',          
            allConcepts: [],
            currentConcepts: [],
            loading: false,
            themeDisplayName: "",
            blockDisplayName: '1',
            buttonDisabled: false,
        };
    }

    componentDidMount() {
        this.setState({ pageLoading: true });
        this.getThemes();
        this.getConcepts();
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({buttonDisabled: true});
        
    }

    createUserIdJson() {
        return {
            id: 1, //this.state.userId,
        };
    }

    getConcepts() {
        axios.post("http://localhost:8081" + "/webapi/review/curriculum", this.createUserIdJson())
            .then(response => {
                this.setState({ buttonDisabled: false, errors: null });

                this.handleCurriculumReponse(response.data);
                this.render();
            })
            .catch((error) => {
                console.log("an error occorured " + error);
                //this.fakeCurriculumResponse();
                //const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({
                    buttonDisabled: false,
                    //errors: this.props.setErrors(custErr)
                });
            });
    }

    handleCurriculumReponse(data) {
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation.name,
            currentConcepts: data.conceptsPlusRatings,
        });
        console.log(this.state);
    }

    fakeCurriculumResponse() {
        this.setState({
            userName: "Niels",
            userLocation: "Utrecht",
            currentConcepts: [{ id: 1, theme: { abbriviation: "OOP", name: "Object Oriented Programmeren", description: "beschrijving van OOP" }, name: "MVC", week: 5, rating: 4 }],
        })
        console.log(this.state);
    }

    getThemes() {
        axios.get(config.url.API_URL + '/webapi/user/themes')
            .then(response => {
                this.setState({
                    themes: response.data.themes,
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
    
    createFilterJson() {
        return {
            theme: this.state.theme,
            block: this.state.block,
            active: this.state.active
        }
    }

    onChangeTheme = (e) => {
        var selectedTheme = this.state.themes.find(theme => theme.id === parseInt(e.target.value));
        
        this.setState({
            theme: selectedTheme,
            themeDisplayName: e.target.value
        });
    }

    onChangeBlock = (e) => {
        this.setState({
            block: this.state.blocks.find(loc => loc.id === parseInt(e.target.value)),
            blockDisplayName: e.target.value,
        });
    }

    getActiveDisplayName(bool) {
        if (bool) return "ja";
        else return "nee";
    }

    render() {
        const {themes, blocks, pageLoading, buttonDisabled} = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)
        
        if (blocks === null || themes === null) {
            return (<span className="center">Mislukt om pagina te laden.</span>)
        }
        const themeOptions = themes.map((theme) => {
            return (
                <option key={theme.id} value={theme.id}>{theme.name}</option>
            )
        });

        var conceptDisplay = this.state.currentConcepts.map((concept) => {
            return (
                <tr className="searchResult" /* onClick={(e) => {this.props.handleDossierRequest(e, concept.id)}} */ >
                    <td className="p-3 text-nowrap align-middle">
                        {concept.concept.week}
                    </td>
                    <td className="abbreviationClass p-3 text-nowrap align-middle">
                        {concept.concept.theme.abbreviation}
                    </td>
                    <td className="p-3 text-nowrap align-middle">
                        {concept.concept.name}
                    </td>
                    <td className="p-3 text-nowrap align-middle">
                        Actief
                    </td>
                </tr >
            )
        });


        return (

            <div>
                <h2 className="text-center">Concepten overzicht</h2>
                <div >
                    <ul className="errors">{this.state.errors}</ul>
                    <form onSubmit={this.handleSubmit}>
                        <div className="w-100 mx-auto align-middle text-center"> 
                            <label className="mr-2 p-2 align-middle" htmlFor="theme">Thema:</label>
                            <select className="mr-5 p-2 align-middle" name="theme" id="theme"
                                value={this.state.themeDisplayName}
                                onChange={this.onChangeTheme}
                                required>

                                <option hidden value=''>Thema</option>
                                {themeOptions}
                            </select>
                            <label className="mr-2 p-2 align-middle" htmlFor="block">Blok:</label>
                            <input className="mr-5 p-2 align-middle" id="block" type="number" name="block" min="1" max="52" onChange={this.handleFormChange} />

                            <label className="mr-2 p-2 align-middle" htmlFor="criteria">Inactief zichtbaar:</label>
                            <input className="mr-5 p-2 align-middle" id="criteria" type="checkbox" name="criteria" onChange={this.toggleActive} />
                        </div>
                        
                        
                        <div className="text-center"> 
                            <button className="w-30 mx-auto btn rvtbutton mt-3" 
                                disabled={buttonDisabled} 
                                type="submit">
                                {(buttonDisabled)?"Laden...": "Filter"}
                            </button>
                        </div>
                    </form>                  
                </div >

                <div className="text-center">
                    <table className="w-100 mx-auto">
                        <thead>
                            <tr>
                                <th className="p-2 text-nowrap align-middle">
                                    Week
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Thema
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Concept
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Actief
                                    </th>
                            </tr>
                        </thead>
                        <tbody>
                        {conceptDisplay}
                        </tbody>
                    </table>
                </div >
            </div>
        )
    }
}

export default conceptOverview;