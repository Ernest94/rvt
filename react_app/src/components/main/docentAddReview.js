import React from 'react';
import axios from 'axios';
import TextareaAutosize from 'react-textarea-autosize';

import { confirmAlert } from 'react-confirm-alert'; 
import 'react-confirm-alert/src/react-confirm-alert.css';
import Rating from '@material-ui/lab/Rating';

import { Checkbox} from '@material-ui/core';
import './review.css'

import { config } from '../constants';

class docentAddReview extends React.Component {
    
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
            value: "",
            setValue: "",
            reviewId: null,
            traineeFeedback: "",
            officeFeedback: "",
            message:"",
        };
    }

    handleFormChange = (e) => {
        const { name, value } = e.target;
        this.setState({
            [name]: value
        });
    }

    handleCheckboxChange = (e) => {
        const { name } = e.target;
    
        this.setState(prevState => ({
            checkboxes: {
                ...prevState.checkboxes,
                [name]: !prevState.checkboxes[name]
            }
        }));
    };
    

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
        axios.post(config.url.API_URL + "/webapi/review/makeReview", this.createUserIdJson())
            .then(response => {
                this.handleCurriculumReponse(response.data);
            })
            .catch((error) => {

                console.log("an error occorured " + error);
            });
    }

    createUserIdJson() {
        return {
            id: this.state.userId, //6 is ID voor trainee 3/11/2020
        };
    }

    createReviewIdJSON() {
        return {
            id: this.state.reviewId
        };
    }

    handleCurriculumReponse(data){
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation,
            reviewDate: data.reviewDate,
            concepts: data.conceptsPlusRatings,
            reviewId:data.reviewId,
            message: "",
        });
        console.log(this.state);
    }
    
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
            default: return ("");
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

    submit = () => {
        confirmAlert({
            title: 'Bevestig',
            message: 'wil je geen verdere wijzigingen maken?',
            buttons: [{
                label: 'nee, sla het op',
                onClick: () => this.submitReview()
            },
            {
                label: 'jawel, breng me terug',
            //     onClick: () => alert('Click No')
            }
        ]
        })
    };

    submitReview() {
        axios.post(config.url.API_URL + "/webapi/review/confirmReview", this.createReviewIdJSON())
        .then(response => {
            this.setState({
                message: "uw review is succesvol opgeslagen."
            });
        })
        .catch((error) => {
            console.log("an error occorured " + error);
        });
    }

    cancel = () => {
        confirmAlert({
            title: 'annuleer',
            message: 'wilt u de review annuleren?',
            buttons: [{
                label: 'ja',
                onClick: () => this.cancelReview()
            },
            {
                label: 'nee, breng me terug naar de review',
            }
            ]   
        })
    };

    cancelReview() {
        axios.post(config.url.API_URL + "/webapi/review/cancelReview", this.createReviewIdJSON())
            .then(response => {
              console.log(this.state)
              this.props.handleReturnToDossier(this.state.userId);
        })
        .catch((error) => {
            console.log("an error occorured " + error);
        });
    }

    setActiveConcept() {
        var checkBox = document.getElementById("myCheck");
        var text = document.getElementById("text");
        if (checkBox.checked == false){
          text.style.display = "block";
        } else {
           text.style.display = "none";
        }
    }

    render() {
        const { pageLoading, traineeFeedback, officeFeedback, reviewDate } = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        var conceptDisplay = this.state.concepts.map((concept, index) => {
            return (
                <tr>
                    <td className="active">
                    <Checkbox
                        // label={option}
                        // isSelected={this.state.checkboxes.}
                        // onCheckboxChange={this.handleCheckboxChange}
                         key={"active_"+concept.concept.id}
                         defaultChecked={true}
                         color="default"
                        />                   
                    </td>
                    <td className="week" id="text">
                        {this.getWeekBlock(concept.concept.week)}
                    </td>
                    <td className="theme" id="text">
                        <span className="theme-text"> {concept.concept.theme.abbreviation}
                        <span className="displayMessage"> {concept.concept.theme.name + ", " + concept.concept.theme.description} </span>
                        </span>
                    </td>
                    <td className="concept" id="text">
                        <span className="concept-text">
                        {concept.concept.name}
                        <span className="displayMessage"> {concept.concept.name} </span>
                        </span>
                    </td>                  
                    <td className="rating" id="text">
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
                    <td className="comment" id="text">
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

        console.log(reviewDate);
        
        return (
                <div className="container">
                <div className="pt-4 row">
                    <div className="col"><h3><input className="border-0 text-center" type="date" id="date" name="reviewDate" value={reviewDate} placeholder="dd-mm-yyyy" onChange={this.handleFormChange}/></h3></div>
                    <div className="col"><h3 classname="text-center">Review {this.state.userName}</h3></div>
                    <div className="col"><h3 classname="text-center">{this.state.userLocation}</h3></div>
                </div>
                    <div >
                        <ul className="errors">{this.state.errors}</ul>
                    </div >
                    <table >
                        <thead>
                            <tr>
                                <th className="active">
                                    actief
                                </th>
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
                        <div className="feedback-box-trainee">
                        <h4 >{"Terugkoppeling naar Trainee:"}</h4>
                        <textarea rows="2" cols="50">{traineeFeedback}</textarea> 
                        </div>
                        <div className="feedback-box-kantoor">
                        <h4 >{"Terugkoppeling naar kantoor:"}</h4>
                        <textarea rows="2" cols="50">{officeFeedback}</textarea> 
                        </div>
                    </div>
                    <div className="container">
                        {(this.state.loading) ? <button className="btn btn-primary float-right" type="submit" disabled> Laden...</button>:
                        <button onClick={this.submit} className="btn btn-primary float-right" type="submit">Bevestig</button>}
                        {(this.state.loading) ? <button className="btn btn-primary float-right mr-1" type="submit" disabled> Laden...</button>:
                        <button onClick={this.cancel} className="btn btn-primary float-right mr-1" type="submit">Annuleer</button>}
                    </div>
                    <div>
                        <p>{this.state.message}</p>
                    </div>
                </div>
        )
    }

}

export default docentAddReview;