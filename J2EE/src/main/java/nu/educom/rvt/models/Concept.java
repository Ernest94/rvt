package nu.educom.rvt.models;

import java.time.LocalDate;
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
	@Column(name="week")
	private Integer week;
	@Column(name="startdate")
	private LocalDate startDate;
	@Column(name="enddate")
	private LocalDate endDate;
	
	//needed for Hibernate
	public Concept() {
		super();
	}
	
	public Concept(Theme theme, String name, String description, int week, LocalDate startDate, LocalDate endDate) {
		super();
		this.theme = theme;
		this.name = name;
		this.description = description;
		this.week = week;
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

	public Integer getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}
