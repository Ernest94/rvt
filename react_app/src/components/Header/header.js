import React from 'react';
import { Link } from 'react-router-dom';

class Header extends React.Component {
    
    
    render() {
        let button;
        if (this.props.data.loggedIn) {
               button = <button onClick={() => this.props.handleLogOut()}> Log uit </button>
        }
        return (
            <header className="App-header">
                <div className="">
                    <Link className="navbar-brand App-link" to="/login">
                        RVT
                    </Link>
                    {button}
                </div>
            </header>
            )
    }
}

export default Header;