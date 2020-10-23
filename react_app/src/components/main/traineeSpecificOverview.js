import React from 'react';
import axios from 'axios';
import Rating from '@material-ui/lab/Rating';
import './traineeSpecificOverview.css'
//import Box from '@material-ui/core/Box';

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
            weeksPerBlock: 2,
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
        console.log(this.createUserIdJson());
        axios.post("http://localhost:8081" + "/webapi/review/curriculum", this.createUserIdJson())
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
            id: 1, //this.state.userId,
        };
    }

    handleCurriculumReponse(data)
    {
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation.name,
            concepts: data.conceptsPlusRatings ,
        });
        console.log(this.state);
    }

    fakeCurriculumResponse() {
        this.setState({
            userName: "Niels",
            userLocation: "Utrecht",
            concepts: [{ id: 1, theme: { abbriviation: "OOP", name: "Object Oriented Programmeren", description: "beschrijving van OOP" }, name: "MVC", week: 5, rating: 4 }],
        })
        console.log(this.state);
    }
    
    getActiveDisplayName(bool) {
        if (bool) return "ja";
        else return "nee";
    }

    getRating(rating) {
        switch (rating) {           
            case 1: return ("Matig");
            case 2: return ("Redelijk");
            case 3: return ("Voldoende");
            case 4: return ("Goed");
            case 5: return ("Uitstekend");
            default: return ("Geen Rating");
        }
    }

    getWeekBlock(week) {
        const wpb = this.state.weeksPerBlock
        var devidedweek = Math.ceil(week / wpb);
        switch (devidedweek) {
            case 1: return ("week " + 1 + " t/m " + wpb);
            case 2: return ("week " + (1 + wpb) + " t/m " + (2 * wpb));
            case 3: return ("week " + (1 + 2 * wpb) + " t/m " + (3 * wpb));
            case 4: return ("week " + (1 + 3 * wpb) + " t/m " + (4 * wpb));
            default: return ("week 9 of later");
        }
    }

    render() {
        const {pageLoading} = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        var conceptDisplay = this.state.concepts.map((concept) => {
            return (
                <tr>
                    <td className="p-1 text-nowrap align-middle">
                        {this.getWeekBlock(concept.concept.week)}
                    </td>
                    <td className="abbreviationClass p-1 text-nowrap align-middle">
                        {concept.concept.theme.abbreviation} 
                    </td>
                    <td className="p-1 text-nowrap align-middle">
                        {concept.concept.name}
                    </td>                  
                    <td className="p-1 text-nowrap align-middle">                 
                        <Rating
                            value={concept.rating}
                            name="rating"
                            readOnly="true"
                        />
                    </td>
                </tr >
            )
        });

        
        return (

            <div className="reviewBody">
                <div className="test1">
                    <h2 className="reviewText">Review {this.state.userName}</h2>
                    <h2 className="reviewText">{this.state.userLocation}</h2>
                    <div >
                        <ul className="errors">{this.state.errors}</ul>                 
                    </div >

                    <div className="reviewTable">
                        <table >
                            <thead>
                                <tr>
                                    <th className="p-2 text-nowrap align-middle">
                                        Blok
                                        </th>
                                    <th className="p-2 text-nowrap align-middle">
                                        Thema
                                        </th>
                                    <th className="p-2 text-nowrap align-middle">
                                        Concept
                                        </th>                               
                                    <th className="p-2 text-nowrap align-middle">
                                        Waardering
                                        </th>
                                </tr>
                            </thead>
                            <tbody className="tableBody">
                                {conceptDisplay}
                            </tbody>
                        </table>
                    </div >
                </div>
                <div className="test2">

                </div>
            </div>

           
        )
    }
}

export default traineeSpecificOverview;