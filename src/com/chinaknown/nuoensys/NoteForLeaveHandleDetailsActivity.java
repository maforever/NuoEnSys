package com.chinaknown.nuoensys;

import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.NoteForLeave;
import com.chinaknown.nuoensys.utils.MyApplication;
import com.chinaknown.nuoensys.utils.NoteForLeaveHttpUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NoteForLeaveHandleDetailsActivity extends Activity {
	
	private TextView titleTv, reasonTv, creatorTv, departmentTv, dutyTv, fromDateTv, toDateTv, totalDays;
	private NoteForLeave nfl;
	private Employee employee;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_noteforleave_handle_details);
		
		MyApplication myApp = (MyApplication) this.getApplication();
		employee = myApp.getLoginer();
		
		nfl = (NoteForLeave) this.getIntent().getSerializableExtra("nfl");
		findViews();
		initViews();
	}
	
	private void findViews() {
		titleTv = (TextView) this.findViewById(R.id.title);
		reasonTv = (TextView) this.findViewById(R.id.reason);
		creatorTv = (TextView) this.findViewById(R.id.creator);
		departmentTv = (TextView) this.findViewById(R.id.department);
		dutyTv = (TextView) this.findViewById(R.id.duty);
		fromDateTv = (TextView) this.findViewById(R.id.fromDate);
		toDateTv = (TextView) this.findViewById(R.id.toDate);
		totalDays = (TextView) this.findViewById(R.id.totalDays);
	}
	
	private void initViews() {
		titleTv.setText(nfl.getTitle());
		reasonTv.setText(nfl.getReason());
		creatorTv.setText(nfl.getCreator());
		departmentTv.setText(nfl.getDepartment());
		dutyTv.setText(nfl.getDuty());
		fromDateTv.setText(nfl.getStartDate());
		toDateTv.setText(nfl.getEndDate());
		totalDays.setText(nfl.getDays() + "天");
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.pizhun:
			//批准该假条
			new AccessAsyncTask().execute(employee.getUserid(), nfl.getIdx());
			break;
		case R.id.jujue:
			//拒绝该假条，转到拒绝原因界面
			break;
		}
	}
	
	private class AccessAsyncTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String result = NoteForLeaveHttpUtils.androidAccess(params[0], params[1]);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null && !"".equals(result)) {
				Toast.makeText(NoteForLeaveHandleDetailsActivity.this, "批准假条成功!", 0).show();
				NoteForLeaveHandleDetailsActivity.this.finish();
				NoteForLeaveHandleListActivity.removeItem(nfl);
				NoteForLeaveHandleListActivity.notifyChanged();
			}else {
				Toast.makeText(NoteForLeaveHandleDetailsActivity.this, "批准假条失败!", 0).show();
			}
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
