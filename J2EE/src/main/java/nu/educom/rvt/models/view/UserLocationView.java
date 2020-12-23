package nu.educom.rvt.models.view;

import java.util.List;

import nu.educom.rvt.models.Location;
import nu.educom.rvt.models.User;

public class UserLocationView {

	private User user;
	private List<Location> locations;
	
	public UserLocationView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserLocationView(User user, List<Location> locations) {
		super();
		this.user = user;
		this.locations = locations;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}


}
