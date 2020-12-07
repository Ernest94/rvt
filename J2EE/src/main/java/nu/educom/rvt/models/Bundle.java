package nu.educom.rvt.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name="bundle")
public class Bundle {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@ManyToOne
	@JoinColumn(name="creator_user_id")
	private User creator;
	@Column(name="name")
	private String name;
	@Column(name="startdate")
	private LocalDate startDate;
	@Column(name="enddate")
	private LocalDate endDate;
	@JsonManagedReference
 	@OneToMany(mappedBy="bundle", fetch=FetchType.LAZY)
	private List<BundleConcept> allConcepts = new ArrayList<BundleConcept>();
	
	//needed for Hibernate
	public Bundle() {
		super();
	}
	
	public Bundle(String name, User creator, LocalDate startDate, LocalDate endDate) {
		super();
		this.name = name;
		this.creator = creator;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Bundle(String name, User creator, LocalDate startDate) {
		this.name = name;
		this.creator = creator;
		this.startDate = startDate;
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
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
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

	public List<BundleConcept> getAllConcepts() {
		return allConcepts;
	}

	public void addConcept(BundleConcept bc) {
		allConcepts.add(bc);
	}

//	public List<BundleConcept> getCurrentConcepts() {
//		return allConcepts.stream().filter(item-> item.getEndDate().isAfter(LocalDate.now())).collect(Collectors.toList());
//	}
	
}
