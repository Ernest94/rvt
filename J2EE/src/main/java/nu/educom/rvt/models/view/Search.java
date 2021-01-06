package nu.educom.rvt.models.view;

import java.util.List;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Role;

public class Search {

	private String criteria;
	private Role role;
	private List<Location> locations;
	
	public String getCriteria() {
		return criteria;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}
