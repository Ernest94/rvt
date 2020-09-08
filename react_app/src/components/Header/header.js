import React from 'react';
import { Link } from 'react-router-dom';

class Header extends React.Component {
    
    
    render() {
        let button;
        let accountSettings;
        if (this.props.data.loggedIn) {
               button = <button onClick={() => this.props.handleLogOut()}> Log uit </button>
               accountSettings = <Link to="/settings" className="App-link">Instellingen</Link>
        }
        return (
            <header className="App-header">
                <div className="">
                    <Link className="navbar-brand App-link" to="/">
                        RVT
                    </Link>
                    {accountSettings}
                    {button}
                </div>
            </header>
            )
    }
}

export default Header;