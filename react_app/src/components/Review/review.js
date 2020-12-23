import React from 'react'
import axios from 'axios'
import TextareaAutosize from 'react-textarea-autosize'
import Rating from '@material-ui/lab/Rating'
import './review.css'
import { config } from '../constants'
import Permissions from '../permissions.js'
import {SelectionTable} from '../Selection.js'
import { GiFeather } from "react-icons/gi";
import { Link, withRouter } from 'react-router-dom';

class review extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: null,
            userName: "",
            userLocation: "",
            reviewDate: new Date(),
            concepts: [],
            pageLoading: true,
            weeksPerBlock: 2,
            errors: "",
            traineeFeedback: "",
        };
    }

    async componentDidMount() {
        if (Permissions.isUserTrainee()) {

            const id = sessionStorage.getItem("userId");

            await this.setState({
                userId: id,
            });
        }
        else {
            const { computedMatch: { params } } = this.props;
            await this.setState({ userId: params.userId });
        }
        this.getConcepts();
        this.setState({ pageLoading: false});
    }

    getThemes() {
        axios.get(config.url.API_URL + "/webapi/theme_concept/themes")
            .then(response => {
                this.handleThemeResponse(response.data);
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            });
    }

    getConcepts() {
        console.log(this.state.userId);
        axios.get(config.url.API_URL + "/webapi/review/curriculum/" + this.state.userId)
            .then(response => {
                this.handleCurriculumReponse(response.data);
            })
            .catch((error) => {
                console.log("an error occorured " + error);
            });
    }
    handleSelectionChange(newValue, name) {
        this.setState({[name+"Selected"]: newValue});
    }

    handleCheckChange(e, id) {
        var localThemes = this.state.themesSelected.slice();
        let index = localThemes.findIndex((obj) => obj.id === id);
        localThemes[index].checked= !localThemes[index].checked;
        this.setState({themesSelected: localThemes});
    }

    createUserIdJson() {
        return {
            id: this.state.userId,
        };
    }

    handleCurriculumReponse(data) {
        var canReview = (Permissions.isUserDocent() 
                    && JSON.parse(sessionStorage.getItem("userLocation")).map(location =>location.name).includes(data.traineeLocation));
        console.log(canReview);
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation,
            reviewDate: new Date(data.reviewDate),
            concepts: data.conceptsPlusRatings,
            traineeFeedback: data.commentStudent,
            canReview: canReview,
        });


    }

    handleThemeResponse(data) {
        this.selection=[];

        for(var i=0; i<data.length; i++){
            this.selection.push({id: data[i].id,checked:true});
        }
        this.setState({
            themes: data},()=>console.log(this.state.themes)
        )
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
        const { pageLoading, traineeFeedback, canReview, userId } = this.state;


        if (pageLoading) return (<span className="center">Laden...</span>)

        const ConceptDisplay = ({selectionFunction,}) => (
        <div class="table-responsive col-md-10">
        <table className="table reviewTable">
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
                    <th className="rating">
                        {""}
                        </th>
                    <th className="comment">
                        Commentaar
                    </th>
                </tr>
            </thead>
            <tbody className="tableBody">
                {(this.state.concepts.map((concept) => {
                    if (selectionFunction(concept)){
                        return (
                            <tr key={"concept_" + concept.concept.id}>
                                <td className="week">
                                    {this.getWeekBlock(concept.week)}
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
                                <td className="feather">
                                    <span className="feather">
                                    {concept.feather?<GiFeather className="feather-icon"/>:""}
                                    </span>
                                </td>
                                <td className="comment">
                                    <TextareaAutosize className="comment-text" readOnly={true} aria-label="minimum height">
                                        {concept.comment}
                                    </TextareaAutosize>
                                </td>
                            </tr >
                        )
                    }
                }))}
            </tbody>
        </table>
        </div>);

        return (
            <div className="container">
                
                <div class="row pt-4">
                    <h3 class="col-md-3 text-center">{this.state.reviewDate.toLocaleDateString('nl-NL')}</h3>
                    <h3 class="col-md-4 text-center">Review {this.state.userName}</h3>
                    <h3 class="col-md-3 text-center">{this.state.userLocation}</h3>
                    <Link to={"/docentAddReview/" + userId} className="btn btn-danger col-md-2 " hidden={!canReview} role="button" >Review aanmaken</Link>
                </div>
                <div >
                    
                    <ul className="errors">{this.state.errors}</ul>
                </div >
                    <SelectionTable
                    fields={["stars","weeks","themes"]}
                    starsSelected={[1,5]}
                        >
                            {paramFunction=>(
                                <ConceptDisplay selectionFunction={paramFunction} />
                            )}
                    </SelectionTable>
                <div className="trainee-feedback-box">
                    <h4 >{"Terugkoppeling:"}</h4>
                    <textarea readOnly rows="2" cols="50" value={traineeFeedback} />
                </div>
            </div>
        )
    }
}

export default review;