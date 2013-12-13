package com.chinaknown.nuoensys.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.widget.ShareActionProvider;

public class AppSharedPreference {
	private Context context = null;
	public AppSharedPreference(Context context) {
		this.context = context;
	}
	
	public void setFirstUse() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isFirstUse", true);
		editor.commit();
	}
	
	public boolean isFirstUse() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getBoolean("isFirstUse", false);
	}
	
	/*
	 * ��ס����
	 */
	public void setJiZhuMiMa(boolean flag) {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("jizhumima", flag);
		editor.commit();
	}
	
	public boolean getJiZhuMiMa() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getBoolean("jizhumima", false);
	}
	
	/*
	 * �Զ���½
	 */
	public void setZiDongDengLu(boolean flag) {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("zidongdenglu", flag);
		editor.commit();
	}
	
	public boolean getZiDongDengLu() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getBoolean("zidongdenglu", false);
	}
	
	/*
	 * �����û���
	 */
	public void setUsername(String username) {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("username", username);
		editor.commit();
	}
	
	public String getUsername() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getString("username", "");
	}
	
	/*
	 * ����������
	 */
	public void setPassword(String password) {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("password", password);
		editor.commit();
	}
	
	public String getPassword() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getString("password", "");
	}
	
	/*����ϵͳ����ַ*/
	public String getBaseUrl() {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		return sp.getString("baseUrl", "");
	}
	
	public void SetBaseUrl(String baseUrl) {
		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("baseUrl", baseUrl);
		editor.commit();
	}
	
	
	/**
	 * �û���½�ɹ����û���username �� idx����
	 * @param username
	 */
//	public void saveLoginUsername(String username) {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putString("loginusername", username);
//		editor.commit();
//	}
//	
//	public void saveLoginUserIdx(Integer idx) {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putInt("loginuseridx", idx);
//		editor.commit();
//	}
//	
//	public void clearUsername() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.remove("loginusername");
//		editor.commit();
//	}
//	
//	public void clearUserIdx() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.remove("loginuseridx");
//		editor.commit();
//	}
//	
//	public String getLoginUsername() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		return sp.getString("loginusername", "");
//	}
//	
//	public Integer getLoginUserIdx() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		return sp.getInt("loginuseridx", 0);
//	}
//	
//	public void saveLoginDepartment(String department) {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putString("logindepartment", department);
//		editor.commit();
//	}
//	
//	public void saveLoginDuty(String duty) {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.putString("loginduty", duty);
//		editor.commit();
//	}
//	
//	public void clearDepartment() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.remove("logindepartment");
//		editor.commit();
//	}
//	
//	public void clearDuty() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		Editor editor = sp.edit();
//		editor.remove("loginduty");
//		editor.commit();
//	}
//	
//	public String getLoginDepartment() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		return sp.getString("logindepartment", "");
//	}
//	
//	public Integer getLoginDuty() {
//		SharedPreferences sp = context.getSharedPreferences("app_info", Context.MODE_PRIVATE);
//		return sp.getInt("loginduty", 0);
//	}
	
	
}


















