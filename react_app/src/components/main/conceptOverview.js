import React from 'react';
import axios from 'axios';

import {config} from '../constants';
import './search.css';

class conceptOverview extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
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
        this.getThemes()
        this.fakeHandleSearchReponse();
    }
    
    handleFormChange = (e) => {
        const {name, value} = e.target;
        this.setState({
           [name]: value 
        });
    }

    toggleActive() {   
    }
    
    handleSubmit = (event) => {
        event.preventDefault();
        this.setState({buttonDisabled: true});
        axios.post(config.url.API_URL + "/webapi/user/conceptFilter", this.createFilterJson())

            .then(response => {
                this.setState({buttonDisabled: false, errors: null});
                
                this.handleSearchReponse(response.data);
                this.render();
            })
            .catch((error) => {
                console.log("an error occorured " + error);
                this.fakeHandleSearchReponse();
                //const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({
                    buttonDisabled: false,
                    //errors: this.props.setErrors(custErr)
                    });
            });
    }

    handleSearchReponse(data)
    {
        this.setState({
            users: data.conceptsearch,
            //users: [{ id: 1, name: "Niels", email: "niels.vanrijn@hotmail.com", role: "Trainee", location: "Utrecht" }, { id: 2, name: "Quinten", email: "quinten@hotmail.com", role: "Trainee", location: "Utrecht" }]//data.date.users;
        });
    }

    fakeHandleSearchReponse() {
        this.setState({
            currentConcepts: [{ id: 7, week: 1, theme: "MySQL", name: "phpMyAdmin", active: true }, { id: 5, theme: "agile/scrum", name: "uses & goals", active: true } ]
        });
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
                <tr className="searchResult" onClick={(e) => {this.props.handleDossierRequest(e, concept.id)}} >
                    <td className="p-2 text-nowrap align-middle">
                        {concept.theme} 
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {concept.name}
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        Ja
                    </td>
                </tr >
            )
        });


        return (

            <div>
                <h2 className="text-center">concepten overzicht</h2>
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