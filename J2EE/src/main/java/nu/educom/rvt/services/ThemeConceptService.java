package nu.educom.rvt.services;

import java.util.List;

import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.TraineeActive;
import nu.educom.rvt.models.User;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ThemeRepository;
import nu.educom.rvt.repositories.TraineeActiveRepository;


public class ThemeConceptService {
	private ConceptRepository conceptRepo;
	private ThemeRepository themeRepo;
	private TraineeActiveRepository traineeActiveRepo;

	public ThemeConceptService() {
		this.conceptRepo = new ConceptRepository();
		this.themeRepo = new ThemeRepository();
		this.traineeActiveRepo = new TraineeActiveRepository();
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
	
//	public List<Concept> getAllActiveConceptsfromUser(User user){
//		return this.conceptRepo.readAll();// hier moeten de filters over gezet worden of hij in de actieve bundel zit oftewel dat hij specifiek voor die user op actief is gezet in de TraineeActive tabel
//	}
	
	public List<TraineeActive> getAllActiveConceptsFromUser(User user) {
		return this.traineeActiveRepo.readAll();
	}
	
	
	
}
	
