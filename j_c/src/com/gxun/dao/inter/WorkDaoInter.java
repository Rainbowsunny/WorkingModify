package com.gxun.dao.inter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gxun.bean.QuestionBean;
import com.gxun.bean.SubmitBean;
import com.gxun.bean.WorkBean;

public interface WorkDaoInter {

	/**
	 * 获取作业信息
	 * @param sql 要执行的sql语句
	 * @param  param 将参数封装到数组对象中
	 * @return
	 */
	public List<WorkBean> getWorkList(String sql,Object[] param);
}
