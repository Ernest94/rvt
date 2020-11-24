import React from 'react';
import axios from 'axios';

import {config} from './constants';
import './UserSearch/search.css';
import Permissions from './permissions.js'
import Utils from './Utils.js'

import {withRouter} from 'react-router-dom';

class conceptOverview extends React.Component {

    constructor(props) {
        super(props);
        this.concepts = [];
        this.bundles = [];
        this.state = {
            pageLoading: true,
            errors: null,
            bundle: null
        };
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
                console.log(response)
                this.concepts = response.data.concepts;
                this.bundles = response.data.bundlesConcepts;
                this.setState({pageLoading: false});
            })
            .catch((error) => {
                const custErr = {search: ["Mislukt om zoek actie uit te voeren."]};
                this.setState({errors: Utils.setErrors(custErr)});
            });
    }

    onChangeBundle = (e) => {
        var selectedBundle = this.bundles.find(bundle => bundle.id === parseInt(e.target.value));
        this.setState({bundle: selectedBundle.name});
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
        const {weekBlocks, pageLoading} = this.state;
        const bundles = this.bundles;
        if (pageLoading) return (<h2 className="center">Laden...</h2>)

        if (weekBlocks === null || bundles === null) {
            return (<span className="center">Mislukt om pagina te laden.</span>)
        }
        const bundleOptions = bundles.map((bundle) => {
            return (
                <option key={bundle.id} value={bundle.id}>{bundle.name}</option>
            )
        });

        var conceptDisplay = this.concepts.map((concept) => {
            return (
                <tr className="searchResult" key={concept.id} /* onClick={(e) => {this.props.handleDossierRequest(e, concept.id)}} */ >
                    <td className="">
                        Actief
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
                        bundel:
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