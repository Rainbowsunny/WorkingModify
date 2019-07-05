package com.gxun.bean;

import java.io.Serializable;

public class QuestionBean implements Serializable {
	private int qid;		/*题号*/
	private String qcontent;		/*题目内容*/
    private int snum;        /*分值*/
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getQcontent() {
		return qcontent;
	}
	public void setQcontent(String qcontent) {
		this.qcontent = qcontent;
	}
	public int getSnum() {
		return snum;
	}
	public void setSnum(int snum) {
		this.snum = snum;
	}
    
}
