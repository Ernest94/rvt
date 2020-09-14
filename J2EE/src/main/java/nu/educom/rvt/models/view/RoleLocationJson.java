package nu.educom.rvt.models.view;

import java.util.List;

import javax.persistence.Entity;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.Location;

@Entity
public class RoleLocationJson {
	
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
