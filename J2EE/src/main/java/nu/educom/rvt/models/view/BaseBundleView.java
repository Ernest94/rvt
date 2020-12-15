package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Bundle;

public class BaseBundleView {
	
	
	protected Integer id;
	protected String name;
	protected String creator_name;
	protected String creator_location;
	
	public BaseBundleView()
	{
		
	}
	
	public BaseBundleView(Bundle bundle)
	{
		this(bundle.getId(), bundle.getName(), bundle.getCreator().getName(), bundle.getCreator().getCurrentLocations().get(0).getName());
	}

	public BaseBundleView(Integer id, String name, String creator_name, String creator_location) {
		super();
		this.id = id;
		this.name = name;
		this.creator_name = creator_name;
		this.creator_location = creator_location;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator_name() {
		return creator_name;
	}

	public void setCreator_name(String creator_name) {
		this.creator_name = creator_name;
	}

	public String getCreator_location() {
		return creator_location;
	}

	public void setCreator_location(String creator_location) {
		this.creator_location = creator_location;
	}

	
}
