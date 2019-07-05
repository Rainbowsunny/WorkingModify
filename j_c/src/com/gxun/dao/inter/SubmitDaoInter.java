package com.gxun.dao.inter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gxun.bean.SubmitBean;
import com.gxun.bean.WorkBean;

public interface SubmitDaoInter {
	/**
	 * 添加一条作业提交记录
	 * @param sql 要执行的sql语句
	 * @param param
	 * @return
	 */
	public void addSubmit(String sql,Object[] param);		/*对提交数据进行增加*/
	
	public int SumSubmit(String sql, List<Object> param);
}
