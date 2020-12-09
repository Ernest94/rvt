package nu.educom.rvt.models.view;

public class BundleTraineeView  {
	
	private int startWeek;
	private BaseBundleView bundle;
	
	public BundleTraineeView(int startWeek, BaseBundleView bundle) {
		this.setStartWeek(startWeek);
		this.setBundle(bundle);
		
	}

	public int getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	public BaseBundleView getBundle() {
		return bundle;
	}

	public void setBundle(BaseBundleView bundle) {
		this.bundle = bundle;
	}
}
