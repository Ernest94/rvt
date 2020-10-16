import React from 'react';
import axios from 'axios';

import {config} from '../constants';
import './search.css';

class traineeSpecificOverview extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            userId: null,
            userName: "",
            userLocation: "",
            concepts: [],
            pageLoading: false,

        };
    }

    async componentDidMount() {
        this.setState({ pageLoading: true });
        if (this.props.isTrainee) {
            this.setState({ userId: this.props.getUserId() });
        }
        else {
            const { computedMatch: { params } } = this.props;
            await this.setState({ userId: params.userId });
        }
        this.getConcepts();
        console.log(this.state.userId);
        this.setState({ pageLoading: false });
    }
    

    getConcepts() {
        axios.post(config.url.Api_URL + "/webapi/concepts/getCurriculum", this.createUserIdJson())
            .then(response => {

                this.handleCurriculumReponse(response.data);
            })
            .catch((error) => {
                this.fakeCurriculumResponse();
                console.log("an error occorured " + error);
            });
    }

    createUserIdJson() {
        return {
            userId: this.state.userId,
        };
    }

    handleCurriculumReponse(data)
    {
        this.setState({
            userName: data.name,
            userLocation: data.location.name,
            concepts: data.concepts,
        });
    }

    fakeCurriculumResponse() {
        this.setState({
            userName: "Niels",
            userLocation: "Utrecht",
            concepts: [{ theme: { abbriviation: "OOP", name: "Object Oriented Programmeren", description: "beschrijving van OOP" }, name: "MVC", blok: "week 5/6", rating: 4 }],
        })
        console.log(this.state);
    }
    
    getActiveDisplayName(bool) {
        if (bool) return "ja";
        else return "nee";
    }

    render() {
        const {pageLoading} = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        var conceptDisplay = this.state.concepts.map((concept) => {
            return (
                <tr>
                    <td className="p-2 text-nowrap align-middle">
                        {concept.theme.abbriviation} 
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {concept.name}
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {concept.blok}
                    </td>
                    <td className="p-2 text-nowrap align-middle">
                        {concept.rating}
                    </td>
                </tr >
            )
        });


        return (

            <div>
                <h2 className="text-center">Curriculum {this.state.userName}</h2>
                <h2 className="text-center">{this.state.userLocation}</h2>
                <div >
                    <ul className="errors">{this.state.errors}</ul>                 
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
                                    Blok
                                    </th>
                                <th className="p-2 text-nowrap align-middle">
                                    Waardering
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

export default traineeSpecificOverview;