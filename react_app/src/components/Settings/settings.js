import React from 'react';
import { Link } from 'react-router-dom';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.userIsAdmin() ? <li><Link className="link" to="/addUser">Voeg een gebruiker toe</Link></li> : <span></span>
        const searchLink = this.props.userIsAdmin() ? <li><Link className="link" to="/Search">Zoeken naar gebruikers</Link></li> : <span></span>
        return (
            <div >
                <h2>Instellingen</h2>
                <ul>
                    <li><Link className="link" to="/password">Verander wachtwoord</Link></li>
					<li><Link className="link" to="/dossier/1">Open Dossier</Link></li>
                    {addUserLink}
                    <li><Link className="link" to="/linking">Bekijk relaties</Link></li>
                    {searchLink}
                </ul>
            </div>
        )
    }
}

export default Settings;