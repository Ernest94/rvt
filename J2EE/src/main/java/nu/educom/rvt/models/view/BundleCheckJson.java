package nu.educom.rvt.models.view;

import java.util.List;

public class BundleCheckJson {

	private List<BundleCheck> bundleCheck;

	public BundleCheckJson(List<BundleCheck> bundleCheck) {
		this.bundleCheck = bundleCheck;
	}
	
	public List<BundleCheck> getBundleCheck() {
		return bundleCheck;
	}

	public void setBundleCheck(List<BundleCheck> bundleCheck) {
		this.bundleCheck = bundleCheck;
	}
	
}
