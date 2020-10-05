package nu.educom.rvt.models;

public class Search {

	private String criteria;
	private Role role;
	private Location location;
	
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
		System.out.println(role);
	}
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
		System.out.println(location);
	}
}
