import React from 'react';

class LocationSelection extends React.Component {
    
    constructor(props) {
        super(props);
        console.log(this.props.roleDisplayName);
    }

    render() {
        const roleDisplayName = this.props.roleDisplayName;
        const locations = this.props.locations;
        const locationsOptions = locations.map((loc) => {
           return (
                <option key={loc.id} value={loc.id}>{loc.name}</option>
           ) 
        });
        
        if (this.props.isTrainee === null) {
            return null;
        }
        // if (roleDisplayName ==="Trainee") {
            return (
                <div className="my-2">
                    <label htmlFor="location">Locatie:</label>
                    <div>
                    <select className="m-1" name="location" id="location" 
                        value={this.props.locationDisplayName} 
                        onChange={this.props.onChangeLocation}
                        required>
                        
                        <option hidden value=''>Locatie</option>
                        {locationsOptions}
                    </select>
                    </div>
                </div>
        )
        // }
        // else {
        //     return(
        //         <div></div>
        //     )
        // }
        
    }
}

export default LocationSelection;