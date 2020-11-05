import React from 'react';
import axios from 'axios';
import TextareaAutosize from 'react-textarea-autosize';

import Rating from '@material-ui/lab/Rating';
import './traineeSpecificOverview.css'

import { config } from '../constants';

class traineeSpecificOverview extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            userId: null,
            userName: "",
            userLocation: "",
            reviewDate: "",
            concepts: [],
            pageLoading: false,
            weeksPerBlock: 2,
            errors: "",
            traineeFeedback: ""
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
        axios.post(config.url.API_URL + "/webapi/review/curriculum", this.createUserIdJson())
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
            id: this.state.userId,
        };
    }

    handleCurriculumReponse(data) {
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation,
            reviewDate: data.reviewDate,
            concepts: data.conceptsPlusRatings,
        });
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
        console.log(this.state);
        const { pageLoading, traineeFeedback } = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        var conceptDisplay = this.state.concepts.map((concept) => {
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
                                name="rating"
                                readOnly={true}
                        />
                        <div className="rating-text"> {this.getRating(concept.rating)} </div>
                        </div>
                    </td>
                    <td className="comment">
                        <TextareaAutosize className="comment-text" readOnly={true} aria-label="minimum height"> 
                            {concept.comment}
                            </TextareaAutosize> 
                    </td> 
                </tr >
            )
        });

        
        return (
                <div className="container">
                    <h2 className="trainee-name">Review {this.state.userName}</h2>
                    <h3 className="trainee-location">{this.state.userLocation}</h3>
                <h3 className="review-date">{this.state.reviewDate}</h3>

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
                    <textarea readOnly rows="4" cols="50">{traineeFeedback}</textarea> 
                    </div>
                </div>
        )
    }
}

export default traineeSpecificOverview;