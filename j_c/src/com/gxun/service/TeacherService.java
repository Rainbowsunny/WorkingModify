package com.gxun.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.gxun.bean.Page;
import com.gxun.bean.QuestionBean;
import com.gxun.bean.StudentBean;
import com.gxun.bean.SubmitBean;
import com.gxun.bean.WQ;
import com.gxun.bean.WorkBean;
import com.gxun.dao.impl.BaseDaoImpl;
import com.gxun.dao.impl.MarkDaoImpl;
import com.gxun.dao.impl.QuestionDaoImpl;
import com.gxun.dao.impl.StudentDaoImpl;
import com.gxun.dao.impl.SubmitDaoImpl;
import com.gxun.dao.impl.WQImpl;
import com.gxun.dao.impl.WorkDaoImpl;
import com.gxun.dao.inter.BaseDaoInter;
import com.gxun.dao.inter.MarkDaoInter;
import com.gxun.dao.inter.QuestionDaoInter;
import com.gxun.dao.inter.StudentDaoInter;
import com.gxun.dao.inter.SubmitDaoInter;
import com.gxun.dao.inter.WQInter;
import com.gxun.dao.inter.WorkDaoInter;
import com.gxun.tools.MysqlTool;
import com.gxun.tools.StringTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeacherService {
	BaseDaoInter dao = new BaseDaoImpl();
	QuestionDaoInter questionDao = new QuestionDaoImpl();
	WorkDaoInter workDao = new WorkDaoImpl();
	WQInter wqDao = new WQImpl();
	SubmitDaoInter submitDao = new SubmitDaoImpl();
	MarkDaoInter markDao = new MarkDaoImpl();
	StudentDaoInter stuDao = new StudentDaoImpl();
	
//	public String getQuestionList(Page page) {
//		//sql语句
//		String sql = "SELECT * FROM question ORDER BY qid DESC LIMIT ?,?";
//		//获取数据
//		List<QuestionBean> list = questionDao.getQuestionList(sql,new Object[]{page.getStart(), page.getSize()});
//		//获取总记录数
//		long total = questionDao.count("SELECT COUNT(*) FROM question", new Object[]{});
//		//定义Map
//		Map<String, Object> jsonMap = new HashMap<String, Object>();  
//		//total键 存放总记录数，必须的
//        jsonMap.put("total", total);
//        //rows键 存放每页记录 list 
//        jsonMap.put("rows", list); 
//        //格式化Map,以json格式返回数据
//        String result = JSONObject.fromObject(jsonMap).toString();
//        //返回
//		return result;
//	}
	/**
	 * 获取题目信息
	 * @param page
	 * @param rows
	 * @return
	 */
	public String getQuestionList(){
		List<Object> list = dao.getList(QuestionBean.class, 
					"SELECT * FROM question");
        String result = JSONArray.fromObject(list).toString();
        return result;
	}
	/**
	 * 删除题目
	 * @param page
	 * @param rows
	 * @return
	 */
	public void deleteQuestion(String[] ids) throws Exception{
		//获取占位符
		String mark = StringTool.getMark(ids.length);
		Integer qid[] = new Integer[ids.length];
		for(int i =0 ;i < ids.length;i++){
			qid[i] = Integer.parseInt(ids[i]);
		}
		//获取连接
		Connection conn = MysqlTool.getConnection();
		//开启事务
		MysqlTool.startTransaction();
		try {
			//删除教师与课程的关联
			questionDao.deleteTransaction(conn, "DELETE FROM question WHERE qid IN("+mark+")", qid);	
			//提交事务
			MysqlTool.commit();
		} catch (Exception e) {
			//回滚事务
			MysqlTool.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			MysqlTool.closeConnection();
		}
	}
	//初始化成绩表
	public void initMark(int wid) {
		Connection conn = MysqlTool.getConnection();
		List<Object> list = dao.getList(StudentBean.class, "SELECT * FROM student ", new Object[]{});//获取学生列表
		String sql = "INSERT INTO mark( sid, wid, grade) value(?,?,?)";
		try{
			for(Object obj : list){
				StudentBean stu = (StudentBean) obj;
				Object[] param = new Object[]{stu.getSid(),wid,0};
				dao.insertTransaction(conn, sql, param);
			}
//	    	for(StudentBean stu : list){
//	    		Object[] param = new Object[]{stu.getSid(),wid,0};
//	    		markDao.insertTransaction(conn, sql, param);
//	    	}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 作业发布
	 * @param page
	 * @param rows
	 * @return
	 */
	public void addWork (WorkBean work,String[] ids) {
		Connection conn = MysqlTool.getConnection();
		try {
			//开启事务
			MysqlTool.startTransaction();			
			String sql = "INSERT INTO work( wname, starttime, endtime, tid) value(?,?,?,?)";
			Object[] param = new Object[]{
					work.getWname(), 
					work.getStarttime(), 
					work.getEndtime(),
					work.getTid()
				};
			//添加作业信息
			dao.insertTransaction(conn, sql, param );
			MysqlTool.commit();
			//添加作业与题目关系
			WorkBean work1 = new WorkBean();
			work1 = (WorkBean)dao.getObject(WorkBean.class,"SELECT * FROM work WHERE wname=?", new Object[]{work.getWname()});
			for(int i=0;i<ids.length;i++)
				dao.insertTransaction(conn, "INSERT INTO wq(wid,qid) value(?,?)", new Object[]{
					work1.getWid(),
					ids[i]
				});
			initMark(work1.getWid());
			//提交事务
			MysqlTool.commit();
		} catch (Exception e) {
			//回滚事务
			MysqlTool.rollback();
			e.printStackTrace();
		}
		finally {
			MysqlTool.closeConnection();
		}
	}
	/**
	 * 题目添加
	 * @param page
	 * @param rows
	 * @return
	 */
	public void addQuestion (QuestionBean question) {
		Connection conn = MysqlTool.getConnection();
		try {
			//开启事务
			MysqlTool.startTransaction();			
			String sql = "INSERT INTO question( qcontent,snum) value(?,?)";
			Object[] param = new Object[]{
					question.getQcontent(), 
					question.getSnum(),
				};
			//添加题目信息
			dao.insertTransaction(conn, sql, param);
			//提交事务
			MysqlTool.commit();
		} catch (Exception e) {
			//回滚事务
			MysqlTool.rollback();
			e.printStackTrace();
		} finally {
			MysqlTool.closeConnection();
		}
	}
	/**
	 * 获取当前老师发布的作业
	 * @param page
	 * @param rows
	 * @return
	 */
	public String getWokeList(String tid){
		List<Object> list = dao.getList(WorkBean.class, 
					"SELECT * FROM work WHERE tid=?", 
					new Object[]{tid});
        String result = JSONArray.fromObject(list).toString();
        return result;
	}
	
	public String getMarkList (String wid){
	    //sql语句
		String sql = "SELECT * FROM mark WHERE wid = ?";
		//获取数据
		List<Object> list = dao.getList(SubmitBean.class,sql,new Object[]{wid});
	    //返回
		 String result = JSONArray.fromObject(list).toString();
		return result;	
	}
	public String getSubmitList (String wid){
	    //sql语句
		String sql = "SELECT * FROM submit WHERE wid = ?";
		//获取数据
		List<Object> list = dao.getList(SubmitBean.class,sql,new Object[]{wid});
	    //返回
		String result = JSONArray.fromObject(list).toString();
		System.out.println(result);
		return result;	
	}
	/**
	 * 录入每题得分
	 * @param page
	 * @param rows
	 * @return
	 */
	public void updateSubmit (int grade,int wid,int qid,String sid) {
		Connection conn = MysqlTool.getConnection();
		try {
			//开启事务
			MysqlTool.startTransaction();			
			String sql = "update submit set grade = ? where wid = ? and qid = ? and sid = ?";
			Object[] param = new Object[]{
					grade,
					wid,
					qid,
					sid
				};
			//添加题目信息
			dao.updateTransaction(conn, sql, param);
			//提交事务
			MysqlTool.commit();
		} catch (Exception e) {
			//回滚事务
			MysqlTool.rollback();
			e.printStackTrace();
		} finally {
			MysqlTool.closeConnection();
		}
	}
	//统计分数
	public void statistical() {
		String sql = "SELECT sum(grade) FROM submit WHERE sid = ? AND wid = ?";
		String sql2 = "update mark set grade = ? where sid = ? and wid = ?";
		List<Object> list = dao.getList(StudentBean.class, "SELECT * FROM student ", new Object[]{});//获取学生列表
		List<WorkBean> wlist = workDao.getWorkList("SELECT * FROM work ", new Object[]{});
		Connection conn = MysqlTool.getConnection();
		try{
			for(Object obj : list){
				StudentBean stu = (StudentBean) obj;
				for(WorkBean wor : wlist){
	    			List<Object> param = new LinkedList<>();
		    		int scorce = submitDao.SumSubmit(sql, param );
		    		Object[] param2 = new Object[]{scorce,stu.getSid(),wor.getWid()};
					//录入成绩信息
					dao.updateTransaction(conn, sql2, param2);
	    		}	
			}
//	    	for(StudentBean stu : list){
//	    		for(WorkBean wor : wlist){
//	    			List<Object> param = new LinkedList<>();
//		    		int scorce = submitDao.SumSubmit(sql, param ).intValue();
//		    		Object[] param2 = new Object[]{scorce,stu.getSid(),wor.getWid()};
//					//录入成绩信息
//					markDao.updateTransaction(conn, sql2, param2);
//	    		}	
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			MysqlTool.closeConnection();
		}
	}
}
