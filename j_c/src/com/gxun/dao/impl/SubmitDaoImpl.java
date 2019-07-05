package com.gxun.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.gxun.bean.SubmitBean;
import com.gxun.bean.WorkBean;
import com.gxun.dao.inter.SubmitDaoInter;
import com.gxun.tools.MysqlTool;

public class SubmitDaoImpl implements SubmitDaoInter{
	/**
	 * 添加一条作业提交记录
	 * @param sql 要执行的sql语句
	 * @param param 参数
	 * @return
	 */
	public void addSubmit(String sql,Object[] param) {
		QueryRunner qr = new QueryRunner(MysqlTool.getDataSource());
		try {
			qr.update(sql, param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int SumSubmit(String sql, List<Object> param) {
		//将集合中的参数封装到数组对象中
		Object[] params = new Object[param.size()];
		for(int i = 0;i < param.size();i++){
			params[i] = param.get(i);
		}
		QueryRunner qr = new QueryRunner(MysqlTool.getDataSource());
		int count = 0;
		try {
			count = Integer.parseInt(qr.query(sql, new ScalarHandler(), params).toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

}
