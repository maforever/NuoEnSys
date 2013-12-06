package com.chinaknown.nuoensys.adapter;

import com.chinaknown.nuoensys.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private int[] imageIds;
	private String[] names;
	private int resourceId;
	public IndexGridViewAdapter(Context context, int[] imageIds, String[] names, int resourceId) {
		this.inflater = LayoutInflater.from(context);
		this.imageIds = imageIds;
		this.names = names;
		this.resourceId = resourceId;
	}
	
	@Override
	public int getCount() {
		return imageIds.length;
	}

	@Override
	public Object getItem(int position) {
		return imageIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView textView;
		ViewHolder vh;
		
		if(convertView == null) {
			convertView = inflater.inflate(resourceId, null);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			textView = (TextView) convertView.findViewById(R.id.name);
			vh = new ViewHolder();
			vh.imageView = imageView;
			vh.textView = textView;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			imageView = vh.imageView;
			textView = vh.textView;
		}
		imageView.setImageResource(imageIds[position]);
		textView.setText(names[position]);
		
		
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView textView;
	}
	
}






