package nu.educom.rvt.models;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue
	@Column(name= "id")
	private int id;
	@Column(name= "name")
	private String name;
	@Column(name= "email")
	private String email;
	@Column(name="password")
	private String password;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	@Column(name="datumActive")
	private LocalDateTime datumActive;
	
	@Column(name="datumInactive")
	private LocalDateTime datumInactive;

	public User() {}
	
	public User(int id, String name, String email, String password, Role role, LocalDateTime datumActive,
			LocalDateTime datumInactive) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.datumActive = datumActive;
		this.datumInactive = datumInactive;
	}
	
	public User(String name, String email, String password, Role role) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public User(int id, String password) {
		super();
		this.id = id;
		this.password = password;
	}

	public User(String name, String email, String password, Role role, LocalDateTime datumActive,
			LocalDateTime datumInactive) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.datumActive = datumActive;
		this.datumInactive = datumInactive;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public LocalDateTime getDatumActive() {
		return datumActive;
	}
	public void setDatumActive(LocalDateTime datumActive) {
		this.datumActive = datumActive;
	}
	public LocalDateTime getDatumInactive() {
		return datumInactive;
	}
	public void setDatumInactive(LocalDateTime datumInactive) {
		this.datumInactive = datumInactive;
	}
	
}
