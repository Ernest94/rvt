package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="roles")
public class Role {

	@Id
	@GeneratedValue
	private long id;
	private String name;
	
	public Role() {
		super();
	}
	
	public Role(String name) {
		super();
		this.name = name;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
