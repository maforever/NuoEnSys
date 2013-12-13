package com.chinaknown.nuoensys;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaknown.nuoensys.dialog.LoadingDialog;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.NoteForLeave;
import com.chinaknown.nuoensys.model.NoteForLeaveProce;
import com.chinaknown.nuoensys.utils.AppConstants;
import com.chinaknown.nuoensys.utils.DateFormater;
import com.chinaknown.nuoensys.utils.MyApplication;
import com.chinaknown.nuoensys.utils.NoteForLeaveHttpUtils;

public class NoteForLeaveInputActivity extends Activity {
	private TextView titleTv, fromDateTv, toDateTv, totalDaysTv, reasonTv,
			proceTv, proceExplanTv;
	private String title, fromDate, toDate, reason, proceName;
	private Intent intent;
	private LoadingDialog ld, sendLd;
	private NoteForLeaveProce proce;
	private int days = 0;
	private Employee employee;
	private Map<String, String> maps;
	private int flag = 1;    //1标示直接发送，完了回到假条首页，2表示再写，清空控件内容
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_noteforleave_input);
		
		MyApplication myApp = (MyApplication) this.getApplication();
		employee = myApp.getLoginer();
		
		findViews();
		initLoadingDialog();
	}

	private void findViews() {
		titleTv = (TextView) this.findViewById(R.id.title);
		fromDateTv = (TextView) this.findViewById(R.id.fromDate);
		toDateTv = (TextView) this.findViewById(R.id.toDate);
		totalDaysTv = (TextView) this.findViewById(R.id.totalDays);
		reasonTv = (TextView) this.findViewById(R.id.reason);
		proceTv = (TextView) this.findViewById(R.id.proce);
		proceExplanTv = (TextView) this.findViewById(R.id.proceExplan);
	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.reset:
			clearViewDatas();
			break;
		case R.id.send:
			if (validateDatas()) {
				maps = new HashMap<String, String>();
				maps.put("creatorid", String.valueOf(employee.getUserid()));
				maps.put("creator", employee.getRealName());
				maps.put("department", employee.getDepartment());
				maps.put("duty", employee.getDuty());
				maps.put("project", employee.getProject());
				maps.put("departmentid", String.valueOf(employee.getDepartmentid()));
				maps.put("dutyid", String.valueOf(employee.getDutyid()));
				maps.put("projectid", String.valueOf(employee.getProjectid()));
				maps.put("fromDate", fromDate);
				maps.put("toDate", toDate);
				maps.put("nfltitle", title);
				maps.put("totaldays", String.valueOf(days));
				maps.put("proceid", String.valueOf(proce.getIdx()));
				maps.put("nflreason", reason);
//				for(Map.Entry<String, String> entry : maps.entrySet()) {
//					Log.i("a", entry.getKey() + " ---- " + entry.getValue());
//				}
				flag = 1;
				sendLd.show();
				new SendNoteForLeaveAsyncTask().execute(maps);
			}
			break;
		case R.id.rewrite:
			if (validateDatas()) {
				maps = new HashMap<String, String>();
				maps.put("creatorid", String.valueOf(employee.getUserid()));
				maps.put("creator", employee.getRealName());
				maps.put("department", employee.getDepartment());
				maps.put("duty", employee.getDuty());
				maps.put("project", employee.getProject());
				maps.put("departmentid", String.valueOf(employee.getDepartmentid()));
				maps.put("dutyid", String.valueOf(employee.getDutyid()));
				maps.put("projectid", String.valueOf(employee.getProjectid()));
				maps.put("fromDate", fromDate);
				maps.put("toDate", toDate);
				maps.put("nfltitle", title);
				maps.put("totaldays", String.valueOf(days));
				maps.put("proceid", String.valueOf(proce.getIdx()));
				maps.put("nflreason", reason);
//				for(Map.Entry<String, String> entry : maps.entrySet()) {
//					Log.i("a", entry.getKey() + " ---- " + entry.getValue());
//				}
				flag = 2;
				sendLd.show();
				new SendNoteForLeaveAsyncTask().execute(maps);
			}
			break;
		case R.id.titlelayout:
			intent = new Intent(NoteForLeaveInputActivity.this,
					TextInputActivity.class);
			intent.putExtra("title", "假条标题");
			intent.putExtra("resultCode", AppConstants.EDIT_TITLE);
			intent.putExtra("editNum", 20);
			intent.putExtra("content", titleTv.getText().toString().trim());
			startActivityForResult(intent, 100);
			break;
		case R.id.fromDatelayout:
			intent = new Intent(NoteForLeaveInputActivity.this,
					DateWheelPickerActivity.class);
			intent.putExtra("resultCode", AppConstants.SELECTED_FROM_DATE);
			startActivityForResult(intent, 100);
			break;
		case R.id.toDatelayout:
			intent = new Intent(NoteForLeaveInputActivity.this,
					DateWheelPickerActivity.class);
			intent.putExtra("resultCode", AppConstants.SELECTED_END_DATE);
			startActivityForResult(intent, 100);
			break;
		case R.id.totalDaysLayout:
			break;
		case R.id.reasonlayout:
			intent = new Intent(NoteForLeaveInputActivity.this,
					TextInputActivity.class);
			intent.putExtra("title", "请假原因");
			intent.putExtra("resultCode", AppConstants.EDIT_REASON);
			intent.putExtra("editNum", 100);
			intent.putExtra("content", reasonTv.getText().toString().trim());
			startActivityForResult(intent, 100);
			break;
		case R.id.procelayout:
			ld.show();
			new LoadProceThreadAsyncTask().execute();
			break;
		case R.id.proceExplanlayout:
			break;
		}
	}

	private class SendNoteForLeaveAsyncTask extends AsyncTask<Map<String, String>, Integer, String> {

		@Override
		protected String doInBackground(Map<String, String>... params) {
			String result = NoteForLeaveHttpUtils.sendNoteForLeave(params[0]);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(sendLd.isShowing()) {
				sendLd.dismiss();
			}
			if(result != null && !"".equals(result)) {
				Toast.makeText(NoteForLeaveInputActivity.this, "请假条申请成功!", 0).show();
				if(NoteForLeaveInputActivity.this.flag == 1) {
					//直接发送
					back();
				}else if(NoteForLeaveInputActivity.this.flag == 2) {
					//再写
					clearViewDatas();
				}
			}else {
				Toast.makeText(NoteForLeaveInputActivity.this, "请假条申请失败!", 0).show();
			}
		}
		
	}
	
	
	
	private boolean validateDatas() {
		title = titleTv.getText().toString().trim();
		reason = reasonTv.getText().toString().trim();
		fromDate = fromDateTv.getText().toString().trim();
		toDate = toDateTv.getText().toString().trim();
		proceName = proceTv.getText().toString().trim();

		if ("".equals(title) || "".equals(reason) || "".equals(fromDate)
				|| "".equals(toDate) || "".equals(proceName)) {
			Toast.makeText(this, "假条信息不完整,请检查信息!", 0).show();
			return false;
		}

		if (days <= 0) {
			Toast.makeText(this, "假期截止日期应该大于假期开始日期!", 0).show();
			return false;
		}

		return true;
	}

	private void clearViewDatas() {
		titleTv.setText("");
		fromDateTv.setText("");
		toDateTv.setText("");
		reasonTv.setText("");
		proceTv.setText("");
		totalDaysTv.setText("");
		days = 0;
		proce = null;
		maps = null;
		proceExplanTv.setText("");
	}

	private class LoadProceThreadAsyncTask extends
			AsyncTask<String, Integer, List<NoteForLeaveProce>> {

		@Override
		protected List<NoteForLeaveProce> doInBackground(String... params) {
			List<NoteForLeaveProce> proces = NoteForLeaveHttpUtils.loadProces();
			return proces;
		}

		@Override
		protected void onPostExecute(List<NoteForLeaveProce> result) {
			if (result == null) {
				Toast.makeText(NoteForLeaveInputActivity.this, "请检查网络地址是否正确!",
						0).show();
			} else if (result.size() <= 0) {
				Toast.makeText(NoteForLeaveInputActivity.this,
						"没有找到任何可用假条申请流程信息!", 0).show();
			} else {
				MyApplication myApp = (MyApplication) NoteForLeaveInputActivity.this
						.getApplication();
				myApp.setNflProces(result);
				intent = new Intent(NoteForLeaveInputActivity.this,
						NoteForLeaveProceSelectActivity.class);
				startActivityForResult(intent, 100);
			}
			if (ld.isShowing()) {
				ld.dismiss();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case AppConstants.EDIT_TITLE:
			String title = data.getStringExtra("content");
			titleTv.setText(title);
			break;
		case AppConstants.EDIT_REASON:
			String reason = data.getStringExtra("content");
			reasonTv.setText(reason);
			break;
		case AppConstants.SELECTED_FROM_DATE:
			String fromDate = data.getStringExtra("dateYMD");
			fromDateTv.setText(fromDate);
			setTotalDays();
			break;
		case AppConstants.SELECTED_END_DATE:
			String toDate = data.getStringExtra("dateYMD");
			toDateTv.setText(toDate);
			setTotalDays();
			break;
		case AppConstants.SELECTED_PROCE:
			proce = (NoteForLeaveProce) data.getSerializableExtra("proce");
			proceTv.setText(proce.getName());
			proceExplanTv.setText(proce.getProce());
			break;
		}
	}

	private void setTotalDays() {
		String fromDate = fromDateTv.getText().toString();
		String toDate = toDateTv.getText().toString();
		if (!"".equals(fromDate) && !"".equals(toDate)) {
			try {
				days = DateFormater.getInstatnce().getDaysBetween(fromDate,
						toDate) + 1;
				totalDaysTv.setText(days + " 天");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initLoadingDialog() {
		ld = new LoadingDialog(NoteForLeaveInputActivity.this, "正在获取请假条申请流程...");
		sendLd = new LoadingDialog(NoteForLeaveInputActivity.this, "正在发送请假条申请信息...");
	}

	private void back() {
		this.finish();
		overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return true;
	}
}
