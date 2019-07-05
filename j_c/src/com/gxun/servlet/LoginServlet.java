package com.gxun.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gxun.bean.AccountBean;
import com.gxun.bean.StudentBean;
import com.gxun.bean.TeacherBean;
import com.gxun.service.LoginService;
import com.gxun.tools.VCodeGenerator;


/**
 * 验证码
 * @author
 *
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private LoginService service = new LoginService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		
		if("GetVCode".equalsIgnoreCase(method)){
			getVCode(request, response);
		}else if("LoginOut".equals(method)){ //退出系统
			loginOut(request, response);
		} else if("toStudentView".equals(method)){ //到学生界面
			request.getRequestDispatcher("/WEB-INF/view/student/StudentFace.jsp").forward(request, response);
		} else if("toTeacherView".equals(method)){ //到教师界面
			request.getRequestDispatcher("/WEB-INF/view/teacher/TeacherFace.jsp").forward(request, response);
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		if("Login".equals(method)){ //验证登录
			login(request, response);
		}
	}
	
	
	/**
	 * 验证用户登录
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取用户输入的账户
		String account = request.getParameter("account");
		//获取用户输入的密码
		String password = request.getParameter("password");
		//获取用户输入的验证码
		String vcode = request.getParameter("vcode");
		//获取登录类型
		int type = Integer.parseInt(request.getParameter("type"));
		
		//返回信息
		String msg = "";
		if(account.equals("")||password.equals("")||vcode.equals("")) {
			msg="null";
			response.getWriter().write(msg);
			return;
		}
		
		//获取session中的验证码
		String sVcode = (String) request.getSession().getAttribute("vcode");
		if(!sVcode.equalsIgnoreCase(vcode)){//先判断验证码是否正确
			msg = "vcodeError";
		} else{	//判断用户名和密码是否正确
			//将账户和密码封装
			AccountBean ac = new AccountBean();
			ac.setAid(account);
			ac.setPwd(password);
			int ctype = Integer.parseInt(request.getParameter("type"));
			//创建系统数据层对象
			//查询用户是否存在
			String result = service.login(ac);
			if(result == "-1"){//如果用户名或密码错误
				msg = "NO";
			} 
			else if(result == "0"){
					msg = "ERFRR";
			} 
			else{
				if(ctype == 1) {
					StudentBean student = new StudentBean();
					student = service.Slogin(result);
					//将该用户名保存到session中
					if(student!=null) {
						request.getSession().setAttribute("account", student);
						msg = "student";
					}
					else
						msg = "ETYPE";
				}
				if(ctype == 2) {
					TeacherBean teacher = new TeacherBean();
					teacher = service.Tlogin(result);
					//将该用户名保存到session中
					if(teacher!=null) {
						request.getSession().setAttribute("account", teacher);
						msg = "teacher";
					}
					else
						msg = "ETYPE";
				}
			}
		}
		//返回登录信息
		response.getWriter().write(msg);
	}
	
	/**
	 * 获取验证码
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void getVCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//创建验证码生成器对象
		VCodeGenerator vcGenerator = new VCodeGenerator();
		//生成验证码
		String vcode = vcGenerator.generatorVCode();
		//将验证码保存在session域中,以便判断验证码是否正确
		request.getSession().setAttribute("vcode", vcode);
		//生成验证码图片
		BufferedImage vImg = vcGenerator.generatorRotateVCodeImage(vcode, true);
		//输出图像
		ImageIO.write(vImg, "gif", response.getOutputStream());
	}
	/**
	 * 退出系统
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void loginOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//退出系统时清除系统登录的用户
		request.getSession().removeAttribute("account");
		String contextPath = request.getContextPath();
		//转发到登录界面
		response.sendRedirect(contextPath+"/index.jsp");
	}
	
}
