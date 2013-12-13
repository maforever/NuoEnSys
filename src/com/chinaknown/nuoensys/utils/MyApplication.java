package com.chinaknown.nuoensys.utils;

import java.util.List;

import android.app.Application;

import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.NoteForLeaveProce;

public class MyApplication extends Application {
	private List<Employee> reportReceivers;     //�ܱ�������
	private Employee loginer;                   //��ǰ�û���½��
	private List<NoteForLeaveProce> nflProces;  //��������������Ϣ
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
