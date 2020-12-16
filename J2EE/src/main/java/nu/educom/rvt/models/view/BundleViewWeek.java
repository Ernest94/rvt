package nu.educom.rvt.models.view;

public class BundleViewWeek {
	
	private static BaseBundleView bundle;
	private int week;
	
	public BundleViewWeek(BaseBundleView bundle, int week) {
		this.week = week;
		BundleViewWeek.bundle = bundle;
	}
	
	public BundleViewWeek() {
		super();
	}
	
	public BaseBundleView getBundle() {
		return bundle;
	}
	public void setBundle(BaseBundleView bundle) {
		BundleViewWeek.bundle = bundle;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	
	
}
