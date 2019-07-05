package com.gxun.bean;

import java.io.Serializable;

public class TeacherBean implements Serializable {
	private String tid;		/*¹¤ºÅ*/
	private String tname;		/*ÐÕÃû*/
	private String timg;		/*Í·Ïñ */
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getTimg() {
		return timg;
	}
	public void setTimg(String timg) {
		this.timg = timg;
	}
}
