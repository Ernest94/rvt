import React from 'react';
import { Link } from 'react-router-dom';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.userIsAdmin() ? <li><Link to="/addUser">Voeg een gebruiker toe</Link></li>: <span></span>
        return (
            <div className="container main-container">

                <h2>Instellingen</h2>
                <ul>
                    <li><Link to="/password">Verander wachtwoord</Link></li>
					<li><Link to="/dossier">Open Dossier</Link></li>
                    {addUserLink}
                </ul>
            </div>
        )
    }
}

export default Settings;