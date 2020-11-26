import React from 'react';
import axios from 'axios';

import {config} from './constants';
import './UserSearch/search.css';
import Permissions from './permissions.js'
import Utils from './Utils.js'
import { Checkbox} from '@material-ui/core';
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
            activeConcepts:[],
            activeConceptsWeekOffset:[],
            activeConceptsAndWeekOffset:[],
        };
        this.onChangeBundle = this.onChangeBundle.bind(this);
        this.onChangeWeek= this.onChangeWeek.bind(this);
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
            console.log(response.data);
            })
            .catch((error) => {
                const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({errors: Utils.setErrors(custErr)});
                this.setState({errors: Utils.setErrors(error)});
            });
    }

    onChangeBundle = (e) => {
        var bundleKeyId = e.target.value;
        this.setState({
            selectedBundle: bundleKeyId,
        });
        this.selectActiveConcepts(bundleKeyId)
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
        console.log(idOfChangedConcept)
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
        newActiveConceptsWeekOffset[indexChangedConceptInActiveConceptIds] = e.target.value;
        this.setState({activeConceptsWeekOffset: newActiveConceptsWeekOffset});
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
                        <Checkbox
                            id={"concept"+concept.id}
                            checked={selected}
                            onChange={this.onChangeActive}
                            />                   
                        </td>
                        <td className="">
                        <select className="m-1" name="week" 
                                    id={"concept"+concept.id}
                                    value={weekoffset}
                                    onChange={this.onChangeWeek}
                                    required
                                    disabled={!selected}>
                                    <option hidden value=''></option>
                                    {weekOptions}
                        </select>
                        </td>
                        <td className="">
                            {concept.theme.name}
                        </td>
                        <td className="">
                            {concept.name}
                        </td>
                    </tr >
            )
        });


        return (

            <div className="container">

                <h2 className="text-center">Concepten overzicht</h2>
                
                <div className="row"> 
                    <ul className="errors">{this.state.errors}</ul>
                </div>

                <div className="row">
                    <div className="col-4">
                        Bundel:
                        <select className="m-1" name="bundle" id="bundle"
                                value={this.state.bundle}
                                onChange={this.onChangeBundle}
                                required>
                                <option hidden value=''>Bundel</option>
                                {bundleOptions}
                            </select>
                    </div>
                    <div className="col-8">
                        <Link className="btn btn-primary float-left" to={"/addBundle/"}>  {/* hidden={} */}
                            <FaPlus/>
                        </Link>
                   </div>
                </div>

                <div className="container m-4">
                <div className="text-center">
                    <table className="w-100 mx-auto">
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
                        <tbody>
                        {conceptDisplay}
                        </tbody>
                    </table>
                </div >
                </div>
            </div>
        )
    }
}

export default withRouter(conceptOverview);