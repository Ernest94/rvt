package nu.educom.rvt.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleConcept;
import nu.educom.rvt.models.BundleTrainee;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.TraineeMutation;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BaseBundleView;
import nu.educom.rvt.models.view.BundleConceptWeekOffset;
import nu.educom.rvt.models.view.BundleTraineeView;
import nu.educom.rvt.models.view.BundleViewWeek;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.TraineeMutationRepository;

public class BundleService {
	private static final Logger LOG = LogManager.getLogger();

	private BundleRepository bundleRepo;
	private BundleConceptRepository bundleConceptRepo;
	private BundleTraineeRepository bundleTraineeRepo;
	private TraineeMutationRepository traineeMutationRepo;
	
	public BundleService(Session session) {
		this.bundleRepo = new BundleRepository(session);
		this.bundleConceptRepo = new BundleConceptRepository(session);
		this.bundleTraineeRepo = new BundleTraineeRepository(session);
		this.bundleTraineeRepo = new BundleTraineeRepository(session);
		this.traineeMutationRepo = new TraineeMutationRepository(session);
	}
	
	public boolean doesBundleExist(Bundle bundle) throws DatabaseException {
		Bundle duplicate = bundleRepo.readByName(bundle.getName());		
		return duplicate==null;
    }
    
	public boolean validateBundle(Bundle bundle) throws DatabaseException {
		if(bundle.getName().trim().isEmpty()) {
			return false;
		}
		else {
			return this.doesBundleExist(bundle);
		}		
	}
	
	
	public Bundle findBundleByName(String name) throws DatabaseException {
		
		return bundleRepo.readAll().stream().filter(b -> b.getName() == name).findFirst().orElse(null);
	}
	
	public Bundle findBundleById(int bundle_id) throws DatabaseException {
		return bundleRepo.readById(bundle_id);
		
	}
	// TODO move to a BundelLogic class
	public int isBundleIdConsistent(List<BundleConceptWeekOffset> bundleConcepts) {
		int bundleId = bundleConcepts.get(0).getBundleId();
		List<BundleConceptWeekOffset> checkBundleConcepts = bundleConcepts.stream()
				.filter(element -> element.getBundleId()==bundleId)
				.collect(Collectors.toList());	
		if (bundleConcepts.size()==checkBundleConcepts.size()) {
			return bundleId;
		} else {
			return -1;
		}
	}
	
	public int updateBundle(int bundleId, List<BundleConceptWeekOffset> frontendBundleConcepts) throws DatabaseException {
	    
		Bundle bundleToUpdate = bundleRepo.readById(bundleId);
		List<BundleConcept> databaseBundleConcepts = bundleToUpdate.getAllConcepts();
		List<Integer> frontendBundleConceptIds = frontendBundleConcepts.stream()
																		.map(item -> item.getConceptId())
																		.collect(Collectors.toList());
		LOG.debug("frontendBundleConcepts: {}", frontendBundleConcepts);
	    LOG.trace("length of frontendBundleConcepts: {}", frontendBundleConcepts.size());
//		List<BundleConceptWeekOffset> bundleConceptsToAddToDB = new ArrayList<>();
		List<BundleConceptWeekOffset> bundleConceptsToAddToDB = frontendBundleConcepts.stream().collect(Collectors.toList());
		int i=0;
		for (BundleConcept databaseBundleConcept : databaseBundleConcepts) {
			LOG.trace("index of databaseBundleConcept: {}", i);
			if (!frontendBundleConceptIds.contains(databaseBundleConcept.getConcept().getId())) {
				databaseBundleConcept.setEndDate(LocalDate.now());
			    bundleConceptRepo.update(databaseBundleConcept);
			    continue;
			}	
			int j=0;
			for (BundleConceptWeekOffset frontendBundleConcept : frontendBundleConcepts) {
				LOG.trace("index of frontendBundleConcept: {} ", j);
				if (((Integer)databaseBundleConcept.getConcept().getId()).equals((Integer)frontendBundleConcept.getConceptId()) 
						&& ((Integer)databaseBundleConcept.getWeekOffset()).equals((Integer)frontendBundleConcept.getWeekOffset())) {
					LOG.trace("Concept exists in database and weekoffset is the same");
					bundleConceptsToAddToDB = bundleConceptsToAddToDB.stream()
											.filter(item -> !(((Integer)item.getConceptId()).equals((Integer)frontendBundleConcept.getConceptId())))
											.collect(Collectors.toList());
				}
				else if (((Integer)databaseBundleConcept.getConcept().getId()).equals((Integer)frontendBundleConcept.getConceptId())
						&& !(((Integer)databaseBundleConcept.getWeekOffset()).equals((Integer)frontendBundleConcept.getWeekOffset()))) {
					databaseBundleConcept.setEndDate(LocalDate.now());
					bundleConceptRepo.update(databaseBundleConcept);
				} 
				++j;
			}
			++i;
		}

		
		if (bundleConceptsToAddToDB.isEmpty()) {
			return 1;
		} else {
			for (BundleConceptWeekOffset bundleConceptToAddToDB : bundleConceptsToAddToDB) {
				Concept conceptToAdd = new Concept();
				conceptToAdd.setId(bundleConceptToAddToDB.getConceptId());
			    bundleConceptRepo.update(new BundleConcept(bundleToUpdate,
								    		conceptToAdd,
								    		bundleConceptToAddToDB.getWeekOffset(),
								    		LocalDate.now()));
				}
		    return 1;
		}	
	}
		
