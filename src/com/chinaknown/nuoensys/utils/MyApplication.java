package com.chinaknown.nuoensys.utils;

import java.util.List;

import android.app.Application;

import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.NoteForLeaveProce;

public class MyApplication extends Application {
	private List<Employee> reportReceivers;     //周报接受者
	private Employee loginer;                   //当前用户登陆者
	private List<NoteForLeaveProce> nflProces;  //假条申请流程信息
	public List<Employee> getReportReceivers() {
		return reportReceivers;
	}

	public void setReportReceivers(List<Employee> reportReceivers) {
		this.reportReceivers = reportReceivers;
	}

	public Employee getLoginer() {
		return loginer;
	}

	public void setLoginer(Employee loginer) {
		this.loginer = loginer;
	}
	
	public void loginOut() {
		this.loginer = null;
	}

	public List<NoteForLeaveProce> getNflProces() {
		return nflProces;
	}

	public void setNflProces(List<NoteForLeaveProce> nflProces) {
		this.nflProces = nflProces;
	}
	
}
