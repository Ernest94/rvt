import React from 'react';
import { validate } from 'validate.js';
import moment from 'moment';

import Header from '../Header/header.js';
import Footer from '../Footer/footer.js'; 
import Login from './login.js';
import Dossier from './dossier.js';
import Home from './home.js';
import Search from './search.js';
import Settings from '../Settings/settings.js';
import AddUser from '../Settings/addUser.js';
import Password from '../Settings/password.js';
import {Switch, Route} from 'react-router-dom';
import PrivateRoute from '../routes/privateRoute.js';
import AccessRoute from '../routes/AccessRoute.js';
import LinkUsers from '../Settings/Linking/LinkUsers.js';
import addTheme from './addTheme.js';
import addConcept from './addConcept.js';
import conceptOverview from './conceptOverview.js';

class Main extends React.Component {
    
    constructor(props) {
        super(props);
        
        this.handleSuccesfullAuth = this.handleSuccesfullAuth.bind(this);
        this.handleLogOut = this.handleLogOut.bind(this);
        this.handleReturnToSettings = this.handleReturnToSettings.bind(this);
        this.handleDossierRequest = this.handleDossierRequest.bind(this);
        this.dateValidation = this.dateValidation.bind(this);
        this.setErrors = this.setErrors.bind(this);
        this.state = {
            loggedIn : false,
            userName: "",
            isTrainee: false,
        }
    }
    
    setErrors = (errors) => {
        const foundErrors = Object.keys(errors).map((key) =>
            <li key={key}>{errors[key][0]}</li>
        );
        return foundErrors;
    }
    
    dateValidation = () => {
        validate.extend(validate.validators.datetime, {
          // The value is guaranteed not to be null or undefined but otherwise it
          // could be anything.
          parse: function(value, options) {
            return +moment.utc(value);
          },
          // Input is a unix timestamp
          format: function(value, options) {
            var format = options.dateOnly ? "YYYY-MM-DD" : "YYYY-MM-DD hh:mm:ss";
            return moment.utc(value).format(format);
          }
        });
    }
    
    handleLogOut() {
        sessionStorage.clear();
        this.setState({
          loggedIn: false  
        });
        this.props.history.push('/login');
    }
    
    handleSuccesfullAuth(data) {
        this.setState({
          loggedIn: true,
          userName: data.name,
          isTrainee: data.role.name === "Trainee",
        });
        sessionStorage.setItem("userId", data.id);
        sessionStorage.setItem("userName", data.name);
        sessionStorage.setItem("userRole", data.role.name);
        sessionStorage.setItem("userLocation", data.location.name);
        sessionStorage.setItem("userLocationId", data.location.id);
        this.props.history.push('/settings');
    }

    handleDossierRequest(event, id) {
        event.preventDefault();
        this.props.history.push('/dossier/' + id);
    }
    
    handleReturnToSettings() {
        this.props.history.push('/settings');
    }

    handleReturnToConcepts() {
        this.props.history.push('/conceptOverview');
    }

    isUserAdmin() {
        return sessionStorage.getItem("userRole") === "Admin";
    }
    
    canAddUser() {
        let isAdmin = sessionStorage.getItem("userRole") === "Admin";
        let isDocent = sessionStorage.getItem("userRole") === "Docent";
        return (isAdmin || isDocent);
    }

    canSearchUser() {
        let isAdmin = sessionStorage.getItem("userRole") === "Admin";
        return (isAdmin);
    }
    
    goToAddUserSpecification(role, location) {
        this.props.history.push('/addUser');
    }
    
    render() {
        const {isTrainee, loggedIn} = this.state;
        return (
            
            <div>
                <Header handleLogOut={this.handleLogOut} data={this.state}/>
                <div className="container main-container">
                    <Switch>
                        <Route exact path="/login"> 
                            <Login 
                                handleSuccessfulAuth={this.handleSuccesfullAuth}
                                setErrors={this.setErrors}
                            /> 
                        </Route>
                        <PrivateRoute exact path="/" 
                            isLoggedIn={loggedIn} 
                            component={Home} 
                        />
                        <PrivateRoute exact path="/settings" 
                            component={Settings} 
                            isLoggedIn={loggedIn} 
                            userHasAccess={!isTrainee}
                        />
                        <PrivateRoute exact path="/password" 
                            component={Password} 
                            isLoggedIn={loggedIn} 
                            handleReturnToSettings={this.handleReturnToSettings}
                            setErrors={this.setErrors}
                        />
                        <AccessRoute exact path="/dossier/:userId" 
                            component={Dossier}
                            setErrors={this.setErrors}
                            editDisabled={true}
                            userHasAccess={true}
                            isTrainee={isTrainee}
                            dateValidation={this.dateValidation}
                            isLoggedIn={loggedIn}
                        />
                        <AccessRoute exact path="/dossier/:userId/edit" 
                            component={Dossier}
                            setErrors={this.setErrors}
                            dateValidation={this.dateValidation}
                            handleReturnToSettings={this.handleReturnToSettings} 
                            editDisabled={false} 
                            userHasAccess={!isTrainee}
                            isLoggedIn={loggedIn}
                        />
                        <AccessRoute exact path="/addUser" 
                            userHasAccess={!isTrainee} 
                            component={AddUser}
                            dateValidation={this.dateValidation}
                            isLoggedIn={loggedIn} 
                            handleReturnToSettings={this.handleReturnToSettings} 
                            setErrors={this.setErrors}
                        />
                        <AccessRoute exact path="/linking" 
                            isLoggedIn={loggedIn}
                            userHasAccess={!isTrainee}
                            handleReturnToSettings={this.handleReturnToSettings} 
                            component={LinkUsers} 
                        />
                        <PrivateRoute exact path="/linking/:userId" 
                            isLoggedIn={loggedIn} 
                            handleReturnToSettings={this.handleReturnToSettings} 
                            component={LinkUsers} 
                        />
                        <AccessRoute exact path="/search" 
                            userHasAccess={!isTrainee} 
                            component={Search} 
                            isLoggedIn={loggedIn} 
                            handleDossierRequest={this.handleDossierRequest} 
                            handleReturnToSettings={this.handleReturnToSettings} 
                            setErrors={this.setErrors}
                        />
                        <AccessRoute exact path="/addTheme"
                            isLoggedIn={loggedIn}
                            userHasAccess={!isTrainee}
                            handleReturnToSettings={this.handleReturnToConcepts}
                            component={addTheme}
                        />
                        <AccessRoute exact path="/addConcept"
                            isLoggedIn={loggedIn}
                            userHasAccess={!isTrainee}
                            handleReturnToSettings={this.handleReturnToConcepts}
                            component={addConcept}
                        />
                        <AccessRoute exact path="/conceptOverview"
                            isLoggedIn={loggedIn}
                            userHasAccess={!isTrainee}
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={conceptOverview}
                        />
                    </Switch>
                </div>
                <Footer/>
            </div >

        )
    }
}

export default Main;