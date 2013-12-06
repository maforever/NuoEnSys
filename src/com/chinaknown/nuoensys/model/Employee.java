package com.chinaknown.nuoensys.model;

import java.io.Serializable;

public class Employee implements Serializable{

	private int userid;
	private String username;
	private String password;
	private String realName;
	private int state;    //�˻�״̬,0�û������������1��½�ɹ�
	private String duty;  //�˻�ְ��
	private String department;  //��������
	private int visible;   //�Ƿ���ְ  1��ְ��0��ְ
	private String imgUrl;  //��Ƭ·��
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public int getVisible() {
		return visible;
	}
	public void setVisible(int visible) {
		this.visible = visible;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return "Employee [userid=" + userid + ", username=" + username
				+ ", password=" + password + ", realName=" + realName
				+ ", state=" + state + ", duty=" + duty + ", department="
				+ department + ", visible=" + visible + ", imgUrl=" + imgUrl
				+ "]";
	}
	
	
}
