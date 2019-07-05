package com.gxun.bean;

import java.io.Serializable;

public class WorkBean implements Serializable {
	private int wid;		/*作业编号*/
	private String tid;		/*工号*/
	private String wname;	/*作业名称*/
	private String starttime;
	private String endtime;
	public int getWid() {
		return wid;
	}
	public void setWid(int wid) {
		this.wid = wid;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getWname() {
		return wname;
	}
	public void setWname(String wname) {
		this.wname = wname;
	}
	public void setStarttime(String start) {
		this.starttime = start;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getEndtime() {
		return endtime;
	}
}
