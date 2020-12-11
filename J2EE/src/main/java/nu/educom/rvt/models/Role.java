package nu.educom.rvt.models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity @XmlRootElement
@Table(name="role")
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	
	public Role() {
		super();
	}
	
	public Role(String name) {
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
	public String toString() {
		return String.format("Role(%s)", getName());
	}
}
