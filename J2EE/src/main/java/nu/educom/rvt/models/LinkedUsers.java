package nu.educom.rvt.models;

import java.util.Date;

import javax.persistence.*;

public class LinkedUsers {

	private int id;
	private String name;
	private Role role;
	private Location location;
	private boolean hasRelation;

	public LinkedUsers() {}

	public LinkedUsers(int id, String name, Role role, Location location, boolean hasRelation) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
		this.location = location;
		this.hasRelation = hasRelation;
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

	public boolean isHasRelation() {
		return hasRelation;
	}

	public void setHasRelation(boolean hasRelation) {
		this.hasRelation = hasRelation;
	}
	
	
}
