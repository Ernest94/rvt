import React from 'react';

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
        this.state = {
            loggedIn : false,
            userName: "",
        }
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

    handleDossierRequest(id) {
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
                    <Route exact path="/login"> <Login handleSuccessfulAuth={this.handleSuccesfullAuth}/> </Route>
                    <PrivateRoute exact path="/" isLoggedIn={this.state.loggedIn} component={Home} />
                    <PrivateRoute exact path="/settings" component={Settings} isLoggedIn={this.state.loggedIn} userIsAdmin={this.canAddUser}/>
                    <PrivateRoute exact path="/password" component={Password} isLoggedIn={this.state.loggedIn} handleReturnToSettings={this.handleReturnToSettings}/>
 					<PrivateRoute exact path="/dossier" component={Dossier} editDisabled={true} isLoggedIn={this.state.loggedIn}/>
                    <PrivateRoute exact path="/dossier/:userId" component={Dossier} editDisabled={!sessionStorage.getItem("userRole")=== "Admin"} isLoggedIn={this.state.loggedIn}/>
                    <AdminRoute exact path="/addUser" userIsAdmin={this.canAddUser} component={AddUser} isLoggedIn={this.state.loggedIn} handleReturnToSettings={this.handleReturnToSettings} />
                    <PrivateRoute exact path="/linking" isLoggedIn={this.state.loggedIn} handleReturnToSettings={this.handleReturnToSettings} component={LinkUsers} />
                    <PrivateRoute exact path="/linking/:userId" isLoggedIn={this.state.loggedIn} handleReturnToSettings={this.handleReturnToSettings} component={LinkUsers} />
                    <AdminRoute exact path="/search" userIsAdmin={this.canSearchUser} component={Search} isLoggedIn={this.state.loggedIn} handleDossierRequest={this.handleDossierRequest} handleReturnToSettings={this.handleReturnToSettings} />

                </Switch>
                <Footer/>
            </div >

        )
    }
}

export default Main;