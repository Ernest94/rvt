package nu.educom.rvt.models.view;


public class BundleTraineeView  {
	
	private int startWeek;
	private BaseBundleView bundle;
	
	public BundleTraineeView() {
		
	}
	public BundleTraineeView(int startWeek, BaseBundleView bundle) {
		this.startWeek = startWeek;
		this.bundle = bundle;
		
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("(%s, %d)", bundle.getName(), startWeek);
	}


}
