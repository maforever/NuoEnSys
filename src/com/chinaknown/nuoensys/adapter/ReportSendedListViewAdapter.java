package com.chinaknown.nuoensys.adapter;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.dialog.DialogAlertBill;
import com.chinaknown.nuoensys.dialog.DialogAlertListener;
import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.model.Report;
import com.chinaknown.nuoensys.utils.HttpUtils;

public class ReportSendedListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Report> reports;
	private DialogAlertBill dab;
	private Context context;
	private Report r;
	private boolean flag;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				if(result != null && !"".equals(result)) {
					Toast.makeText(context, "周报删除成功!", 0).show();
					reports.remove(r);
					notifyDataSetChanged();
				}else {
					Toast.makeText(context, "周报删除失败!", 0).show();
				}
				break;
			}
		};
	};
	public ReportSendedListViewAdapter(Context context, List<Report> reports, boolean flag) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.reports = reports;
		this.flag = flag;
	}
	
	public void addItems(List<Report> _reports) {
		this.reports.addAll(_reports);
		this.notifyDataSetChanged();
	}
	
	public void clear() {
		reports.clear();
		//page = 1;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return reports.size();
	}

	@Override
	public Object getItem(int position) {
		return reports.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView title;
		TextView createDate;
		ImageView deleteBtn = null;
		ViewHolder vh;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.sendedreportlistview_item, null);
			title = (TextView) convertView.findViewById(R.id.title);
			createDate = (TextView) convertView.findViewById(R.id.createDate);
			deleteBtn = (ImageView) convertView.findViewById(R.id.deleteBtn);
			vh = new ViewHolder();
			vh.title = title;
			vh.createDate = createDate;
			vh.deleteBtn = deleteBtn;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			createDate = vh.createDate;
			deleteBtn = vh.deleteBtn;
		}
		
		if(flag) {
			final int location = position;
			deleteBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
						dab = new DialogAlertBill(context, new DialogAlertListener() {
						
						@Override
						public void onDialogUnSave(Dialog dlg, Object param) {
							
						}
						
						@Override
						public void onDialogSave(Dialog dlg, Object param) {
							
						}
						
						@Override
						public void onDialogOk(Dialog dlg, Object param) {
							r = reports.get(location);
							Log.i("a", r.toString());
							new DeleteReportThread(r.getIdx()).start();
						}
						
						@Override
						public void onDialogCreate(Dialog dlg, Object param) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onDialogCancel(Dialog dlg, Object param) {
							dab.dismiss();
						}
					}, "您确定删除该条周报信息吗?");
					dab.show();
				}
			});
		}else {
			deleteBtn.setVisibility(ViewGroup.GONE);
		}
		
		
		title.setText(reports.get(position).getTitle());
		createDate.setText(reports.get(position).getCreateDate());
		
		return convertView;
	}

	private class DeleteReportThread extends Thread {
		private int idx;
		public DeleteReportThread(int idx) {
			this.idx = idx;
		}
		@Override
		public void run() {
			String result = HttpUtils.deleteReportById(idx);
			handler.sendMessage(handler.obtainMessage(1, result));
		}
	}
	
	
	static class ViewHolder {
		TextView title;
		TextView createDate;
		ImageView deleteBtn;
	}
}








