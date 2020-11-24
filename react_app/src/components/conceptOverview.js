import React from 'react';
import axios from 'axios';

import {config} from './constants';
import './UserSearch/search.css';
import Permissions from './permissions.js'
import Utils from './Utils.js'
import { Checkbox} from '@material-ui/core';

import {withRouter} from 'react-router-dom';

class conceptOverview extends React.Component {

    constructor(props) {
        super(props);
        this.concepts = [];
        this.bundles = [];
        this.state = {
            pageLoading: true,
            errors: "",
            selectedBundle: "",
            selectedConceptIds:[]
        };
        this.onChangeBundle = this.onChangeBundle.bind(this);
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

    selectActiveConcepts(bundleId) {
        this.bundles.find(bundle => bundle.id === parseInt(bundleId));
        var conceptIdsInBundle = this.bundles.find(bundle => bundle.id === parseInt(bundleId)).list_of_concept_ids;
        // var conceptsInBundle = this.concepts.filter(concept => conceptIdsInBundle.includes(concept.id));
        this.setState({
            selectedConceptIds: conceptIdsInBundle,
        });        
    }
    // onChangeWeekBlock = (e) => {
    //     this.setState({
    //         block: this.state.blocks.find(loc => loc.id === parseInt(e.target.value)),
    //         blockDisplayName: e.target.value,
    //     });
    // }

    getActiveDisplayName(bool) {
        if (bool) return "ja";
        else return "nee";
    }

    render() {
        const bundleOptions = this.bundles.map((bundle) => {
            return (
                <option key={bundle.id} value={bundle.id}> {bundle.name}</option>
            )
        });

        // const {bundle,weekBlocks, pageLoading} = this.state;
        const {pageLoading} = this.state;
        if (pageLoading) return (<h2 className="center">Laden...</h2>)
        // if (weekBlocks === null) {
        //     return (<span className="center">Mislukt om pagina te laden.</span>)
        // }
  
        var conceptDisplay = this.concepts.map((concept) => {
        var selected = (this.state.selectedConceptIds.includes(concept.id)) ? true:false;
            return (
                <tr className={"searchResult " + (selected ? 'text-dark' : 'text-muted')} key={concept.id}>
                    <td>
                    <Checkbox
                         key={"active_"+concept.id}
                         checked={this.state.selectedConceptIds.includes(concept.id)}
                         color="red"
                        />                   
                    </td>
                    <td className="">
                        {concept.week}
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

            <div>
                <h2 className="text-center">Concepten overzicht</h2>
                <div >
                    <ul className="errors">{this.state.errors}</ul>

                    <div>
                        Bundel:
                        <select name="bundle" id="bundle"
                                value={this.state.bundle}
                                onChange={this.onChangeBundle}
                                required>
                                <option hidden value=''>Bundel</option>
                                {bundleOptions}
                            </select>

                    </div>
                </div >

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