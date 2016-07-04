package Web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.AuthDao;
import Dao.RoleDao;
import Model.Auth;
import Model.User;
import Util.DbUtil;
import Util.ResponseUtil;
import Util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月6日 AuthServlet.java
 * @author CZP
 * @parameter
 */
public class AuthServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DbUtil dbUtil = new DbUtil();
	private AuthDao authDao = new AuthDao();
	private RoleDao roleDao = new RoleDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("menu".equalsIgnoreCase(action)) {
			this.menuAction(req, resp);
		} else if ("authMenu".equals(action)) {
			this.authMenuAction(req, resp);
		} else if ("authTreeGridMenu".equals(action)) {
			this.authTreeGridMenuAction(req, resp);
		} else if ("save".equals(action)) {
			this.saveAction(req, resp);
		} else if ("delete".equals(action)) {
			this.deleteAction(req, resp);
		}
	}

	private void deleteAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		String authId = req.getParameter("authId");
		String parentId = req.getParameter("parentId");
		JSONObject jsonObject = new JSONObject();
		int delNum = 0;
		int sonNum = 0;
		try {
			conn = dbUtil.getConn();
			boolean isLeaf = authDao.isLeaf(conn, authId);
			if (isLeaf) {// 是叶子节点
				sonNum = authDao.getAuthCountByParentId(conn, parentId);
				if (sonNum == 1) {// 父节点下只有 一个节点（要删除的节点）
					// 有多个操作，要使用jdbc事务
					conn.setAutoCommit(false);
					authDao.updateStateByAuthId(conn, "open", parentId);
					delNum = authDao.authDelete(conn, authId);
					conn.commit();
				} else {// 父节点下还有子节点
					delNum = authDao.authDelete(conn, authId);
				}
				if (delNum > 0) {
					jsonObject.put("success", true);
				} else {
					jsonObject.put("errorMsg", "删除失败");
				}
			} else {// 不是叶子节点
				jsonObject.put("errorMsg", "该菜单不是叶子节点，不能删除！");
			}
			ResponseUtil.write(resp, jsonObject);
		} catch (Exception e) {
			if (sonNum == 1) {
				// 事务回滚
				// 父节点下只有 一个节点（要删除的节点）
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void saveAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		String authId = req.getParameter("authId");
		String authName = req.getParameter("authName");
		String authPath = req.getParameter("authPath");
		String authDescription = req.getParameter("authDescription");
		String parentId = req.getParameter("parentId");
		String iconCls = req.getParameter("iconCls");
		JSONObject jsonObject = new JSONObject();
		Auth auth = new Auth(authName, authPath, authDescription, iconCls);
		if (StringUtil.isNotEmpty(authId)) {// 更新操作
			auth.setAuthId(Integer.parseInt(authId));
		} else {// 添加操作
			auth.setParentId(Integer.parseInt(parentId));
		}
		int num = 0;
		boolean isleaf = false;
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isNotEmpty(authId)) {// 更新操作
				num = authDao.authUpdate(conn, auth);
				if (num > 0) {
					jsonObject.put("success", true);
				} else {
					jsonObject.put("errorMsg", "更新失败");
				}
			} else {// 添加操作
					// 判断是否是叶子节点
				isleaf = authDao.isLeaf(conn, parentId);
				if (isleaf) {
					// 是叶子节点，要进行更新state和添加操作
					// 因为有两个操作，所以要用jdbc事务
					conn.setAutoCommit(false);
					authDao.updateStateByAuthId(conn, "closed", parentId);
					num = authDao.authAdd(conn, auth);
					conn.commit();
				} else {
					num = authDao.authAdd(conn, auth);
				}

				if (num > 0) {
					jsonObject.put("success", true);
				} else {
					jsonObject.put("errorMsg", "添加失败");
				}
			}
			ResponseUtil.write(resp, jsonObject);
		} catch (Exception e) {
			if (isleaf) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void authTreeGridMenuAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String parentId = req.getParameter("parentId");
		String id = req.getParameter("id");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			JSONArray jsonArray = authDao.getTreeGridAuthsByParentId(conn, parentId);

			ResponseUtil.write(resp, jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void authMenuAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String roleId = req.getParameter("roleId");
		String parentId = req.getParameter("parentId");
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			String authIds = roleDao.getAuthIds(conn, roleId);
			JSONArray array = authDao.getCheckedAuthsByParentId(conn, parentId, authIds);
			// 把array输出
			ResponseUtil.write(resp, array);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void menuAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String parentId = req.getParameter("parentId");
		// parentId=-1表示为根节点
		HttpSession session = req.getSession();
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			User currentUser = (User) session.getAttribute("currentUser");
			String authIds = roleDao.getAuthIds(conn, String.valueOf(currentUser.getRoleId()));
			JSONArray array = authDao.getAuthsByParentId(conn, parentId, authIds);
			// 把array输出
			ResponseUtil.write(resp, array);
			/**
			 * [{"id":1,"text":"某系统","state":"closed","iconCls":"icon-home",
			 * "attributes":{"authPath":""},"children"
			 * :[{"id":2,"text":"权限管理","state":"closed","iconCls":
			 * "icon-permission","attributes":{"authPath":""},"children"
			 * :[{"id":12,"text":"用户管理","state":"open","iconCls":
			 * "icon-userManage","attributes":{"authPath":"userManage
			 * .html"}},{"id":13,"text":"角色管理","state":"open","iconCls":"icon-
			 * roleManage","attributes":{"authPath":"roleManage
			 * .html"}},{"id":14,"text":"菜单管理","state":"open","iconCls":"icon-
			 * menuManage","attributes":{"authPath":"menuManage
			 * .html"}}]},{"id":3,"text":"学生管理","state":"closed","iconCls":"icon
			 * -student","attributes":{"authPath":""
			 * },"children":[{"id":5,"text":"住宿管理","state":"open","iconCls":
			 * "icon-item","attributes":{"authPath":"zsgl
			 * .html"}},{"id":6,"text":"学生信息管理","state":"open","iconCls":"icon-
			 * item","attributes":{"authPath":"xsxxgl
			 * .html"}},{"id":7,"text":"学籍管理","state":"open","iconCls":"icon-
			 * item","attributes":{"authPath":"xjgl.html"
			 * }},{"id":8,"text":"奖惩管理","state":"open","iconCls":"icon-item",
			 * "attributes":{"authPath":"jcgl.html"}}
			 * ]},{"id":4,"text":"课程管理","state":"closed","iconCls":"icon-course"
			 * ,"attributes":{"authPath":""},"children"
			 * :[{"id":9,"text":"课程设置","state":"open","iconCls":"icon-item",
			 * "attributes":{"authPath":"kcsz.html"}},
			 * {"id":10,"text":"选课情况","state":"open","iconCls":"icon-item",
			 * "attributes":{"authPath":"xkqk.html"}},{"id"
			 * :11,"text":"成绩录入","state":"open","iconCls":"icon-item",
			 * "attributes":{"authPath":"cjlr.html"}}]},{"id"
			 * :15,"text":"修改密码","state":"open","iconCls":"icon-modifyPassword",
			 * "attributes":{"authPath":""}},{"id"
			 * :16,"text":"安全退出","state":"open","iconCls":"icon-exit",
			 * "attributes":{"authPath":""}}]}]
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
