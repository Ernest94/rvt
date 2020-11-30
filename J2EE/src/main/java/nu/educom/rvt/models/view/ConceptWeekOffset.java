package nu.educom.rvt.models.view;

public class ConceptWeekOffset {

	private int conceptId;
	private int weekOffset;
	
	public ConceptWeekOffset(int conceptId, int weekOffset) {
		super();
		this.conceptId = conceptId;
		this.weekOffset = weekOffset;
	}

	public int getConceptId() {
		return conceptId;
	}

	public void setConceptId(int conceptId) {
		this.conceptId = conceptId;
	}

	public int getWeekOffset() {
		return weekOffset;
	}

	public void setWeekOffset(int weekOffset) {
		this.weekOffset = weekOffset;
	}
	
	
}
