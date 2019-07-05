package com.gxun.bean;

import java.io.Serializable;

public class SubmitBean implements Serializable {
	private int num;		/*编号*/
	private String sid;		/*学号*/
	private String tid;		/*工号*/
	private int wid;		/*作业号*/
	private int qid;		/*题号*/
	private String answer;		/*学生答案 */
	private int grade;		/*分数*/
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public int getWid() {
		return wid;
	}
	public void setWid(int wid) {
		this.wid = wid;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
}
