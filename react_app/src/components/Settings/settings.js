import React from 'react';
import { Link } from 'react-router-dom';
import './settings.css';

class Settings extends React.Component {
    
    render() {
        const addUserLink = this.props.isAdmin || this.props.isOffice || this.props.isDocent ? <li><Link className="link" to="/addUser">Gebruiker toevoegen</Link></li> : <span></span>
        const searchLink = !this.props.isTrainee ? <li><Link className="link" to="/Search">Zoeken naar gebruikers</Link></li> : <span></span>
        //const relationLink = this.props.userHasAccess ? <li><Link className="link" to="/linking">Bekijk relaties</Link></li> : <span></span>
        const addThemeLink = this.props.isAdmin ? <li><Link className="link" to="/addTheme">Thema toevoegen</Link></li> : <span></span>
        const addConceptLink = this.props.isAdmin || this.props.isDocent ? <li><Link className="link" to="/addConcept">Concept toevoegen</Link></li> : <span></span>
        const addLocationLink = this.props.isAdmin ? <li><Link className="link" to="/addLocation">Locatie toevoegen</Link></li> : <span></span>
        const conceptOverviewLink = this.props.isAdmin || this.props.isDocent ? <li><Link className="link" to="/conceptOverview">Concepten overzicht</Link></li> : <span></span>
        const review = this.props.isTrainee ? < li > <Link className="link" to="/curriculum">Review</Link></li> : <span></span>
        const docentAddReviewLink = this.props.userHasAccess ? < li > <Link className="link" to="/docentAddReview">Review toevoegen</Link></li> : <span></span>


        console.log(this.props.userHasAccess);
        return ( 
            <div >
                <h2>Menu</h2>
                <ul>
                    <li><Link className="link" to={"/dossier/" + sessionStorage.getItem("userId")}>Open Dossier</Link></li>
                    {searchLink}
                    {conceptOverviewLink}
                    {review}
                    {docentAddReviewLink}
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