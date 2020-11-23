package nu.educom.rvt.models.view;

import java.util.List;

import nu.educom.rvt.models.Bundle;

public class BundleJson {
	private List<Bundle> bundles;

	public BundleJson(List<Bundle> bundles) {
		this.bundles = bundles;
	}
	
	public List<Bundle> getBundles() {
		return bundles;
	}

	public void setBundles(List<Bundle> bundles) {
		this.bundles = bundles;
	}
}
