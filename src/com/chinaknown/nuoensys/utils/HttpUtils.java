package com.chinaknown.nuoensys.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.DeadObjectException;
import android.util.Log;

import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.Report;

public class HttpUtils {

	/**
	 * 检查基地址是否正确
	 * @param urlpath
	 * @return
	 */
	public static boolean androidCheckBaseUrl(String urlpath) {
		String urlStr = urlpath
				+ "/NuoEnSys/control/center/employee_androidCheckUrl";
//		Log.i("a", "urlStr = " + urlStr);
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * 用户登陆
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static Employee login(String username, String password) {
		String urlStr = URLAddress.BASIC_URL
				+ "/NuoEnSys/control/center/employee_login?username="
				+ username + "&password=" + password + "&type=2";
		Log.i("a", "urlStr = " + urlStr);
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				return pasreLoginJson(StreamTool.readStream(is));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Employee pasreLoginJson(String jsonStr) {
		Employee employee = new Employee();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int state = jsonObject.getInt("state");
			if (state == 0) {
				return null;
			} else if (state == 1) {
				int visible = jsonObject.getInt("visible");
				if (visible == 0) {
					return null;
				}
				employee.setState(1);
				employee.setUserid(jsonObject.getInt("userid"));
				employee.setUsername(jsonObject.getString("username"));
				employee.setPassword(jsonObject.getString("password"));
				employee.setDuty(jsonObject.getString("duty"));
				employee.setVisible(visible);
				employee.setImgUrl(jsonObject.getString("imgUrl"));
				employee.setDepartment(jsonObject.getString("department"));
				employee.setRealName(jsonObject.getString("realName"));
				employee.setProject(jsonObject.getString("project"));
				employee.setDepartmentid(jsonObject.getInt("departmentid"));
				employee.setDutyid(jsonObject.getInt("dutyid"));
				employee.setProjectid(jsonObject.getInt("projectid"));
				return employee;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}

	/**
	 * 加载用户头像
	 * @param urlStr
	 * @param cache
	 * @return
	 */
	public static Uri loadImage(String urlStr, File cache) {
		String urlPath = URLAddress.BASIC_URL + urlStr;
		String imageName = urlPath.substring(urlPath.lastIndexOf("/") + 1);
//		Log.i("a", "imageName = " + imageName);
		File imageFile = new File(cache, imageName);
		if (imageFile.exists()) {
			return Uri.fromFile(imageFile);
		} else {
			try {
				URL url = new URL(urlPath);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				if (conn.getResponseCode() == 200) {
					FileOutputStream outputStream = new FileOutputStream(
							imageFile);
					InputStream is = conn.getInputStream();
					Bitmap bm = imageCompress(is, 300);
					bm.compress(CompressFormat.JPEG, 100, outputStream);
					outputStream.close();
					return Uri.fromFile(imageFile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static Bitmap imageCompress(InputStream is, int bitmapMaxWidth) {
		try {
			return ImageUtils.decodeImage(is, bitmapMaxWidth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取公司新闻
	 * @param page
	 * @return
	 */
	public static List<CompanyNew> loadCompanyNews(int page) {
		String urlStr = URLAddress.COMPANYNEWS_URL + "?page=" + page;
//		Log.i("a", "url = " + urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlStr);
		HttpResponse response;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				// Log.i("a", "result = " + result);
				return parseCompanyNewJson(result);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<CompanyNew> parseCompanyNewJson(String json) {
		List<CompanyNew> news = new ArrayList<CompanyNew>();
		CompanyNew n = null;
		try {
			JSONArray jArray = new JSONArray(json);
			JSONObject jObject = null;
			for (int i = 0; i < jArray.length(); i++) {
				jObject = jArray.getJSONObject(i);
				n = new CompanyNew();
				n.setTitle(jObject.getString("title"));
				n.setCreateDate(jObject.getString("createDate"));
				n.setContent(jObject.getString("content"));
				news.add(n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		for (CompanyNew n2 : news) {
//			Log.i("a", n.toString());
//		}
		return news;
	}

	/**
	 * 获得周报收取人信息并进行解析
	 * 
	 * @param idx
	 * @return
	 */

	public static List<Employee> loadReportReceivers(Integer idx) {
		String urlStr = URLAddress.BASIC_URL
				+ "/NuoEnSys/control/center/employee_androidListReportReceiver?id="
				+ idx;
//		Log.i("a", "urlStr = " + urlStr);
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(urlStr);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String jsonStr = EntityUtils.toString(response.getEntity());
				return parseReportReceiverJson(jsonStr);
				// Log.i("a", "jsonStr = " + jsonStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<Employee> parseReportReceiverJson(String jsonStr) {
		List<Employee> employees = new ArrayList<Employee>();
		Employee employee;
		try {
			JSONArray ja = new JSONArray(jsonStr);
			JSONObject jo;
			for (int i = 0; i < ja.length(); i++) {
				jo = ja.getJSONObject(i);
				employee = new Employee();
				employee.setUserid(jo.getInt("toId"));
				employee.setRealName(jo.getString("toName"));
				employee.setDuty(jo.getString("duty"));
				employee.setDepartment(jo.getString("department"));
				employees.add(employee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// for(Employee e : employees) {
		// Log.i("a", e.toString());
		// }
		return employees;
	}

	/**
	 * 提交发送周报信息
	 * @param map
	 * @return
	 */
	public static String androidSendReport(Map<String, String> map){
		String url = URLAddress.BASIC_URL + "/NuoEnSys/control/center/report_androidSendReport";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			BasicNameValuePair basicNameValuePair = new BasicNameValuePair(
					entry.getKey(), entry.getValue());
			params.add(basicNameValuePair);
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String json = EntityUtils.toString(response.getEntity());
				Log.i("a", "json = " + json);
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 读取当前登陆人发送的周报列表
	 * @param page
	 * @param creatorid
	 * @return
	 */
	public static List<Report> loadSendedReports(int page, int creatorid) {
		String urlStr = URLAddress.SENDEDREPORT_URL + "?page=" + page+"&creatorid=" + creatorid;
//		Log.i("a", "url = " + urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlStr);
		HttpResponse response;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				return parseSendedReportsJson(result);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static List<Report> parseSendedReportsJson(String jsonStr) {
		List<Report> reports = new ArrayList<Report>();
		Report r = null;
		try {
			JSONArray jArray = new JSONArray(jsonStr);
			JSONObject jObject = null;
			for (int i = 0; i < jArray.length(); i++) {
				jObject = jArray.getJSONObject(i);
				r = new Report();
				r.setTitle(jObject.getString("title"));
				r.setCreateDate(jObject.getString("createDate"));
				r.setIdx(jObject.getInt("reportidx"));
				r.setContent(jObject.getString("content"));
				r.setCreator(jObject.getString("creator"));
				r.setDuty(jObject.getString("duty"));
				r.setDepartment(jObject.getString("department"));
				r.setToName(jObject.getString("toname"));
				reports.add(r);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Report r2 : reports) {
			Log.i("a", r2.toString());
		}
		return reports;
	}
	
	/**
	 * 删除周报
	 * @param idx
	 * @return
	 */
	public static String deleteReportById(int idx) {
		String path = URLAddress.BASIC_URL + "/NuoEnSys/control/center/report_androidDelete?idx=" + idx;
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
	
	public static List<Report> loadReadReports(int page, int touserid) {
		String urlStr = URLAddress.READREPORT_URL + "?page=" + page+"&touserid=" + touserid;
		Log.i("a", "url = " + urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlStr);
		HttpResponse response;
		try {
			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				return parseSendedReportsJson(result);
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
}






























