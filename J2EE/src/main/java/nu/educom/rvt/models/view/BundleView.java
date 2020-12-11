package nu.educom.rvt.models.view;

import java.util.List;

public class BundleView extends BaseBundleView {


	private List<ConceptWeekOffset> bundleConceptWeekOffset;

		
	public BundleView(Integer id, String name, String creator_name, 
			String creator_location, List<ConceptWeekOffset> bundleConceptWeekOffset) {
		super(id, name, creator_name, creator_location);
		this.bundleConceptWeekOffset = bundleConceptWeekOffset;
	}
		
	public List<ConceptWeekOffset> getBundleConceptWeekOffset() {
		return bundleConceptWeekOffset;
	}

	public void setBundleConceptWeekOffset(List<ConceptWeekOffset> bundleConceptWeekOffset) {
		this.bundleConceptWeekOffset = bundleConceptWeekOffset;
	}

	
}
