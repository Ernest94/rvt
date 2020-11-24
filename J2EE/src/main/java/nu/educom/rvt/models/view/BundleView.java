package nu.educom.rvt.models.view;

import java.util.List;

public class BundleView {

	private Integer id;
	private String name;
	private String creator_name;
	private String creator_location;
	private List<Integer> list_of_concept_ids;
	private List<Integer> list_of_concept_week_offset;
		
	public BundleView(Integer id, String name, String creator_name, String creator_location, 
			List<Integer> list_of_concept_ids, List<Integer> list_of_concept_week_offset ) {
		super();
		this.id = id;
		this.name = name;
		this.creator_name = creator_name;
		this.creator_location = creator_location;
		this.list_of_concept_ids = list_of_concept_ids;
		this.list_of_concept_week_offset = list_of_concept_week_offset;
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
	public List<Integer> getList_of_concept_ids() {
		return list_of_concept_ids;
	}
	public void setList_of_concept_ids(List<Integer> list_of_concept_ids) {
		this.list_of_concept_ids = list_of_concept_ids;
	}
	public List<Integer> getList_of_concept_week_offset() {
		return list_of_concept_week_offset;
	}
	public void setList_of_concept_week_offset(List<Integer> list_of_concept_week_offset) {
		this.list_of_concept_week_offset = list_of_concept_week_offset;
	}
	
}
