package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Role;

public class UserSearch {

	private String name;
	private String email;
	private Role role;
	private Location location;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
}
