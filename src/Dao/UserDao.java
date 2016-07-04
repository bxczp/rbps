package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.PageBean;
import Model.User;
import Util.StringUtil;

/**
 * @date 2016年3月5日 UserDao.java
 * @author CZP
 * @parameter
 */
public class UserDao {
	public User login(Connection conn, User user) throws Exception {
		User resultUser = null ;
		String sql = "select * from t_user where userName=? and password=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, user.getUserName());
		preparedStatement.setString(2, user.getPassword());
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			resultUser = new User();
			resultUser.setRoleId(rs.getInt("roleId"));
			resultUser.setPassword(rs.getString("password"));
			resultUser.setUserDescription(rs.getString("userDescription"));
			resultUser.setUserId(rs.getInt("userId"));
			resultUser.setUserName(rs.getString("userName"));
			resultUser.setUserType(rs.getInt("userType"));
		}
		return resultUser;
	}

	public int modifyPassword(Connection conn, String userId, String password) throws Exception {
		String sql = "update t_user set password =? where userId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, password);
		preparedStatement.setInt(2, Integer.parseInt(userId));
		return preparedStatement.executeUpdate();
	}

	public ResultSet userList(Connection conn, PageBean pageBean, User user) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select * from t_user u,t_role r where u.roleId=r.roleId and u.usertype!=1");
		if (StringUtil.isNotEmpty(user.getUserName())) {
			sql.append(" and u.userName like '%" + user.getUserName() + "%'");
		}
		if (user.getRoleId()!=-1) {
			sql.append(" and u.roleId =" + user.getRoleId());
		}
		if (pageBean != null) {
			sql.append(" limit " + pageBean.getStart() + "," + pageBean.getRows());
		}
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
		return preparedStatement.executeQuery();
	}

	public int userCount(Connection conn, User user) throws Exception {
		StringBuffer sql = new StringBuffer(
				"select count(*) as total from t_user u,t_role r where u.roleId=r.roleId and u.userType!=1 ");
		if(StringUtil.isNotEmpty(user.getUserName())){
			sql.append(" and u.userName like '%"+user.getUserName()+"%'");
		}
		if (user.getRoleId()!=-1) {
			sql.append(" and u.roleId =" + user.getRoleId());
		}
		PreparedStatement preparedStatement = conn.prepareStatement(sql.toString());
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return -1;
		}
	}
	
	
	public int userAdd(Connection conn,User user)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("insert into t_user values(null,?,?,2,?,?)");
		PreparedStatement preparedStatement=conn.prepareStatement(sql.toString());
		preparedStatement.setString(1,user.getUserName() );
		preparedStatement.setString(2, user.getPassword());
		preparedStatement.setInt(3, user.getRoleId());
		preparedStatement.setString(4,user.getUserDescription() );
		return preparedStatement.executeUpdate();
	}

	public int userUpadate(Connection conn,User user)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("update t_user set userName=?,password=?, roleId=?,userDescription=? where userId=?");
		PreparedStatement preparedStatement=conn.prepareStatement(sql.toString());
		preparedStatement.setString(1, user.getUserName());
		preparedStatement.setString(2, user.getPassword());
		preparedStatement.setInt(3, user.getRoleId());
		preparedStatement.setString(4, user.getUserDescription());
		preparedStatement.setInt(5, user.getUserId());
		return preparedStatement.executeUpdate();
	}
	
	public boolean existUserWithUserName(Connection conn,String userName)throws Exception{
		String sql="select * from t_user where userName=?";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		preparedStatement.setString(1,userName);
		ResultSet rs=preparedStatement.executeQuery();
		if(rs.next()){
			return true;
		}else {
			return false;
		}
	}
	
	public int userDelte(Connection conn,String userIds)throws Exception{
		String sql="delete from t_user where userId in ("+userIds+")";
		PreparedStatement preparedStatement=conn.prepareStatement(sql);
		return preparedStatement.executeUpdate();
	}
	
}
