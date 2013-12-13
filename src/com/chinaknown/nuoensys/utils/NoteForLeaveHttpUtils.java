package com.chinaknown.nuoensys.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.chinaknown.nuoensys.model.NoteForLeave;
import com.chinaknown.nuoensys.model.NoteForLeaveProce;
import com.chinaknown.nuoensys.model.Report;

public class NoteForLeaveHttpUtils {

	/**
	 * 加载请假条申请流程信息
	 * @return
	 */
	public static List<NoteForLeaveProce> loadProces() {
		List<NoteForLeaveProce> proces = null;
		String path = URLAddress.BASIC_URL + "/NuoEnSys/control/center/nflProcedure_androidLoadProce";
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(path);
		try {
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200) {
				String jsonStr = EntityUtils.toString(response.getEntity());
				return parseProceJson(jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proces;
	}
	
	private static List<NoteForLeaveProce> parseProceJson(String jsonStr) {
		List<NoteForLeaveProce> proces = new ArrayList<NoteForLeaveProce>();
		NoteForLeaveProce proce;
		try {
			JSONArray ja = new JSONArray(jsonStr);
			JSONObject jo = null;
			for(int i=0; i<ja.length(); i++) {
				jo = ja.getJSONObject(i);
				proce = new NoteForLeaveProce();
				proce.setIdx(jo.getInt("idx"));
				proce.setName(jo.getString("name"));
				proce.setProce(jo.getString("proce"));
				proces.add(proce);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(NoteForLeaveProce p : proces) {
			Log.i("a", p.toString());
		}
		return proces;
	}
	
	/**
	 * 发送请假条
	 * @param datas
	 * @return
	 */
	public static String sendNoteForLeave(Map<String, String> datas) {
		try {
			String path = URLAddress.BASIC_URL + URLAddress.SENDNOTEFORLEAVE_URL;
			Log.i("a", "url = " + path);
			List<NameValuePair> values = new ArrayList<NameValuePair>();
			for(Map.Entry<String, String> entry : datas.entrySet()) {
				values.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(path);
			request.setEntity(new UrlEncodedFormEntity(values, "UTF-8"));
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200) {
				String jsonStr = EntityUtils.toString(response.getEntity());
				return jsonStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 我的请假条列表
	 * creatorid 申请人 ， state 假条状态 0申请中，1通过， 2拒绝
	 * @param page
	 * @param creatorid
	 * @param state
	 * @return
	 */
	public static List<NoteForLeave> loadNoteForLeaveList(int page, int creatorid, int state) {
		String urlStr = URLAddress.BASIC_URL + URLAddress.NOTEFORLEAVE_LIST_URL + "?page=" + page+"&creatorid=" + creatorid+"&state=" + state;
		Log.i("a", "url = " + urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlStr);
		HttpResponse response;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				Log.i("a", "result = " + result);
				return parseNoteForLeavesJson(result);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static List<NoteForLeave> parseNoteForLeavesJson(String jsonStr) {
		List<NoteForLeave> nfls = new ArrayList<NoteForLeave>();
		NoteForLeave nfl = null;
		try {
			JSONArray jArray = new JSONArray(jsonStr);
			JSONObject jo = null;
			for (int i = 0; i < jArray.length(); i++) {
				jo = jArray.getJSONObject(i);
				nfl = new NoteForLeave();
				nfl.setIdx(jo.getInt("idx"));
				nfl.setTitle(jo.getString("title"));
				nfl.setReason(jo.getString("reason"));
				nfl.setCreatorid(jo.getInt("creatorid"));
				nfl.setCreator(jo.getString("creator"));
				nfl.setDepartment(jo.getString("department"));
				nfl.setDuty(jo.getString("duty"));
				nfl.setProject(jo.getString("project"));
				nfl.setProcedureIdx(jo.getInt("proceid"));
				nfl.setProcedureName(jo.getString("proceName"));
				nfl.setProce(jo.getString("proceExplan"));
				nfl.setStartDate(jo.getString("fromDate"));
				nfl.setEndDate(jo.getString("toDate"));
				nfl.setDays(jo.getInt("days"));
				nfl.setLocationName(jo.getString("locationName"));
				nfl.setRefusalreason(jo.getString("refusalreason"));
				nfl.setLocation(jo.getInt("location"));
				nfl.setApprover(jo.getString("approver"));
				nfl.setApproverIdx(jo.getInt("approverid"));
				nfl.setApplicationDate(jo.getString("applicationdate"));
				nfl.setApprovaldate(jo.getString("approvaldate"));
				nfl.setState(jo.getInt("state"));
				
				nfls.add(nfl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (NoteForLeave r2 : nfls) {
			Log.i("a", r2.toString());
		}
		return nfls;
	}
	
	
	
	/**
	 * 删除请假条
	 * @param idx
	 * @return
	 */
	public static String deleteNoteForLeaveById(int idx) {
		String path = URLAddress.BASIC_URL + URLAddress.NOTEFORLEAVE_DELETE_URL + "?id=" + idx;
		Log.i("a", path);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(path);
		try {
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获得当前登陆人收到的申请列表
	 * @param page
	 * @param userid
	 * @param state
	 * @return
	 */
	public static List<NoteForLeave> loadNoteForLeaveHandleList(int page, int userid, int state) {
		String urlStr = URLAddress.BASIC_URL + URLAddress.NOTEFORLEAVE_HANDLELIST_URL + "?page=" + page+"&id=" + userid+"&state=" + state;
		Log.i("a", "url = " + urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlStr);
		HttpResponse response;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				Log.i("a", "result = " + result);
				return parseNoteForLeavesJson(result);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static String androidAccess(int userid, int nflid) {
		String path = URLAddress.BASIC_URL + URLAddress.NOTEFORLEAVE_ACCESS_URL + "?userid=" + userid + "&nflid=" + nflid;
		Log.i("a", "url = " + path);
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(path);
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200) {
				return  EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}



















