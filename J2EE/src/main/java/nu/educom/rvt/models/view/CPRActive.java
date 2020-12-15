package nu.educom.rvt.models.view;


public class CPRActive extends ConceptPlusRating{
	
	private Boolean active;
	
//	public CPRActive(Concept concept, Boolean active) {
//		super(concept,0,"",0);
//		this.active = active;
//	}
//	
//	public CPRActive(ConceptPlusRating CPR, Boolean active) {
//		super(CPR.getConcept(),CPR.getRating(),CPR.getComment(),CPR.getWeek());
//		this.active = active;
//	}
//	
//	public CPRActive(Boolean active, String comment, Concept concept,int rating, Integer week) {
//		super(concept, rating, comment, week);
//		this.active = active;
//	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
