import React from 'react'; /* JH: Zou de file niet PrivateRoute.js moeten heten? */
import { Route, Redirect} from 'react-router-dom';

const PrivateRoute = ({component: Component, isLoggedIn: loggedIn, ...rest}) => (
    <Route {...rest} render={() => (
        loggedIn ? <Component {...rest} /> : <Redirect to='/login' />
    
    )} />
)

export default PrivateRoute;