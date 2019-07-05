package com.gxun.dao.inter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseDaoInter {
	/**
	 * 获取数据集合
	 * @param type 返回对象的class
	 * @param sql 要执行的sql语句
	 * @return
	 */
	List<Object> getList(Class type, String sql);
	/**
	 * 获取数据集合
	 * @param type 返回对象的class
	 * @param sql 执行的sql语句
	 * @param param Object[] 数组参数
	 * @return
	 */
	List<Object> getList(Class type, String sql, Object[] param);
	
	/**
	 * 获取一个对象
	 * @param type
	 * @param sql
	 * @param param
	 * @return
	 */
	Object getObject(Class type, String sql, Object[] param);
	/**
	 * 插入数据
	 * 开启事务
	 * @param conn
	 * @param sql
	 * @param param
	 * @throws SQLException 
	 */
	void insertTransaction(Connection conn, String sql, Object[] param) throws SQLException;
	/**
	 * 插入或更新数据
	 * @param conn
	 * @param sql
	 * @param param
	 * @throws SQLException 
	 */
	void updateTransaction(Connection conn, String sql, Object[] param) throws SQLException;
}
