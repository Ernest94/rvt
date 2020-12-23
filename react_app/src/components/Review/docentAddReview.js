import React from 'react';
import axios from 'axios';
import TextareaAutosize from 'react-textarea-autosize';
import { withRouter } from 'react-router-dom';

import {Select, MenuItem } from '@material-ui/core';
import { confirmAlert } from 'react-confirm-alert'; 
import 'react-confirm-alert/src/react-confirm-alert.css';
import Rating from '@material-ui/lab/Rating';

import { Checkbox} from '@material-ui/core';
import './review.css'

import { config } from '../constants';
import Permissions from '../permissions.js'
import {SelectionTable} from '../Selection.js'
import { GiFeather } from "react-icons/gi";
import { BsDot } from "react-icons/bs";
import Utils from '../Utils';
import { Link} from 'react-router-dom';


class docentAddReview extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            pendingUsers: [],
            userId: null,
            userName: "",
            userLocation: "",
            reviewDate: new Date(),
            concepts: [],
            pageLoading: true,
            weeksPerBlock: 2,
            setValue: "",
            reviewId: null,
            traineeFeedback: "",
            officeFeedback: "",
            message:"",
        };
    }

    static hasAccess() {
        return Permissions.canAddReview();
    }

    async componentDidMount() {
        this.setState({ pageLoading: true });
        const { computedMatch: { params } } = this.props;
        await this.setState({ userId: params.userId });
  
        await this.getConcepts();
    }

    handleFormChange = (e) => {
        const { name, value } = e.target;
        this.setState({
            [name]: value
        });
    }

    onChangePendingUser = (e) => {
        var selectedUser = this.state.pendingUsers.find(user => user.id === parseInt(e.target.value));
        this.setTheState(selectedUser);
        this.getPendingUsers();
    }

    async setTheState(selectedUser) {
        await this.setState({
            pageLoading: true,
            userId: selectedUser.id,
            userName: selectedUser.name,
            userLocation: selectedUser.currentUserLocations[0].name,
        });

        this.getConcepts();
        this.setState({
            pageLoading: false
        })
    }

    getPendingUsers() {
        axios.get(config.url.API_URL + "/webapi/review/pending/docent/" + sessionStorage.getItem("userId"))
            .then(response => {
                this.handleUsersReponse(response.data);

            })
            .catch((error) => {
                console.log("an error occurred " + error);
            })
            this.setState({ pageLoading: false });
    }

    getConcepts() {
        axios.post(config.url.API_URL + "/webapi/review/makeReview", this.createUserIdJson())
            .then(response => {
                this.handleCurriculumReponse(response.data);
                this.getPendingUsers();
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
        this.setState({
            pendingUsers: data.userSearch,
        });
    }

    handleCurriculumReponse(data){
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation,
            reviewDate: data.reviewDate,
            concepts: data.conceptsPlusRatings,
            reviewId: data.reviewId,
            traineeFeedback: data.commentStudent,
            officeFeedback: data.commentOffice,
            message: "",
        });
        if (!JSON.parse(sessionStorage.getItem("userLocation")).map(location =>location.name).includes(data.traineeLocation)){
            this.props.history.push('/settings')
        }
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
        if (value == concept.rating) {
            return;
        }
        concept.rating = value;
        concepts[index] = concept;
        await this.setState({
            concepts: concepts
        });

        let conceptRatingJson = this.createConceptRatingJson(concept);
        this.submitConceptRatingChange(conceptRatingJson);
    }

    async setComment(event) {
        const index = event.target.name.substring(7);
        const value = event.target.value;
        let concepts = this.state.concepts;
        let concept = concepts[index];
        if (value === concept.comment) {
            return;
        }
        concept.comment = value;
        concepts[index] = concept;
        await this.setState({
            concepts: concepts
        });

        let conceptRatingJson = this.createConceptRatingJson(concept);
        this.submitConceptRatingChange(conceptRatingJson);
    }

    async setFeather(event) {
        const index = event.target.name.substring(7);
        let concepts = this.state.concepts;
        let concept = concepts[index];
        concept.feather = !concept.feather;
        concepts[index] = concept;
        await this.setState({
            concepts: concepts
        });
        let conceptRatingJson = this.createConceptRatingJson(concept);
        this.submitConceptRatingChange(conceptRatingJson);
    }

    async setReviewData(event) {
        const { name, value } = event.target;

        if (value === "") return;
        await this.setState({
            [name]: value
        });
        let reviewJson = this.createReviewJson();
        this.submitReviewChange(reviewJson);
    }

    createReviewJson(){
        return {
            id: this.state.reviewId,
            commentOffice: this.state.officeFeedback,
            commentStudent: this.state.traineeFeedback,
            date: this.state.reviewDate,
        }
    }

    createConceptRatingJson(concept) {
        return {
            reviewId: this.state.reviewId,
            conceptPlusRating: concept
        }
    }

    submitReviewChange(ReviewJson) {
        axios.post(config.url.API_URL + "/webapi/review/updateReview", ReviewJson)
            .then(response => {
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            });
    }

    submitConceptRatingChange(conceptRatingJson) {
        axios.post(config.url.API_URL + "/webapi/review/addConceptRating", conceptRatingJson)
            .then(response => {
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            });
    }

    submit = () => {
        confirmAlert({
            message: 'Wilt u de review bevestigen? Let op! Hiermee wordt de review opgeslagen en zichtbaar voor trainees.',
            buttons: [{
                label: 'Ja',
                onClick: () => this.submitReview()
            },
            {
                label: 'Nee',
            }
        ]
        })
    };

    submitReview() {
        this.setState({reviewDate: new Date()})
        axios.post(config.url.API_URL + "/webapi/review/confirmReview", this.createReviewIdJSON())
        .then(response => {
            this.props.history.push('/curriculum/' + this.state.userId);
        })
        .catch((error) => {
            this.setState({
                message: "Er is een technische fout opgetreden bij het bevestigen van deze review."
            });
            console.log("an error occurred " + error);
        });
    }

    cancel = () => {
        confirmAlert({
            // title: 'annuleer',
            message: 'Wilt u de review annuleren? Let op! Hiermee verwijdert u de gemaakte veranderingen.',
            buttons: [{
                label: 'Ja',
                onClick: () => this.cancelReview()
            },
            {
                label: 'Nee',
            }
            ]
        })
    };

    cancelReview() {
        axios.post(config.url.API_URL + "/webapi/review/cancelReview", this.createReviewIdJSON())
            .then(response => {
              this.props.history.push('/dossier/' + this.state.userId);
        })
        .catch((error) => {
            console.log("an error occurred " + error);
        });
    }

    handleWeekChange(e,changedConceptId){
        this.setState(prevState => 
                ({concepts: prevState.concepts.map(concept => 
                    concept.concept.id===changedConceptId? 
                    {...concept, week: e.target.value }
                    :concept)
                })
        );
        this.changeConceptWeek(changedConceptId,e.target.value);

    }

    changeConceptWeek(changedConceptId,newWeek) {
        axios.post(config.url.API_URL + "/webapi/theme_concept/week", 
                        {
                        user: {id:this.state.userId}, 
                        concept:{id: changedConceptId},
                        week: newWeek
                        })
        .then(response => {
        })
        .catch((error) => {
            console.log("an error occurred " + error);
        });
    }

    createReviewIdJSON() {
        return {
            id: this.state.reviewId,
            docent: {id: +sessionStorage.getItem("userId")}
        };
    }

    handleCheckboxChange(e,changedConceptId){

        this.setState(prevState => 
            ({concepts: prevState.concepts.map(concept => 
                concept.concept.id===changedConceptId? 
                {...concept, active: (!concept.active)}
                :concept)
            })
        );
        this.changeConceptActive(changedConceptId);
    };

    changeConceptActive(changedConceptId) {
        axios.post(config.url.API_URL + "/webapi/theme_concept/active", 
                {user: {id:this.state.userId}, 
                concept: {id: changedConceptId}})
        .then(response => {
            
        })
        .catch((error) => {
            console.log("an error occurred " + error);
        });
    }

    handleFeatherChange(e,changedConceptId){
        this.setState(prevState => 
            ({concepts: prevState.concepts.map(concept => 
                concept.concept.id===changedConceptId? 
                {...concept, active: (!concept.feather)}
                :concept)
            })
        );
        this.changeConceptActive(changedConceptId);
    };




    getWeekBlock(week) {
        const wpb = this.state.weeksPerBlock
        var devidedweek = Math.ceil(week / wpb);
        switch (devidedweek) {
            case 0: return ("geen week gegeven");
            case 1: return ("week " + 1 + " t/m " + wpb);
            case 2: return ("week " + (1 + wpb) + " t/m " + (2 * wpb));
            case 3: return ("week " + (1 + 2 * wpb) + " t/m " +  (3 * wpb));
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
        const weeks = [0,1,2,3,4,5,6,7,8,9,10,11,12];
        const weekoptions = weeks.map((week) =>(
                            <MenuItem key={"week_"+week} value={week}>
                                {"week " + week}
                            </MenuItem>))

        const { pageLoading, traineeFeedback, officeFeedback, userId, pendingUsers } = this.state;
        if (pageLoading) return (<div className="error-message-center"><span> Laden...</span></div>);

        let userOptions = null;
        userOptions = pendingUsers.map((user) => {
            return (
                <option className="text-center" key={user.id} value={user.id}>{user.name}</option>
            )
        });



        const ConceptDisplay = ({selectionFunction,}) => (
            <div>
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
                        <th className="feather">
                            Inzet
                        </th>
                        <th className="rating">
                            Vaardigheid
                        </th>
                        <th className="comment">
                            Commentaar
                        </th>
                    </tr>
                </thead>
                {this.state.concepts.map((concept, index) => {    
                   var checkboxDisabled = (concept.comment!=="" || concept.rating!==0);

                    if (selectionFunction(concept)){
                        return (
                        <tr className={(concept.active ? 'text-black' : 'text-muted')}>
                            <td className="active">
                            <Checkbox className="activeCheckbox"
                                id={"concept"+concept.id}
                                onChange={(e)=>this.handleCheckboxChange(e,concept.concept.id)}
                                checked={concept.active}
                                disabled={checkboxDisabled}
                                
                                />                   
                            </td>
                            <td className="week" id="text">
                                <Select name={"weeks"+concept.concept.id} 
                                    id={"weeks"+concept.concept.id}
                                    value={concept.week}
                                    renderValue={(value) => this.getWeekBlock(value)}
                                    onChange={(e)=>this.handleWeekChange(e,concept.concept.id)}
                                    required
                                    disabled={!concept.active}>
                                    {weekoptions}
                                </Select>                    
                            </td>
                            <td className="theme" id="text">
                                <span className="theme-text"> {concept.concept.theme.abbreviation}
                                <span className="displayMessage"> {concept.concept.theme.name + ", " + concept.concept.theme.description} </span>
                                </span>
                            </td>
                            <td className="concept" id="text">
                                <span className="concept-text">
                                {concept.concept.name}
                                <span className="displayMessage"> {concept.concept.description} </span>
                                </span>
                            </td>
                            <td className="feather">
                            <div className="">
                                <Checkbox
                                className="featherCheckbox"
                                checked={concept.feather}
                                name={"feather" +  index}
                                onChange={(event) => {this.setFeather(event)}}
                                disabled={!concept.active}
                                checkedIcon={<GiFeather/>}
                                icon={<BsDot/>}
                                />
                            </div>
                            </td>                  
                            <td className="rating" id="text">
                            <div>
                                <Rating className={"rating-star"}
                                    value={concept.rating}
                                    name={"rating" +  index}
                                    onChange={(event) => {this.setRating(event)}}
                                    disabled={!concept.active}
                                />
                                <div className="rating-text"> {this.getRating(concept.rating)} </div>
                            </div>
                            </td>
                            <td className="comment" id="text">
                                <TextareaAutosize className="comment-text"
                                    disabled={!concept.active}
                                    aria-label="minimum height"
                                    name={"comment" + index} 
                                    onBlur={(event) => {
                                    this.setComment(event); }}
                                    >
                                    {concept.comment}
                                    </TextareaAutosize>
                            </td>
                        </tr>
                        )
                    }
        })
    }</div>);

        return (
            <div className="container">
                <div className="pt-4 row">
                    {/* <div className="col"> Disabled change date function for now because it clashes with DateTime for reviewDate
                        <h3>
                            <input 
                                className="border-0 text-center" 
                                type="date"
                                id="date" 
                                name="reviewDate" 
                                value={reviewDate.getFullYear() + "-" + reviewDate.getMonth() + "-" + reviewDate.getDate()} 
                                placeholder="dd-mm-yyyy" 
                                onChange={(event) => { this.setDate(event) }} />
                        </h3>
                    </div> */}
                    <div className="col-5">
                        <h3 classname="text-center">Review van 
                            <select 
                                className="border-0" 
                                name="pendingUser" 
                                id="pendingUser" 
                                value={this.state.userId} 
                                onChange={this.onChangePendingUser}>
                                    <option 
                                        className="text-center" 
                                        value="" 
                                        selected 
                                        disabled 
                                        hidden>{this.state.userName}
                                    </option>
                                    {userOptions}
                                </select>
                        </h3>
                    </div>

                    <div className="col-5"><h3 classname="text-center">{this.state.userLocation}</h3></div>
                    <Link
                            className="btn btn-danger col-2 btn-small"
                            to={"/curriculum/" + userId /*+ "/" + name */}
                            >
                                Review bekijken</Link>
                </div>

                <div >
                    <ul className="errors">{this.state.errors}</ul>
                </div >


                <div className="table-responsive col-md-12">
                    <table className="addReviewTable table">
                            <SelectionTable fields={["inactive", "stars", "weeks", "themes"]} starsSelected={[0, 5]}>
                                {paramFunction => (<ConceptDisplay selectionFunction={paramFunction} />)}
                            </SelectionTable>
                    </table>
                </div>

                <div className="float-right mr-1">
                    <p>{this.state.message}</p>
                </div>

                <div className="review-bottom-bar container d-flex">
                    <div>
                        <h4 >{"Terugkoppeling naar Trainee:"}</h4>
                        <textarea rows="2" name="traineeFeedback" onBlur={(event) => {
                            this.setReviewData(event);
                        }}>{traineeFeedback}</textarea>
                    </div>
                    <div>
                        <h4 >{"Terugkoppeling naar kantoor:"}</h4>
                        <textarea rows="2" name="officeFeedback" onBlur={(event) => {
                            this.setReviewData(event);
                        }}>
                        {officeFeedback}
                        </textarea>
                    </div>
                    <div>
                        {(this.state.loading) ? <button className="btn btn-danger" type="submit" disabled> Laden...</button>:
                        <button onClick={this.submit} className="btn btn-danger" type="submit">Bevestig</button>}
                        {(this.state.loading) ? <button className="btn btn-danger mr-1" type="submit" disabled> Laden...</button>:
                        <button onClick={this.cancel} className="btn btn-danger mr-1" type="submit">Annuleer</button>}
                    </div>
                </div>

            </div>
        )
    }

}

export default withRouter(docentAddReview);
