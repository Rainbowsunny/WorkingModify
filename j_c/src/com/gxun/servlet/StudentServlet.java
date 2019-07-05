package com.gxun.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileUploadException;

import com.gxun.bean.Page;
import com.gxun.bean.QuestionBean;
import com.gxun.bean.StudentBean;
import com.gxun.bean.SubmitBean;
import com.gxun.bean.TeacherBean;
import com.gxun.service.StudentService;
import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;

import net.sf.json.JSONObject;


/**
 * 学生类Servlet
 * @author
 *
 */
public class StudentServlet extends HttpServlet {
	
	//创建服务层对象
	private StudentService service = new StudentService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		String action = request.getParameter("action");
		if("LoginOut".equals(method)){ //退出系统
			loginOut(request, response);
		}else if("toStuWriteView".equalsIgnoreCase(method)){ //转发到作业提交列表页
			request.getRequestDispatcher("/WEB-INF/view/student/StuWrite.jsp").forward(request, response);
		} else if("toStuQueryView".equalsIgnoreCase(method)){ //转发到成绩查询列表页
			request.getRequestDispatcher("/WEB-INF/view/student/StuQuery.jsp").forward(request, response);
		} else if("toStudentPersonalView".equalsIgnoreCase(method)){ //转发到学生个人列表页
			request.getRequestDispatcher("/WEB-INF/view/student/StudentPersonal.jsp").forward(request, response);
		}
		else if("displaywork".equalsIgnoreCase(action)){ //转发到学生个人列表页
			request.getRequestDispatcher("/WEB-INF/view/student/SS.jsp").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		
		if("getWorkList".equalsIgnoreCase(method)){ //获取所有作业数据
			getWorkList(request, response);
		} else if("addSubmit".equalsIgnoreCase(method)){ //提交作业
			addSubmit(request, response);
		}else if("getGradeList".equalsIgnoreCase(method)){ //获取所有成绩数据
			getGradeList(request, response);
		}
		else if("getQuestionList".equalsIgnoreCase(method)){ //获取所有题目数据
			getQuestionList(request, response);
		}
		

	}
	private void getWorkList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取数据
		String result = service.getWokeList();
		//返回数据
        response.getWriter().write(result);
	}
	private void addSubmit(HttpServletRequest request, HttpServletResponse response)throws IOException {
		String answer = request.getParameter("myanswer");
		String[] wids = request.getParameterValues("wids[]");
		String[] tids = request.getParameterValues("tids[]");
		String[] qids = request.getParameterValues("qids[]");
		String sid = ((StudentBean) request.getSession().getAttribute("account")).getSid();
		int	wid = Integer.parseInt(wids[0]);
		String	tid = tids[0];
		int	qid = Integer.parseInt(qids[0]);
		System.out.println(answer+" "+wid+" "+tid+" "+sid+" "+qid);
		SubmitBean submit = new SubmitBean();
		submit.setAnswer(answer);
		submit.setQid(qid);
		submit.setSid(sid);
		submit.setTid(tid);
		submit.setWid(wid);
		try {
			service.addSubmit(submit);
			response.getWriter().write("success");
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	
	private void getQuestionList(HttpServletRequest request, HttpServletResponse response)throws IOException {
		//获取作业号
		String wid=request.getParameter("wid");
		System.out.println(wid);
		try {
		   //获取数据
			String result=service.getQuestionList(wid);
//			System.out.print(result);
			response.getWriter().write(result);
			} catch (Exception e) {
				response.getWriter().write("fail");
				e.printStackTrace();
			}
	}
	/*获取该学生的全部成绩*/
	private void getGradeList(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//获取学生学号
		StudentBean student=(StudentBean) request.getSession().getAttribute("account");
		try {
			//获取数据
			String result=service.getStuScoreList(student.getSid());
			response.getWriter().write(result);
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
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
		request.getSession().removeAttribute("user");
		String contextPath = request.getContextPath();
		//转发到登录界面
		response.sendRedirect(contextPath+"/index.jsp");
	}
}