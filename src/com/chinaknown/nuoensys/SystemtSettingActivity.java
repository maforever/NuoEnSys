package com.chinaknown.nuoensys;

import java.io.File;

import com.chinaknown.nuoensys.dialog.LoadingDialog;
import com.chinaknown.nuoensys.utils.AppSharedPreference;
import com.chinaknown.nuoensys.utils.HttpUtils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SystemtSettingActivity extends Activity {
	private RelativeLayout urlLayout, clearLayout, aboutLayout; 
	private EditText urlEt;
	private int height;
	private AppSharedPreference asp;
	private LoadingDialog checkLd, cleartLd;
	private File cache;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				boolean flag = (Boolean) msg.obj;
				if(flag) {
					//地址正确，保存到appsharepreference
					Toast.makeText(SystemtSettingActivity.this, "网络地址正确", 0).show();
					asp.SetBaseUrl(urlEt.getText().toString());
				}else {
					//地址不正确
					Toast.makeText(SystemtSettingActivity.this, "网络地址不正确", 0).show();
				}
				break;
			case 2:
				Toast.makeText(SystemtSettingActivity.this, "缓存清除成功!", 0).show();
				break;
			}
			if(checkLd.isShowing()) checkLd.dismiss(); 
			if(cleartLd.isShowing()) cleartLd.dismiss(); 
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_systemsetting);
		asp = new AppSharedPreference(SystemtSettingActivity.this);
		initProgressDialog();
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cache = new File(Environment.getExternalStorageDirectory() + File.separator + "NuoEnSys" + File.separator + "cache");
	        if(!cache.exists()) cache.mkdirs();
		}else {
			Toast.makeText(this, "存储卡不可用", 1).show();
		}
		
		findViews();
		urlLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				height = urlLayout.getHeight();
				clearLayout.getLayoutParams().height = height;
				clearLayout.invalidate();
				aboutLayout.getLayoutParams().height = height;
				aboutLayout.invalidate();
			}
		});
		
		
	}
	
	private void findViews() {
		urlLayout = (RelativeLayout) this.findViewById(R.id.urlLayout);
		clearLayout = (RelativeLayout) this.findViewById(R.id.clearLayout);
		aboutLayout = (RelativeLayout) this.findViewById(R.id.aboutLayout);
		urlEt = (EditText) this.findViewById(R.id.url);
		urlEt.setText(asp.getBaseUrl());
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.checkUrl:
			if(urlEt.getText().toString().trim().equals("")) {
				urlEt.setText(asp.getBaseUrl());
				Toast.makeText(SystemtSettingActivity.this, "系统基地址不能为空!", 0).show();
			}else {
				checkLd.show();
				new CheckBaseUrlThread().start();
			}
			break;
		case R.id.clearCache:
			cleartLd.show();
			new ClearCacheThread().start();

			break;
		case R.id.aboutLayout:
			break;
		}
	}
	
	private class ClearCacheThread extends Thread {
		@Override
		public void run() {
			if(cache.exists()) {
				File[] files = cache.listFiles();
				if(files.length > 0) {
					for(File f : files) {
						f.delete();
					}
				}
			}
			handler.sendMessage(handler.obtainMessage(2));
		}
	}
	
	private class CheckBaseUrlThread extends Thread {
		@Override
		public void run() {
			boolean flag = HttpUtils.androidCheckBaseUrl(urlEt.getText().toString());
			handler.sendMessage(handler.obtainMessage(1, flag));
		}
	}
	
	private void back() {
		this.finish();
		overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return true;
	}
	
	private void initProgressDialog() {
		checkLd = new LoadingDialog(this, "正在检测服务器地址是否正确，请稍等!");
		cleartLd = new LoadingDialog(this, "清除缓存中...");
	}
	
}
