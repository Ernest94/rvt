package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="curriculums")
public class Curriculum {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;

	public Curriculum() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
