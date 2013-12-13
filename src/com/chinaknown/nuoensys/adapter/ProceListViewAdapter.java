package com.chinaknown.nuoensys.adapter;

import java.util.List;

import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.model.NoteForLeaveProce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProceListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List datas;
	private int type = 1;   // 1为假条流程，2为报销单流程
	public ProceListViewAdapter(Context context, List datas, int type) {
		this.inflater = LayoutInflater.from(context);
		this.type = type;
		this.datas = datas;
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView name;
		TextView proce;
		ViewHolder vh;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.proce_listview_item, null);
			name = (TextView) convertView.findViewById(R.id.name);
			proce = (TextView) convertView.findViewById(R.id.proce);
			vh = new ViewHolder();
			vh.name = name;
			vh.proce = proce;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			name = vh.name;
			proce = vh.proce;
		}
		
		switch (type) {
		case 1:
			name.setText(((NoteForLeaveProce)datas.get(position)).getName());
			proce.setText(((NoteForLeaveProce)datas.get(position)).getProce());
			break;
		case 2:
			break;
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView name;
		TextView proce;
	}
	
	
	

}
