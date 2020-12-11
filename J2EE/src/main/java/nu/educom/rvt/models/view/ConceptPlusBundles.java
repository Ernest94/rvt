package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;
import java.util.List;

public class ConceptPlusBundles {
	
	
	private static Concept concept;
	private static List<BundleViewWeek> bundles;

	public ConceptPlusBundles(List<BundleViewWeek> bundles, Concept concept) {
		super();
		ConceptPlusBundles.concept = concept;
		ConceptPlusBundles.bundles = bundles;
	}
	
	public ConceptPlusBundles() {
		super();
	}
	
	public List<BundleViewWeek> getBundles() {
		return bundles;
	}

	public void setBundles(List<BundleViewWeek> bundle) {
		ConceptPlusBundles.bundles = bundle;
	}

	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		ConceptPlusBundles.concept = concept;
	}
}
