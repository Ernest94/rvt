package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="bundle")
public class bundle {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
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
	
	public Bundle(Bundle bundle, String name, String startDate, String endDate) {
		super();
		this.bundle = bundle;
		this.name = name;

		this.startDate = startDate;
		this.endDate = endDate;
	}
	
}
