package Model;
/**
 *@date 2016年3月5日
 * Role.java
 *@author CZP
 *@parameter
 */
public class Role {
	private int roleId;
	private String roleName;
	private String authIds;
	private String roleDescription;
	private String authStrs;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAuthIds() {
		return authIds;
	}
	public void setAuthIds(String authIds) {
		this.authIds = authIds;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public String getAuthStrs() {
		return authStrs;
	}
	public void setAuthStrs(String authStrs) {
		this.authStrs = authStrs;
	}
	
	
}
