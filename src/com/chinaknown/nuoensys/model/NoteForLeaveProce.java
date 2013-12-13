package com.chinaknown.nuoensys.model;

import java.io.Serializable;

public class NoteForLeaveProce implements Serializable {

	private int idx;
	private String name;
	private String proce;
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProce() {
		return proce;
	}
	public void setProce(String proce) {
		this.proce = proce;
	}
	@Override
	public String toString() {
		return "NoteForLeaveProce [idx=" + idx + ", name=" + name + ", proce="
				+ proce + "]";
	}
	
	
	
}
