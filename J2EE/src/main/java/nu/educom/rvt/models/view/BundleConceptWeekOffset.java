package nu.educom.rvt.models.view;

public class BundleConceptWeekOffset {

	private int bundleId;
	private int conceptId;
	private int weekOffset;
	
	public BundleConceptWeekOffset() {
		
	}
	
	
	public BundleConceptWeekOffset(int bundleId, int conceptId, int weekOffset) {
		super();
		this.bundleId = bundleId;
		this.conceptId = conceptId;
		this.weekOffset = weekOffset;
	}
	
	public int getBundleId() {
		return bundleId;
	}
	public void setBundleId(int bundleId) {
		this.bundleId = bundleId;
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
