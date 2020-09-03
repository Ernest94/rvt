import React from 'react';

import Header from '../Header/header.js';
import Footer from '../Footer/footer.js'; 
import Login from './login.js';
import Home from './home.js';
import {Switch, Route} from 'react-router-dom';
import PrivateRoute from './privateRoute.js';

class Main extends React.Component {
    constructor(props) {
        super(props);
        
        this.handleSuccesfullAuth = this.handleSuccesfullAuth.bind(this);
        this.handleLogOut = this.handleLogOut.bind(this);
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
        sessionStorage.setItem("userRole", data.role);
        this.props.history.push('/');
    }
    
    render() {
        return (
            
            <div>
                <Header handleLogOut={this.handleLogOut} data={this.state}/>
                <Switch>
                    <Route exact path="/login"> <Login handleSuccessfulAuth={this.handleSuccesfullAuth}/> </Route>
                    <PrivateRoute exact path="/" isLoggedIn={this.state.loggedIn} component={Home} />
                </Switch>
                <Footer/>
            </div >

        )
    }
}

export default Main;