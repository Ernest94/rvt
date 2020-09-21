package nu.educom.rvt.models.view;

import java.util.List;

public class UserSearchJson {

	private List<UserSearch> userSearch;
	
	public UserSearchJson (List<UserSearch> userSearch)
	{
		super();
		this.userSearch = userSearch;
	}
	
	public List<UserSearch> getUserSearch() {
		return userSearch;
	}
	public void setLocation(List<UserSearch> userSearch) {
		this.userSearch = userSearch;
	}
}
