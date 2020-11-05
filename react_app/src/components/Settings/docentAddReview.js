import React from 'react';
import axios from 'axios';
import TextareaAutosize from 'react-textarea-autosize';

import Rating from '@material-ui/lab/Rating';
import './docentAddReview.css'
import Box from '@material-ui/core/Box';

import {config} from '../constants';

class docentAddReview extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            userId: null,
            userName: "",
            userLocation: "",
            concepts: [],
            pageLoading: false,
            weeksPerBlock: 2,
            value: "",
            setValue: ""                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
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
            id: this.state.userId, //6 is ID voor trainee 3/11/2020
        };
    }

    handleCurriculumReponse(data){
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation.name,
            concepts: data.conceptsPlusRatings ,
        });
        console.log(this.state);
    }

    // fakeCurriculumResponse() {
    //     this.setState({
    //         userName: "Niels",
    //         userLocation: "Utrecht",
    //         concepts: [{ id: 1, theme: { abbriviation: "OOP", name: "Object Oriented Programmeren", description: "beschrijving van OOP" }, name: "MVC", week: 5, rating: 4 }],
    //     })
    //     console.log(this.state);
    // }
    
    getActiveDisplayName(bool) {
        if (bool) return "ja";
        else return "nee";
    }

    getRating(rating) {
        const intRating = parseInt(rating);
        switch (intRating) {           
            case 1: return ("Matig");
            case 2: return ("Redelijk");
            case 3: return ("Voldoende");
            case 4: return ("Goed");
            case 5: return ("Uitstekend");
            default: return ("Geen Rating");
        }
    }

    setRating(event) {
        const index = event.target.name.substring(6);
        const value = event.target.value;        

        let concepts = this.state.concepts;
        let concept = concepts[index];
        concept.rating = value;
        concepts[index] = concept;
        this.setState({
            concepts: concepts
        });
    }

    setComment(event) {
        const index = event.target.name.substring(7);
        const value = event.target.value;

        console.log(value);

        let concepts = this.state.concepts;
        let concept = concepts[index];
        concept.comment = value;
        concepts[index] = concept;
        this.setState({
            concepts: concepts
        });
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

        var conceptDisplay = this.state.concepts.map((concept, index) => {
            return (
                <tr>
                    <td className="week">
                        {this.getWeekBlock(concept.concept.week)}
                    </td>
                    <td className="theme">
                        <span className="theme-text"> {concept.concept.theme.abbreviation}
                        <span className="displayMessage"> {concept.concept.theme.name + ", " + concept.concept.theme.description} </span>
                        </span>
                    </td>
                    <td className="concept">
                        <span className="concept-text">
                        {concept.concept.name}
                        <span className="displayMessage"> {concept.concept.name} </span>
                        </span>
                    </td>                  
                    <td className="rating">
                    <div>
                            <Rating className="rating-star"
                                value={concept.rating}
                                name={"rating" +  index}
                                onChange={(event) => {
                                this.setRating(event);
                                }}
                            />
                        <div className="rating-text"> {this.getRating(concept.rating)} </div>
                        </div>
                    </td>
                    <td className="comment">
                        <TextareaAutosize className="comment-text"
                            aria-label="minimum height"
                            name={"comment" + index} onChange={(event) => {
                            this.setComment(event); }}> 
                            {concept.comment}
                        </TextareaAutosize> 
                    </td> 
                </tr>
            )
        });

        
        return (
                <div className="container">
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
                    <div>
                        <div className="feedback-box">
                            <h4 >{"Feedback voor Trainee"}</h4>
                            <textarea id="trainee-feedback-boxid" rows="4" cols="50"> </textarea> 
                        </div>
                        <div className="feedback-box">
                            <h4 >{"Feedback voor kantoor"}</h4>
                            <textarea id="kantoor-feedback-boxid" rows="4" cols="50"> </textarea> 
                        </div>
                    </div>
                    <div>
                    {(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>:
                        <button className="btn btn-primary float-right" type="submit">Review toevoegen</button>}
                    </div>
                </div>
        )
    }

}

export default docentAddReview;