package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;

public class ConceptPlusRating {

	private Concept concept;
	private int rating;
	private String comment;
	private int week;
	
	public ConceptPlusRating() {
		
	}

	public ConceptPlusRating(Concept concept,int rating, Integer week) {
		this.concept = concept;
		this.rating = rating;
		this.week = week;
	}
	
	public ConceptPlusRating(Concept concept,int rating, String comment, Integer week) {
		this.concept = concept;
		this.rating = rating;
		this.comment = comment;
		this.week = week;
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
	
}
