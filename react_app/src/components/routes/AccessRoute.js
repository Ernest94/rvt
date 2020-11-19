import React from 'react';
import { Route, Redirect} from 'react-router-dom';

const AccessRoute = ({component: Component, ...rest}) => (
    <Route {...rest} render={props => (
        Component.hasAccess(props) && sessionStorage.getItem("isUserLoggedIn") ? <Component {...rest} /> : <Redirect to='/settings' />
    )} />
)

export default AccessRoute;