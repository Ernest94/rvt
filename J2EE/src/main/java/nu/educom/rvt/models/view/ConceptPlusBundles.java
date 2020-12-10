package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;

import java.time.LocalDate;
import java.util.List;

public class ConceptPlusBundles extends Concept {
	
	private List<BundleConceptData> bundleConcept;

	public ConceptPlusBundles(Theme theme, String name, String description, LocalDate startDate, List<BundleConceptData> bundleConcept) {
		super();
		this.setTheme(theme);
		this.setName(name);
		this.setDescription(description);
		this.setStartDate(startDate);
		this.bundleConcept = bundleConcept;
	}
	
	public List<BundleConceptData> getBundleConcept() {
		return bundleConcept;
	}

	public void setBundleConcept(List<BundleConceptData> bundleConcept) {
		this.bundleConcept = bundleConcept;
	}
}
