package nu.educom.rvt.models.view;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Location;

@Entity @XmlRootElement
public class RoleLocationJson implements Serializable {
	
	public RoleLocationJson () {
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Role> roles;
	private List<Location> locations;
	
	public List<Role> getRoles() {
		return roles;
	}	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}
