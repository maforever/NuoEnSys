package com.chinaknown.nuoensys.model;

import java.io.Serializable;

public class Report implements Serializable{

	private int idx;
	private String title;
	private String creator;
	private int creatorIdx;
	private String toName;
	private int toUserIdx;
	private String content;
	private String createDate;
	private String department;
	private String duty;
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public int getCreatorIdx() {
		return creatorIdx;
	}
	public void setCreatorIdx(int creatorIdx) {
		this.creatorIdx = creatorIdx;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public int getToUserIdx() {
		return toUserIdx;
	}
	public void setToUserIdx(int toUserIdx) {
		this.toUserIdx = toUserIdx;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	@Override
	public String toString() {
		return "Report [idx=" + idx + ", title=" + title + ", creator="
				+ creator + ", creatorIdx=" + creatorIdx + ", toName=" + toName
				+ ", toUserIdx=" + toUserIdx + ", content=" + content
				+ ", createDate=" + createDate + ", department=" + department
				+ ", duty=" + duty + "]";
	}
	
	
	
}
