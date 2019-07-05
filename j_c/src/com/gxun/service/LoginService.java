package com.gxun.service;

import com.gxun.bean.AccountBean;
import com.gxun.bean.StudentBean;
import com.gxun.bean.TeacherBean;
import com.gxun.dao.impl.AccountDaoImpl;
import com.gxun.dao.impl.BaseDaoImpl;
import com.gxun.dao.impl.StudentDaoImpl;
import com.gxun.dao.impl.TeacherDaoImpl;
import com.gxun.dao.inter.AccountDaoInter;
import com.gxun.dao.inter.BaseDaoInter;
import com.gxun.dao.inter.StudentDaoInter;
import com.gxun.dao.inter.TeacherDaoInter;

public class LoginService {
	BaseDaoInter dao = new BaseDaoImpl();
	/**
	* 验证用户登录
	* @param 账号对象
	* @return 登录结果 
	*/
	public String login(AccountBean account){
		String s = "-1";	//表示账号不存在
		AccountBean ac = (AccountBean) dao.getObject(AccountBean.class, 
				"SELECT * FROM Account WHERE account =? ", 
				new Object[]{account.getAid()});
		if(ac!=null){
			if(!ac.getPwd().equals(account.getPwd())){
				s = "0";	//表示密码错误
			}
			else
				s = ac.getStid();	//表示返回学生或教师的id
		}
		return s;
	}
	/**
	* 获取登录的教师信息
	* @param 工号
	* @return 教师信息
	*/
	public TeacherBean Tlogin(String tid){
		TeacherBean teacher = (TeacherBean) dao.getObject(TeacherBean.class, 
				"SELECT * FROM Teacher WHERE tid=? ", 
				new Object[]{tid});
		return teacher;
	}
	/**
	*获取登录的学生信息 
	* @param 学号
	* @return 学生信息
	*/
	public StudentBean Slogin(String sid){
		StudentBean student = (StudentBean) dao.getObject(StudentBean.class, 
				"SELECT * FROM Student WHERE sid=? ", 
				new Object[]{sid});
		return student;
	}
}
