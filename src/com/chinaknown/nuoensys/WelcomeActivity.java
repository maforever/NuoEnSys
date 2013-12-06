package com.chinaknown.nuoensys;

import java.util.Timer;
import java.util.TimerTask;
import com.chinaknown.nuoensys.utils.AppSharedPreference;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class WelcomeActivity extends Activity {
	private AppSharedPreference appSp = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			jump();
		};
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        	
        	appSp = new AppSharedPreference(WelcomeActivity.this);
        
     		Timer timer = new Timer();
     		TimerTask task = new TimerTask() {

     			@Override
     			public void run() {
     				handler.sendEmptyMessage(0);
     			}
     		};
     		timer.schedule(task, 3000);
    }
    
	private void jump() {
		if (appSp.isFirstUse()) {
			// ���ǵ�һ��ʹ����ת���׽���
			jumpSecond();
			WelcomeActivity.this.finish();
			overridePendingTransition(R.anim.activity_up,
					R.anim.activity_steady);
		} else {
			appSp.setFirstUse();
			jumpFirst();
			WelcomeActivity.this.finish();
			overridePendingTransition(R.anim.activity_up,
					R.anim.activity_steady);

		}
	}
	
	// ��һ������Ӧ��
	private void jumpFirst() {
		startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
		finish();
	}

	// �ǵ�һ������Ӧ��
	private void jumpSecond() {
		startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
		finish();
	}

}
