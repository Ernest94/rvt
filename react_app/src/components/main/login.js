import React from 'react';

class Login extends React.Component {
    render() {
        return (
            <div>
                <form method="Post" action="/">
                    <div className="form-group">
                        <label>Email:</label>
                        <input type="email" name="email"/>
                    </div>

                    <div className="form-group">
                        <label>Password:</label>
                        <input type="password" name="password" />
                    </div>
                    <button type="submit">Log in </button>
                </form>
            </div >

        )
    }
}

export default Login;