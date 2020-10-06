package nu.educom.rvt.models.view;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import nu.educom.rvt.models.User;
import nu.educom.rvt.models.LinkedUsers;

@Entity @XmlRootElement
public class LinkJson implements Serializable {
	
	public LinkJson () {
		
	}
	
	private static final long serialVersionUID = 1L;
	private User user;
	private List<LinkedUsers> users;
	
	
	public LinkJson(User user, List<LinkedUsers> users) {
		super();
		this.user = user;
		this.users = users;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<LinkedUsers> getUsers() {
		return users;
	}
	public void setUsers(List<LinkedUsers> users) {
		this.users = users;
	}
	
	
}
