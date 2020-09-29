import React from 'react';
import { Link } from 'react-router-dom';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.userIsAdmin() ? <li><Link to="/addUser">Voeg een gebruiker toe</Link></li> : <span></span>
        const searchLink = this.props.userIsAdmin() ? <li><Link to="/Search">Gebruikers</Link></li> : <span></span>
        return (
            <div className="container main-container">

                <h2>Instellingen</h2>
                <ul>
                    <li><Link to="/password">Verander wachtwoord</Link></li>
					<li><Link to="/dossier/1">Open Dossier</Link></li>
                    {addUserLink}
                    <li><Link to="/linking">Bekijk relaties</Link></li>
                    {searchLink}

                </ul>
            </div>
        )
    }
}

export default Settings;