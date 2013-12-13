package com.chinaknown.nuoensys.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.model.Employee;

public class ReportReceiverListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Employee> employees;
	private int resourceId;
	private int currentSelectedId = -1;
	public ReportReceiverListAdapter(Context context, List<Employee> employees, int resourceId) {
		inflater = LayoutInflater.from(context);
		this.employees = employees;
		this.resourceId = resourceId;
	}
	
	@Override
	public int getCount() {
		return employees.size();
	}

	@Override
	public Object getItem(int position) {
		return employees.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelected(int selectedId) {
		this.currentSelectedId = selectedId;
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView title;
		ImageView image;
		ViewHolder vh;
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			title = (TextView) convertView.findViewById(R.id.title);
			image = (ImageView) convertView.findViewById(R.id.selectedImage);
			vh = new ViewHolder();
			vh.title = title;
			vh.image = image;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			image = vh.image;
		}
		Employee employee = employees.get(position);
		title.setText(employee.getDepartment() + "-" + employee.getDuty() + "-" + employee.getRealName());
		image.setImageResource(R.drawable.company_unselect);
		if(currentSelectedId == position) {
			image.setImageResource(R.drawable.company_select);
		}
		
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		ImageView image;
	}
}







