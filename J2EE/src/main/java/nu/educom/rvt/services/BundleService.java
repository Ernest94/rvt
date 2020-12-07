package nu.educom.rvt.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import nu.educom.rvt.repositories.HibernateSession;
import one.util.streamex.StreamEx;

public class BundleService {

	private BundleRepository bundleRepo;
	private BundleConceptRepository bundleConceptRepo;
	private BundleTraineeRepository bundleTraineeRepo;
	
	public BundleService() {
		this.bundleRepo = new BundleRepository();
		this.bundleConceptRepo = new BundleConceptRepository();
	}
	
	public Bundle findBundleByName(String name) {
		return bundleRepo.readAll().stream().filter(b -> b.getName() == name).findFirst().orElse(null);
	}
	
	public Bundle findBundleById(int bundle_id) {
		return bundleRepo.readById(bundle_id);
		
	}
	
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
	
	public int updateBundle(int bundleId, List<BundleConceptWeekOffset> frontendBundleConcepts) {
		Session session = HibernateSession.getSessionFactory().openSession();
	    session.beginTransaction();
	    
		Bundle bundleToUpdate = bundleRepo.readById(bundleId);
		List<BundleConcept> databaseBundleConcepts = bundleToUpdate.getAllConcepts();
		List<Integer> frontendBundleConceptIds = frontendBundleConcepts.stream()
																		.map(item -> item.getConceptId())
																		.collect(Collectors.toList());
		System.out.print("frontendBundleConcepts: " + frontendBundleConcepts + "\n");
		System.out.print("length of frontendBundleConcepts: " + frontendBundleConcepts.size() + "\n");
	
//		List<BundleConceptWeekOffset> bundleConceptsToAddToDB = new ArrayList<>();
		List<BundleConceptWeekOffset> bundleConceptsToAddToDB = frontendBundleConcepts.stream().collect(Collectors.toList());
		int i=0;
		for (BundleConcept databaseBundleConcept : databaseBundleConcepts) {
			System.out.print("index of databaseBundleConcept: " + i + "\n");
			if (!frontendBundleConceptIds.contains(databaseBundleConcept.getConcept().getId())) {
				databaseBundleConcept.setEndDate(LocalDate.now());
			    session.saveOrUpdate(databaseBundleConcept);
			    continue;
			}	
			int j=0;
			for (BundleConceptWeekOffset frontendBundleConcept : frontendBundleConcepts) {
				System.out.print("index of frontendBundleConcept: " + j + "\n");
				if (((Integer)databaseBundleConcept.getConcept().getId()).equals((Integer)frontendBundleConcept.getConceptId()) 
						&& ((Integer)databaseBundleConcept.getWeekOffset()).equals((Integer)frontendBundleConcept.getWeekOffset())) {
					System.out.print("Concept exists in database and weekoffset is the same" + "\n");
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
	
	public void createNewBundle(Bundle bundle) {
		bundleRepo.create(bundle);
	}
	
	public List<Bundle> getAllBundles()
	{
		return bundleRepo.readAll();
	}
	 
	public List<Bundle> getAllBundlesFromUser(User user){
		return bundleTraineeRepo.readAll().stream().filter(bundleTrainee -> bundleTrainee.getUser().getId() == user.getId())
												   .map(bundleTrainee -> bundleTrainee.getBundle()).collect(Collectors.toList());
	}
	
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



















