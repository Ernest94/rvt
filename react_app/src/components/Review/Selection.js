import React from 'react';
import axios from 'axios';

import './review.css'

import { config } from '../constants';

import { FormControlLabel, Checkbox,Slider, Button } from '@material-ui/core';


class ConceptSelection extends React.Component {

    constructor(props) {
        super(props);
        this.state ={
            stars: [1,5],
            weeks: [1,10],
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
                            key = {"theme_" + theme.id}
                            />}
                    label = {theme.abbreviation}
                    className = "checkboxControl"
                    key = {"theme_checkbox_" + theme.id}
            />
        )
    });
    return (
    <div className="conceptSelection" >
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
            min={1}
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
class SelectionTable extends React.Component {

    constructor(props){
        super(props);
        this.state={
            starsSelected: [1,5], //starting selection
            weeksSelected: [1,10],
            themesSelected: [], 
            themes: [],
        }
    }
    componentDidMount(){
        this.getThemes();
    }
    handleThemeResponse(data){
        var selection=[];
        for(var i=0; i<data.length; i++){
            selection.push({id: data[i].id,checked:true});
        }
        this.setState({
        themesSelected:selection,
        themes: data});
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

    inSelection(concept) {
        let index = this.state.themesSelected.findIndex((obj) => obj.id === concept.concept.theme.id);
        return(
            this.state.starsSelected[0] <= concept.rating && concept.rating <= this.state.starsSelected[1]
            &&
            this.state.weeksSelected[0] <= concept.concept.week && concept.concept.week <= this.state.weeksSelected[1]
            &&
            (this.state.themesSelected[index]===undefined? true : this.state.themesSelected[index].checked === true)
        )
    }
    handleSelectionChange(newValue, name)
    {
        this.setState({[name+"Selected"]: newValue});
    }
    handleCheckChange(e, id)
    {
        var localThemes = this.state.themesSelected.slice();
        let index = localThemes.findIndex((obj) => obj.id === id);
        localThemes[index].checked= !localThemes[index].checked;
        this.setState({themesSelected: localThemes});
    }

    render(){
        console.log(this.props.children);
        return(
            <div className="d-flex">
            <ConceptSelection 
                themes={this.state.themes} 
                handleChange={this.handleSelectionChange.bind(this)}
                handleCheckChange={this.handleCheckChange.bind(this)}
                />
                
                {this.props.children(this.inSelection.bind(this))}
            </div>
        );
    }
}

export {SelectionTable}