package nu.educom.rvt.models.view;

import java.util.List;

import javax.persistence.Entity;

import nu.educom.rvt.models.Role;

@Entity
public class RoleJson {
	
	private List<Role> roles;
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
