//package nu.educom.rvt.models;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name="curriculums")
//public class Curriculum implements BaseEntity {
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id")
//	private int id;
//	
//	@OneToOne
//	@JoinColumn(name="user_id")
//	private User user;
//
//	public Curriculum() {
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
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//	
//	
//}
