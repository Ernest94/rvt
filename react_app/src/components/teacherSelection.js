import React from 'react';
import './roleAndLocation.css';

class TeacherSelection extends React.Component {
    
    render() {
        
     
        const teachers = this.props.teachers;

        
        
        const teachersOptions = teachers.map((tea) => {
           return (
                <option key={tea.id} value={tea.id}>{tea.name}</option>
           ) 
        });
        
        if (this.props.isTrainee === null || !this.props.isTrainee) {
            return null;
        }
        
        return (
                 
                <div className="form-group selection_spacing">
                    <label htmlFor="teacher">Docent:</label>
                    <select name="teacher" id="teacher" 
                        value={this.props.teacherDisplayName} 
                        onChange={this.props.onChangeTeacher}
                        required>
                        
                        <option hidden value=''>Docent</option>
                        {teachersOptions}
                    </select>
                </div>
        )
    }
}

export default TeacherSelection;