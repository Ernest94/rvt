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
import AdminRoute from '../routes/AdminRoute.js';
import LinkUsers from '../Settings/Linking/LinkUsers.js';

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
        return (
            
            <div>
                <Header handleLogOut={this.handleLogOut} data={this.state}/>
                <Switch>
                    <Route exact path="/login"> 
                        <Login 
                            handleSuccessfulAuth={this.handleSuccesfullAuth}
                            setErrors={this.setErrors}
                        /> 
                    </Route>
                    <PrivateRoute exact path="/" 
                        isLoggedIn={this.state.loggedIn} 
                        component={Home} 
                    />
                    <PrivateRoute exact path="/settings" 
                        component={Settings} 
                        isLoggedIn={this.state.loggedIn} 
                        userIsAdmin={this.canAddUser}
                    />
                    <PrivateRoute exact path="/password" 
                        component={Password} 
                        isLoggedIn={this.state.loggedIn} 
                        handleReturnToSettings={this.handleReturnToSettings}
                        setErrors={this.setErrors}
                    />
 					<PrivateRoute exact path="/dossier/:userId" 
                        component={Dossier}
                        setErrors={this.setErrors}
                        editDisabled={true} 
                        dateValidation={this.dateValidation}
                        isLoggedIn={this.state.loggedIn}
                    />
                    <PrivateRoute exact path="/dossier/:userId/edit" 
                        component={Dossier}
                        setErrors={this.setErrors}
                        dateValidation={this.dateValidation}
                        handleReturnToSettings={this.handleReturnToSettings} 
                        editDisabled={!sessionStorage.getItem("userRole") === "Admin"} 
                        isLoggedIn={this.state.loggedIn}
                    />
                    <AdminRoute exact path="/addUser" 
                        userIsAdmin={this.canAddUser} 
                        component={AddUser}
                        dateValidation={this.dateValidation}
                        isLoggedIn={this.state.loggedIn} 
                        handleReturnToSettings={this.handleReturnToSettings} 
                        setErrors={this.setErrors}
                    />
                    <PrivateRoute exact path="/linking" 
                        isLoggedIn={this.state.loggedIn} 
                        handleReturnToSettings={this.handleReturnToSettings} 
                        component={LinkUsers} 
                    />
                    <PrivateRoute exact path="/linking/:userId" 
                        isLoggedIn={this.state.loggedIn} 
                        handleReturnToSettings={this.handleReturnToSettings} 
                        component={LinkUsers} 
                    />
                    <AdminRoute exact path="/search" 
                        userIsAdmin={this.canSearchUser} 
                        component={Search} 
                        isLoggedIn={this.state.loggedIn} 
                        handleDossierRequest={this.handleDossierRequest} 
                        handleReturnToSettings={this.handleReturnToSettings} 
                        setErrors={this.setErrors}
                    />
                </Switch>
                <Footer/>
            </div >

        )
    }
}

export default Main;