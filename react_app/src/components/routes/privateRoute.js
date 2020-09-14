import React from 'react';
import { Route, Redirect} from 'react-router-dom';

const PrivateRoute = ({component: Component, isLoggedIn: loggedIn, ...rest}) => (
    <Route {...rest} render={() => (
        loggedIn ? <Component {...rest} /> : <Redirect to='/login' />
    
    )} />
)

export default PrivateRoute;