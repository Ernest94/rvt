import React from 'react';
import { Link } from 'react-router-dom';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.userHasAccess ? <li><Link to="/addUser">Voeg een gebruiker toe</Link></li> : <span></span>
        const searchLink = this.props.userHasAccess ? <li><Link to="/Search">Gebruikers</Link></li> : <span></span>
        const relationLink = this.props.userHasAccess ? <li><Link to="/linking">Bekijk relaties</Link></li> : <span></span>
        return (
            <div className="container main-container">

                <h2>Instellingen</h2>
                <ul>
                    <li><Link to="/password">Verander wachtwoord</Link></li>
					<li><Link to={"/dossier/" + sessionStorage.getItem("userId")}>Open Dossier</Link></li>
                    {addUserLink}
                    {relationLink}
                    {searchLink}

                </ul>
            </div>
        )
    }
}

export default Settings;