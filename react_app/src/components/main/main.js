import React from 'react';

import Header from '../Header/header.js';
import Footer from '../Footer/footer.js'; 
import Login from './login.js';
import Home from './home.js';
import Settings from '../Settings/settings.js';
import AddUser from '../Settings/addUser.js';
import Password from '../Settings/password.js';
import {Switch, Route} from 'react-router-dom';
import PrivateRoute from '../routes/privateRoute.js';
import AdminRoute from '../routes/AdminRoute.js';

class Main extends React.Component {
    constructor(props) {
        super(props);
        
        this.handleSuccesfullAuth = this.handleSuccesfullAuth.bind(this);
        this.handleLogOut = this.handleLogOut.bind(this);
        this.handleReturnToSettings = this.handleReturnToSettings.bind(this);
        this.state = {
            loggedIn : false
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
          loggedIn: true  
        });
        sessionStorage.setItem("userId", data.id);
        sessionStorage.setItem("userName", data.name);
        sessionStorage.setItem("userRole", data.role.name);
        this.props.history.push('/');
    }
    
    handleReturnToSettings() {
        this.props.history.push('/settings');
    }

    isUserAdmin() {
        console.log("User is admin: ");
        console.log(sessionStorage.getItem("userRole") === "Admin");
        console.log(sessionStorage.getItem("userRole"));
        return sessionStorage.getItem("userRole") === "Admin";
    }
    
    render() {
        return (
            
            <div>
                <Header handleLogOut={this.handleLogOut} data={this.state}/>
                <Switch>
                    <Route exact path="/login"> <Login handleSuccessfulAuth={this.handleSuccesfullAuth}/> </Route>
                    <PrivateRoute exact path="/" isLoggedIn={this.state.loggedIn} component={Home} />
                    <PrivateRoute exact path="/settings" component={Settings} isLoggedIn={this.state.loggedIn} userIsAdmin={this.isUserAdmin}/>
                    <PrivateRoute exact path="/password" component={Password} isLoggedIn={this.state.loggedIn} handleReturnToSettings={this.handleReturnToSettings}/>
                    <AdminRoute exact path="/addUser" userIsAdmin={this.isUserAdmin} component={AddUser} isLoggedIn={this.state.loggedIn} handleReturnToSettings={this.handleReturnToSettings} />
                </Switch>
                <Footer/>
            </div >

        )
    }
}

export default Main;