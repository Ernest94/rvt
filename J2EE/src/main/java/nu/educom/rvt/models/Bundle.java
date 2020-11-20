package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="bundle")
public class Bundle {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column (name="creator_user_id")
	private User creator;
	@Column(name="name")
	private String name;
	@Column(name="startdate")
	private String startDate;
	@Column(name="enddate")
	private String endDate;
	
	//needed for Hibernate
	public Bundle() {
		super();
	}
	
	public Bundle(String name, User creator, String startDate, String endDate) {
		super();
		this.name = name;
		this.creator = creator;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Bundle(String name, User creator, String startDate) {
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
