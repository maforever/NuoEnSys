package com.chinaknown.nuoensys;

import java.io.File;
import java.io.InputStream;

import com.chinaknown.nuoensys.adapter.IndexGridViewAdapter;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.utils.AppSharedPreference;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AppIndexActivity extends Activity {
	private Employee employee;
	private TextView realNameTv, departmentNameTv, dutyNameTv;
	private ImageView headIv;
	private GridView gridView;
	private IndexGridViewAdapter adapter;
	private int[] imageIds = {R.drawable.news, R.drawable.report, R.drawable.noteforleave, R.drawable.reimburesement, R.drawable.help, R.drawable.setting};
	private String[] names = {"新闻","周报","请假条","报销单","帮助","设置"};
	private Intent intent;
	private AppSharedPreference asp;

	File cache;      //图片缓存目录
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Uri uri = (Uri) msg.obj;
				if(uri != null) {
					headIv.setBackgroundResource(0);
					headIv.setImageURI(uri);
				}else {
					headIv.setImageResource(R.drawable.head_phonex2);
				}
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_index);
		
		asp = new AppSharedPreference(this);
		employee = (Employee) this.getIntent().getSerializableExtra("employee");
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cache = new File(Environment.getExternalStorageDirectory() + File.separator + "NuoEnSys" + File.separator + "cache");
	        if(!cache.exists()) cache.mkdirs();
		}else {
			Toast.makeText(this, "存储卡不可用", 1).show();
		}
		
		
		findViews();
		initViews();
	}
	
	private void findViews() {
		realNameTv = (TextView) this.findViewById(R.id.realName);
		departmentNameTv = (TextView) this.findViewById(R.id.departmentName);
		dutyNameTv = (TextView) this.findViewById(R.id.dutyName);
		headIv = (ImageView) this.findViewById(R.id.headImg);
		gridView = (GridView) this.findViewById(R.id.gridView);
	}
	
	private void initViews() {
		realNameTv.setText(employee.getRealName());
		if ("总经理".equals(employee.getDuty()) || "公司副总".equals(employee.getDuty())) {
			departmentNameTv.setText("公司");
		}else {
			departmentNameTv.setText(employee.getDepartment());
		}
		dutyNameTv.setText(employee.getDuty());
		if(employee.getImgUrl() != null && !"".equals(employee.getImgUrl())) {
			new LoadHeadImageThread().start();
		}else {
			headIv.setImageResource(R.drawable.head_phonex2);
		}
		adapter = new IndexGridViewAdapter(this, imageIds, names, R.layout.index_gridview_item);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//新闻
					intent = new Intent(AppIndexActivity.this, NewsIndexActivity.class);
					break;
				case 1:
					//周报
					intent = new Intent(AppIndexActivity.this, ReportIndexActivity.class);
					break;
				case 2:
					//请假条
					intent = new Intent(AppIndexActivity.this, NoteForLeaveIndexActivity.class);
					break;
				case 5:
					intent = new Intent(AppIndexActivity.this, SystemtSettingActivity.class);
					break;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
			}
		});
	}
	
	
	private class LoadHeadImageThread extends Thread {
		@Override
		public void run() {
			Uri uri = HttpUtils.loadImage(employee.getImgUrl(), cache);
			handler.sendMessage(handler.obtainMessage(1, uri));
		}
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.zhuxiao:
//			asp.clearUserIdx();
//			asp.clearUsername();
//			asp.clearDepartment();
//			asp.clearDuty();
			
			MyApplication myApp = (MyApplication) AppIndexActivity.this.getApplicationContext();
			myApp.loginOut();
			this.finish();
			overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			break;
		}
	}
	
	
}
