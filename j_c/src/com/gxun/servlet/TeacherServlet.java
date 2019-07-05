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
import com.gxun.bean.TeacherBean;
import com.gxun.bean.WorkBean;
import com.gxun.service.TeacherService;
import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;

import net.sf.json.JSONObject;

/**
 * 教师类TeacherServlet
 *
 */
public class TeacherServlet extends HttpServlet {
	
	//创建服务层对象
	private TeacherService service = new TeacherService();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		if("LoginOut".equals(method)){ //退出系统
			loginOut(request, response);
		}else if("toTeacherPubView".equalsIgnoreCase(method)){ //转发到作业发布
			request.getRequestDispatcher("/WEB-INF/view/teacher/TeacherPub.jsp").forward(request, response);
		} else if("toTeacherWorkCorrectView".equalsIgnoreCase(method)){ //转发到作业批改
			request.getRequestDispatcher("/WEB-INF/view/teacher/TeacherWorkCorrect.jsp").forward(request, response);
		} else if("toTeacherPersonalView".equalsIgnoreCase(method)){ //转发到个人信息
			request.getRequestDispatcher("/WEB-INF/view/teacher/TeacherPersonal.jsp").forward(request, response);
		}else if("toQueryEvenView".equalsIgnoreCase(method)){ //转发到查询成绩列表页
			request.getRequestDispatcher("/WEB-INF/view/teacher/TeacherQuery.jsp").forward(request, response);
		}  
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取请求的方法
		String method = request.getParameter("method");
		if("getQuestionList".equalsIgnoreCase(method)){ //获取所有教师数据
			getQuestionList(request, response);
		} else if("addWork".equalsIgnoreCase(method)){ //添加教师
			addWork(request, response);
		}else if("deleteQuestion".equalsIgnoreCase(method)) {
			deleteQuestion(request, response);
		}else if("correctSub".equalsIgnoreCase(method)) {
			correctSub(request, response);
		}else if("addQuestion".equalsIgnoreCase(method)) {
			addQuestion(request, response);
		}else if("getGradeList".equalsIgnoreCase(method)) {
			getGradeList(request, response);
		}else if("getWorkList".equalsIgnoreCase(method)) {
			getWorkList(request, response);
		}
		else if("getSubmitList".equalsIgnoreCase(method)) {
			getSubmitList(request, response);
		}
	}
	private void getQuestionList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取数据
		String result = service.getQuestionList();
		//返回数据
        response.getWriter().write(result);
	}
	private void addWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//作业发布
		WorkBean work = new WorkBean();
		work.setTid(((TeacherBean)request.getSession().getAttribute("account")).getTid());
		work.setWname(request.getParameter("wname"));
		work.setStarttime(request.getParameter("starttime"));
		work.setEndtime(request.getParameter("endtime"));
		//获取作业的题号
		String[] ids = request.getParameterValues("ids[]");
		try {
			service.addWork(work,ids);
			response.getWriter().write("success");
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	private void correctSub(HttpServletRequest request, HttpServletResponse response)throws IOException {
		int grade = Integer.parseInt(request.getParameter("grade"));
		String[] wids = request.getParameterValues("wids[]");
		String[] qids = request.getParameterValues("qids[]");
		String[] sids = request.getParameterValues("sids[]");
		int	wid = Integer.parseInt(wids[0]);
		int	qid = Integer.parseInt(qids[0]);
		String	sid = sids[0];
		try {
			service.updateSubmit(grade,wid,qid,sid);
			response.getWriter().write("success");
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	private void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//增加题目
		QuestionBean question = new QuestionBean();
		question.setQcontent(request.getParameter("qcontent"));
		question.setSnum(Integer.parseInt(request.getParameter("snum")));
		try {
			service.addQuestion(question);
			response.getWriter().write("success");
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	private void deleteQuestion(HttpServletRequest request, HttpServletResponse response)throws IOException {
		//获取要删除的题号
		String[] qids = request.getParameterValues("qids[]");
		try {
			service.deleteQuestion(qids);
			response.getWriter().write("success");
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	private void getGradeList(HttpServletRequest request, HttpServletResponse response)throws IOException {
		//获取作业号
		String wid=request.getParameter("wid");
		try {
			service.statistical();
			//获取数据
			String result=service.getMarkList(wid);
			response.getWriter().write(result);
			} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	private void getWorkList(HttpServletRequest request, HttpServletResponse response)throws IOException {
		//获取老师工号
		TeacherBean teacher=(TeacherBean) request.getSession().getAttribute("account");
		try {
			//获取数据
			String result=service.getWokeList(teacher.getTid());
			response.getWriter().write(result);
		} catch (Exception e) {
			response.getWriter().write("fail");
			e.printStackTrace();
		}
	}
	private void getSubmitList(HttpServletRequest request, HttpServletResponse response)throws IOException {
		//获取作业号
		String wid=request.getParameter("wid");
		System.out.println(wid);
		try {
			//获取数据
			String result=service.getSubmitList(wid);
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

