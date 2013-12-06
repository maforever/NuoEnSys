package com.chinaknown.nuoensys.model;

import java.io.Serializable;

public class CompanyNew implements Serializable{

	private String title;
	private String createDate;
	private String content;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "CompanyNew [title=" + title + ", createDate=" + createDate
				+ ", content=" + content + "]";
	}
	
	
	
}
