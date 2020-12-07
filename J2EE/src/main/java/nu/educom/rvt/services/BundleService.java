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
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleCheck;
import nu.educom.rvt.models.view.BundleConceptWeekOffset;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import nu.educom.rvt.repositories.DatabaseException;
import nu.educom.rvt.repositories.HibernateSession;
import one.util.streamex.StreamEx;

public class BundleService {
	private static final Logger LOG = LogManager.getLogger();

	private BundleRepository bundleRepo;
	private BundleConceptRepository bundleConceptRepo;
	private BundleTraineeRepository bundleTraineeRepo;
	
	public BundleService(Session session) {
		this.bundleRepo = new BundleRepository(session);
		this.bundleConceptRepo = new BundleConceptRepository(session);
		this.bundleTraineeRepo = new BundleTraineeRepository(session);
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
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	    
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
			    session.saveOrUpdate(databaseBundleConcept);
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
				    session.saveOrUpdate(databaseBundleConcept);
				} 
				++j;
			}
			++i;
		}

		
		if (bundleConceptsToAddToDB.isEmpty()) {
		    session.getTransaction().commit();
			session.close();
			return 1;
		} else {
			for (BundleConceptWeekOffset bundleConceptToAddToDB : bundleConceptsToAddToDB) {
				Concept conceptToAdd = session.get(Concept.class, bundleConceptToAddToDB.getConceptId());
			    session.saveOrUpdate(new BundleConcept(bundleToUpdate,
								    		conceptToAdd,
								    		bundleConceptToAddToDB.getWeekOffset(),
								    		LocalDate.now()));
				}
		    session.getTransaction().commit();
		    session.close();
		    return 1;
		}	
	}
	
	public void createNewBundle(Bundle bundle) throws DatabaseException {
		bundleRepo.create(bundle);
	}
	
	public List<Bundle> getAllBundles() throws DatabaseException {
		return bundleRepo.readAll();
	}
	 
	public List<Bundle> getAllBundlesFromUser(User user) throws DatabaseException{
		// TODO Change to return new UserRepository().readById(user.getId()).Bundels();
		return bundleTraineeRepo.readAll().stream().filter(bundleTrainee -> bundleTrainee.getUser().getId() == user.getId())
												   .map(bundleTrainee -> bundleTrainee.getBundle()).collect(Collectors.toList());
	}
	// TODO move to a BundelLogic class
	public List<BundleCheck> convertToBundleCheck(List<Bundle> bundleAll, List<Bundle> bundleTrainee){
		
		List<BundleCheck> bundleCheck = new ArrayList<BundleCheck>();
		
		for(Bundle bundle : bundleTrainee) {
			bundleCheck.add(new BundleCheck(bundle, true));
		}
		for(Bundle bundle : bundleAll) {
			bundleCheck.add(new BundleCheck(bundle, false));
		}
		
		bundleCheck = StreamEx.of(bundleCheck).distinct(bundleC -> bundleC.getBundle().getId()).toList();
		return bundleCheck;		
	}
	
}



















