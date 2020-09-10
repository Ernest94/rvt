import React from 'react';
import { Route, Redirect} from 'react-router-dom';

const AdminRoute = ({component: Component, isLoggedIn: loggedIn, userIsAdmin: isAdmin, ...rest}) => (
    <Route {...rest} render={() => (
        isAdmin() && loggedIn ? <Component {...rest} /> : <Redirect to='/' />
    )} />
)

export default AdminRoute;