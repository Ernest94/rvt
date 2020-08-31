package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="user_relation")
public class UserRelation {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne
	@JoinColumn(name="linked_to")
	private User linked;
	@ManyToOne
	@JoinColumn(name="type")
	private UserRelationType type;
	
	
	public UserRelation() {
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


	public User getLinked() {
		return linked;
	}


	public void setLinked(User linked) {
		this.linked = linked;
	}


	public UserRelationType getType() {
		return type;
	}


	public void setType(UserRelationType type) {
		this.type = type;
	}
	
	
}
