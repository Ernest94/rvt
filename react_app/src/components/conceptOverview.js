import React from 'react';
import axios from 'axios';

import {config} from './constants';
import './conceptOverview.css';
import Permissions from './permissions.js'
import Utils from './Utils.js'
import {Select, Checkbox} from '@material-ui/core';
import { FaPlus } from "react-icons/fa";

import {Link, withRouter} from 'react-router-dom';




class conceptOverview extends React.Component {

    constructor(props) {
        super(props);
        this.concepts = [];
        this.bundles = [];
        this.state = {
            pageLoading: true,
            errors: "",
            selectedBundle: "",
            selectedBundleCreator: "",
            activeConcepts:[],
            activeConceptsWeekOffset:[],
        };
        this.onChangeBundle = this.onChangeBundle.bind(this);
        this.onChangeWeek= this.onChangeWeek.bind(this);
        this.saveBundle = this.saveBundle.bind(this);
    }

    static hasAccess() {
        return Permissions.canSeeConceptOverview();
    }

    componentDidMount() {
        this.getConceptsAndBundles();
    }

    getConceptsAndBundles() {
        axios.get(config.url.API_URL + "/webapi/theme_concept/concepts/bundles")
            .then(response => {
                this.concepts = response.data.concepts;
                this.bundles = response.data.bundlesConcepts;
                this.setState({
                    pageLoading: false,
                });
            })
            .catch((error) => {
                const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({errors: Utils.setErrors(custErr)});
                this.setState({errors: Utils.setErrors(error)});
            });
    }

