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
import addTheme from '../Settings/addTheme.js';
import addConcept from '../Settings/addConcept.js';
import conceptOverview from './conceptOverview.js';
import traineeSpecificOverview from './traineeSpecificOverview.js';
import addLocation from '../Settings/addLocation.js';
import docentAddReview from '../Settings/docentAddReview.js';

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
            isDocent: false,
            isAdmin: false,
            isOffice: false,
            isSales: false
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
            isDocent: data.role.name === "Docent",
            isAdmin: data.role.name === "Admin",
            isOffice: data.role.name === "Kantoor",
            isSales: data.role.name === "Sales"           
        });
        sessionStorage.setItem("userId", data.id);
        sessionStorage.setItem("userName", data.name);
        sessionStorage.setItem("userRole", data.role.name);
        sessionStorage.setItem("userLocation", data.location.name);
        sessionStorage.setItem("userLocationId", data.location.id);
        this.props.history.push('/settings');
        console.log(this.state);
    }

    handleDossierRequest(event, id) {
        event.preventDefault();
        this.props.history.push('/dossier/' + id);
    }

    handleCurriculumRequest(event, id) {
        event.preventDefault();
        this.props.history.push('/curriculum/' + id);
    }
    
    handleReturnToSettings() {
        this.props.history.push('/settings');
    }

    handleReturnToConcepts() {
        this.props.history.push('/conceptOverview');
    }
    /* JH TIP: Zet dit soort access functies in een eigen js class permissions.js zodat je ze ook buiten main kan gebruiken */
    isUserAdmin() {
        return sessionStorage.getItem("userRole") === "Admin";
    }

    getUserId() {
        return sessionStorage.getItem("userId");
    }

    getUserRole() {
        return sessionStorage.getItem("userRole");
    }

    canAddUser() {
        let isAdmin = sessionStorage.getItem("userRole") === "Admin";
        let isDocent = sessionStorage.getItem("userRole") === "Docent";
        /* JH: Mis hier isOffice zij kunnen ook trainees toevoegen */
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
        const { isTrainee, isAdmin, isDocent, isOffice, isSales, loggedIn } = this.state;

        console.log("isTrainee: " + isTrainee);
        console.log("loggedIn: " + loggedIn);
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
                            isTrainee={isTrainee}
                            isDocent={isDocent}
                            isOffice={isOffice}
                            isAdmin={isAdmin}
                            isSales={isSales}
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
                            dateValidation={this.dateValidation}
                            isLoggedIn={loggedIn}
                        />
                        <AccessRoute exact path="/dossier/:userId/edit" 
                            component={Dossier}
                            setErrors={this.setErrors}
                            dateValidation={this.dateValidation}
                            handleReturnToSettings={this.handleReturnToSettings}
                            editDisabled={false} 
                            userHasAccess={isAdmin || isOffice || isDocent}
                            isLoggedIn={loggedIn}
                        />
                        <AccessRoute exact path="/addUser"
                            userHasAccess={isAdmin || isOffice || isDocent}
                            component={AddUser}
                            dateValidation={this.dateValidation}
                            isLoggedIn={loggedIn} 
                            handleReturnToSettings={this.handleReturnToSettings}
                            setErrors={this.setErrors}
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
                            userHasAccess={isAdmin}
                            handleReturnToConcepts={this.handleReturnToConcepts}
                            component={addTheme}
                        />
                        <AccessRoute exact path="/addConcept"
                            isLoggedIn={loggedIn}
                            userHasAccess={isAdmin || isDocent}
                            handleReturnToConcepts={this.handleReturnToConcepts}
                            component={addConcept}              
                        />
                        <AccessRoute exact path="/addLocation"
                            isLoggedIn={loggedIn}
                            userHasAccess={isAdmin}
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={addLocation}
                        />
                        <AccessRoute exact path="/conceptOverview"
                            isLoggedIn={loggedIn}
                            userHasAccess={isAdmin || isDocent} /* JH: Volgens mij moet dit isAdmin || isDocent zijn */
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={conceptOverview}
                        />
                        <AccessRoute exact path="/curriculum/:userId"
                            isLoggedIn={loggedIn}
                            userHasAccess={true}
                            component={traineeSpecificOverview}
                            getUserId={this.getUserId}
                            getUserRole={this.getUserRole}
                        />
                        <AccessRoute exact path="/curriculum"
                            isLoggedIn={loggedIn}
                            userHasAccess={true}
                            component={traineeSpecificOverview}
                            getUserId={this.getUserId}
                            getUserRole={this.getUserRole}
                        />
                        <AccessRoute exact path="/docentAddReview"
                            isLoggedIn={loggedIn}
                            userHasAccess={!isTrainee}
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={docentAddReview}
                            getUserRole={this.getUserRole}
                        />
                            <AccessRoute exact path="/docentAddReview/:userId"
                            isLoggedIn={loggedIn}
                            userHasAccess={!isTrainee}
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={docentAddReview}
                            getUserRole={this.getUserRole}
                        />
                    </Switch>
                </div>
                <Footer/>
            </div >

        )
    }
}

export default Main;


                        // <AccessRoute exact path="/linking" 
                        //    isLoggedIn={loggedIn}
                        //    userHasAccess={!isTrainee} /* JH: Volgens mij moet dit zijn {isAdmin || isOffice || isDocent} of of {canAddUser()} */
                        //    handleReturnToSettings={this.handleReturnToSettings}
                        //    component={LinkUsers} 
                        ///>
                        //<PrivateRoute exact path="/linking/:userId" 
                        //    isLoggedIn={loggedIn} 
                        //    handleReturnToSettings={this.handleReturnToSettings}
                        //    component={LinkUsers} 
                        //    /* JH: Mis hier de useHasAccess */
                        ///>