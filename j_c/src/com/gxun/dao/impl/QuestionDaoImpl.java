package com.gxun.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.gxun.bean.QuestionBean;
import com.gxun.dao.inter.QuestionDaoInter;
import com.gxun.tools.MysqlTool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class QuestionDaoImpl implements QuestionDaoInter {

	public List<QuestionBean> getQuestionList(String sql,Object[] param) {
		//数据集合
		List<QuestionBean> list = new LinkedList<>();
		try {
			//获取数据库连接
			Connection conn = MysqlTool.getConnection();
			//预编译
			PreparedStatement ps = conn.prepareStatement(sql);
			//设置参数
			if(param != null && param.length > 0){
				for(int i = 0;i < param.length;i++){
					ps.setObject(i+1, param[i]);
				}
			}
			//执行sql语句
			ResultSet rs = ps.executeQuery();
			//获取元数据
			ResultSetMetaData meta = rs.getMetaData();
			//遍历结果集
			while(rs.next()){
				//创建对象
				QuestionBean question = new QuestionBean();
				//遍历每个字段
				for(int i=1;i <= meta.getColumnCount();i++){
					String field = meta.getColumnName(i);
					BeanUtils.setProperty(question, field, rs.getObject(field));
				}
				//添加到集合
				list.add(question);
			}
			//关闭连接
			MysqlTool.closeConnection();
			MysqlTool.close(ps);
			MysqlTool.close(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void deleteTransaction(Connection conn, String sql, Object[] param) throws SQLException {
		QueryRunner qr = new QueryRunner();
		qr.update(conn, sql, param);
	}

}
