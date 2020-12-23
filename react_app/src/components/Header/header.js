import React from 'react';
import axios from 'axios';
import { Link,withRouter } from 'react-router-dom';
import './header.css';

class Header extends React.Component {
    
    handleLogOut() {
        sessionStorage.clear();
        this.props.handleLogOutState();
        this.props.history.push('/login');
    }

    render() {
        axios.defaults.headers.common['Authorization'] = 'Bearer '+ sessionStorage.getItem("token");
        let button;
        let accountSettings;

        if (sessionStorage.getItem("isUserLoggedIn")) {
               button = <div>
                            <span className="userName">Welkom {sessionStorage.getItem("userName")}</span>
                            <button className="btn rvtbutton logoutbutton" onClick={() => this.handleLogOut()}> Log uit </button>
                        </div>;
               accountSettings = <Link to="/settings" className="header-link">Menu</Link> ;
        }
        return (
            <header className="App-header">
                <nav className="navigation navbar mr-auto">
                    <Link className="navbar-brand header-link" to="/">
                        <img className="logo" alt="educom logo" src={process.env.PUBLIC_URL + "/pictures/educom.jpg"} /> Registratie Voortgang Trainee
                    </Link>
                    {accountSettings}
                    {button}
                </nav>
                <div className="align-bottom1Content"></div>
            </header>
            )
    }
}

export default withRouter(Header);