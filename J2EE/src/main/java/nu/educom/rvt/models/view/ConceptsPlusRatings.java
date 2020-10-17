package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;

public class ConceptsPlusRatings {

	private Concept concept;
	private int rating;
	private Theme theme;

	public ConceptsPlusRatings(Concept concept,int rating,Theme theme) {
		this.concept = concept;
		this.theme = theme;
		this.rating = rating;
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
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	
}
