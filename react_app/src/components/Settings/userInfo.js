import React from 'react';
import { validate } from 'validate.js';
import moment from 'moment';


class UserInfo extends React.Component {
    
    componentDidMount() {
        validate.extend(validate.validators.datetime, {
          // The value is guaranteed not to be null or undefined but otherwise it
          // could be anything.
          parse: function(value, options) {
            return +moment.utc(value);
          },
          // Input is a unix timestamp
          format: function(value, options) {
            var format = options.dateOnly ? "YYYY-MM-DD" : "YYYY-MM-DD hh:mm:ss";
            return moment.utc(value).format(format);
          }
        });
    }
    
    render() {
        
        if (this.props.currentStep !== 2) {
            return null;
        }
        
        
        return (
            <div>
                <div className="form-group">
                    <label htmlFor="name">Naam:</label>
                    <input className="form-control" id="name" type="text" name="name" value={this.props.name} onChange={this.props.handleFormChange}/>
                </div>

                <div className="form-group">
                    <label htmlFor="email">Email:</label>
                    <input className="form-control " id="email" type="email" name="email" value={this.props.email} onChange={this.props.handleFormChange}/>
                </div>
                
                <div className="form-group">
                    <label htmlFor="password">Wachtwoord:</label>
                    <input className="form-control " id="password" type="password" name="password"  value={this.props.password} onChange={this.props.handleFormChange}/>
                </div>
                
                <div className="form-group">
                    <label htmlFor="date">Datum actief:</label>
                    <input className="form-control " id="date" type="date" name="dateActive" value={this.props.date} onChange={this.props.handleFormChange}/>
                </div>
                
                <div className="form-group">
                    <label htmlFor="role">Rol:</label>
                    <input className="form-control " id="role" type="text" name="role" value={this.props.role.name} disabled/>
                </div>
                
                <div className="form-group">
                    <label htmlFor="date">Locatie:</label>
                    <input className="form-control " id="location" type="text" name="location" value={this.props.location.name} disabled/>
                </div>
                
                
            </div>
        )
    }
}

export default UserInfo;