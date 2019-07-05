package com.gxun.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.gxun.dao.inter.BaseDaoInter;
import com.gxun.tools.MysqlTool;

public class BaseDaoImpl implements BaseDaoInter{
	public List<Object> getList(Class type, String sql, Object[] param) {
		QueryRunner qr = new QueryRunner(MysqlTool.getDataSource());
		List<Object> list = new LinkedList<>();
		try {
			list = qr.query(sql, new BeanListHandler(type), param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public List<Object> getList(Class type, String sql) {
		QueryRunner qr = new QueryRunner(MysqlTool.getDataSource());//QueryRunner的性质与statement等同，执行SQL语句
		List<Object> list = new LinkedList<>();//ArrayList和LinkedList都是实现了List接口的容器类，用于存储一系列的对象引用。他们都可以对元素的增删改查进行操作。
		try {
			list = qr.query(sql, new BeanListHandler(type));//BeanListHandler将ResultSet中某一列的数据存成List
		// 把结果集转为一个 Bean 的 List, 并返回.。Bean的类型在创建 BeanListHanlder对象时以 Class对象的方式传入，可以适应列的别名来映射 JavaBean 的属性 名:  String sql = "SELECT id, name customerName, email, birth " + "FROM customers WHERE id = ?"; BeanListHandler(Class<T> type)。
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public Object getObject(Class type, String sql, Object[] param) {
		QueryRunner qr = new QueryRunner(MysqlTool.getDataSource());
		Object obj = new LinkedList<>();
		try {
			obj = qr.query(sql, new BeanHandler(type), param);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public void insertTransaction(Connection conn, String sql, Object[] param) throws SQLException {
		try {
			PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			//设置值
			if(param != null && param.length > 0){
				for(int i = 0;i < param.length;i++){
					ps.setObject(i+1, param[i]);
				}
			}
			//执行sql语句
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateTransaction(Connection conn, String sql, Object[] param) throws SQLException {
		QueryRunner qr = new QueryRunner();
		qr.update(conn, sql, param);
	}
}
