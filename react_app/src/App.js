import React from 'react';

import {Switch, Route} from 'react-router-dom';
import './App.css';

import Header from './components/Header/header.js';
import Footer from './components/Footer/footer.js'; 
import Login from './components/login.js';
import Dossier from './components/dossier.js';
import Home from './components/home.js';
import Search from './components/UserSearch/search.js';
import Menu from './components/Menu/menu.js';
import AddUser from './components/addUser.js';
import Password from './components/password.js';
import adminPassword from './components/adminPassword.js';
import PrivateRoute from './components/routes/PrivateRoute.js';
import AccessRoute from './components/routes/AccessRoute.js';
import addTheme from './components/addTheme.js';
import addConcept from './components/addConcept.js';
import conceptOverview from './components/conceptOverview.js';
import addLocation from './components/addLocation.js';
import addBundle from './components/addBundle.js';
import review from './components/Review/review.js';
import docentAddReview from './components/Review/docentAddReview.js';

class App extends React.Component {
    
    constructor(props) {
        super(props);
        this.handleLoginState = this.handleLoginState.bind(this);
        this.handleLogOutState = this.handleLogOutState.bind(this);
    }

    handleLoginState() {
        this.setState({
            loggedIn:true  
          });
    }

    handleLogOutState() {
        this.setState({
            loggedIn: false  
          });
    }
    
    render() {
        return (

            <div>
                <Header handleLogOutState={this.handleLogOutState}/>
            
                    <div className="container main-container">
                        <Switch>
                            
                            <Route exact path="/login">
                                <Login handleLoginState={this.handleLoginState}/> 
                            </Route>

                            <PrivateRoute exact path="/logout"/>

                            <PrivateRoute exact path="/" component={Home}/>
             
                            <PrivateRoute exact path="/settings" component={Menu}/>

                            <PrivateRoute exact path="/password" component={Password}/>

                            <PrivateRoute exact path="/dossier/:userId" component={Dossier} editDisabled={true}/>

                            <PrivateRoute exact path="/curriculum/:userId" component={review}/>

                            <PrivateRoute exact path="/curriculum" component={review}/>

                            <AccessRoute exact path="/dossier/:userId/edit" component={Dossier} editDisabled={false}/>

                            <AccessRoute exact path="/addUser" component={AddUser}/>

                            <AccessRoute exact path="/search" component={Search}/>

                            <AccessRoute exact path="/addTheme" component={addTheme}/>

                            <AccessRoute exact path="/addConcept" component={addConcept}/>

                            <AccessRoute exact path="/addLocation" component={addLocation}/>

                            <AccessRoute exact path="/addBundle" component={addBundle}/>

                            <AccessRoute exact path="/conceptOverview" component={conceptOverview}/>

                            <AccessRoute exact path="/docentAddReview" component={docentAddReview}/>

                            <AccessRoute exact path="/docentAddReview/:userId" component={docentAddReview} />

                            <AccessRoute exact path="/adminPassword/:userId" component={adminPassword} />

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