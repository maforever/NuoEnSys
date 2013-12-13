package com.chinaknown.nuoensys;

import com.chinaknown.nuoensys.dialog.DialogAlertBill;
import com.chinaknown.nuoensys.dialog.DialogAlertListener;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.NoteForLeave;
import com.chinaknown.nuoensys.utils.MyApplication;
import com.chinaknown.nuoensys.utils.NoteForLeaveHttpUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ApplyNoteForLeaveDetailsActivity extends Activity {
	private TextView titleTv, fromDateTv, toDateTv, totalDaysTv, proceNameTv, proceExplanTv, locationNameTv, creatorTv, stateTv, reasonTv;
	private ImageView deleteBtn;
	private NoteForLeave nfl;
	private Employee employee;
	private DialogAlertBill dab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_noteforleave_apply_details);
		
		MyApplication myApp = (MyApplication) this.getApplication();
		employee = myApp.getLoginer();
		nfl = (NoteForLeave) this.getIntent().getSerializableExtra("nfl");
		
		findViews();
		initViews();
	}
	
	
	private void findViews() {
		titleTv = (TextView) this.findViewById(R.id.title);
		fromDateTv = (TextView) this.findViewById(R.id.fromDate);
		toDateTv = (TextView) this.findViewById(R.id.toDate);
		totalDaysTv = (TextView) this.findViewById(R.id.totalDays);
		proceNameTv = (TextView) this.findViewById(R.id.proceName);
		proceExplanTv = (TextView) this.findViewById(R.id.proceExplan);
		locationNameTv = (TextView) this.findViewById(R.id.locationName);
		creatorTv = (TextView) this.findViewById(R.id.creator);
		stateTv = (TextView) this.findViewById(R.id.state);
		reasonTv = (TextView) this.findViewById(R.id.reason);
		deleteBtn = (ImageView) this.findViewById(R.id.delete);
	}
	
	
	
	private void initViews() {
		titleTv.setText(nfl.getTitle());
		fromDateTv.setText(nfl.getStartDate());
		toDateTv.setText(nfl.getEndDate());
		totalDaysTv.setText(nfl.getDays() + "天");
		proceNameTv.setText(nfl.getProcedureName());
		proceExplanTv.setText(nfl.getProce());
		locationNameTv.setText(nfl.getLocationName());
		creatorTv.setText(employee.getRealName());
		reasonTv.setText(nfl.getReason());
		if(nfl.getState() == 0) {
			stateTv.setText("申请中");
		}
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.delete:
			dab = new DialogAlertBill(this, new DialogAlertListener() {
				
				@Override
				public void onDialogUnSave(Dialog dlg, Object param) {
					
				}
				
				@Override
				public void onDialogSave(Dialog dlg, Object param) {
					
				}
				
				@Override
				public void onDialogOk(Dialog dlg, Object param) {
					new DeleteNoteForLeaveAsyncTask().execute(nfl.getIdx());
				}
				
				@Override
				public void onDialogCreate(Dialog dlg, Object param) {
					
				}
				
				@Override
				public void onDialogCancel(Dialog dlg, Object param) {
					dab.dismiss();
				}
			}, "您确定撤销该请假条信息吗?");
			dab.show();
			
			break;
		}
	}
	
	private class DeleteNoteForLeaveAsyncTask extends AsyncTask<Integer, Integer, String> {

		@Override
		protected String doInBackground(Integer... params) {
			String result = NoteForLeaveHttpUtils.deleteNoteForLeaveById(params[0]);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null && !"".equals(result)) {
				Toast.makeText(ApplyNoteForLeaveDetailsActivity.this, "撤销申请中的请假条成功!", 0).show();
				NoteForLeaveApplyActivity.removeItem(nfl);
				NoteForLeaveApplyActivity.notifyChanged();
				back();
			}else {
				Toast.makeText(ApplyNoteForLeaveDetailsActivity.this, "撤销申请中的请假条失败!", 0).show();
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












