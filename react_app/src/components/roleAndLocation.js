import React from 'react';
import LocationSelection from './locationSelection.js';
import {Select} from '@material-ui/core'

class RoleAndLocation extends React.Component {
    
    render() {
        
        const roles = this.props.roles;
        const locations = this.props.locations;
        if (roles === null) return <span> Problemen met laden van de pagina. </span>;
        if (locations === null) return <span> Problemen met laden van de pagina. </span>;

        const rolesOptions = roles.map((role) => {
           return (
                <option key={role.id} value={role.id}>{role.name}</option>
           ) 
        });
        
        
        if (this.props.currentStep !== 1) {
            return null;
        }
        
        return (
            <div className="m-3 p-2">

                <div className="text-center">
                    <label htmlFor="role">Rol:</label>
                    <div>
                    <select className="form-control" name="role" id="role" 
                        value={this.props.roleDisplayName} 
                        onChange={this.props.onChangeRole}
                        required>
                        
                        <option hidden value=''></option>
                        {rolesOptions}
                    </select>
                    </div>
                </div>
                
                <LocationSelection 
                    locations={this.props.locations}
                    isTrainee={this.props.isTrainee}
                    locationDisplayName= {this.props.locationDisplayName}
                    roleDisplayName = {this.props.roleDisplayName}
                    onChangeLocation={this.props.onChangeLocation}
                /> 
                
            </div>
        )
    }
}

export default RoleAndLocation;