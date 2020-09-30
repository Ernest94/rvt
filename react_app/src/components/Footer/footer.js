import React from 'react';
import './footer.css';

class Footer extends React.Component {
    render() {
        return (
            <footer className="modal-footer fixed-bottom text-right">
                <div className="wrapper">
                    <div className="container">
                        <div className="row">
                            <div ><img className="edudeta" alt="educom/detacom logo" src={process.env.PUBLIC_URL + "/pictures/edudetacom.png"}/></div>
                            <div className="col">RVT &copy; 2020 </div>
                        </div>
                    </div>
                </div>
            </footer>
            )
    }
}

export default Footer;