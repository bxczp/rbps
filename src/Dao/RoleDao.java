package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.PageBean;
import Model.Role;
import Util.StringUtil;

/**
 *@date 2016年3月5日
 * RoleDao.java
 *@author CZP
 *@parameter
 */
public class RoleDao {

//	根据用户的roleId获取该用户的roleName
	public String getRoleName(Connection conn,String roleId) throws Exception{
		String sql="select roleName from t_role where roleId=?";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(roleId));
		ResultSet rs=preparedStatement.executeQuery();
		if(rs.next()){
			return rs.getString("roleName");
		}
		else {
			return "没有分配角色";
		}
	}
	
	
	public String getAuthIds(Connection conn,String roleId) throws Exception{
		String sql="select authIds from t_role where roleId=?";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(roleId));
		ResultSet rs=preparedStatement.executeQuery();
		if(rs.next()){
			return rs.getString("authIds");
		}
		else {
			return "没有分配权限";
		}
	}
	
	
	public ResultSet roleList(Connection conn,PageBean pageBean,Role role)throws Exception{
		StringBuffer sql=new StringBuffer("select * from t_role");
		if(StringUtil.isNotEmpty(role.getRoleName())){
			sql.append(" where roleName like '%"+role.getRoleName()+"%'");
		}
		if(pageBean!=null){
			sql.append(" limit "+pageBean.getStart()+","+pageBean.getRows());
		}
		PreparedStatement preparedStatement=conn.prepareStatement(sql.toString());
		return preparedStatement.executeQuery();
	}
	
	
	public int roleCount(Connection conn,Role role)throws Exception{
		StringBuffer sql=new StringBuffer("select count(*) as total from t_role");
		if(StringUtil.isNotEmpty(role.getRoleName())){
			sql.append(" where roleName like '%"+role.getRoleName()+"%'");
		}PreparedStatement preparedStatement=conn.prepareStatement(sql.toString());
		ResultSet rs=preparedStatement.executeQuery();
		if(rs.next()){
			return rs.getInt("total");
		}else {
			return -1;
		}
	}
	
	public int roleDelete(Connection conn,String delIds)throws Exception{
		String sql="delete from t_role where roleId in ("+delIds+")";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		return preparedStatement.executeUpdate();
	}
	
	public boolean existRoleWithRoleId(Connection conn,String roleId) throws Exception{
		String sql="select * from t_user where roleId=?";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(roleId));
		ResultSet rs=preparedStatement.executeQuery();
		if(rs.next()){
			return true;
		}else{
			return false;
		}
	}
	
	public int roleAdd(Connection conn,Role role) throws Exception{
		String sql="insert into t_role values(null,?,'',?)";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setString(1, role.getRoleName());
		preparedStatement.setString(2, role.getRoleDescription());
		return preparedStatement.executeUpdate();
	}
	
	
	
	public int roleUpdate(Connection conn,Role role) throws Exception{
		String sql="update t_role set roleName=?,roleDescription=? where roleId=?";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setString(1, role.getRoleName());
		preparedStatement.setString(2, role.getRoleDescription());
		preparedStatement.setInt(3, role.getRoleId());
		return preparedStatement.executeUpdate();
	}
	
	
	
	public int roleAuthIdsUpdate(Connection conn,String roleId,String authIds)throws Exception{
		String sql="update t_role set authIds=? where roleId=?";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setString(1, authIds);
		preparedStatement.setInt(2, Integer.parseInt(roleId));
		return preparedStatement.executeUpdate();
	}
	
}
