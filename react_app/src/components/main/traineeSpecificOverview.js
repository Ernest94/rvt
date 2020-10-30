import React from 'react';
import axios from 'axios';
import TextareaAutosize from 'react-textarea-autosize';

import Rating from '@material-ui/lab/Rating';
import './traineeSpecificOverview.css'

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
            errors: ""
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
                this.setState({
                    errors: error
                });
                console.log("an error occorured " + error);
            });
    }

    createUserIdJson() {
        return {
            id: this.state.userId, //5
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

    //fakeCurriculumResponse() {
    //    this.setState({
    //        userName: "Niels",
    //        userLocation: "Utrecht",
    //        concepts: [{ id: 1, theme: { abbriviation: "OOP", name: "Object Oriented Programmeren", description: "beschrijving van OOP" }, name: "MVC", week: 5, rating: 4 }],
    //    })
    //    console.log(this.state);
    //}
    
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
            default: return ("");
        }
    }

    getWeekBlock(week) {
        const wpb = this.state.weeksPerBlock
        var devidedweek = Math.ceil(week / wpb);
        switch (devidedweek) {
            case 0: return ("geen blok gegeven");
            case 1: return ("week " + 1 + " t/m " + wpb);
            case 2: return ("week " + (1 + wpb) + " t/m " + (2 * wpb));
            case 3: return ("week " + (1 + 2 * wpb) + " t/m " + (3 * wpb));
            case 4: return ("week " + (1 + 3 * wpb) + " t/m " + (4 * wpb));
            default: return ("week "+ (4*wpb+1) + " of later");
        }
    }

    render() {
        const {pageLoading} = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        var conceptDisplay = this.state.concepts.map((concept) => {
            return (
                <tr>
                    <td className="week">   
                        {this.getWeekBlock(concept.concept.week)}
                    </td>
                    <td className="theme">
                        {concept.concept.theme.abbreviation} 
                        <span className="displayMessage"> {concept.concept.theme.name + ", " + concept.concept.theme.description} </span>
                    </td>
                    <td className="concept">
                        {concept.concept.name}
                        <span className="displayMessage"> {concept.concept.name} </span>
                    </td>                  
                    <td className="rating">
                    <div>
                        <Rating className="rating-star"
                            value={concept.rating}
                            name="rating"
                            readOnly="false"
                        />
                        <span className="rating-text"> {this.getRating(concept.rating)} </span>
                        </div>
                    </td>
                    <td className="comment">
                        <TextareaAutosize readOnly aria-label="minimum height" cols="14"> 
                            {concept.comment}
                            </TextareaAutosize> 
                    </td> 
                </tr >
            )
        });

        
        return (
                <div>
                    <h2 className="trainee-name">Review {this.state.userName}</h2>
                    <h2 className="trainee-location">{this.state.userLocation}</h2>
                    <h2 className="review-date">{""}</h2>

                    <div >
                        <ul className="errors">{this.state.errors}</ul>                 
                    </div >
                    <table >
                        <thead>
                            <tr>
                                <th className="week">
                                    Blok
                                    </th>
                                <th className="theme">
                                    Thema
                                    </th>
                                <th className="concept">
                                    Concept
                                    </th> 
                                <th className="rating">
                                    Vaardigheid
                                    </th>
                                <th className="comment">
                                    Commentaar
                                </th>
                            </tr>
                        </thead>
                        <tbody className="tableBody">
                            {conceptDisplay}
                        </tbody>
                    </table>
                    <div className="trainee-feedback-box">
                        <h4 >{"Feedback voor Trainee"}</h4>
                        <textarea rows="4" cols="50"> </textarea> 
                    </div>
                    <div className="kantoor-feedback-box">
                        <h4 >{"Feedback voor kantoor"}</h4>
                        <textarea rows="4" cols="50"> </textarea> 
                    </div>
                </div>
        )
    }
}

export default traineeSpecificOverview;