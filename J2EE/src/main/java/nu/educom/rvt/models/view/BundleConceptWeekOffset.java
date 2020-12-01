package nu.educom.rvt.models.view;

public class BundleConceptWeekOffset {

	private int bundle_id;
	private int concept_id;
	private int weekOffset;
	
	public BundleConceptWeekOffset(int bundle_id, int concept_id, int weekOffset) {
		super();
		this.bundle_id = bundle_id;
		this.concept_id = concept_id;
		this.weekOffset = weekOffset;
	}
	
	public int getBundle_id() {
		return bundle_id;
	}
	public void setBundle_id(int bundle_id) {
		this.bundle_id = bundle_id;
	}
	public int getConcept_id() {
		return concept_id;
	}
	public void setConcept_id(int concept_id) {
		this.concept_id = concept_id;
	}
	public int getWeekOffset() {
		return weekOffset;
	}
	public void setWeekOffset(int weekOffset) {
		this.weekOffset = weekOffset;
	}
	
	
	
	
	
	
}
