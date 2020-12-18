package nu.educom.rvt.models;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nu.educom.rvt.models.view.LocalDateAdapter;

@Entity
@Table(name="concept")
public class Concept implements ReadOnlyEntity {

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
	@Column(name="start_date")
	private LocalDate startDate;
	@Column(name="end_date")
	private LocalDate endDate;
	
	//needed for Hibernate
	public Concept() {
		super();
	}
	
	public Concept(Theme theme, String name, String description, LocalDate startDate, LocalDate endDate) {
		super();
		this.theme = theme;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
    }
    
    public Concept(Theme theme, String name, String description, LocalDate startDate) {
		super();
		this.theme = theme;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
	}

	
	public int getId() {
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

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getStartDate() {
		return startDate;
	}
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getEndDate() {
		return endDate;
	}
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Concept)) {
			return false;
		}
		Concept other = (Concept) obj;
		Integer Zero = Integer.valueOf(0);
		if (Zero.compareTo(getId()) < 0 && Zero.compareTo(other.getId()) < 0) {
			return getId() == other.getId();
		} 
		return Objects.equals(getName(), other.getName()) &&
			   Objects.equals(getDescription(), other.getDescription()) &&
			   Objects.equals(getStartDate(), other.getStartDate()) &&
			   Objects.equals(getEndDate(), other.getEndDate());
	}
	@Override
	public String toString() {
		return String.format("Concept(%s)", getName());
	}

}