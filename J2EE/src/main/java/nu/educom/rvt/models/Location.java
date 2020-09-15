package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="locations")
public class Location {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	
	public Location() {
		super();
	}
	
	public Location(String name) {
		super();
		this.name = name;
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
}