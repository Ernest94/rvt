package nu.educom.rvt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleConcept;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.ConceptRating;
import nu.educom.rvt.models.Review;
import nu.educom.rvt.models.Theme;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.models.view.BundleView;
import nu.educom.rvt.models.view.ConceptBundleJSON;
import nu.educom.rvt.models.view.ConceptView;
import nu.educom.rvt.models.view.ConceptWeekOffset;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import nu.educom.rvt.repositories.ConceptRepository;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.ThemeRepository;
import nu.educom.rvt.repositories.TraineeActiveRepository;
import nu.educom.rvt.repositories.TraineeMutationRepository;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import one.util.streamex.StreamEx;


public class ThemeConceptService {
	private ConceptRepository conceptRepo;
	private ThemeRepository themeRepo;
	private TraineeActiveRepository traineeActiveRepo;
	private TraineeMutationRepository traineeMutationRepo;
	private BundleTraineeRepository bundleTraineeRepo;  
	private BundleConceptRepository bundleConceptRepo;
	private BundleRepository bundleRepo;
	private ReviewService reviewServ;

	public ThemeConceptService(Session session) {
		this.conceptRepo = new ConceptRepository(session);
		this.themeRepo = new ThemeRepository(session);
		this.traineeActiveRepo = new TraineeActiveRepository(session);
		this.traineeMutationRepo = new TraineeMutationRepository(session);
		this.bundleTraineeRepo = new BundleTraineeRepository(session);
		this.bundleConceptRepo = new BundleConceptRepository(session);
		this.bundleRepo = new BundleRepository(session);
		this.reviewServ = new ReviewService(session);
	}
  
	public Theme addTheme(Theme theme) throws DatabaseException {
		Theme createdTheme = this.themeRepo.create(theme);
		return createdTheme;
	}
	public List<Theme> getAllThemes() throws DatabaseException {
		List<Theme> themes = this.themeRepo.readAll();
		return themes;
	}
	
	public Concept addConcept(Concept concept) throws DatabaseException {
		Concept createdConcept = this.conceptRepo.create(concept);
		return createdConcept;
	}
	public List<Concept> getAllConcepts() throws DatabaseException {
		List<Concept> concepts = this.conceptRepo.readAll();
		return concepts;
	}
	
//	public List<Concept> getAllActiveConceptsfromUser(User user){
//		return this.conceptRepo.readAll();// hier moeten de filters over gezet worden of hij in de actieve bundel zit oftewel dat hij specifiek voor die user op actief is gezet in de TraineeActive tabel
//	}
	
	public List<Concept> getAllActiveConceptsFromUser(User user) throws DatabaseException {
		List<Review> reviews = reviewServ.getAllCompletedReviewsForUser(user);
		List<ConceptRating> conceptRatings = reviewServ.getAllConceptRatings();
		List<Concept> traineeActiveConcepts = new ArrayList<>();
		
		for(Review review: reviews) {
			traineeActiveConcepts.addAll(conceptRatings.stream().filter(CR -> CR.getReview().getId() == review.getId()).map(CR -> CR.getConcept()).collect(Collectors.toList()));
		}		
		
		traineeActiveConcepts.addAll(this.traineeActiveRepo.readAll().stream().filter(traineeActiveConcept -> traineeActiveConcept.getUser().getId() == user.getId())
																				       .filter(traineeActiveConcept -> traineeActiveConcept.getActive() == true)
																				       .map(traineeActiveConcept -> traineeActiveConcept.getConcept())
																				       .collect(Collectors.toList()));

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
	
	public List<ConceptPlusRating> converToCPRActive (List<ConceptPlusRating> CPRs) throws DatabaseException {
		
		List<ConceptPlusRating> CPRAs = new ArrayList<>();
		for(ConceptPlusRating CPR: CPRs) {
			CPR.setActive(true);
			CPRAs.add(CPR);
		}
		
		List<Concept> inactives = conceptRepo.readAll();
		for(Concept concept: inactives) {
			CPRAs.add(new ConceptPlusRating(concept, false));
		}
		
		CPRAs = StreamEx.of(CPRAs).distinct(foo -> foo.getConcept().getId()).toList();
		
		return CPRAs;		
	}

	
	public ConceptBundleJSON getAllConceptsAndAllBundles() throws DatabaseException {
		List<ConceptView> conceptsView =  new ArrayList<ConceptView>();
		List<BundleView> bundlesConceptsView = new ArrayList<BundleView>();
		
		List<Concept> conceptsModel = this.conceptRepo.readAll();
		List<BundleConcept> bundlesConceptsModel = this.bundleConceptRepo.readAll();		
		List<Bundle> bundlesModel = this.bundleRepo.readAll();
	
		for (Concept concept : conceptsModel) {
			conceptsView.add(new ConceptView(concept.getId(),concept.getName(),concept.getDescription(),concept.getTheme()));
		}
		
		for (Bundle bundle : bundlesModel) {
			Integer bundleId =  bundle.getId();
			String bundleName = bundle.getName();
			String bundleCreatorName = bundle.getCreator().getName();
			String bundleCreatorLocation = bundle.getCreator().getLocation().getName();
			List<ConceptWeekOffset> bundleConceptWeekOffset = new ArrayList<ConceptWeekOffset>();
			for (BundleConcept bundleConcept : bundlesConceptsModel) {
				if (bundleConcept.getBundle().getId()==bundle.getId()) {
					bundleConceptWeekOffset.add(new ConceptWeekOffset(bundleConcept.getConcept().getId(),bundleConcept.getWeekOffset()));
				}
			}
			bundlesConceptsView.add(new BundleView(bundleId,bundleName,bundleCreatorName,bundleCreatorLocation,bundleConceptWeekOffset));
		}	
		ConceptBundleJSON conceptBundleJSON = new ConceptBundleJSON(conceptsView,bundlesConceptsView);
		
		return conceptBundleJSON;
    }
    
	public boolean doesThemeExist(Theme theme) throws DatabaseException {
		Theme duplicate = themeRepo.readByName(theme.getName());		
		return duplicate == null;
    }
    
	public boolean validateTheme(Theme theme) throws DatabaseException {
		if (theme.getName().trim().isEmpty() || 
		    theme.getDescription().trim().isEmpty() ||
		    theme.getAbbreviation().trim().isEmpty()) {
			return false;
		}
		else {
			return this.doesThemeExist(theme);
		}		
	}
	public boolean doesConceptExist(Concept concept) throws DatabaseException {
		Concept duplicate = conceptRepo.readByName(concept.getName());		
		return duplicate==null;
    }
    
	public boolean validateConcept(Concept concept) throws DatabaseException {
		if(concept.getName().trim().isEmpty() 
		|| concept.getDescription().trim().isEmpty()
		|| concept.getTheme()==null) {
			return false;
		}
		else {
			return this.doesConceptExist(concept);
		}		
	}
}
