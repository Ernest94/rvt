package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="concepts")
public class Concept {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@ManyToOne
	@JoinColumn(name="theme_id")
	private Theme theme;
	@Column(name="name")
	private String name;
	@Column(name="description")
	private String description;
	@Column(name="startdate")
	private String startDate;
	@Column(name="enddate")
	private String endDate;
	
	//needed for Hibernate
	public Concept() {
		super();
	}
	
	public Concept(Theme theme, String name, String description, String startDate, String endDate) {
		super();
		this.theme = theme;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
