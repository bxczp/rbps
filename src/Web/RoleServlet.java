package Web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.AuthDao;
import Dao.RoleDao;
import Dao.UserDao;
import Model.PageBean;
import Model.Role;
import Util.DbUtil;
import Util.JsonUtil;
import Util.ResponseUtil;
import Util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月6日 RoleServlet.java
 * @author CZP
 * @parameter
 */
public class RoleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserDao userDao = new UserDao();
	private DbUtil dbUtil = new DbUtil();
	private RoleDao roleDao = new RoleDao();
	private AuthDao authDao=new AuthDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("comBoList".equals(action)) {
			this.comBoList(req,resp);
		} else if ("list".equals(action)) {
			this.roleList(req,resp);
		} else if ("delete".equals(action)) {
			this.roleDelete(req,resp);
		} else if ("save".equals(action)) {
			this.roleSave(req,resp);
		}else if("auth".equals(action)){
			this.roleAuthUpdate(req,resp);
		}
	}

	private void roleAuthUpdate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String roleId=req.getParameter("roleId");
		String authIds=req.getParameter("authIds");
		Connection conn=null;
		JSONObject jsonObject=new JSONObject();
		try {
			conn=dbUtil.getConn();
			int updNum=roleDao.roleAuthIdsUpdate(conn, roleId, authIds);
			if(updNum>0){
				jsonObject.put("success", true);
			}else{
				jsonObject.put("errorMsg", "角色权限更新失败");
			}
			ResponseUtil.write(resp, jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void roleSave(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String roleId=req.getParameter("roleId");
		String roleName=req.getParameter("roleName");
		String roleDescription=req.getParameter("roleDescription");
		Connection conn=null;
		JSONObject jsonObject=new JSONObject();
		Role role=new Role();
		role.setRoleDescription(roleDescription);
		role.setRoleName(roleName);
		if(StringUtil.isNotEmpty(roleId)){
			role.setRoleId(Integer.parseInt(roleId));
		}
		try {
			conn=dbUtil.getConn();
			if(StringUtil.isNotEmpty(roleId)){
				int updNum=roleDao.roleUpdate(conn, role);
				if(updNum>0){
					jsonObject.put("success", true);
				}else{
					jsonObject.put("errorMsg", "更新失败");
				}
			}else{
				int addNum=roleDao.roleAdd(conn, role);
				if(addNum>0){
					jsonObject.put("success", true);
				}else {
					jsonObject.put("errorMsg","添加失败");
				}
			}
			ResponseUtil.write(resp,jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}

	private void roleDelete(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException{
		String delIds=req.getParameter("delIds");
		Connection conn=null;
		JSONObject jsonObject=new JSONObject();
		try {
			conn=dbUtil.getConn();
			String[] roleId=delIds.split(",");
			for(int i=0;i<roleId.length;i++){
				if(roleDao.existRoleWithRoleId(conn, roleId[i])){
					jsonObject.put("errorIndex", i);
					jsonObject.put("errorMsg", "该角色下有用户，不能删除");
					ResponseUtil.write(resp, jsonObject);
					return;
				}
			}
			int delNum=roleDao.roleDelete(conn, delIds);
			if(delNum>0){
				jsonObject.put("success", true);
			}else{
				jsonObject.put("errorMsg", "角色删除失败");
			}
			ResponseUtil.write(resp, jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void roleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String roleName=req.getParameter("s_roleName");
		Connection conn=null;
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
		Role role=new Role();
		if(StringUtil.isNotEmpty(roleName)){
			role.setRoleName(roleName);
		}
		try {
			conn=dbUtil.getConn();
			 JSONObject jsonObject=new JSONObject();
			 JSONArray jsonArray=new JSONArray();
			 jsonArray=JsonUtil.formatRsToJsonArray(roleDao.roleList(conn, pageBean, role));
			 int total=roleDao.roleCount(conn, role);
			 jsonObject.put("rows", jsonArray);
			 jsonObject.put("total", total);
			 ResponseUtil.write(resp, jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void comBoList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Connection conn=null;
		try {
			conn=dbUtil.getConn();
			JSONArray jsonArray=new JSONArray();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("roleId", "");
			jsonObject.put("roleName", "请选择...");
			jsonArray.add(jsonObject);
			jsonArray.addAll(JsonUtil.formatRsToJsonArray(roleDao.roleList(conn,null,new Role())));
			ResponseUtil.write(resp, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
