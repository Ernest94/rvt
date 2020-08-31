package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="user_relation_type")
public class UserRelationType {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="type")
	private String type;
	
	public UserRelationType() {
		super();
	}
	public UserRelationType(String type) {
		super();
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
