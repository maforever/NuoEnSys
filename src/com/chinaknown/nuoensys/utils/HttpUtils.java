package com.chinaknown.nuoensys.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class HttpUtils {
	
	/**
	 * ÓÃ»§µÇÂ½
	 * @param username
	 * @param password
	 * @return
	 */
	public static Employee login(String username, String password) {
		String urlStr = URLAddress.BASIC_URL + "/NuoEnSys/control/center/employee_login?username=" + username + "&password=" + password + "&type=2";
		URL url;
		try {
			url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() == 200) {
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
			if(state == 0) {
				return null;
			}else if(state == 1) {
				int visible = jsonObject.getInt("visible");
				if(visible == 0) {
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
				return employee;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return employee;
	}
	
	public static Uri loadImage(String urlStr, File cache) {
		String urlPath = URLAddress.BASIC_URL + urlStr;
		String imageName = urlPath.substring(urlPath.lastIndexOf("/") + 1);
		Log.i("a", "imageName = " + imageName);
		File imageFile = new File(cache, imageName);
		if(imageFile.exists()) {
			return Uri.fromFile(imageFile);
		}else {
			try {
				URL url = new URL(urlPath);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				if(conn.getResponseCode() == 200) {
					FileOutputStream outputStream = new FileOutputStream(imageFile);
					InputStream is = conn.getInputStream();
					Bitmap bm =  imageCompress(is, 100);
					bm.compress(CompressFormat.PNG, 70, outputStream);
					outputStream.close();
					return Uri.fromFile(imageFile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static Bitmap imageCompress(InputStream is,  int bitmapMaxWidth) {
		try {
			return ImageUtils.decodeImage(is, bitmapMaxWidth);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static List<CompanyNew> loadCompanyNews(int page) {
		String urlStr = URLAddress.COMPANYNEWS_URL + "?page=" + page;
		Log.i("a", "url = " + urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(urlStr);
		HttpResponse response;
		try {
			response = client.execute(request);
			if(response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
//				Log.i("a", "result = " + result);
				return parseCompanyNewJson(result);
			}else {
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
			for(int i=0; i<jArray.length(); i++) {
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
		for(CompanyNew n2 : news) {
			Log.i("a", n.toString());
		}
		return news;
	}
}





