import React from 'react';
import axios from 'axios';

import './Review/review.css'

import { config } from './constants';

import { FormControlLabel, Checkbox,Slider, Button, Switch } from '@material-ui/core';

class SliderSelection extends React.Component {
    render (){
      return (
        <Slider
            classes={{thumb: 'sliderThumb' ,
                    valueLabel: 'sliderLabel' }}
            name={this.props.name}
            value={this.props.value}
            step={1}
            marks
            onChange={(e, newValue) => this.props.handleChange(newValue, this.props.name)}
            onChangeCommitted={(e,newValue) => this.props.handleChangeCommit(newValue, this.props.name)}
            aria-labelledby={this.props.name + "-slider"}
            valueLabelDisplay="on"
            min={this.props.min}
            max={this.props.max}
        />
        )
      }
  }

class SwitchSelection extends React.Component {
    render(){
        return(
            <div>Nee 
                <Switch 
                classes={{
                    track: 'switchTrack',
                    thumb: 'switchThumb',
                    checked: 'switchChecked',
                }} />
            Ja
        </div>)}
}

class CheckboxSelection extends React.Component {
    render(){
        const {name} = this.props;
        return (this.props.toggleArray.map((toggle) => {
            return (
                <FormControlLabel
                    control = {<Checkbox defaultChecked={true}  
                                name = {name + "_" + toggle.id} 
                                onChange = {(e) => this.props.handleCheckChange(e,toggle.id)} 
                                className = "selectionCheckbox"
                                />}
                        label = {toggle.abbreviation}
                        className = "checkboxControl"
                        key = {name +"_checkbox_" + toggle.id}
                />
            )
            })
        )}
}

class Selector extends React.Component {
    render(){
    return(
        <div>
            <div>
                <h5 className="selectionTitle" id={this.props.name + "-slider"} >{this.props.title}</h5>
                {this.props.allButton? 
                    <Button 
                        onClick={()=> {this.props.handleChange([this.props.min,this.props.max],this.props.name);
                        this.props.handleChangeCommit([this.props.min,this.props.max],this.props.name)}} 
                        size="small" 
                        value="All" 
                        className="lightButton">all
                    </Button>
                    :<div></div>}
            </div>
            {this.props.children}
        </div>    
        )}
}

class ConceptSelection extends React.Component {

    constructor(props) {
        super(props);
        this.state ={
            stars: [0,5],
            weeks: [1,10],
        }

    }
    
    handleSliderChange(newValue, name){
        this.setState({[name]: newValue})
    }
    render()
    {
        let fieldoptions = {};
        const active = 
            <Selector
            title="Inactieven"
            name="active"
            allButton={false}
           >
               <SwitchSelection />
           </Selector>
        const weeks = 
            <Selector
            allButton={true}
            title="Weken"
            name = "weeks"
            min={0}
            max={10}
            handleChange={this.handleSliderChange.bind(this)}
            handleChangeCommit={this.props.handleChange.bind(this)}>
            <SliderSelection 
                name="weeks"
                value={this.state.weeks} 
                handleChange={this.handleSliderChange.bind(this)}
                handleChangeCommit={this.props.handleChange.bind(this)}
                min={1}
                max={10} 
            />
            </Selector>
        const stars = 
            <Selector
            allButton={true}
            title="Rating"
            name = "stars"
            min={0}
            max={5}
            handleChange={this.handleSliderChange.bind(this)}
            handleChangeCommit={this.props.handleChange.bind(this)}
            >
               <SliderSelection 
                   value={this.state.stars} 
                   handleChange={this.handleSliderChange.bind(this)}
                   handleChangeCommit={this.props.handleChange.bind(this)}
                   name = "stars"
                   min={0}
                   max={5} 
               />
           </Selector>
        
        const themes = 
            <Selector 
            allButton={false}
            title="Thema's"
            name="themes"
        >
            <CheckboxSelection 
                toggleArray={this.props.themes}
                handleCheckChange={this.props.handleCheckChange}
            />
        </Selector>
        
        fieldoptions["stars"]=stars;
        fieldoptions["weeks"]=weeks;
        fieldoptions["active"]=active;
        fieldoptions["themes"]=themes;

    return (
    <div className="conceptSelection">
        {this.props.fields.map(field=> fieldoptions[field])}
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
            activeSelected: false,
            themes: [],

        }
    }
    componentDidMount(){
        this.getThemes();
        this.setState({starsSelected:this.props.starsSelected});
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

    handleThemeResponse(data){
        var selection=[];
        for(var i=0; i<data.length; i++){
            selection.push({id: data[i].id,checked:true});
        }
        this.setState({
            themesSelected:selection,
            themes: data
        });
    }
        
    inSelectionTotal(concept) {
        var selected=true;
        
        for(var i=0; i<this.props.fields.length; i++){
            selected=this.inSelection(concept,this.props.fields[i]);
            if(selected===false){
                break;
            }
        }
        return selected;
    }
    inSelection(concept,selector){
        switch(selector){
            case "active":
                //Dependent on new concept implementation
                return true;
            case "stars":
                return this.state.starsSelected[0] <= concept.rating && concept.rating <= this.state.starsSelected[1];
            case "weeks":
                //This will probably have to change dependent on new concept implementation
                return this.state.weeksSelected[0] <= concept.concept.week && concept.concept.week <= this.state.weeksSelected[1];
            case "themes":
                let index = this.state.themesSelected.findIndex((obj) => obj.id === concept.concept.theme.id);
                return(this.state.themesSelected[index]===undefined? true : this.state.themesSelected[index].checked === true)
            default:
                return true;
        }
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
        return(
            <div className="d-flex">
            <ConceptSelection 
                fields={this.props.fields}
                themes={this.state.themes} 
                handleChange={this.handleSelectionChange.bind(this)}
                handleCheckChange={this.handleCheckChange.bind(this)}
                />
                {this.props.children(this.inSelectionTotal.bind(this))}
            </div>
        );
    }
}

export {SelectionTable}