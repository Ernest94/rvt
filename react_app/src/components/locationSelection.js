import React from 'react';
import {Select} from '@material-ui/core'

class LocationSelection extends React.Component {
    
    render() {
        const locations = this.props.locations;
        const locationsOptions = locations.map((loc) => {
           return (
                <option key={loc.id} value={loc.id}>{loc.name}</option>
           ) 
        });
        
        if (this.props.isTrainee === null) {
            return null;
        }
        
        return (
                <div className="text-center">
                    <label htmlFor="location">Locatie:</label>
                    <div>
                    <select className="form-control" name="location" id="location" 
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

export default LocationSelection;