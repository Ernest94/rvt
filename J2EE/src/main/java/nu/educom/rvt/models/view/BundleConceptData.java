package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Bundle;

public class BundleConceptData {
	
	private Bundle bundle;
	private int week;
	
	public BundleConceptData(Bundle bundle, int week) {
		this.week = week;
		this.bundle = bundle;
	}
	
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public Bundle getBundle() {
		return bundle;
	}
	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}
	
}
