//package nu.educom.rvt.models;
//
//import java.time.LocalDate;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//@Entity
//@Table(name="user_concept")
//public class UserConcept implements ReadOnlyEntity {
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id")
//	private int id;
//	@ManyToOne
//	@JoinColumn(name="concept_id")
//	private Concept concept;
//	@ManyToOne
//	@JoinColumn(name="user_id")
//	private User user;
//	@Column(name="start_date")
//	private LocalDate startDate;
//	@Column(name="is_active")
//	private LocalDate endDate;
//	
//	//needed for Hibernate
//	public UserConcept() {
//		super();
//	}
//
//	@Override
//	public int getId() {
//		return id;
//	}
//
//	@Override
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public Concept getConcept() {
//		return concept;
//	}
//
//	public void setConcept(Concept concept) {
//		this.concept = concept;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public LocalDate getStartDate() {
//		return startDate;
//	}
//
//	public void setStartDate(LocalDate startDate) {
//		this.startDate = startDate;
//	}
//
//	public LocalDate getEndDate() {
//		return endDate;
//	}
//
//	public void setEndDate(LocalDate endDate) {
//		this.endDate = endDate;
//	}
//}
