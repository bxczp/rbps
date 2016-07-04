package Dao;
/**
 *@date 2016年3月5日
 * AuthDao.java
 *@author CZP
 *@parameter
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Model.Auth;
import Util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AuthDao {
	// 根据parentId获取所有的authName
	public JSONArray getAuthByParentId(Connection conn, String parentId, String authIds) throws Exception {
		// json数组
		JSONArray jsonArray = new JSONArray();
		String sql = "select * from t_auth where parentId=? and authId in (" + authIds + ")";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(parentId));
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			// json对象
			JSONObject jsonObject = new JSONObject();
			// easyui的树节点中有id，text等属性
			jsonObject.put("id", rs.getInt("authId"));
			jsonObject.put("text", rs.getString("authName"));
			if (!this.isHaveChildren(conn, String.valueOf(rs.getInt("authId")), authIds)) {
				// 没有children
				jsonObject.put("state", "open");
			} else {
				jsonObject.put("state", rs.getString("state"));
			}
			jsonObject.put("iconCls", rs.getString("iconCls"));
			// 树节点中有attributes属性
			// attributes中包含其他自定义的属性
			JSONObject attridute = new JSONObject();
			attridute.put("authPath", rs.getString("authPath"));
			jsonObject.put("attributes", attridute);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	// 递归
	public JSONArray getAuthsByParentId(Connection conn, String parentId, String authIds) throws Exception {
		JSONArray array = this.getAuthByParentId(conn, parentId, authIds);
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			if ("open".equals(jsonObject.getString("state"))) {
				// 叶子节点
				continue;
			} else {
				// 下面还有子节点
				// 要使用递归
				jsonObject.put("children", this.getAuthsByParentId(conn, jsonObject.getString("id"), authIds));
			}
		}
		return array;
	}

	// 查询所有的树
	public JSONArray getCheckedAuthByParentId(Connection conn, String parentId, String authIds) throws Exception {
		String sql = "select * from t_auth where parentId=?";
		JSONArray jsonArray = new JSONArray();
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(parentId));
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			// json对象
			JSONObject jsonObject = new JSONObject();
			// easyui的树节点中有id，text等属性
			jsonObject.put("id", rs.getInt("authId"));
			jsonObject.put("text", rs.getString("authName"));
			jsonObject.put("state", rs.getString("state"));
			jsonObject.put("iconCls", rs.getString("iconCls"));
			// 判断取出的authId在不在传入的authIds中
			if (StringUtil.isInAuthIds(rs.getInt("authId") + "", authIds.split(","))) {
				// 选中已存在的authId
				jsonObject.put("checked", true);
			}
			// 树节点中有attributes属性
			// attributes中包含其他自定义的属性
			JSONObject attridute = new JSONObject();
			attridute.put("authPath", rs.getString("authPath"));
			jsonObject.put("attributes", attridute);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	// 递归
	public JSONArray getCheckedAuthsByParentId(Connection conn, String parentId, String authIds) throws Exception {
		JSONArray array = this.getCheckedAuthByParentId(conn, parentId, authIds);
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			if ("open".equals(jsonObject.getString("state"))) {
				// 叶子节点
				continue;
			} else {
				// 下面还有子节点
				// 要使用递归
				jsonObject.put("children", this.getCheckedAuthsByParentId(conn, jsonObject.getString("id"), authIds));
			}
		}
		return array;
	}

	public JSONArray getTreeGridAuthByParentId(Connection conn, String parentId) throws Exception {
		String sql = "select * from t_auth where parentId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(parentId));
		JSONArray jsonArray = new JSONArray();
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			// json对象
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", rs.getInt("authId"));
			jsonObject.put("text", rs.getString("authName"));
			jsonObject.put("state", rs.getString("state"));
			jsonObject.put("iconCls", rs.getString("iconCls"));
			jsonObject.put("authPath", rs.getString("authPath"));
			jsonObject.put("authDescription", rs.getString("authDescription"));
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	// 递归
	public JSONArray getTreeGridAuthsByParentId(Connection conn, String parentId) throws Exception {
		JSONArray array = this.getTreeGridAuthByParentId(conn, parentId);
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			if ("open".equals(jsonObject.getString("state"))) {
				// 叶子节点
				continue;
			} else {
				// 下面还有子节点
				// 要使用递归
				jsonObject.put("children", this.getTreeGridAuthsByParentId(conn, jsonObject.getString("id")));
			}
		}
		return array;
	}

	public int authDelete(Connection conn, String authId) throws Exception {
		String sql = "delete from  t_auth where authId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(authId));
		return preparedStatement.executeUpdate();
	}

	// 返回一个节点下有几个兄弟结点
	public int getAuthCountByParentId(Connection conn, String parentId) throws Exception {
		String sql = "select count(*) as total from t_auth where parentId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(parentId));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}

	public int authAdd(Connection conn, Auth auth) throws Exception {
		String sql = "insert into t_auth values(null,?,?,?,?,'open',?)";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, auth.getAuthName());
		preparedStatement.setString(2, auth.getAuthPath());
		preparedStatement.setInt(3, auth.getParentId());
		preparedStatement.setString(4, auth.getAuthDescription());
		preparedStatement.setString(5, auth.getIconCls());
		return preparedStatement.executeUpdate();
	}

	public int authUpdate(Connection conn, Auth auth) throws Exception {
		String sql = "update t_auth set authName=?,authPath=?,authDescription=?,iconCls=? where authId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, auth.getAuthName());
		preparedStatement.setString(2, auth.getAuthPath());
		preparedStatement.setString(3, auth.getAuthDescription());
		preparedStatement.setString(4, auth.getIconCls());
		preparedStatement.setInt(5, auth.getAuthId());
		return preparedStatement.executeUpdate();
	}

	// 判断某节点是否是叶子节点
	public boolean isLeaf(Connection conn, String parentId) throws Exception {
		String sql = "select * from t_auth where parentId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(parentId));
		ResultSet rs = preparedStatement.executeQuery();
		if (rs.next()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isHaveChildren(Connection conn, String parentId, String authIds) throws Exception {
		String sql = "select * from t_auth where parentId=? and authId in (" + authIds + ")";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, Integer.parseInt(parentId));
		ResultSet rs = preparedStatement.executeQuery();
		return rs.next();
	}

	// 改变auth的state
	public int updateStateByAuthId(Connection conn, String state, String parentId) throws Exception {
		String sql = "update t_auth set state = ? where authId=?";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setString(1, state);
		preparedStatement.setInt(2, Integer.parseInt(parentId));
		return preparedStatement.executeUpdate();
	}

}
