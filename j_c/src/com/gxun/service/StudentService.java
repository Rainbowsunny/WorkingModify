package com.gxun.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.gxun.bean.Page;
import com.gxun.bean.QuestionBean;
import com.gxun.bean.StudentBean;
import com.gxun.bean.SubmitBean;
import com.gxun.bean.WQ;
import com.gxun.bean.WorkBean;
import com.gxun.dao.impl.BaseDaoImpl;
import com.gxun.dao.impl.MarkDaoImpl;
import com.gxun.dao.impl.QuestionDaoImpl;
import com.gxun.dao.impl.SubmitDaoImpl;
import com.gxun.dao.impl.WQImpl;
import com.gxun.dao.impl.WorkDaoImpl;
import com.gxun.dao.inter.BaseDaoInter;
import com.gxun.dao.inter.MarkDaoInter;
import com.gxun.dao.inter.QuestionDaoInter;
import com.gxun.dao.inter.SubmitDaoInter;
import com.gxun.dao.inter.WQInter;
import com.gxun.dao.inter.WorkDaoInter;
import com.gxun.tools.MysqlTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StudentService {
	BaseDaoInter dao = new BaseDaoImpl();
	WorkDaoInter workDao = new WorkDaoImpl();
	SubmitDaoInter submitDao = new SubmitDaoImpl();
	WQInter wqDao = new WQImpl();
	MarkDaoInter markDao = new MarkDaoImpl();
	QuestionDaoInter questionDao = new QuestionDaoImpl();
	
	/**
	 * 获取当前老师发布的作业
	 * @param page
	 * @param rows
	 * @return
	 */
	public String getWokeList(){
		List<Object> list = dao.getList(WorkBean.class, 
					"SELECT * FROM work");
        String result = JSONArray.fromObject(list).toString();
        return result;
	}
	/**
	 * 作业提交
	 * @param page
	 * @param rows
	 * @return
	 */
	public void addSubmit(SubmitBean submit) {
		try {
			//开启事务
			MysqlTool.startTransaction();			
			String sql = "INSERT INTO submit( wid, tid, sid, answer,qid) value(?,?,?,?,?)";
			Object[] param = new Object[]{
					submit.getWid(),
					submit.getTid(),
					submit.getSid(),
					submit.getAnswer(),
					submit.getQid()
				};
			//添加教师信息
			submitDao.addSubmit(sql, param);
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
	 * 获取当前学生的全部作业成绩
	 * @param page
	 * @param rows
	 * @return
	 */
	public String getStuScoreList(String sid){
		List<Object> list;
	    list = dao.getList(SubmitBean.class, 
					"SELECT * FROM mark WHERE sid=?", 
					new Object[]{sid});
        String result = JSONArray.fromObject(list).toString();
        return result;

	}
	public String getQuestionList(String wid){
		List<Object> list;
		List<QuestionBean> questionList=new ArrayList<QuestionBean>();
		QuestionBean question = null;
	    list = dao.getList(WQ.class, "SELECT * FROM wq WHERE wid=?", new Object[]{wid});
	    //获取题目
	    try{
	    	for(Object obj : list){
	    		WQ wq = (WQ) obj;
	    		question= (QuestionBean)dao.getObject(QuestionBean.class, "SELECT * FROM question WHERE qid= ?", 
						new Object[]{wq.getQid()});
				questionList.add(question);
			}
//	    	for(WQ wq : list){
//	    		question= (QuestionBean)dao.getObject(QuestionBean.class, "SELECT * FROM question WHERE qid= ?", 
//					new Object[]{wq.getQid()});
//			questionList.add(question);}
	    	}catch(Exception e){
			e.printStackTrace();
		}
        String result = JSONArray.fromObject(questionList).toString();
        return result;
	}


}
