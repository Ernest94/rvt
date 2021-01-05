import React from 'react';
import {Link} from 'react-router-dom';

import '../AddComponents/form.css'

class NotFound extends React.Component {
    
    constructor(props) {
        super(props);
    }


    render() {
        return (
            <div className="container">
                
                <h2 className="text-center">Pagina bestaat niet</h2>
                
                    <div className="row justify-content-center m-5">
                        Sorry, deze pagina bestaat niet (meer).
                    </div>
                    <div className="m-2">
                        <Link className="btn buttons btn-danger btn-block" to={"/menu"}>
                            Terug naar menu
                        </Link>
                    </div>
            </div>

        )
    }

}

export default NotFound;