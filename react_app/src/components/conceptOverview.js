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
            selectedConceptIds:[],
            selectedConceptsWeekOffset:[]
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
                this.setState({pageLoading: false});
                // console.log(this.bundles)
            })
            .catch((error) => {
                const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({errors: Utils.setErrors(custErr)});
                this.setState({errors: Utils.setErrors(error)});
            });
    }

    onChangeBundle = (e) => {
        var bundleId = e.target.value;
        this.setState({
            selectedBundle: bundleId,
        });
        this.selectActiveConcepts(bundleId)
    }    
    onChangeWeek = (e) => {
        console.log(e.target.value);
    }

    selectActiveConcepts(bundleId) {
        this.bundles.find(bundle => bundle.id === parseInt(bundleId));
        var conceptIdsInBundle = this.bundles.find(bundle => bundle.id === parseInt(bundleId)).list_of_concept_ids;
        var conceptsWeekOffsetInBundle = this.bundles.find(bundle => bundle.id === parseInt(bundleId)).list_of_concept_week_offset;
        this.setState({
            selectedConceptIds: conceptIdsInBundle,
            selectedConceptsWeekOffset: conceptsWeekOffsetInBundle  
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
            var selected = (this.state.selectedConceptIds.includes(concept.id)) ? true:false;
            var weekoffset =  (this.state.selectedConceptIds.includes(concept.id)) ? this.state.selectedConceptsWeekOffset[concept.id-1] : "";
            return (
                    <tr className={"searchResult " + (selected ? 'text-black' : 'text-muted')} key={concept.id}>
                        <td>
                        <Checkbox
                            key={"active_"+concept.id}
                            checked={this.state.selectedConceptIds.includes(concept.id)}/>                   
                        </td>
                        <td className="">
                        <select className="m-1" name="week" id="week"
                                    value={weekoffset}
                                    onChange={this.onChangeWeek}
                                    required>
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