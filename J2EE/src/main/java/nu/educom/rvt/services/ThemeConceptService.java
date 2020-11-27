package nu.educom.rvt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleTrainee;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.TraineeActive;
import nu.educom.rvt.models.User;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.ThemeRepository;
import nu.educom.rvt.repositories.TraineeActiveRepository;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;




public class ThemeConceptService {
	private ConceptRepository conceptRepo;
	private ThemeRepository themeRepo;
	private TraineeActiveRepository traineeActiveRepo;
	private BundleTraineeRepository bundleTraineeRepo;  
	private BundleConceptRepository bundleConceptRepo;

	public ThemeConceptService() {
		this.conceptRepo = new ConceptRepository();
		this.themeRepo = new ThemeRepository();
		this.traineeActiveRepo = new TraineeActiveRepository();
		this.bundleTraineeRepo = new BundleTraineeRepository();
		this.bundleConceptRepo = new BundleConceptRepository();
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
	
	public List<Concept> getAllActiveConceptsFromUser(User user) {
		List<Concept> traineeActiveConcepts = this.traineeActiveRepo.readAll().stream().filter(traineeActiveConcept -> traineeActiveConcept.getUser().getId() == user.getId())
																				       .filter(traineeActiveConcept -> traineeActiveConcept.getActive() == true)
																				       .map(traineeActiveConcept -> traineeActiveConcept.getConcept())
																				       .collect(Collectors.toList());
		
		List<Bundle> bundleTrainees = this.bundleTraineeRepo.readAll().stream().filter(bundleTrainee -> bundleTrainee.getUser().getId() == user.getId())
																			   .map(bundleTrainee -> bundleTrainee.getBundle())
																			   .collect(Collectors.toList());
		//alle BundleTrainee gezocht met onze Trainee(user). Deze gemapt naar Bundles waardoor we een lijst van bundles krijgen.
		
		List<Concept> conceptsInBundle = new ArrayList<Concept>();
		
		for(Bundle bundle : bundleTrainees) {
			conceptsInBundle.addAll(this.bundleConceptRepo.readAll().stream().filter(conceptsBundle -> conceptsBundle.getBundle().getId() == bundle.getId())
																			 .map(conceptsBundle -> conceptsBundle.getConcept())
																			 .collect(Collectors.toList()));
		}
		//Hierin gekeken welke concepten in de bundel zaten en deze aan de lijst toegevoegd.
		
		List<Concept> inActiveConcepts = this.traineeActiveRepo.readAll().stream().filter(traineeActiveConcept -> traineeActiveConcept.getUser().getId() == user.getId())
																			      .filter(traineeActiveConcept -> traineeActiveConcept.getActive() == false)
																			      .map(traineeActiveConcept -> traineeActiveConcept.getConcept())
																			      .collect(Collectors.toList());
		
		for(Concept inActiveConcept : inActiveConcepts) {
			conceptsInBundle = conceptsInBundle.stream().filter(conceptInBundle -> conceptInBundle.getId() != inActiveConcept.getId())
														.collect(Collectors.toList());
		}
		//checkt of het ID van de conceptBundle gelijk is aan een inactief ID in het inActiveConcept object.
		
		traineeActiveConcepts.addAll(conceptsInBundle);
		
		traineeActiveConcepts = traineeActiveConcepts.stream().distinct().collect(Collectors.toList());
		//verwijdert alle duplicaten door de .distinct()
		
		return traineeActiveConcepts;
	}
<<<<<<< .merge_file_a02156
	public boolean doesThemeExist(Theme theme) {
		ThemeRepository themeRepo = new ThemeRepository();
		Theme duplicate = themeRepo.readByName(theme.getName());		
		return duplicate==null;
	}
	public boolean validateTheme(Theme theme) {
		if(theme.getName().trim().isEmpty() 
		|| theme.getDescription().trim().isEmpty()
		|| theme.getAbbreviation().trim().isEmpty()) {
			return false;
		}
		else {
			return this.doesThemeExist(theme);
		}		
	}
=======
	


	
	
	
	
	
>>>>>>> .merge_file_a18164
}
	
