package nu.educom.rvt.models.view;

import java.time.LocalDate;
import java.util.List;

import nu.educom.rvt.models.Role;
import nu.educom.rvt.models.UserLocation;

public class UserSearch {

    private int id;
	private String name;
	private String email;
	private Role role;
	private List<UserLocation> userLocations;
	private String dateActive;
	
	public UserSearch(int id, String name, String email, Role role, List<UserLocation> userLocations, String dateActive)
	{
        this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.userLocations = userLocations;
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

	public List<UserLocation> getUserLocations() {
		return userLocations;
	}

	public void setUserLocations(List<UserLocation> userLocations) {
		this.userLocations = userLocations;
	}

	public String getDateActive() {
		return dateActive;
	}
	public void setDateActive(String dateActive) {
		this.dateActive = dateActive;
	}
}
