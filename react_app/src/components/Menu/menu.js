import React from 'react';
import { Link } from 'react-router-dom';
import './menu.css';
import Permissions from '../permissions.js'

class Menu extends React.Component {
    
    render() {

        const addUserLink = Permissions.canAddUser() ? <li><Link className="link" to="/addUser">Gebruiker toevoegen</Link></li> : <span></span>
        const searchLink = Permissions.canSearch() ? <li><Link className="link" to="/Search">Zoeken naar gebruikers</Link></li> : <span></span>
        const addThemeLink = Permissions.canAddTheme() ? <li><Link className="link" to="/addTheme">Thema toevoegen</Link></li> : <span></span>
        const addConceptLink = Permissions.canAddConcept() ? <li><Link className="link" to="/addConcept">Concept toevoegen</Link></li> : <span></span>
        const addLocationLink = Permissions.canAddLocation() ? <li><Link className="link" to="/addLocation">Locatie toevoegen</Link></li> : <span></span>
        const conceptOverviewLink = Permissions.canSeeConceptOverview() ? <li><Link className="link" to="/conceptOverview">Concepten overzicht</Link></li> : <span></span>
        const review = Permissions.canSeeOwnReview() ? < li > <Link className="link" to="/curriculum">Review</Link></li> : <span></span>
        // const docentAddReviewLink = this.props.userHasAccess ? < li > <Link className="link" to="/docentAddReview">Review toevoegen</Link></li> : <span></span>
        //const relationLink = this.props.userHasAccess ? <li><Link className="link" to="/linking">Bekijk relaties</Link></li> : <span></span>

        return ( 
            <div >
                <h2>Menu</h2>
                <ul>
                    <h4>Account</h4>
                    <li><Link className="link" to={"/dossier/" + sessionStorage.getItem("userId")}>Gebruikersaccount</Link></li>
                    <li><Link className="link" to="/password">Verander wachtwoord</Link></li>

                    <h4>Overig</h4>
                    {searchLink}
                    {conceptOverviewLink}
                    {review}
                    {/* {docentAddReviewLink} */}
                    {addUserLink}
                    {addConceptLink}
                    {addThemeLink}
                    {addLocationLink}
                </ul>
            </div>
        )
    }
}

export default Menu;