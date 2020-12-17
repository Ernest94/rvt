package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="location")
public class Location implements BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Location) || obj == null) {
			return false;
		}
		else {
			Location toCompare = (Location) obj;
			if(this.id != 0 && toCompare.getId() != 0) {
				return this.id == toCompare.getId();
			}
			else {
				//Add other attribute comparisons here if they are added to object
				return (this.name == toCompare.getName());
			}
		}
	}
	
	@Override
	public String toString() {
		return String.format("Location(%d: %s)", getId(), getName());
	}
}