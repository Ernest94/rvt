package nu.educom.rvt.services;

import java.util.List;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.User;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ThemeRepository;

public class ThemeConceptService {
	private ConceptRepository conceptRepo;
	private ThemeRepository themeRepo;

	public ThemeConceptService() {
		this.conceptRepo = new ConceptRepository();
		this.themeRepo = new ThemeRepository();
	}
  
	public Theme addTheme(Theme theme) {
		Theme createdTheme = this.themeRepo.create(theme);
		return createdTheme;
	}
	public List<Theme> getAllThemes() {
		List<Theme> themes = this.themeRepo.readAll();
		return themes;
	}
	
	public Concept addConcept(Concept concept) {
		Concept createdConcept = this.conceptRepo.create(concept);
		return createdConcept;
	}
	public List<Concept> getAllConcepts() {
		List<Concept> concepts = this.conceptRepo.readAll();
		return concepts;
	}
	
	public List<Concept> getAllÁctiveConceptsfromUser(User user){
		return this.conceptRepo.readAll();
	}
}
	
