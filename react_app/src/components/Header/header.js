import React from 'react';
import { Link } from 'react-router-dom';
import './header.css';

class Header extends React.Component {
    
    
    render() {
        let button;
        let accountSettings;

        if (this.props.data.loggedIn) {
               button = <div>
                            <span className="userName">Welkom "{this.props.data.userName}"</span>
                            <button className="btn rvtbutton logoutbutton" onClick={() => this.props.handleLogOut()}> Log uit </button>
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

export default Header;