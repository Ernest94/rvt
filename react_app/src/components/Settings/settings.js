import React from 'react';
import { Link } from 'react-router-dom';
import './settings.css';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.userHasAccess ? <li><Link className="link" to="/addUser">Gebruiker toevoegen</Link></li> : <span></span>
        const searchLink = this.props.userHasAccess ? <li><Link className="link" to="/Search">Zoeken naar gebruikers</Link></li> : <span></span>
        //const relationLink = this.props.userHasAccess ? <li><Link className="link" to="/linking">Bekijk relaties</Link></li> : <span></span>
        const addThemeLink = this.props.userHasAccess ? <li><Link className="link" to="/addTheme">Thema toevoegen</Link></li> : <span></span>
        const addConceptLink = this.props.userHasAccess ? <li><Link className="link" to="/addConcept">Concept toevoegen</Link></li> : <span></span>
        const addLocationLink = this.props.userHasAccess ? <li><Link className="link" to="/addLocation">Locatie toevoegen</Link></li> : <span></span>
        const conceptOverviewLink = this.props.userHasAccess ? <li><Link className="link" to="/conceptOverview">Concepten overzicht</Link></li> : <span></span>
        const traineeSpecificOverview = this.props.isTrainee ? < li > <Link className="link" to="/curriculum">Review trainee</Link></li> : <span></span>

        console.log(this.props.userHasAccess);
        return (
            <div >
                <h2>Menu</h2>
                <ul>
                    <li><Link className="link" to={"/dossier/" + sessionStorage.getItem("userId")}>Open Dossier</Link></li>
                    {searchLink}
                    {conceptOverviewLink}
                    {traineeSpecificOverview}
                    {addUserLink}
                    {addConceptLink}
                    {addThemeLink}
                    {addLocationLink}
                    <li><Link className="link" to="/password">Verander wachtwoord</Link></li>

                </ul>
            </div>
        )
    }
}

export default Settings;