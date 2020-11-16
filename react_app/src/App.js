import React from 'react';

import Login from './components/main/main.js';
import {useHistory} from 'react-router-dom';

function App() {
  return (
      <div className="App">
            <Main/> {/* history={useHistory()}/> */}
    </div>
  );
}

export default App;








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
   
    render() {

        console.log(Permissions.canEditDossier());
        console.log(Permissions.isUserAdmin());
        

        return (

            <div>
                <Header handleLogOut={this.handleLogOut} data={this.state}/>
            
            
                    <div className="container main-container">
                        <Switch>
                            
                            <Route exact path="/login"> 
                                <Login setErrors={this.setErrors}/> 
                            </Route>

                            <PrivateRoute exact path="/" component={Home}/>

                            {/* <PrivateRoute exact path="/logout"/> */}
                                                    
                            <PrivateRoute exact path="/settings" component={Settings}/>

                            <PrivateRoute exact path="/password" 
                                component={Password} 
                                setErrors={this.setErrors}/>

                            <PrivateRoute exact path="/dossier/:userId" 
                                setErrors={this.setErrors}
                                editDisabled={true}
                                dateValidation={this.dateValidation}
                                component={Dossier}/>

                            <PrivateRoute exact path="/curriculum/:userId" component={review}/>

                            <PrivateRoute exact path="/curriculum" component={review}/>


                            <AccessRoute exact path="/dossier/:userId/edit" 
                                userHasAccess= {Permissions.canEditDossier()}
                                setErrors={this.setErrors}
                                dateValidation={this.dateValidation}
                                editDisabled={false}
                                component={Dossier}/>

                            <AccessRoute exact path="/addUser"
                                userHasAccess={Permissions.canAddUser()}
                                component={AddUser}
                                dateValidation={this.dateValidation}
                                setErrors={this.setErrors}/>

                            <AccessRoute exact path="/search"
                                userHasAccess={Permissions.canSearch()}
                                component={Search}
                                setErrors={this.setErrors}/>

                            <AccessRoute exact path="/addTheme"
                                userHasAccess={Permissions.canAddTheme()}
                                component={addTheme}/>

                            <AccessRoute exact path="/addConcept"
                                userHasAccess={Permissions.canAddConcept()}
                                component={addConcept}/>

                            <AccessRoute exact path="/addLocation"
                                userHasAccess={Permissions.canAddLocation()}
                                component={addLocation}/>

                            <AccessRoute exact path="/conceptOverview"
                                userHasAccess={Permissions.canSeeConceptOverview()}
                                component={conceptOverview}/>

                            <AccessRoute exact path="/docentAddReview"
                                userHasAccess={Permissions.canAddReview()}
                                component={docentAddReview}/>

                            <AccessRoute exact path="/docentAddReview/:userId"
                                userHasAccess={Permissions.canAddReview()}
                                component={docentAddReview}/>

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