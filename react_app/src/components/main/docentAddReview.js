import React from 'react';
import axios from 'axios';
import TextareaAutosize from 'react-textarea-autosize';

import { confirmAlert } from 'react-confirm-alert'; 
import 'react-confirm-alert/src/react-confirm-alert.css';
import Rating from '@material-ui/lab/Rating';

import './review.css'

import { config } from '../constants';
import Permissions from './permissions.js'

class docentAddReview extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            pendingUsers: [],
            userId: null,
            userName: "",
            userLocation: "",
            reviewDate: "",
            concepts: [],
            pageLoading: true,
            weeksPerBlock: 2,
            value: "",
            setValue: "",
            reviewId: null,
            traineeFeedback: "",
            officeFeedback: "",
            message:"",
        };
    }

    async componentDidMount() {
        console.log("begin");
        this.setState({ pageLoading: true });      
        if (Permissions.isUserTrainee()) {
            await this.setState({ userId: sessionStorage.getItem("userId") });
        }
        else {
            const { computedMatch: { params } } = this.props;
            await this.setState({ userId: params.userId });
        }
        console.log(this.state.userId);
        this.setState({ pageLoading: false });
        await this.getPendingUsers();
        await this.getConcepts();
        
        
    }

    onChangePendingUser = (e) => {
        var selectedUser = this.state.pendingUsers.find(user => user.id === parseInt(e.target.value));

        this.setTheState(selectedUser);
    }

    async setTheState(selectedUser) {
        await this.setState({
            pageLoading: true,
            userId: selectedUser.id,
            userName: selectedUser.name,
            userLocation: selectedUser.location.name,
        });

        this.getConcepts();
        this.setState({
            pageLoading: false
        })
    }

    getPendingUsers() {
        console.log("getPendingUsers")
        axios.get(config.url.API_URL + "/webapi/review/pendingUsers")
            .then(response => {
                console.log("succes");
                this.handleUsersReponse(response.data);
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            })
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

    handleUsersReponse(data) {
        console.log(data);
        this.setState({
            pendingUsers: data.userSearch,
        });
        console.log(this.state);
    }

    handleCurriculumReponse(data){
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation,
            reviewDate: data.reviewDate,
            concepts: data.conceptsPlusRatings,
            reviewId: data.reviewId,
            message: "",
        });
        console.log(this.state);
    }
    
    getActiveDisplayName(bool) {
        if (bool) return "ja";
        else return "nee";
    }

    async setRating(event) {
        const index = event.target.name.substring(6);
        const value = event.target.value;        

        let concepts = this.state.concepts;
        let concept = concepts[index];
        concept.rating = value;
        concepts[index] = concept;
        await this.setState({
            concepts: concepts
        });

        let conceptRatingJson = this.createConceptRatingJson(concept);
        console.log(conceptRatingJson);
        this.submitConceptRatingChange(conceptRatingJson);
    }

    async setComment(event) {
        const index = event.target.name.substring(7);
        const value = event.target.value;

        console.log(value);

        let concepts = this.state.concepts;
        let concept = concepts[index];
        concept.comment = value;
        concepts[index] = concept;
        await this.setState({
            concepts: concepts
        });

        let conceptRatingJson = this.createConceptRatingJson(concept);
        console.log(conceptRatingJson);
        this.submitConceptRatingChange(conceptRatingJson);
    }

    async setReviewData(event) {
        const { name, value } = event.target;
        await this.setState({
            [name]: value
        });
        let reviewJson = this.createReviewJson();
        console.log(reviewJson);
        this.submitReviewChange(reviewJson);
    }

    createReviewJson(){
        return {
            id: this.state.reviewId,
            commentOffice: this.state.officeFeedback,
            commentStudent: this.state.traineeFeedback,
            date: this.state.reviewDate
        }
    }

    createConceptRatingJson(concept) {
        return {
            id: this.state.reviewId,
            conceptPlusRating: concept
        }
    }

    submitReviewChange(ReviewJson) {
        axios.post(config.url.API_URL + "/webapi/review/addReview", ReviewJson)
            .then(response => {
                console.log(response);
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            });
    }

    submitConceptRatingChange(conceptRatingJson) {
        axios.post(config.url.API_URL + "/webapi/review/addConceptRatings", conceptRatingJson)
            .then(response => {
                console.log(response);
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            });
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

    createReviewIdJSON() {
        return {
            id: this.state.reviewId
        };
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

    render() {
        const { pageLoading, traineeFeedback, officeFeedback, reviewDate, pendingUsers } = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        let userOptions = null;
        console.log(pendingUsers);
            userOptions = pendingUsers.map((user) => {
                return (
                    <option className="text-center" key={user.id} value={user.id}>{user.name}</option>
                )
            });
        

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
                            name={"comment" + index} onBlur={(event) => {
                            this.setComment(event); }}> 
                            {concept.comment}
                        </TextareaAutosize> 
                    </td> 
                </tr>
            )
        });
        
        return (
                <div className="container">
                <div className="pt-4 row">
                    <div className="col"><h3><input className="border-0 text-center" type="date" id="date" name="reviewDate" value={reviewDate} placeholder="dd-mm-yyyy" onChange={(event) => { this.setReviewData(event) }} /></h3></div>
                    <div className="col">
                        <h3 classname="text-center">Review
                            <select className="border-0" name="pendingUser" id="pendingUser" value={this.state.UserId} onChange={this.onChangePendingUser}><option className="text-center" value="" selected disabled hidden>{this.state.userName}</option>{userOptions}</select>
                        </h3>
                    </div>
                    <div className="col"><h3 classname="text-center">{this.state.userLocation}</h3></div>
                </div>
                    <div >
                        <ul className="errors">{this.state.errors}</ul>
                    </div >
                    <div className="table-responsive">
                    <table className="reviewTable table">
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
                </div>
                    <div>
                        <div className="feedback-box-trainee">
                        <h4 >{"Terugkoppeling naar Trainee:"}</h4>
                        <textarea rows="2" name="traineeFeedback" cols="50" onBlur={(event) => {
                            this.setReviewData(event);
                        }}
                        >{traineeFeedback}</textarea> 
                        </div>
                        <div className="feedback-box-kantoor">
                        <h4 >{"Terugkoppeling naar kantoor:"}</h4>
                        <textarea rows="2" name="officeFeedback" cols="50" onBlur={(event) => {
                            this.setReviewData(event);
                        }}
                        >{officeFeedback}</textarea> 
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