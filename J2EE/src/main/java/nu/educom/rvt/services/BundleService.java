package nu.educom.rvt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nu.educom.rvt.models.Bundle;
import nu.educom.rvt.models.BundleConcept;
import nu.educom.rvt.models.BundleTrainee;
import nu.educom.rvt.models.Concept;
import nu.educom.rvt.models.TraineeMutation;
import nu.educom.rvt.models.User;
import nu.educom.rvt.models.view.BundleCheck;
import nu.educom.rvt.models.view.ConceptPlusRating;
import nu.educom.rvt.repositories.BundleConceptRepository;
import nu.educom.rvt.repositories.BundleRepository;
import nu.educom.rvt.repositories.BundleTraineeRepository;
import nu.educom.rvt.repositories.TraineeActiveRepository;
import nu.educom.rvt.repositories.TraineeMutationRepository;
import one.util.streamex.StreamEx;

public class BundleService {

	private BundleRepository bundleRepo;
	private BundleConceptRepository bundleConceptRepo;
	private BundleTraineeRepository bundleTraineeRepo;
	private TraineeActiveRepository traineeActiveRepo;
	private TraineeMutationRepository traineeMutationRepo;
	
	public BundleService() {
		this.bundleRepo = new BundleRepository();
		this.bundleConceptRepo = new BundleConceptRepository();
		this.bundleTraineeRepo = new BundleTraineeRepository();
		this.traineeActiveRepo = new TraineeActiveRepository();
		this.traineeMutationRepo = new TraineeMutationRepository();
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
	
	public List<ConceptPlusRating> getWeekForCPR(List<ConceptPlusRating> CPRs, User user) {
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
	
	
	
}



















