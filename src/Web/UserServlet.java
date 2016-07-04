package Web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.RoleDao;
import Dao.UserDao;
import Model.PageBean;
import Model.User;
import Util.DbUtil;
import Util.JsonUtil;
import Util.PropertiesUtil;
import Util.ResponseUtil;
import Util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @date 2016年3月5日 UserServlet.java
 * @author CZP
 * @parameter
 */
public class UserServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserDao userDao = new UserDao();
	private DbUtil dbUtil = new DbUtil();
	private RoleDao roleDao = new RoleDao();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if ("login".equals(action)) {
			this.login(req, resp);
		} else if ("logout".equals(action)) {
			this.logout(req, resp);
		} else if ("modifyPassword".equals(action)) {
			this.modifyPassword(req, resp);
		} else if ("list".equals(action)) {
			this.list(req, resp);
		} else if ("save".equals(action)) {
			this.save(req, resp);
		}else if ("delete".equals(action)) {
			this.delete(req, resp);
		}
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Connection conn=null;
		String userIds=req.getParameter("delIds");
		JSONObject jsonObject = new JSONObject();
		try {
			conn=dbUtil.getConn();
			int delNum=userDao.userDelte(conn, userIds);
			if(delNum>0){
				jsonObject.put("success", true);
			}else{
				jsonObject.put("errorMsg", "删除失败");
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

	private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Connection conn = null;
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String roleId = req.getParameter("roleId");
		String userDescription = req.getParameter("userDescription");
		String userId = req.getParameter("userId");
		User user = new User();
		user.setPassword(password);
		user.setRoleId(Integer.parseInt(roleId));
		user.setUserDescription(userDescription);
		user.setUserName(userName);
		JSONObject jsonObject = new JSONObject();
		if (StringUtil.isNotEmpty(userId)) {
			user.setUserId(Integer.parseInt(userId));
		}
		try {
			conn = dbUtil.getConn();
			if (StringUtil.isEmpty(userId)) {//添加操作
				if (userDao.existUserWithUserName(conn, userName)) {
					jsonObject.put("errorMsg", "用户名重复");
				} else {
					int addNum = userDao.userAdd(conn, user);
					if (addNum > 0) {
						jsonObject.put("success", true);
					} else {
						jsonObject.put("errorMsg", "保存失败");
					}
				}
			}else if(StringUtil.isNotEmpty(userId)){//更新操作
				int updateNum=userDao.userUpadate(conn, user);
				if (updateNum > 0) {
					jsonObject.put("success", true);
				} else {
					jsonObject.put("errorMsg", "修改失败");
				}
			}
			ResponseUtil.write(resp, jsonObject);
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

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String page = req.getParameter("page");
		String rows = req.getParameter("rows");
		User user = new User();
		String s_userName = req.getParameter("s_userName");
		String s_roleId = req.getParameter("s_roleId");
		if (StringUtil.isNotEmpty(s_roleId)) {
			user.setRoleId(Integer.parseInt(s_roleId));
		}
		if (StringUtil.isNotEmpty(s_userName)) {
			user.setUserName(s_userName);
		}
		Connection conn = null;
		try {
			conn = dbUtil.getConn();
			PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
			int totalNum = userDao.userCount(conn, user);
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			jsonArray = JsonUtil.formatRsToJsonArray(userDao.userList(conn, pageBean, user));
			jsonObject.put("total", totalNum);
			jsonObject.put("rows", jsonArray);
			ResponseUtil.write(resp, jsonObject);
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

	private void modifyPassword(HttpServletRequest req, HttpServletResponse resp) {
		Connection conn = null;
		JSONObject object = new JSONObject();
		String newPassword = req.getParameter("newPassword");
		String userId = req.getParameter("userId");
		try {
			conn = dbUtil.getConn();
			int upNum = userDao.modifyPassword(conn, userId, newPassword);
			if (upNum > 0) {
				object.put("success", true);
			} else {
				object.put("errorMsg", "修改失败");
			}
			ResponseUtil.write(resp, object);
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

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Connection conn = null;
		HttpSession session = req.getSession();
		// 清除session
		session.invalidate();
		// resp.sendRedirect("login.jsp");
		req.getRequestDispatcher("login.jsp").forward(req, resp);
	}

	private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		HttpSession session = req.getSession();
		Connection conn = null;
		// 验证码
		String imageCode = req.getParameter("imageCode");
		User user = new User();
		req.setAttribute("userName", userName);
		req.setAttribute("password", password);
		req.setAttribute("imagecCode", imageCode);
		if (StringUtil.isEmpty(password) || StringUtil.isEmpty(userName)) {
			req.setAttribute("error", "用户名或密码为空");
			req.getRequestDispatcher("login.jsp").forward(req, resp);
			return;
		}
		if (StringUtil.isEmpty(imageCode)) {
			req.setAttribute("error", "验证码为空");
			req.getRequestDispatcher("login.jsp").forward(req, resp);
			return;
		}
		if (StringUtil.isNotEmpty(imageCode)) {
			if (!imageCode.equals((String) session.getAttribute("sRand"))) {
				req.setAttribute("error", "验证码错误");
				req.getRequestDispatcher("login.jsp").forward(req, resp);
				return;
			}
		}
		if (StringUtil.isNotEmpty(password)) {
			user.setPassword(password);
		}
		if (StringUtil.isNotEmpty(userName)) {
			user.setUserName(userName);
		}
		try {
			conn = dbUtil.getConn();
			User currentUser = userDao.login(conn, user);
			if (currentUser == null) {
				req.setAttribute("error", "用户名或密码错误");
				req.getRequestDispatcher("login.jsp").forward(req, resp);
			} else {
				String roleName = roleDao.getRoleName(conn, String.valueOf((currentUser.getRoleId())));
				currentUser.setRoleName(roleName);
				session.setAttribute("currentUser", currentUser);
				req.getRequestDispatcher("main.jsp").forward(req, resp);
				// resp.sendRedirect("main.jsp");
			}
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
