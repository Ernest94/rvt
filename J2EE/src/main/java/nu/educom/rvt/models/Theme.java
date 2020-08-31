package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="themas")
public class Theme {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	public Theme() {
		super();
	}
	@Column(name="abbreviation")
	private String abbreviation;
	@Column(name="description", length=150)
	private String description;
}
