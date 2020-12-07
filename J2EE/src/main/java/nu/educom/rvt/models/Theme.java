package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="theme")
public class Theme {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="description", length=450)
	private String description;
	@Column(name="abbreviation")
	private String abbreviation;

	
	public Theme() {
		super();
	}
	
	public Theme(int id, String name,String abbreviation, String description) {
		super();
		this.id = id;
		this.name = name;
		this.abbreviation = abbreviation;
		this.description = description;
	}
	
	public Theme(String name,String abbreviation, String description) {
		super();
		this.name = name;
		this.abbreviation = abbreviation;
		this.description = description;
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
	public String getAbbreviation() {
	  return abbreviation;
	}  
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
}
