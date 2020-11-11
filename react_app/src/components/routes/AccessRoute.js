import React from 'react';
import { Route, Redirect} from 'react-router-dom';

const AccessRoute = ({component: Component, userHasAccess: hasAccess, ...rest}) => (
    <Route {...rest} render={() => (
        hasAccess && sessionStorage.getItem("isUserLoggedIn") ? <Component {...rest} /> : <Redirect to='/login' />
    )} />
)

export default AccessRoute;