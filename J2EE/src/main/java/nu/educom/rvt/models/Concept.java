package nu.educom.rvt.models;

import javax.persistence.*;

@Entity
@Table(name="concepts")
public class Concept {

	@Id
	@GeneratedValue
	@Column(name="id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="colegate")
	private Boolean collagate;
	@Column(name="active")
	private Boolean active;
	@ManyToOne()
	@JoinColumn(name="theme_id")
	private Theme theme;
	
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
	public Boolean getCollagate() {
		return collagate;
	}
	public void setCollagate(Boolean collagate) {
		this.collagate = collagate;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
}
