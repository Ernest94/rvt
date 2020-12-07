import React from 'react';
import Utils from './Utils.js'
class UserInfo extends React.Component {
    
    componentDidMount() {
        Utils.dateValidation();
    }
    
    render() {
        
        if (this.props.currentStep !== 2) {
            return null;
        }
        
        
        return (
            <div className="row">
                <div className="col-6 ">
                    <div className="float-right">
                        <label className="form-label" htmlFor="name">Naam:</label>
                        <input className="form-control small-form" id="name" type="text" name="name" value={this.props.name} onChange={this.props.handleFormChange}/>
                        
                        <label htmlFor="email">Email:</label>
                        <input className="form-control" id="email" type="email" name="email" value={this.props.email} onChange={this.props.handleFormChange}/>
                    
                        <label htmlFor="password">Wachtwoord:</label>
                        <input className="form-control" id="password" type="password" name="password"  value={this.props.password} onChange={this.props.handleFormChange}/>
                    </div>
                </div>
                
                <div className="col-6">
                    <div className="float-left">

                        <label htmlFor="date">Datum actief:</label>
                        <input className="form-control" id="date" type="date" name="dateActive" value={this.props.date} onChange={this.props.handleFormChange}/>
                    
                        <label htmlFor="role">Rol:</label>
                        <input className="form-control" id="role" type="text" name="role" value={this.props.role.name} disabled/>
                    
                        <label htmlFor="date">Locatie:</label>
                        <input className="form-control" id="location" type="text" name="location" value={this.props.location.name} disabled/>
                    </div>
                </div>

                
            </div>
        )
    }
}

export default UserInfo;