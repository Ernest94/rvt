import React from 'react';
import './roleAndLocation.css';

class LocationSelection extends React.Component {
    
    render() {
        
     
        const locations = this.props.locations;

        
            
        const locationsOptions = locations.map((loc) => {
           return (
                <option key={loc.id} value={loc.id}>{loc.name}</option>
           ) 
        });
        
        if (this.props.isTrainee === null) { //|| this.props.isTrainee
            return null;
        }
        
        return (
                 
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
        )
    }
}

export default LocationSelection;