package com.chinaknown.nuoensys.adapter;

import java.util.List;

import com.chinaknown.nuoensys.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoteForLeaveIndexGridViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private String[] titles;
	private int[] imageIds;

	public NoteForLeaveIndexGridViewAdapter(Context context, String[] titles, int[] imageIds) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.titles = titles;
		this.imageIds = imageIds;
	}


	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Object getItem(int position) {
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		TextView title;
		ViewHolder vh;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gridview_item_noteforleave_index,
					null);
			imageView = (ImageView) convertView.findViewById(R.id.imageView);
			title = (TextView) convertView.findViewById(R.id.title);
			vh = new ViewHolder();
			vh.imageView = imageView;
			vh.title = title;
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			imageView = vh.imageView;
		}
		title.setText(titles[position]);
		imageView.setImageResource(imageIds[position]);

		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView title;
	}

}
