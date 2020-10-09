package nu.educom.rvt.services;

import javax.persistence.RollbackException;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ThemeRepository;

public class ConceptService {
	private ConceptRepository conceptRepo;
	private ThemeRepository themeRepo;

	public ConceptService() {
		this.conceptRepo = new ConceptRepository();
		this.themeRepo = new ThemeRepository();
	}
  
	public int addTheme(Theme theme) throws RollbackException {
		int themeId = this.themeRepo.create(theme);
		return themeId;
	}
  
	public boolean addConcept(Concept concept) {
	    this.conceptRepo.create(concept);
		return false;
	}  
  
  
}
