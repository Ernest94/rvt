import React from 'react';
import LocationSelection from './locationSelection.js';
import TeacherSelection from './teacherSelection.js';
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
                

                <LocationSelection 
                    locations={this.props.locations}
                    isTrainee={this.props.isTrainee}
                    locationDisplayName= {this.props.locationDisplayName}
                    onChangeLocation={this.props.onChangeLocation}
                /> 
                <TeacherSelection
                    isTrainee={this.props.isTrainee}
                    teachers={this.props.teachers}
                    onChangeTeacher={this.props.onChangeTeacher}
                    teacherDisplayName={this.props.teacherDisplayName}
                />
                
            </div>
        )
    }
}

export default RoleAndLocation;