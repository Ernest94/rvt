package nu.educom.rvt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nu.educom.rvt.models.Bundle;
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
	}
	
	public Bundle findBundleByName(String name) {
		return bundleRepo.readAll().stream().filter(b -> b.getName() == name).findFirst().orElse(null);
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



















