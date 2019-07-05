package com.gxun.dao.inter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gxun.bean.QuestionBean;

public interface QuestionDaoInter {
	/**
	 * 获取题目信息
	 * @param sql 要执行的sql语句
	 * @param  param 将参数封装到数组对象中
	 * @return
	 */
	public List<QuestionBean> getQuestionList(String sql,Object[] param);
	public void deleteTransaction(Connection conn, String sql, Object[] param)throws SQLException;
}
