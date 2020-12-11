package nu.educom.rvt.models.view;

import java.time.LocalDate;
import java.util.List;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.Role;

public class UserSearch {

    private int id;
	private String name;
	private String email;
	private Role role;
	private Location location;
	private String dateActive;
	
	public UserSearch(int id, String name, String email, Role role, Location location, String dateActive)
	{
        this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.location = location;
		this.dateActive = dateActive;
	}
    
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getDateActive() {
		return dateActive;
	}
	public void setDateActive(String dateActive) {
		this.dateActive = dateActive;
	}
}
