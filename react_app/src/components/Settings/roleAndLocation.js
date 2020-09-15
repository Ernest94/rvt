import React from 'react';
import './roleAndLocation.css';

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
        
        const locationsOptions = locations.map((loc) => {
           return (
                <option key={loc.id} value={loc.id}>{loc.name}</option>
           ) 
        });
        
        if (this.props.currentStep !== 1) {
            return null;
        }
        
        return (
            <div>
                <div className="form-group selection_spacing">
                    <label htmlFor="role">Rol:</label>
                    <select name="role" id="role" 
                        value={this.props.roleDisplayName} 
                        onChange={this.props.onChangeRole}
                        required>
                        
                        <option hidden value=''>Rol</option>
                        {rolesOptions}
                    </select>
                </div>
                    
                <div className="form-group selection_spacing">
                    <label htmlFor="location">Locatie:</label>
                    <select name="location" id="location" 
                        value={this.props.locationDisplayName} 
                        onChange={this.props.onChangeLocation}
                        required>
                        
                        <option hidden value=''>Locatie</option>
                        {locationsOptions}
                    </select>
                </div>
            </div>
        )
    }
}

export default RoleAndLocation;