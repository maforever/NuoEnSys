package com.chinaknown.nuoensys;

import com.chinaknown.nuoensys.dialog.LoadingDialog;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.utils.AppSharedPreference;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.MyApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText usernameEt;
	private EditText passwordEt;
	private CheckBox jizhumimaCb;
	private CheckBox zidongdengluCb;
	private String username;
	private String password;
	private TextView warningTv;
	private LoadingDialog ld;
	private AppSharedPreference asp;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				ld.dismiss();
				Employee result = (Employee) msg.obj;
				if(result == null) {
					//登陆失败
					Toast.makeText(LoginActivity.this, "用户名或密码错误或当前用户已经离职!", 0).show();
				}else {
					//登陆成功
//					Toast.makeText(LoginActivity.this, result.toString(), 1).show();
					if(jizhumimaCb.isChecked()) {
						asp.setJiZhuMiMa(true);
						asp.setUsername(username);
						asp.setPassword(password);
					}else {
						asp.setJiZhuMiMa(false);
					}
					if(zidongdengluCb.isChecked()) {
						asp.setZiDongDengLu(true);
						asp.setUsername(username);
						asp.setPassword(password);
					}else {
						asp.setZiDongDengLu(false);
					}
					
					
//					asp.saveLoginUsername(result.getUsername());
//					asp.saveLoginUserIdx(result.getUserid());
//					asp.saveLoginDepartment(result.getDepartment());
//					asp.saveLoginDuty(result.getDuty());
					MyApplication mApp = (MyApplication) LoginActivity.this.getApplicationContext();
					mApp.setLoginer(result);
					
					Intent intent = new Intent(LoginActivity.this, AppIndexActivity.class);
					intent.putExtra("employee", result);
					startActivity(intent);
					overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				}
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		
		asp = new AppSharedPreference(this);
		initProgressDialog();
		findViews();
	}
	
	
	private void findViews() {
		usernameEt = (EditText) this.findViewById(R.id.username);
		passwordEt = (EditText) this.findViewById(R.id.password);
		jizhumimaCb = (CheckBox) this.findViewById(R.id.jizhumimaCb);
		zidongdengluCb = (CheckBox) this.findViewById(R.id.zidongdengluCb);
		warningTv = (TextView) this.findViewById(R.id.warning);
		if(asp.getJiZhuMiMa()) {
			jizhumimaCb.setChecked(true);
			usernameEt.setText(asp.getUsername());
			passwordEt.setText(asp.getPassword());
		}
		if(asp.getZiDongDengLu()) {
			zidongdengluCb.setChecked(true);
			usernameEt.setText(asp.getUsername());
			passwordEt.setText(asp.getPassword());
			validateDatasAndLogin();
		}
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.login:
			
			validateDatasAndLogin();
			
			break;
		}
	}
	
	private void loadViewDatas() {
		username = this.usernameEt.getText().toString();
		password = this.passwordEt.getText().toString();
	}
	
	private void validateDatasAndLogin() {
		loadViewDatas();
		if(username == null || "".equals(username) || password == null || "".equals(password)) {
			warningTv.setText("用户名或密码不能为空!");
			warningTv.setVisibility(ViewGroup.VISIBLE);
		}else {
			warningTv.setVisibility(ViewGroup.GONE);
			ld.show();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Employee employee= HttpUtils.login(username, password);
					handler.sendMessage(handler.obtainMessage(1, employee));
				}
			}).start();
			
		}
	}
	
	private void initProgressDialog() {
		ld = new LoadingDialog(this, "正在连接服务器，请稍后!");
	}
}







