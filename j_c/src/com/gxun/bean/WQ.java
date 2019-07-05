package com.gxun.bean;

import java.io.Serializable;

public class WQ implements Serializable {
	private int wqid;
	private int qid;   /*题号*/
	private int wid;   /*作业号*/
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public int getWid() {
		return wid;
	}
	public void setWid(int wid) {
		this.wid = wid;
	}
	public void setWqid(int wqid) {
		this.wqid = wqid;
	}
	public int getWqid() {
		return wqid;
	}
}
