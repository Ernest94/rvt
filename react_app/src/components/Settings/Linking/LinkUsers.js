import React from 'react';
import axios from 'axios';

import {config} from '../../constants';

import './linkUser.css';

class LinkUsers extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = {
            users: [],
            pageLoading: false,
            notFound: false,
            buttonText: "Opslaan",
            buttonDisabled: true,
            userId: null,
            user: null,
            changed: [],
            error: "",
        };
    }
    
    componentDidMount() {
       this.setState({
           pageLoading: true,
       });
       const { computedMatch: { params } } = this.props;
       if (params.userId !== undefined ) {
           console.log(params.userId);
           this.setState({userId: params.userId});
       }
       else {
           this.setState({userId: sessionStorage.getItem("userId")});
       }
       
       this.getUsers();
    }
    
    getUsers(){
        let userId = this.state.userId;
        axios.get(config.url.API_URL + '/webapi/user/' + userId + "/UserRelations")
            .then( response => {
                    this.setState({
                        user: response.data.user,
                        users: response.data.users,
                        pageLoading: false
                    });
                })
        .catch(() => {
            this.setState({
                user: null, //{id: 1, name:"Jeroen", role: {name: "Docent"}, location: {name: "Utrecht"}},
                users: null, //[{id:2, name: "Pieter", role: {name: "Trainee"}, location: {name: "Utrecht"}, hasRelation: true}, {id:3, name: "Klaas", role: {name: "Trainee"}, location: {name: "Utrecht"}, hasRelation: true}],
                pageLoading: false,
                notFound: false
            });
        })
    }
    
    handleSaveClick = (e) => {
        const previousButtonText = this.state.buttonText;
        
        this.setState({
            buttonDisabled: true,
            buttonText: "Laden..."
        });
        
        this.submitRelations(previousButtonText);
        
    }
    
    submitRelations(text) {
        axios.post(config.url.API_URL + '/webapi/user/changeRelation', this.createRelationJson())
        .then(response => {
                    this.props.handleReturnToSettings();
                })
                .catch((error) => {
                    console.log("an error occorured " + error);  
                    this.props.handleReturnToSettings();
                    this.setState({
                        error: "Er is iets misgegaan met het opslaan van de relaties. Probeer het later opnieuw.",
                        buttonDisabled: false,
                        buttonText: text,
                    });
                });
    }
    
    createRelationJson() {
        return {
            userId: this.state.userId,
            usersWithChangedRelation: this.state.changed,
        }
    }
    
    handleChangeRelation = (e, id, list) => {
        let userIndex = list.findIndex((obj) => obj.id === id);
        list[userIndex].hasRelation = e.target.checked;
        var {changed} = this.state
        if (changed.includes(id)) {
            const numberIndex = changed.indexOf(id);
            changed.splice(numberIndex, 1);
        }
        else {
            changed.push(id);
        }
        
        this.setState({
            users: list,
            changed: changed,
            buttonDisabled: changed < 1,
        });
    }
    
    render() {
        const {users, user, pageLoading, notFound, error, buttonDisabled, buttonText} = this.state
        if (pageLoading) return (<span className="container center">Laden...</span>);
        if (notFound) return (<span className="container center">Geen gebruikers gevonden.</span>)
        if (user === null) return (<span className="container center">Geen relaties voor deze gebruiker.</span>)
        const usersList = users.map((user) => {
            return(<tr key={user.id}>
                        <td>{user.name}</td>
                        <td>{user.role.name}</td>
                        <td>{user.location.name}</td>
                        <td><input type="checkbox" defaultChecked={user.hasRelation} onChange={(e) => {this.handleChangeRelation(e, user.id, users)}}/></td>
                   </tr>
                   ) 
        });
        return (
            <div>
                <h2>Gelinkte gebruikers</h2>
                <p>{user.role.name} : {user.name}</p>
                <br/>
                <span className="errors">{error}</span>
                
                <table className="relationTable">
                    <thead>
                        <tr>
                            <th>Naam</th>
                            <th>Rol</th>
                            <th>Locatie</th>
                            <th>Relatie</th>
                        </tr>
                    </thead>
                    <tbody>
                    {usersList}
                    </tbody>
                </table>
                
                <button className="btn rvtbutton float-right" 
                        disabled={buttonDisabled} 
                        onClick={this.handleSaveClick}>
                        {buttonText}
                </button>
            </div >
        )
    }
}

export default LinkUsers;