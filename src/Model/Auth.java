package Model;
/**
 *@date 2016年3月5日
 * Auth.java
 *@author CZP
 *@parameter
 */
public class Auth {
	private int authId;
	private String authName;
	private String authPath;
	private int parentId;
	private String authDescription;
	private String stats;
	private String iconCls;
	
	
	
	public Auth(String authName, String authPath, String authDescription, String iconCls) {
		super();
		this.authName = authName;
		this.authPath = authPath;
		this.authDescription = authDescription;
		this.iconCls = iconCls;
	}
	public Auth() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getAuthId() {
		return authId;
	}
	public void setAuthId(int authId) {
		this.authId = authId;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getAuthPath() {
		return authPath;
	}
	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getAuthDescription() {
		return authDescription;
	}
	public void setAuthDescription(String authDescription) {
		this.authDescription = authDescription;
	}
	public String getStats() {
		return stats;
	}
	public void setStats(String stats) {
		this.stats = stats;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	
	
	
	
}