	public Bundle getBundleById(int bundleId) throws DatabaseException {
		return bundleRepo.readByKnownId(bundleId);
	}
	
	public Bundle createNewBundle(Bundle bundle) throws DatabaseException {
		return bundleRepo.create(bundle);
	}
	public void setBundleConceptsNewBundle(Bundle dbBundle,Bundle duplicateBundle) throws DatabaseException {
		List<BundleConcept> newBundleConcepts = new ArrayList<BundleConcept>();
		Bundle bundleInDB = this.findBundleById(dbBundle.getId());
		List<BundleConcept> linkedBundleConcepts = bundleInDB.getAllConcepts();
		for (BundleConcept bundleConcept : linkedBundleConcepts) {
			newBundleConcepts.add(new BundleConcept(duplicateBundle, bundleConcept.getConcept(), bundleConcept.getWeekOffset(), LocalDate.now()));
		}
		bundleConceptRepo.createMulti(newBundleConcepts);
	}
	
	public List<Bundle> getAllBundles() throws DatabaseException {
		return bundleRepo.readAll();
	}
	public List<BaseBundleView> getAllBundleViews() throws DatabaseException {
		return bundleRepo.readAll().stream().map(bundle -> new BaseBundleView(bundle)).collect(Collectors.toList());
	}
	
	public List<BaseBundleView> getAllCreatorBundles(User user) throws DatabaseException{
		return bundleRepo.readAll().stream().filter(bundle -> bundle.getCreator().getId() == user.getId()).map(bundle -> new BaseBundleView(bundle)).collect(Collectors.toList());
	}
	 
	 
	public List<BundleTraineeView> getAllBundlesFromUser(User user) throws DatabaseException
	{
		List<BundleTraineeView> bunTs = bundleTraineeRepo.readAll().stream()
								.filter(bundleTrainee -> bundleTrainee.getUser().getId() == user.getId())
								.map(bundleTrainee -> new BundleTraineeView(bundleTrainee.getStartWeek(), new BaseBundleView(bundleTrainee.getBundle())))
								.collect(Collectors.toList());
		return bunTs;
	}

	public void setBundlesForUser(User user, List<BundleTraineeView> bundles) throws DatabaseException 
	{
		List<BundleTrainee> records = bundleTraineeRepo.readByUserId(user.getId());
		List<BundleTrainee> updated = new ArrayList<BundleTrainee>();
		
		for(BundleTrainee record : records) {
			boolean found=false;
			for(BundleTraineeView btv: bundles) {
				if(record.getBundle().getId() == btv.getBundle().getId()) {
					found=true;
				}
			}
			if(!found) {
				record.setEndDate(LocalDate.now());
				updated.add(record);
			}
		}
		
		for(BundleTraineeView btv: bundles) {
			if(!(btv.getBundle()==null || btv.getBundle().getId()< 0 || btv.getStartWeek() <= 0)) {
				boolean alreadyMutated = false;
				for(BundleTrainee bt: updated) {
					if(bt.getBundle().getId()==btv.getBundle().getId() && bt.getEndDate() != null) {
						alreadyMutated = true;
					}
				}
				if(!alreadyMutated) {
					boolean found=false;
					
					for(BundleTrainee record : records) {
						if(record.getBundle().getId() == btv.getBundle().getId()) {
							
							found = true;
							if(record.getStartWeek() == btv.getStartWeek()) {
								continue;
							}
							//end the old record
							record.setEndDate(LocalDate.now());
							updated.add(record);
							
							//create new record with same bundle but different startweek
							BundleTrainee newRecord = new BundleTrainee(user, record.getBundle(), btv.getStartWeek(), LocalDate.now());
							updated.add(newRecord);
						}
					}
					if(!found) {
						BundleTrainee newRecord = new BundleTrainee(user, bundleRepo.readById(btv.getBundle().getId()), btv.getStartWeek(), LocalDate.now());
						updated.add(newRecord);
					}
				}

			}
		}
		System.out.println("Ik ga deze records in de BundleTrainee tabel zetten:");
		for(BundleTrainee bt : updated) {
			System.out.println("Id: " + bt.getId() + " Bundle: " + bt.getBundle().getName() + " met startweek: "+ bt.getStartWeek());
			bundleTraineeRepo.update(bt);
		}

	    
	}

