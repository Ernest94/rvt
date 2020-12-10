package nu.educom.rvt.models.view;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.User;

@Entity @XmlRootElement
public class WeekChangeForUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private User user;
	private Concept concept;
	private int week;
	
	public WeekChangeForUser() {
		
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
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	@Override
	public String toString() {
		return String.format("WeekChange: %s %s week %d endDate %s", user, concept, week);
	}
}