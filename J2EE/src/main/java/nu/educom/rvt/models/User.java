package nu.educom.rvt.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import nu.educom.rvt.models.view.LocalDateAdapter;

@Entity
@Table(name="user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	@Column(name="dateActive")
	private LocalDate dateActive;
	@Column(name="dateInactive")
	private LocalDate dateInactive;
		
	@JsonIgnore
 	@OneToMany(mappedBy="user", fetch=FetchType.LAZY)
	private List<UserLocation> allUserLocations = new ArrayList<UserLocation>();
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_location", 
        joinColumns = { @JoinColumn(name = "user_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "location_id"),}
    )
	@WhereJoinTable(clause = "endDate IS NULL")
	private List<Location> currentLocations = new ArrayList<Location>();
	
	public User() {}
	
	public User(int id, String name, String email, String password, Role role, LocalDate dateActive,
			LocalDate dateInactive) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.dateActive = dateActive;
		this.dateInactive = dateInactive;
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

	public User(String name, String email, String password, Role role, 
			LocalDate dateActive, LocalDate dateInactive) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.dateActive = dateActive;
		this.dateInactive = dateInactive;
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
//	public Location getLocation() {
//		return location;
//	}
//	public void setLocation(Location location) {
//		this.location = location;
//	}
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getDateActive() {
		return dateActive;
	}
	public void setDateActive(LocalDate dateActive) {
		this.dateActive = dateActive;
	}
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getDateInactive() {
		return dateInactive;
	}
	public void setDateInactive(LocalDate dateInactive) {
		this.dateInactive = dateInactive;
	}	
	public List<UserLocation> getAllUserLocations() {
		return allUserLocations;
	}
	public void setAllUserLocations(List<UserLocation> allUserLocations) {
		this.allUserLocations = allUserLocations;
	}

	public List<Location> getCurrentLocations() {
		return currentLocations;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		Integer Zero = Integer.valueOf(0);
		if (Zero.compareTo(getId()) < 0 && Zero.compareTo(other.getId()) < 0) {
			return getId() == other.getId();
		} 
		return Objects.equals(getName(), other.getName()) &&
			   Objects.equals(getEmail(), other.getEmail()) &&
			   Objects.equals(getDateActive(), other.getDateActive()) &&
			   Objects.equals(getDateInactive(), other.getDateInactive());
	}
	@Override
	public String toString() {
		return String.format("User(%d: %s)", getId(), getName() == null ? getEmail() : getName());
	}
}