	public boolean doesBundleExists(int bundleId) throws DatabaseException {
		return bundleRepo.readById(bundleId) != null;
	}
	
	public List<Concept> getAllConceptsFromBundle(Bundle bundle) throws DatabaseException{
		return bundleConceptRepo.readAll().stream().filter(conceptBundle -> conceptBundle.getBundle().getId() == bundle.getId())
												   .map(conceptBundle -> conceptBundle.getConcept()).collect(Collectors.toList());
	}
	
	public List<ConceptPlusRating> getWeekForCPR(List<ConceptPlusRating> CPRs, User user) throws DatabaseException {
		List<ConceptPlusRating> CPRWeek = new ArrayList<>();
		List<TraineeMutation> traineeMutations = traineeMutationRepo.readAll();
		traineeMutations = traineeMutations.stream().filter(traineeMutation -> traineeMutation.getUser().getId() == user.getId())
																			  .collect(Collectors.toList());
		List<BundleTrainee> bundlesTrainee = bundleTraineeRepo.readAll().stream().filter(bundleTrainee -> bundleTrainee.getUser().getId() == user.getId())
																		      .collect(Collectors.toList());
		List<BundleConcept> bundlesConcept = bundleConceptRepo.readAll();
		
		for(ConceptPlusRating CPR : CPRs) {
			CPRWeek.add(getWeek(CPR, traineeMutations, bundlesTrainee, bundlesConcept));
		}
		
		return CPRWeek;
	}
	
	private ConceptPlusRating getWeek(ConceptPlusRating CPR, List<TraineeMutation> traineeMutations, List<BundleTrainee> bundlesTrainee, List<BundleConcept> bundlesConcept) {
		
		ConceptPlusRating CPRWeek = CPR;
		
		TraineeMutation traineeMutation = traineeMutations.stream().filter(TM -> TM.getConcept().getId() == CPR.getConcept().getId()).findFirst().orElse(null);
		if(traineeMutation != null) {
			CPRWeek.setWeek(traineeMutation.getWeek());
			return CPRWeek;
		}
		
		List<BundleConcept> conceptB = bundlesConcept.stream().filter(bundleConcept -> bundleConcept.getConcept().getId() == CPR.getConcept().getId()).collect(Collectors.toList());
		List<BundleTrainee> traineeB = new ArrayList<>();
				
		for(BundleConcept bundleC: conceptB)
		{
			traineeB.addAll(bundlesTrainee.stream().filter(bundleTrainee -> bundleTrainee.getBundle().getId() == bundleC.getBundle().getId()).collect(Collectors.toList()));
		}
		
		if(traineeB.isEmpty()) {
			CPRWeek.setWeek(0);
			return CPRWeek;
		}
		
		int week = 0;
		
		for(BundleTrainee bundleTrainee: traineeB) {
			BundleConcept bundleConcept = conceptB.stream().filter(BC -> BC.getBundle().getId() == bundleTrainee.getBundle().getId()).findFirst().orElse(null);
			
			int localWeek = bundleTrainee.getStartWeek() + bundleConcept.getWeekOffset();
			if(week == 0 || week > localWeek) week = localWeek;
		}
		
		CPRWeek.setWeek(week);
		return CPRWeek;
	}
	
	public void addBundlesToConcept(Concept concept, List<BundleViewWeek> bundles) throws DatabaseException {
		
		List<BundleConcept> bundleConcepts = new ArrayList<>();
		
		for(BundleViewWeek bundle: bundles) {
			if(bundleConcepts.stream().filter(dis -> dis.getBundle().getId() == bundle.getBundle().getId()).findFirst().orElse(null) == null)
			{
				Bundle actualBundle = bundleRepo.readById(bundle.getBundle().getId());
				bundleConcepts.add(new BundleConcept(actualBundle, concept, bundle.getWeek(), LocalDate.now()));
			}			
		}
		
		bundleConceptRepo.createMulti(bundleConcepts);
	}	
}

