import React from 'react';
import { Link } from 'react-router-dom';
import './header.css';

class Header extends React.Component {
    
    render() {
        let button;
        let accountSettings;

        if (this.props.data.loggedIn) {
               button = <div>
                            <span className="userName">Welkom "{sessionStorage.getItem("userName")}"</span>
                            <button className="logOut" onClick={() => this.props.handleLogOut()}> Log uit </button>
                        </div>;
               accountSettings = <Link to="/settings" className="header-link">Instellingen</Link>;
        }
        return (
            <header className="App-header">
                <nav className="navigation navbar mr-auto">
                    <Link className="navbar-brand header-link" to="/">
                        RVT
                    </Link>
                    {accountSettings}
                    {button}
                </nav>
            </header>
            )
    }
}

export default Header;