package com.chinaknown.nuoensys.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.model.CompanyNew;

public class NewsIndexListViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CompanyNew> news;
	
	public NewsIndexListViewAdapter(Context context, List<CompanyNew> news) {
		inflater = LayoutInflater.from(context);
		this.news = news;
	}
	
	public void addItems(List<CompanyNew> _news) {
		this.news.addAll(_news);
		this.notifyDataSetChanged();
	}
	
	public void clear() {
		news.clear();
		//page = 1;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return news.size();
	}

	@Override
	public Object getItem(int position) {
		return news.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView title;
		TextView createDate;
		ViewHolder vh;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.news_listview_item, null);
			title = (TextView) convertView.findViewById(R.id.title);
			createDate = (TextView) convertView.findViewById(R.id.createDate);
			vh = new ViewHolder();
			vh.title = title;
			vh.createDate = createDate;
			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
			title = vh.title;
			createDate = vh.createDate;
		}
		
		title.setText(news.get(position).getTitle());
		createDate.setText(news.get(position).getCreateDate());
		
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView createDate;
	}
}








