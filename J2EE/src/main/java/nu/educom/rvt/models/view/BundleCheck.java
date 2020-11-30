package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Bundle;

public class BundleCheck {

	private Bundle bundle;
	private Boolean active;
	
	public BundleCheck(Bundle bundle, Boolean active) {
		this.bundle = bundle;
		this.active = active;
	}
	
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
}
