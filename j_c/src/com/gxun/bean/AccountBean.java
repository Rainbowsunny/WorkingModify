package com.gxun.bean;

import java.io.Serializable;

public class AccountBean implements Serializable {
	private String aid;		/*账号*/
	private String pwd;		/*密码*/
	private String stid;		/*学工号 */
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getStid() {
		return stid;
	}
	public void setStid(String stid) {
		this.stid = stid;
	}
	
	

}
