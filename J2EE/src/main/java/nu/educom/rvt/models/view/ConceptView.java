package nu.educom.rvt.models.view;

import nu.educom.rvt.models.Theme;

public class ConceptView {

	private int id;
	private String name;
	private String description;
	private Theme theme;
	
	
	public ConceptView(int id, String name, String description, Theme theme) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.theme = theme;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
}
