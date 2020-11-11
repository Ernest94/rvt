// import React from 'react';

// import Main from './components/main/main.js';
// import {useHistory} from 'react-router-dom';

// function App() {
//   return (
//       <div className="App">
//             <Main/> {/* history={useHistory()}/> */}
//     </div>
//   );
// }

// export default App;
import React from 'react';
import { validate } from 'validate.js';
import moment from 'moment';
import {Switch, Route} from 'react-router-dom';
import './App.css';

import Header from './components/Header/header.js';
import Footer from './components/Footer/footer.js'; 
import Login from './components/main/login.js';
import Dossier from './components/main/dossier.js';
import Home from './components/main/home.js';
import Search from './components/main/search.js';
import Settings from './components/Settings/settings.js';
import AddUser from './components/Settings/addUser.js';
import Password from './components/Settings/password.js';
import PrivateRoute from './components/routes/PrivateRoute.js';
import AccessRoute from './components/routes/AccessRoute.js';
import addTheme from './components/Settings/addTheme.js';
import addConcept from './components/Settings/addConcept.js';
import conceptOverview from './components/main/conceptOverview.js';
import addLocation from './components/Settings/addLocation.js';
import review from './components/main/review.js';
import docentAddReview from './components/main/docentAddReview.js';
import Permissions from './components/main/permissions.js'

class App extends React.Component {
    
    constructor(props) {
        super(props);
        
        // this.handleSuccesfullAuth = this.handleSuccesfullAuth.bind(this);
        this.handleLogOut = this.handleLogOut.bind(this);
        this.handleReturnToSettings = this.handleReturnToSettings.bind(this);
        this.handleDossierRequest = this.handleDossierRequest.bind(this);
        this.dateValidation = this.dateValidation.bind(this);
        this.setErrors = this.setErrors.bind(this);
        sessionStorage.clear();
        this.state = {
            loggedIn:false,
            userName: ""
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
    
    // handleSuccesfullAuth(data) {
    //     sessionStorage.setItem("isUserLoggedIn", true);
    //     sessionStorage.setItem("userId", data.id);
    //     sessionStorage.setItem("userName", data.name);
    //     sessionStorage.setItem("userRole", data.role.name);
    //     sessionStorage.setItem("userLocation", data.location.name);
    //     sessionStorage.setItem("userLocationId", data.location.id);
    //     this.setState({
    //         loggedIn: true,
    //         userName: data.name
    //     });

    //     this.props.history.push('/settings');
    // }

    handleDossierRequest(event, id) {
        event.preventDefault();
        this.props.history.push('/dossier/' + id);
    }

    handleReturnToDossier(id) {
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
    
    goToAddUserSpecification(role, location) {
        this.props.history.push('/addUser');
    }
    


    render() {

        console.log(Permissions.canEditDossier());
        console.log(Permissions.isUserAdmin());
        

        return (
            <div>
                 <Header handleLogOut={this.handleLogOut} data={this.state}/>
                <div className="container main-container">
                    <Switch>
                        
                        <Route exact path="/login"> 
                            <Login 
                            // history={this.props.history}
                            // handleSuccessfulAuth={this.handleSuccesfullAuth}
                                setErrors={this.setErrors}
                            /> 
                        </Route>

                        <PrivateRoute exact path="/" 
                            component={Home} 
                        />
                        <PrivateRoute exact path="/logout" 

                        />                        
                        <PrivateRoute exact path="/settings"
                            component={Settings}
                        />
                        <PrivateRoute exact path="/password" 
                            component={Password} 
                            handleReturnToSettings={this.handleReturnToSettings}
                            setErrors={this.setErrors}
                        />
                        <PrivateRoute exact path="/dossier/:userId" 
                            setErrors={this.setErrors}
                            editDisabled={true}
                            dateValidation={this.dateValidation}
                            component={Dossier}
                        />
                        <PrivateRoute exact path="/curriculum/:userId"
                            component={review}
                        />
                        <PrivateRoute exact path="/curriculum"
                            component={review}
                        />

                        <AccessRoute exact path="/dossier/:userId/edit" 
                            userHasAccess= {Permissions.canEditDossier()}
                            setErrors={this.setErrors}
                            dateValidation={this.dateValidation}
                            handleReturnToSettings={this.handleReturnToSettings}
                            editDisabled={false}
                            component={Dossier}
                        />
                        <AccessRoute exact path="/addUser"
                            userHasAccess={Permissions.canAddUser()}
                            component={AddUser}
                            dateValidation={this.dateValidation}
                            handleReturnToSettings={this.handleReturnToSettings}
                            setErrors={this.setErrors}
                        />
                        <AccessRoute exact path="/search"
                            userHasAccess={Permissions.canSearch()}
                            component={Search}
                            // handleDossierRequest={this.handleDossierRequest}
                            handleReturnToSettings={this.handleReturnToSettings}
                            setErrors={this.setErrors}
                        />
                        <AccessRoute exact path="/addTheme"
                            userHasAccess={Permissions.canAddTheme()}
                            handleReturnToConcepts={this.handleReturnToConcepts}
                            component={addTheme}
                        />
                        <AccessRoute exact path="/addConcept"
                            userHasAccess={Permissions.canAddConcept()}
                            handleReturnToConcepts={this.handleReturnToConcepts}
                            component={addConcept}              
                        />
                        <AccessRoute exact path="/addLocation"
                            userHasAccess={Permissions.canAddLocation()}
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={addLocation}
                        />
                        <AccessRoute exact path="/conceptOverview"
                            userHasAccess={Permissions.canSeeConceptOverview()}
                            handleReturnToSettings={this.handleReturnToSettings}
                            component={conceptOverview}
                        />
                        <AccessRoute exact path="/docentAddReview"
                            userHasAccess={Permissions.canAddReview()}
                            handleReturnToDossier={this.handleReturnToDossier}
                            component={docentAddReview}
                        />
                        <AccessRoute exact path="/docentAddReview/:userId"
                            userHasAccess={Permissions.canAddReview()}
                            handleReturnToDossier={this.handleReturnToDossier}
                            component={docentAddReview}
                        />
                    </Switch>
                </div>
                <Footer/>
            </div >

        )
    }
}

export default App;


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