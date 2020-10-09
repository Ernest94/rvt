import React from 'react';
import { Link } from 'react-router-dom';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.userHasAccess ? <li><Link className="link" to="/addUser">Voeg een gebruiker toe</Link></li> : <span></span>
        const searchLink = this.props.userHasAccess ? <li><Link className="link" to="/Search">Zoeken naar gebruikers</Link></li> : <span></span>
        const relationLink = this.props.userHasAccess ? <li><Link className="link" to="/linking">Bekijk relaties</Link></li> : <span></span>
        const addThemeLink = this.props.userHasAccess ? <li><Link className="link" to="/addTheme">Thema toevoegen</Link></li> : <span></span>
        const addConceptLink = this.props.userHasAccess ? <li><Link className="link" to="/addConcept">Concept toevoegen</Link></li> : <span></span>
        const conceptOverviewLink = this.props.userHasAccess ? <li><Link className="link" to="/conceptOverview">Curriculum</Link></li> : <span></span>
        return (
            <div >
                <h2>Instellingen</h2>
                <ul>
                    <li><Link className="link" to="/password">Verander wachtwoord</Link></li>
					<li><Link className="link" to={"/dossier/" + sessionStorage.getItem("userId")}>Open Dossier</Link></li>
                    {addUserLink}
                    {searchLink}
                    {addThemeLink}
                    {addConceptLink}
                    {conceptOverviewLink}
                </ul>
            </div>
        )
    }
}

export default Settings;