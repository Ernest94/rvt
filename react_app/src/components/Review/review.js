import React from 'react'
import axios from 'axios'
import TextareaAutosize from 'react-textarea-autosize'
import Rating from '@material-ui/lab/Rating'
import './review.css'
import { config } from '../constants'
import Permissions from '../permissions.js'
import {SelectionTable} from '../Selection.js'
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Button from '@material-ui/core/Button';
import Checkbox from '@material-ui/core/Checkbox';
import Slider from '@material-ui/core/Slider';

class ConceptSelection extends React.Component {

    constructor(props) {
        super(props);
        this.state ={
            stars: [1,5],
            weeks: [0,10],
        }

    }

    handleSliderChange(newValue, name){
        this.setState({[name]: newValue})
    }
    render()
    {
    var themeSelection = this.props.themes.map((theme) => {
        return (
            <FormControlLabel
                control = {<Checkbox defaultChecked={true}
                            name = {"theme_" + theme.id}
                            onChange = {(e) => this.props.handleCheckChange(e,theme.id)}
                            className = "themeCheckbox"
                            />}
                    label = {theme.abbreviation}
                    className = "checkboxControl"
            />
        )
    });
    return (
    <div class={"conceptSelection " + this.props.className} >


        <SliderSelection
            title="Rating tussen"
            value={this.state.stars}
            handleChange={this.handleSliderChange.bind(this)}
            handleChangeCommit={this.props.handleChange.bind(this)}
            min={0}
            max={5}
            name = "stars"
        />
        <SliderSelection
            title="Weken"
            value={this.state.weeks}
            handleChange={this.handleSliderChange.bind(this)}
            handleChangeCommit={this.props.handleChange.bind(this)}
            min={0}
            max={10}
            name = "weeks"
            />
        <div><h5 className="selectionTitle">Thema's</h5></div>
        {themeSelection}
    </div>
    )
    }
}

class SliderSelection extends React.Component {

    render (){
        return (
            <div >
                <div><h5 className="selectionTitle" id={this.props.name + "-slider"} >{this.props.title}</h5>
                    <Button
                        onClick={()=> {this.props.handleChange([this.props.min,this.props.max],this.props.name);
                            this.props.handleChangeCommit([this.props.min,this.props.max],this.props.name)}}
                        size="small"
                        value="All"
                        className="lightButton">all
                    </Button>
                </div>
                <Slider
                    classes={{thumb: 'sliderThumb' ,
                            valueLabel: 'sliderLabel' }}
                    name={this.props.name}
                    value={this.props.value}
                    step={1}
                    marks
                    onChange={(e, newValue) => this.props.handleChange(newValue, this.props.name)}
                    onChangeCommitted={(e,newValue) => this.props.handleChangeCommit(newValue, this.props.name)}
                    valueLabelDisplay="auto"
                    aria-labelledby={this.props.name + "-slider"}
                    valueLabelDisplay="on"
                    min={this.props.min}
                    max={this.props.max}
                />
        </div>
        )
    }
}

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
        this.setState({ pageLoading: false });
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
        this.setState({
            userName: data.traineeName,
            userLocation: data.traineeLocation,
            reviewDate: new Date(data.reviewDate),
            concepts: data.conceptsPlusRatings,
        });
        console.log(this.state);
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
        const { pageLoading, traineeFeedback } = this.state;

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
                    <h3 class="col-md-4 text-center">{this.state.reviewDate.toLocaleDateString('nl-NL')}</h3>
                    <h3 class="col-md-4 text-center">Review {this.state.userName}</h3>
                    <h3 class="col-md-4 text-center">{this.state.userLocation}</h3>
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