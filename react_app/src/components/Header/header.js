import React from 'react';
import { Link } from 'react-router-dom';

class Header extends React.Component {
    render() {
        return (
            <header>
                <div className="container">
                    <Link className="navbar-brand" to="/">
                        RVT
                    </Link>
                </div>
            </header>
            )
    }
}

export default Header;