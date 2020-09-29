package nu.educom.rvt.models.view;

import java.time.LocalDateTime;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Role;

public class UserSearch {

	private String name;
	private String email;
	private Role role;
	private Location location;
	private LocalDateTime dateActive;
	
	public UserSearch(String name, String email, Role role, Location location, LocalDateTime dateActive)
	{
		this.name = name;
		this.email = email;
		this.role = role;
		this.location = location;
		this.dateActive = dateActive;
	}
	
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
	public LocalDateTime getDateActive() {
		return dateActive;
	}
	public void setDateActive(LocalDateTime datumActive) {
		this.dateActive = datumActive;
	}
}
