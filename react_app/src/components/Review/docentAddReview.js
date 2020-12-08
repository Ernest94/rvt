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
        if (Permissions.isUserTrainee()) {
            await this.setState({ userId: sessionStorage.getItem("userId") });
        }
        else {
            const { computedMatch: { params } } = this.props;
            await this.setState({ userId: params.userId });
        }
        this.setState({ pageLoading: false });
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
            userLocation: selectedUser.location.name,
        });

        this.getConcepts();
        this.setState({
            pageLoading: false
        })
    }

    getPendingUsers() {
        axios.get(config.url.API_URL + "/webapi/review/pending/location/" + sessionStorage.getItem("userLocationId"))
            .then(response => {
                this.handleUsersReponse(response.data);
            })
            .catch((error) => {
                console.log("an error occurred " + error);
            })
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
        console.log(data);
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
        this.submitConceptRatingChange(conceptRatingJson);
    }

    async setComment(event) {
        const index = event.target.name.substring(7);
        const value = event.target.value;

        let concepts = this.state.concepts;
        let concept = concepts[index];
        concept.comment = value;
        concepts[index] = concept;
        await this.setState({
            concepts: concepts
        });

        let conceptRatingJson = this.createConceptRatingJson(concept);
        this.submitConceptRatingChange(conceptRatingJson);
    }

    // async setDate(event){
    //     console.log(event);
    //     const { name, value } = event.target;
    //     console.log("Date Input Value: " + value);
    //     console.log("Date parsed Value: " + new Date(value.split("-")).setTime(new Date().getTime()).toLocaleString());
    //     this.setState({
    //         reviewDate: new Date(value.split("-")).setTime(new Date().getTime()),
    //     },()=>console.log(this.state.reviewDate));
    // }

    async setReviewData(event) {
        const { name, value } = event.target;
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

    // setActiveConcept() {
    //     var checkBox = document.getElementById("myCheck");
    //     var text = document.getElementById("text");
    //     if (checkBox.checked === false){
    //       text.style.display = "block";
    //     } else {
    //        text.style.display = "none";
    //     }
    // }

    handleWeekChange(e,id){
        this.setState(prevState => 
                ({concepts: prevState.concepts.map(concept => 
                    concept.concept.id===id? 
                    {...concept, week: e.target.value }
                    :concept)
                })
        );

    }

    createReviewIdJSON() {
        return {
            id: this.state.reviewId
        };
    }

    handleCheckboxChange(e,id){
        this.setState(prevState => 
            ({concepts: prevState.concepts.map(concept => 
                concept.concept.id===id? 
                {...concept, active: (!concept.active)}
                :concept)
            })
        );
        //need to change the state of concept active attribute here, depending on new concept json
        this.changeConceptActive(id);
    };

    changeConceptActive(id) {
        axios.post(config.url.API_URL + "/webapi/theme_concept/active", {user: {id:this.state.userId}, concept:{id: id}})
        .then(response => {
            
        })
        .catch((error) => {
            console.log("an error occurred " + error);
        });
    }

    getWeekBlock(week) {
        const wpb = this.state.weeksPerBlock
        var devidedweek = Math.ceil(week / wpb);
        switch (devidedweek) {
            case 0: return ("geen week aangegeven");
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

        const { pageLoading, traineeFeedback, officeFeedback, reviewDate, pendingUsers } = this.state;
        if (pageLoading) return (<span className="center">Laden...</span>)

        let userOptions = null;
        userOptions = pendingUsers.map((user) => {
            return (
                <option className="text-center" key={user.id} value={user.id}>{user.name}</option>
            )
        });


        const ConceptDisplay = ({selectionFunction,}) => (
            <div className="table-responsive col-md-10">
                    <table className="addReviewTable">
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

            <tbody className="tableBody table">
            
                {this.state.concepts.map((concept, index) => {                   
                    if (selectionFunction(concept)){
                        return (
                        <tr className={"searchResult " + (concept.active ? 'text-black' : 'text-muted')}>
                            <td className="active">
                            <Checkbox className=""
                                id={"concept"+concept.id}
                                onChange={(e)=>this.handleCheckboxChange(e,concept.concept.id)}
                                checked={concept.active}
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
                                    name={"comment" + index} onBlur={(event) => {
                                    this.setComment(event); }}
                                    value={concept.comment} />
                            </td>
                        </tr>
                        )
                    }
        })}
        </tbody>
        </table>
        </div>
        );

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
                    <div className="col">
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

                    <div className="col"><h3 classname="text-center">{this.state.userLocation}</h3></div>
                </div>

                <div >
                    <ul className="errors">{this.state.errors}</ul>
                </div >

                <SelectionTable fields={["active","stars","weeks","themes"]} starsSelected={[0,5]}>
                    {paramFunction=>(<ConceptDisplay selectionFunction={paramFunction}/>)}
                </SelectionTable>

                <div className="float-right mr-1">
                    <p>{this.state.message}</p>
                </div>

                <div className="review-bottom-bar container d-flex">
                    <div>
                        <h4 >{"Terugkoppeling naar Trainee:"}</h4>
                        <textarea value={traineeFeedback} rows="2" name="traineeFeedback" onBlur={(event) => {
                            this.setReviewData(event);
                        }}/>
                    </div>
                    <div>
                        <h4 >{"Terugkoppeling naar kantoor:"}</h4>
                        <textarea value={officeFeedback} rows="2" name="officeFeedback" onBlur={(event) => {
                            this.setReviewData(event);
                        }}/>
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
