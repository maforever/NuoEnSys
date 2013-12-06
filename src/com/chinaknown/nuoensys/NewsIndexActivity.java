package com.chinaknown.nuoensys;

import java.util.ArrayList;
import java.util.List;

import com.chinaknown.nuoensys.adapter.NewsIndexListViewAdapter;
import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.pulldownlistview.LoadingHelper;
import com.chinaknown.nuoensys.pulldownlistview.LoadingListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.onLoadMoreListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.pullToRefreshListener;
import com.chinaknown.nuoensys.utils.HttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NewsIndexActivity extends Activity implements LoadingListener{
	PullDownRefreshView refreshView;
	LoadingHelper loadingHelper;
	public int pageindex = 1;
	public int DataSizePerPage = 10;

	ListView listView;
	public NewsIndexListViewAdapter listAdapter;
	ArrayList<String> items = new ArrayList<String>();
	List<CompanyNew> news = new ArrayList<CompanyNew>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				final List<CompanyNew> news = (ArrayList<CompanyNew>) msg.obj;
				
				this.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						onSuccess2(news);
					}
				}, 1500);
				
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_news);
		
		
		init();
	}
	
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
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
	
	
	
	
	
	
	private void init() {
		// ���Զ����������ص��������Ϊ�յ�view
		loadingHelper = new LoadingHelper(this, findViewById(R.id.loading_prompt_linear), findViewById(R.id.loading_empty_prompt_linear));
		loadingHelper.ShowLoading();
		loadingHelper.SetListener(this);

		// ����ˢ���������ظ���ؼ�
		refreshView = (PullDownRefreshView) findViewById(R.id.pulldown_refreshview);
		listView = (ListView) refreshView.getChildAt(1);
		
		listAdapter = new NewsIndexListViewAdapter(this, news);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(NewsIndexActivity.this, NewDetailActivity.class);
				intent.putExtra("cn", news.get(position));
				startActivity(intent);
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
			}
		});
		refreshView.setOnRefreshListener(new pullToRefreshListener() {
			@Override
			public void onRefresh() {
				refreshView.post(new Runnable() {
					@Override
					public void run() {
						refreshView.setOnLoadState(false, true); // ���ü���״̬,����ΪisFinished��isRefreshing
						pageindex = 1;
						doLoadMore();
					}
				});
			}
		}, 0);// ����idֻ��һ����־,�������ֲ�ͬҳ���ϴ�����ˢ�µ�ʱ��
		refreshView.setOnLoadMoreListener(new onLoadMoreListener() {
			@Override
			public void onLoadMore() {
				refreshView.setOnLoadState(false, false);
				pageindex++;
				doLoadMore();
			}
		});
		doLoadMore();
	}

	/**
	 * ����ģ����ظ�������
	 */
	public void doLoadMore() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<CompanyNew> news = HttpUtils.loadCompanyNews(pageindex);
				handler.sendMessage(handler.obtainMessage(1, news));
			}
		}).start();
		
		
		
	}

	
	/**
	 * ���ݼ��سɹ�
	 */
	public void onSuccess2(List<CompanyNew> news) {
		loadingHelper.HideLoading(8);
		if (refreshView.getRefreshState()) {
			if (listAdapter != null)
				listAdapter.clear();
			refreshView.finishRefreshing();// ˢ�������������headview
		}
		refreshView.setOnLoadState(false, false);
		refreshView.initListFootView(listAdapter); // ��ʼ�����ظ����footview
		listAdapter.addItems(news);
		if ((news == null || news.size() == 0) && pageindex == 1) {
			loadingHelper.ShowEmptyData();
			refreshView.removeListFootView(); // �Ƴ����ظ����footview
			return;
		}
		if (news == null || news.size() < DataSizePerPage) {
			Toast.makeText(this, R.string.loading_data_finished, Toast.LENGTH_SHORT).show();
			refreshView.removeListFootView();
		}
	}

	/**
	 * ���ݼ���ʧ��
	 */
	public void onFail() {
		loadingHelper.ShowError("��ʾ���������Ϣ!");
	}

	@Override
	public void OnRetryClick() {
		
		loadingHelper.ShowLoading();
		if (listAdapter != null)
			listAdapter.clear();
		refreshView.setOnLoadState(false, false);
		pageindex = 1;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loadingHelper.HideLoading(8);
				doLoadMore();
			}
		}, 1500);
		
		
	}

	
}








