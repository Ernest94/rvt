package nu.educom.rvt.models.view;

import java.util.ArrayList;
import java.util.List;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleConcept;

public class BundleView extends BaseBundleView {


	private List<ConceptWeekOffset> bundleConceptWeekOffset;

		
	public BundleView(Bundle bundle) {
		super(bundle);
		this.bundleConceptWeekOffset = new ArrayList<ConceptWeekOffset>();
		for(BundleConcept bc: bundle.getAllConcepts()) {
			ConceptWeekOffset cbwo = new ConceptWeekOffset(bc);
			bundleConceptWeekOffset.add(cbwo);
		}
		
	}
	
	public BundleView(Integer id, String name, String creator_name, 
			List<ConceptWeekOffset> bundleConceptWeekOffset) {
		super(id, name, creator_name);
		this.bundleConceptWeekOffset = bundleConceptWeekOffset;
	}
		
	public List<ConceptWeekOffset> getBundleConceptWeekOffset() {
		return bundleConceptWeekOffset;
	}

	public void setBundleConceptWeekOffset(List<ConceptWeekOffset> bundleConceptWeekOffset) {
		this.bundleConceptWeekOffset = bundleConceptWeekOffset;
	}

	
}
