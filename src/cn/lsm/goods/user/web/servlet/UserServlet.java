package cn.lsm.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.lsm.goods.user.domain.User;
import cn.lsm.goods.user.service.UserService;
import cn.lsm.goods.user.service.exception.UserException;

/**
 * 用户模块WEB层
 * 
 * @author Wister.M.Li
 * 
 */
public class UserServlet extends BaseServlet {

	private UserService userService = new UserService();

	/*
	 * ajax用户名是否注册校验
	 */
	public String ajaxValidateLoginname(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		// 将用户名传进来
		String loginname = req.getParameter("loginname");
		// 通过service得到校验结果，
		boolean b = userService.ajaxValidateLoginname(loginname);
		// 发给客户端
		resp.getWriter().print(b);
		return null;
	}

	/*
	 * ajax Email是否注册校验
	 */
	public String ajaxValidateEmail(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		// 将email传进来
		String email = req.getParameter("email");
		// 通过service得到校验结果，
		boolean b = userService.ajaxValidateEmail(email);
		// 发给客户端
		resp.getWriter().print(b);
		return null;
	}

	/*
	 * 验证码是否正确校验
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		// 获取输入框中的校验码
		String verifyCode = req.getParameter("verifyCode");
		// 获取图片上真是的验证码
		String vcode = (String) req.getSession().getAttribute("vCode");
		// 进行比较，忽略大小写
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		// 发送给客户端
		resp.getWriter().print(b);
		return null;
	}

	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1.封装表单数据到User中
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		/*
		 * 2.校验数据，如果校验失败，保存错误信息，返回到regist.jsp
		 */
		Map<String, String> errors = validateRegist(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}

		/*
		 * 3.使用Sercice完成业务
		 */
		userService.regist(formUser);
		/*
		 * 4.保存成功信息，转发发msg.jsp显示！
		 */
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册功能，请马上到邮箱激活");
		return "f:/jsps/msg.jsp";
	}

	/**
	 * 校验注册 对表单的校验进行了逐个校验，如果有错误使用当前字段名称为key，错误信息为value，保存到map中返回到map
	 */
	private Map<String, String> validateRegist(User formUser,
			HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		// 1.校验登录名
		String loginname = formUser.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3到10之间！");
		} else if (!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已经被注册！");
		}

		// 2.校验登录密码
		String loginpass = formUser.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3到10之间！");
		}

		// 3.校验确认登录密码
		String reloginpass = formUser.getReloginpass();
		if (reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if (!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次密码不一致！");
		}

		// 4.校验Email
		String email = formUser.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "密码不能为空！");
		} else if (!email
				.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "邮箱格式不正确！");
		} else if (!userService.ajaxValidateEmail(email)) {
			errors.put("email", "邮箱已经被注册！");
		}

		// 5.验证码校验
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}

		return errors;
	}

	/**
	 * 激活功能
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String activation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 1.获取参数激活码，
		// 2.用激活码调用service方法完成激活
		// service方法有可能抛出异常，把异常信息拿来，报讯到request中，转发到msg.jsp显示
		// 3.保存成功信息到request，转发到msg.jsp显示
		String code = req.getParameter("activationCode");
		try {
			userService.activation(code);
			req.setAttribute("code", "success");// 通知msg.jsp显示对号
			req.setAttribute("msg", "恭喜，激活成功，请马上登陆");
		} catch (UserException e) {
			// 说明service抛出了异常
			req.setAttribute("msg", e.getMessage());
			req.setAttribute("code", "error");// 通知msg.jsp显示x
		}

		return "f:/jsps/msg.jsp";
	}

	/**
	 * 修改密码
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updatePassword(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1.封装表单数据到user中 2.从session中获取到uid
		 * 3.使用uid和表单中的oldpass和newpass来调用service
		 * -->如果出现异常，保存异常信息到request中，转发到pwd.jsp 4.保存成功信息到request中 5.转发到msg.jsp
		 */
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		User user = (User) req.getSession().getAttribute("sessionUser");
		if (user == null) {
			req.setAttribute("msg", "您还没有登录");
			return "f:/jsps/user/login.jsp";
		}
		try {
			userService.updatePassword(user.getUid(), formUser.getNewpass(),
					formUser.getLoginpass());
			req.setAttribute("msg", "修改密码成功");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			req.setAttribute("msg", e.getMessage());// 保存异常信息到request
			req.setAttribute("user", formUser);// 为了回显
			return "f:/jsps/user/pwd.jsp";
		}
	}

	/**
	 * 登录的方法
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * //1.封装表单数据到user2.校验表单数据3.使用service查询，得到User4.查看用户是否存在，如果不存在：
		 * 1.保存错误信息：用户名和密码错误。2.保存表单数据，为了回显示3.转发到login.jsp5.如果存在，查看状态，如果状态为false；
		 * 1.如果错误信息：您没有激活2、保存表单数据：为了回显3.转发到login.jsp6.登录成功
		 * 1.保存当前查询出的user到session中 2.保存当前用户的名称到cookie中，注意中文需要编码处理
		 */
		// 1.封装表单数据到user
		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		// 2.校验表单数据
		Map<String, String> errors = validatelogin(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}
		// 3.调用userserivce#login（）方法
		User user = userService.login(formUser);
		// 4.开始判断
		if (user == null) {
			req.setAttribute("msg", "用户名或者密码错误！");
			req.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		} else {
			if (!user.isStatus()) {
				req.setAttribute("msg", "您还没有激活！");
				req.setAttribute("user", formUser);
				return "f:/jsps/user/login.jsp";
			} else {
				// 保存用户到session
				req.getSession().setAttribute("sessionUser", user);
				// 湖区用户名保存到cookie中
				String loginname = user.getLoginname();
				// 将用户名的名字改变编码
				loginname = URLEncoder.encode(loginname, "utf-8");
				Cookie cookie = new Cookie("loginname", loginname);
				// 设置cookie的时间
				cookie.setMaxAge(60 * 60 * 60 * 24 * 10);
				resp.addCookie(cookie);
				return "f:/index.jsp";
			}
		}

	}

	/**
	 * 登录校验方法，内容等你自己来完成
	 */
	private Map<String, String> validatelogin(User formUser, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		return errors;
	}

	/**
	 * 推出功能
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
}
