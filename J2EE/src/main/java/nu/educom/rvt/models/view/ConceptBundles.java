package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;

import java.util.List;

import nu.educom.rvt.models.BundleConcept;

public class ConceptBundles {

	private Concept concept;
	private List<BundleConcept> bundleConcepts;
	
	public ConceptBundles() {
		
	}
	
	public ConceptBundles(Concept concept, List<BundleConcept> bundleConcepts) {
		this.concept = concept;
		this.bundleConcepts = bundleConcepts;		
	}
	
	public Concept getConcept() {
		return concept;
	}
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	public List<BundleConcept> getBundleConcepts() {
		return bundleConcepts;
	}
	public void setBundleConcepts(List<BundleConcept> bundleConcepts) {
		this.bundleConcepts = bundleConcepts;
	}	
}
