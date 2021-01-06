import React from 'react';
import {Switch, Route} from 'react-router-dom';

import './App.css';
import Header from './components/Header/header.js';
import Footer from './components/Footer/footer.js';
import NotFound from './components/MISC/NotFound.js';
import Login from './components/MISC/login.js';
import Dossier from './components/Dossier/dossier.js';
import Search from './components/UserSearch/search.js';
import Menu from './components/Menu/menu.js';
import AddUser from './components/AddComponents/addUser.js';
import Password from './components/PasswordChange/password.js';
import adminPassword from './components/PasswordChange/adminPassword.js';
import PrivateRoute from './components/routes/PrivateRoute.js';
import AccessRoute from './components/routes/AccessRoute.js';
import addTheme from './components/AddComponents/addTheme.js';
import addConcept from './components/AddComponents/addConcept.js';
import conceptOverview from './components/ConceptOverview/conceptOverview.js';
import addLocation from './components/AddComponents/addLocation.js';
import addBundle from './components/AddComponents/addBundle.js';
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

                            <PrivateRoute exact path="/" component={Menu}/>
                            
                            <PrivateRoute exact path="/menu" component={Menu}/>

                            <PrivateRoute exact path="/password" component={Password}/>

                            <PrivateRoute exact path="/dossier/:userId" component={Dossier} editDisabled={true}/>

                            <PrivateRoute exact path="/curriculum/:userId" component={review}/>

                            <PrivateRoute exact path="/curriculum" component={review}/>

                            <AccessRoute exact path="/dossier/:userId/edit" component={Dossier} editDisabled={false}/>

                            <AccessRoute exact path="/user" component={AddUser}/>

                            <AccessRoute exact path="/search" component={Search}/>

                            <AccessRoute exact path="/theme" component={addTheme}/>

                            <AccessRoute exact path="/concept" component={addConcept}/>

                            <AccessRoute exact path="/location" component={addLocation}/>

                            <AccessRoute exact path="/bundle" component={addBundle}/>

                            <AccessRoute exact path="/bundles" component={conceptOverview}/>

                            <AccessRoute exact path="/review" component={docentAddReview}/>

                            <AccessRoute exact path="/review/:userId" component={docentAddReview} />

                            <AccessRoute exact path="/password/:userId" component={adminPassword} />
                            
                            <PrivateRoute component={NotFound}/>

                        </Switch>
                    </div>
                <Footer/>
            </div >

        )
    }
}

export default App;