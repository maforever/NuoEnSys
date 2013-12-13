package com.chinaknown.nuoensys.fragments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaknown.nuoensys.DateWheelPickerActivity;
import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.ReportReceiverSelectedActivity;
import com.chinaknown.nuoensys.SheZhiReportContentActivity;
import com.chinaknown.nuoensys.SheZhiReportTitle;
import com.chinaknown.nuoensys.dialog.LoadingDialog;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.utils.AppConstants;
import com.chinaknown.nuoensys.utils.AppSharedPreference;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.MyApplication;

public class FragmentWriteReport extends Fragment {
	private ImageView titleArrow, contentArrow, createDateArrow, sendToArrow;
	private Intent intent;
	public static TextView titleTv, contentTv, createDateTv, sendToTv;
	public static Employee toEmployee;
	private TextView resetBtn, sendBtn;
	private AppSharedPreference asp;
	private MyApplication myApp;
	private Map<String, String> map;
	private LoadingDialog listReceiverLd, sendReportLd;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				List<Employee> employees = (List<Employee>) msg.obj;
				if(employees == null) {
					Toast.makeText(getActivity(), "数据加载失败,请检查网络是否正常!", 0).show();
				}else if(employees.size() == 0) {
					Toast.makeText(getActivity(), "没有可发送的人物信息!", 0).show();
				}else {
					
					MyApplication mApp = (MyApplication) getActivity().getApplicationContext();
					mApp.setReportReceivers(employees);
					ReportReceiverSelectedActivity.prepare(getActivity(), R.id.root);
					intent = new Intent(getActivity(), ReportReceiverSelectedActivity.class);
					intent.putExtra("titleName", "选择周报接收人");
					startActivity(intent);
					getActivity().overridePendingTransition(0, 0);
				}
				break;
			case 2:
				//发送周报信息
				String result = (String) msg.obj;
				if(result != null) {
					Toast.makeText(getActivity(), "周报发送成功!", 0).show();
					reset();
				}else {
					Toast.makeText(getActivity(), "周报发送失败!", 0).show();
				}
				break;
			}
			listReceiverLd.dismiss();
			sendReportLd.dismiss();
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		asp = new AppSharedPreference(getActivity());
		myApp = (MyApplication) getActivity().getApplicationContext();
		initProgressDialog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_writereport, container, false);
		findViews(view);
		titleTv.setText(SheZhiReportTitle.value);
		titleArrow.setOnClickListener(new MyOnClickListener());
		contentArrow.setOnClickListener(new MyOnClickListener());
		createDateArrow.setOnClickListener(new MyOnClickListener());
		sendToArrow.setOnClickListener(new MyOnClickListener());
		resetBtn.setOnClickListener(new MyOnClickListener());
		sendBtn.setOnClickListener(new MyOnClickListener());
		titleTv.setOnClickListener(new MyOnClickListener());
		contentTv.setOnClickListener(new MyOnClickListener());
		createDateTv.setOnClickListener(new MyOnClickListener());
		sendToTv.setOnClickListener(new MyOnClickListener());
		return view;
	}
	
	private void findViews(View view) {
		titleArrow = (ImageView) view.findViewById(R.id.titleArrow);
		contentArrow = (ImageView) view.findViewById(R.id.contentArrow);
		createDateArrow = (ImageView) view.findViewById(R.id.createDateArrow);
		sendToArrow = (ImageView) view.findViewById(R.id.sendToArrow);
		titleTv = (TextView) view.findViewById(R.id.title);
		contentTv = (TextView) view.findViewById(R.id.content);
		createDateTv = (TextView) view.findViewById(R.id.createDate);
		sendToTv = (TextView) view.findViewById(R.id.sendTo);
		resetBtn = (TextView) view.findViewById(R.id.reset);
		sendBtn = (TextView) view.findViewById(R.id.send);
	}
	
	private class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.titleArrow:
			case R.id.title:
				SheZhiReportTitle.prepare(getActivity(), R.id.root);
				intent = new Intent(getActivity(), SheZhiReportTitle.class);
				intent.putExtra("value", titleTv.getText().toString().trim());
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
				break;
			case R.id.contentArrow:
			case R.id.content:
				SheZhiReportContentActivity.prepare(getActivity(), R.id.root);
				intent = new Intent(getActivity(), SheZhiReportContentActivity.class);
				intent.putExtra("value", contentTv.getText().toString().trim());
				intent.putExtra("titleNameStr", "设置周报内容");
				startActivity(intent);
				getActivity().overridePendingTransition(0, 0);
				break;
			case R.id.createDateArrow:
			case R.id.createDate:
				intent = new Intent(getActivity(), DateWheelPickerActivity.class);
				intent.putExtra("resultCode", AppConstants.SELECTED_DATE_FINISHED);
				startActivityForResult(intent, 100);
				break;
			case R.id.sendToArrow:
			case R.id.sendTo:
				listReceiverLd.show();
				new LoadReportReceiver().start();
				break;
			case R.id.reset:
				reset();
				break;
			case R.id.send:
				if(validate()) {
					sendReportLd.show();
					initReportInfo();
					new SendReportThread().start();
				}
				break;
			}
		}
		
	}
	
	private void initReportInfo() {
		map = new HashMap<String, String>();
		map.put("title", titleTv.getText().toString().trim());
		map.put("content", contentTv.getText().toString().trim());
		Employee loginer = myApp.getLoginer();
		map.put("creator", loginer.getRealName());
		map.put("creatorid", String.valueOf(loginer.getUserid()));
		map.put("department", loginer.getDepartment());
		map.put("duty", loginer.getDuty());
		map.put("createDate", createDateTv.getText().toString().trim());
		map.put("touserid", String.valueOf(toEmployee.getUserid()));
		map.put("toname", toEmployee.getRealName());
	}
	
	private class SendReportThread extends Thread {
		public void run() {
			String result = HttpUtils.androidSendReport(map);
			handler.sendMessage(handler.obtainMessage(2, result));
		};
	}
	
	private void reset() {
		titleTv.setText("");
		contentTv.setText("");
		createDateTv.setText("");
		sendToTv.setText("");
	}
	
	private boolean validate() {
		String title = titleTv.getText().toString().trim();
		String content = contentTv.getText().toString().trim();
		String createDate = createDateTv.getText().toString().trim();
		String sendTo = sendToTv.getText().toString().trim();
		
		if("".equals(title) || "".equals(content) || "".equals(createDate) || "".equals(sendTo)) {
			Toast.makeText(getActivity(), "周报信息不完整,请填写完整周报信息", 0).show();
			return false;
		}
		
		return true;
	}
	
	private class LoadReportReceiver extends Thread {
		@Override
		public void run() {
			Integer idx = myApp.getLoginer().getUserid();
			List<Employee> employees = HttpUtils.loadReportReceivers(idx);
			handler.sendMessage(handler.obtainMessage(1, employees));
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case AppConstants.SELECTED_DATE_FINISHED:
			String date = data.getStringExtra("date");
			createDateTv.setText(date);
			break;
			
		default:
			break;
		}
	}
	
	private void initProgressDialog() {
		listReceiverLd = new LoadingDialog(getActivity(), "加载周报收件人中..");
		sendReportLd = new LoadingDialog(getActivity(), "正在发送周报中...");
	}
	
}
