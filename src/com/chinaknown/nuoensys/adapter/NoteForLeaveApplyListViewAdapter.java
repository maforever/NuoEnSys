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
import com.chinaknown.nuoensys.model.NoteForLeave;
import com.chinaknown.nuoensys.model.Report;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.NoteForLeaveHttpUtils;

public class NoteForLeaveApplyListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<NoteForLeave> nfls;
	private DialogAlertBill dab;
	private Context context;
	private NoteForLeave nfl;
	private boolean flag;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				if(result != null && !"".equals(result)) {
					Toast.makeText(context, "请假条删除成功!", 0).show();
					nfls.remove(nfl);
					notifyDataSetChanged();
				}else {
					Toast.makeText(context, "请假条删除失败!", 0).show();
				}
				break;
			}
		};
	};
	public NoteForLeaveApplyListViewAdapter(Context context, List<NoteForLeave> nfls, boolean flag) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.nfls = nfls;
		this.flag = flag;
	}
	
	public void addItems(List<NoteForLeave> _nfls) {
		this.nfls.addAll(_nfls);
		this.notifyDataSetChanged();
	}
	
	public void clear() {
		nfls.clear();
		//page = 1;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return nfls.size();
	}

	@Override
	public Object getItem(int position) {
		return nfls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void myNotifyChanged(List<NoteForLeave> nfls) {
		this.nfls = nfls;
		Log.i("a", "size2 = " + nfls.size());
		this.notifyDataSetChanged();
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
							nfl = nfls.get(location);
							Log.i("a", nfl.toString());
							new DeleteReportThread(nfl.getIdx()).start();
						}
						
						@Override
						public void onDialogCreate(Dialog dlg, Object param) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onDialogCancel(Dialog dlg, Object param) {
							dab.dismiss();
						}
					}, "您确定删除该条申请中的假条吗?");
					dab.show();
				}
			});
		}else {
			deleteBtn.setVisibility(ViewGroup.GONE);
		}
		
		
		title.setText(nfls.get(position).getTitle());
		createDate.setText(nfls.get(position).getApplicationDate());
		
		return convertView;
	}

	private class DeleteReportThread extends Thread {
		private int idx;
		public DeleteReportThread(int idx) {
			this.idx = idx;
		}
		@Override
		public void run() {
			String result = NoteForLeaveHttpUtils.deleteNoteForLeaveById(idx);
			handler.sendMessage(handler.obtainMessage(1, result));
		}
	}
	
	
	static class ViewHolder {
		TextView title;
		TextView createDate;
		ImageView deleteBtn;
	}
}








