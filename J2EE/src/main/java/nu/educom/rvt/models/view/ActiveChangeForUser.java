package nu.educom.rvt.models.view;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.User;

@Entity @XmlRootElement
public class ActiveChangeForUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private User user;
	private Concept concept;
	private boolean active;
	
	public ActiveChangeForUser() {
		
	}
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}