    onChangeBundle = (e) => {
        var bundleKeyId = parseInt(e.target.value);
        var bundleName = this.bundles.filter(bundle => bundle.id===bundleKeyId).map(bundle => bundle.name)[0];
        var bundleCreatorName = this.bundles.filter(bundle => bundle.id===bundleKeyId).map(bundle => bundle.creator_name)[0];

        this.setState({
            selectedBundle: bundleKeyId,
            selectedBundleName: bundleName,
            selectedBundleCreator: bundleCreatorName
        });
        this.selectActiveConcepts(bundleKeyId)
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

    selectActiveConcepts(bundleKeyId) {
        var activeBundleConceptsAndWeekOffsets = this.bundles.find(bundle => bundle.id === parseInt(bundleKeyId)).bundleConceptWeekOffset;       
        this.setState({
            activeConcepts:activeBundleConceptsAndWeekOffsets.map(element=> element.conceptId),
            activeConceptsWeekOffset:activeBundleConceptsAndWeekOffsets.map(element => element.weekOffset)
        });     
    }

    onChangeActive = (e) => {
        var idOfChangedConcept = e.target.id;;
        var indexChangedConceptInActiveConceptIds = this.state.activeConcepts.indexOf(parseInt(idOfChangedConcept.slice(7)));
       if (indexChangedConceptInActiveConceptIds>=0) {
        this.setState({
            activeConcepts: this.state.activeConcepts.filter(function(value,index,arr) { 
                return index !== indexChangedConceptInActiveConceptIds}),
            activeConceptsWeekOffset: this.state.activeConceptsWeekOffset.filter(function(value, index, arr) { 
                return index !== indexChangedConceptInActiveConceptIds})
        });
        } else {
            this.setState(previousState => ({
                activeConcepts: [...previousState.activeConcepts, parseInt(idOfChangedConcept.slice(7))],
                activeConceptsWeekOffset: [...previousState.activeConceptsWeekOffset,0],
            }));        
        }
    }
    
    onChangeWeek = (e) => {
        var indexChangedConceptInActiveConceptIds = this.state.activeConcepts.indexOf(parseInt(e.target.id.slice(7)));
        let newActiveConceptsWeekOffset = this.state.activeConceptsWeekOffset;
        newActiveConceptsWeekOffset[indexChangedConceptInActiveConceptIds] = parseInt(e.target.value);
        this.setState({activeConceptsWeekOffset: newActiveConceptsWeekOffset});
    }

    bundleJSON() {
        var activeConceptsAndWeekOffset=[];
        var activeConcept;
        for (activeConcept of this.state.activeConcepts) {
            var indexChangedConceptInActiveConceptIds = this.state.activeConcepts.indexOf(activeConcept);
            activeConceptsAndWeekOffset.push({
                bundleId:this.state.selectedBundle,
                conceptId:activeConcept,
                weekOffset:this.state.activeConceptsWeekOffset[indexChangedConceptInActiveConceptIds]});
        }
        return activeConceptsAndWeekOffset
    }

    saveBundle() {
        axios.post(config.url.API_URL + "/webapi/bundle/change", this.bundleJSON())
        .then(response => {
            this.setState({
                message: "De wijzigingen in de bundel zijn verwerkt"
            });
            this.props.history.push('/settings');
        })
        .catch((error) => {
            this.setState({errors: ["Mislukt om bundel op te slaan"]}); 
            console.log("an error occurred " + error);
        });
    }


    handleAddBundle() {
        this.props.history.push('/addBundle');
    }

    render() {
        const bundleOptions = this.bundles.map((bundle) => {
            return (
                <option key={bundle.id} value={bundle.id}> {bundle.name + " (" + bundle.creator_name + ")"}</option>
            )
        });

        const weekOptions = [0,1,2,3,4,5,6,7,8,9,10,11,12].map((weekOffset) => {
            return (
                <option key={weekOffset} value={weekOffset}> {"Startweek " + (weekOffset ? "+ " + weekOffset : "")}</option>
            )
        });

        if (this.state.pageLoading) return (<h2 className="center">Laden...</h2>)

        var conceptDisplay = this.concepts.map((concept) => {
            var selected = (this.state.activeConcepts.includes(concept.id)) ? true:false;
            var weekoffset =  (this.state.activeConcepts.includes(concept.id)) ? this.state.activeConceptsWeekOffset[this.state.activeConcepts.indexOf(concept.id)]: "";
            return (
                    <tr className={"searchResult " + (selected ? 'text-black' : 'text-muted')} key={concept.id}>
                        <td>
                        <Checkbox className="" 
                            id={"concept"+concept.id}
                            checked={selected}
                            onChange={this.onChangeActive}
                            disabled={sessionStorage.getItem("userRole")!=="Admin"&&sessionStorage.getItem("userName")!==this.state.selectedBundleCreator}
                            />                   
                        </td>
                        <td className="">
                        <select className="p-2" name="week" 
                                    id={"concept"+concept.id}
                                    value={weekoffset}
                                    onChange={this.onChangeWeek}
                                    required
                                    disabled={!selected||sessionStorage.getItem("userRole")!=="Admin"&&sessionStorage.getItem("userName")!==this.state.selectedBundleCreator}>
                                    <option hidden value=''></option>
                                    {weekOptions}
                        </select>
                        </td>
                        <td className="" id="text">
                                <span className="concept-theme-text conceptOverview-theme-text"> 
                                    <span className="no-wrap">{concept.theme.name}</span>
                                    <span className="displayMessage"> {concept.theme.description} </span>
                                </span>
                            </td>
                        <td className="" id="text">
                            <span className="concept-theme-text conceptOverview-concept-text"> 
                                <span className="no-wrap">{concept.name}</span>
                                <span className="displayMessage"> {concept.description} </span>
                            </span>
                            </td>
                    </tr >
            )
        });

        return (

            <div className="container">

                <h2 className="text-center">Concepten overzicht</h2>
                
                <div className="row justify-content-center">
                        <ul className="errors text-center">{this.state.errors}</ul>
                </div>

                <div className="row justify-content-lg-center">
                    
                    <div className="col-2 col-lg-6">
                        Selecteer een bundel:
                        <Select className="m-auto col-6" name="bundle" id="bundle"
                                value={this.state.bundle}
                                onChange={this.onChangeBundle}
                                required>
                                <option hidden value=''></option>
                                {bundleOptions}
                        </Select>
                    </div>
                    <div className="col-2">
                        <span>
                            <Link className="btn btn-danger" 
                                to={{pathname:"/addBundle/",                                
                                    state:{bundleId:-1}}}>

                                <FaPlus/>
                            </Link>
                        </span>
                    </div>

                    <div className="col">
                        {(this.state.selectedBundle!=="") ? 
                        <span>
                            <Link className="btn btn-danger" 
                                to={{pathname:"/addBundle/",                                
                                    state:{
                                            bundleId:this.state.selectedBundle,
                                            bundleName:this.state.selectedBundleName,
                                            bundleCreator:this.state.selectedBundleCreator}}}>
                                Dupliceer bundel
                            </Link>
                        </span>:<span></span>}
                    </div>
                    <div className="col">
                        {(this.state.selectedBundle!==""&&(this.state.selectedBundleCreator===sessionStorage.getItem("userName") 
                            || "Admin"===sessionStorage.getItem("userName"))) ? 
                        <button className="btn btn-danger bundle-submit-button" onClick={this.saveBundle}> 
                            Bundel opslaan
                        </button>: <span></span>}
                    </div>

                </div>

                <div className="container mt-4">
                    <div className="row justify-content-center">
                    <div className="col-9 text-center table-responsive">
                        <table className="concept-overview-table">
                            <thead>
                                <tr>
                                    <th className="">
                                        Actief
                                        </th>
                                    <th className="">
                                        Week
                                        </th>
                                    <th className="">
                                        Thema
                                        </th>
                                    <th className="">
                                        Concept
                                        </th>
                                </tr>
                            </thead>
                            <tbody className="tableBody table">
                                {conceptDisplay}
                            </tbody>
                        </table>
                    </div >

                </div>
                </div>
            </div>
        )
    }
}

export default withRouter(conceptOverview);