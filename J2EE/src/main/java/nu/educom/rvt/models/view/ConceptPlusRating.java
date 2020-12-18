package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;

public class ConceptPlusRating {

	private Concept concept;
	private Boolean feather;
	private int rating;
	private String comment;
	private int week;
	private Boolean active;
	
	
	public ConceptPlusRating() {
		
	}

	public ConceptPlusRating(Concept concept,int rating, Integer week) {
		this.concept = concept;
		this.rating = rating;
		this.week = week;
		this.comment = "";
	}
	
	public ConceptPlusRating(Concept concept, Boolean active) {
		this.concept = concept;
		this.active = active;
		this.feather = false;
		this.rating = 0;
		this.week = 0;
		this.comment = "";
	}
	
	public ConceptPlusRating(Concept concept, Boolean feather, int rating, String comment, Integer week) {
		this.concept = concept;
		this.rating = rating;
		this.comment = comment;
		this.week = week;
		this.feather = feather;
	}
	
	public ConceptPlusRating(Concept concept, Boolean feather, int rating, String comment, Integer week, Boolean active) {
		this.concept = concept;
		this.feather = feather;
		this.rating = rating;
		this.comment = comment;
		this.week = week;
		this.active = active;
	}
	
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getFeather() {
		return feather;
	}

	public void setFeather(Boolean feather) {
		this.feather = feather;
	}
	
	
}
