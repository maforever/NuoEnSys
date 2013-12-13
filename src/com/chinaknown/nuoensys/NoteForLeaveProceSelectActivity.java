package com.chinaknown.nuoensys;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chinaknown.nuoensys.adapter.ProceListViewAdapter;
import com.chinaknown.nuoensys.model.NoteForLeaveProce;
import com.chinaknown.nuoensys.utils.AppConstants;
import com.chinaknown.nuoensys.utils.MyApplication;

public class NoteForLeaveProceSelectActivity extends Activity {
	private ListView listView;
	private List<NoteForLeaveProce> proces;
	private ProceListViewAdapter adapter;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_process_select);
		
		MyApplication myApp = (MyApplication) this.getApplication();
		proces = myApp.getNflProces();
		
		findViews();
	}
	
	private void findViews() {
		this.listView = (ListView) this.findViewById(R.id.listView);
		adapter = new ProceListViewAdapter(this, proces, 1);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent();
				intent.putExtra("proce", proces.get(position));
				setResult(AppConstants.SELECTED_PROCE, intent);
				NoteForLeaveProceSelectActivity.this.finish();
				overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
			}
		});
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
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
}

