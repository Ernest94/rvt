package nu.educom.rvt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleTrainee;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleCheck;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import one.util.streamex.StreamEx;

public class BundleService {

	private BundleRepository bundleRepo;
	private BundleConceptRepository bundleConceptRepo;
	private BundleTraineeRepository bundleTraineeRepo;
	
	public BundleService() {
		this.bundleRepo = new BundleRepository();
		this.bundleConceptRepo = new BundleConceptRepository();
		this.bundleTraineeRepo = new BundleTraineeRepository();
	}
	
	public Bundle findBundleByName(String name) {
		return bundleRepo.readAll().stream().filter(b -> b.getName() == name).findFirst().orElse(null);
	}
	
	public Bundle getBundleById(int bundleId) {
		return bundleRepo.readById(bundleId);
	}
	
	public void createNewBundle(Bundle bundle) {
		bundleRepo.create(bundle);
	}
	
	public List<Bundle> getAllBundles()
	{
		return bundleRepo.readAll();
	}
	 
	public List<Bundle> getAllBundlesFromUser(User user){
		List<BundleTrainee> allBundle = bundleTraineeRepo.readAll();
		List<BundleTrainee> bundleTrainee = allBundle.stream().filter(bundleT -> bundleT.getUser().getId() == user.getId())
												   								.collect(Collectors.toList());
		List<Bundle> bundles = bundleTrainee.stream().map(bundleT -> bundleT.getBundle()).collect(Collectors.toList());
		return bundles;		
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
	
	public List<Concept> getAllConceptsFromBundle(Bundle bundle){
		return bundleConceptRepo.readAll().stream().filter(conceptBundle -> conceptBundle.getBundle().getId() == bundle.getId())
												   .map(conceptBundle -> conceptBundle.getConcept()).collect(Collectors.toList());
	}
	
}



















