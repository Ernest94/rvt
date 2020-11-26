package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;

public class CPRActive extends ConceptPlusRating{
	
	private Boolean active;
	
	public CPRActive(Concept concept,int rating, String comment, Integer week, Boolean active) {
		super(concept,rating,comment,week);
		this.active = active;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
