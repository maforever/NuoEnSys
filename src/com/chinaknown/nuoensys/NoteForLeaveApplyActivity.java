package com.chinaknown.nuoensys;

import java.util.ArrayList;
import java.util.List;

import com.chinaknown.nuoensys.adapter.NewsIndexListViewAdapter;
import com.chinaknown.nuoensys.adapter.NoteForLeaveApplyListViewAdapter;
import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.model.NoteForLeave;
import com.chinaknown.nuoensys.pulldownlistview.LoadingHelper;
import com.chinaknown.nuoensys.pulldownlistview.LoadingListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.onLoadMoreListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.pullToRefreshListener;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.MyApplication;
import com.chinaknown.nuoensys.utils.NoteForLeaveHttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.ClipData.Item;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NoteForLeaveApplyActivity extends Activity implements LoadingListener{
	PullDownRefreshView refreshView;
	LoadingHelper loadingHelper;
	public int pageindex = 1;
	public int DataSizePerPage = 10;
	private Employee employee;
	ListView listView;
	public static NoteForLeaveApplyListViewAdapter listAdapter;
	ArrayList<String> items = new ArrayList<String>();
	public static List<NoteForLeave> nfls = new ArrayList<NoteForLeave>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				final List<NoteForLeave> nfls = (ArrayList<NoteForLeave>) msg.obj;
				
				this.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						onSuccess2(nfls);
					}
				}, 1500);
				
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_noteforleave_list);
		
		MyApplication myApp = (MyApplication) this.getApplication();
		employee = myApp.getLoginer();
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
		
		TextView titleTv = (TextView) this.findViewById(R.id.title);
		titleTv.setText("申请中的假条");
		
		// 可自定义的网络加载挡板和数据为空的view
		loadingHelper = new LoadingHelper(this, findViewById(R.id.loading_prompt_linear), findViewById(R.id.loading_empty_prompt_linear));
		loadingHelper.ShowLoading();
		loadingHelper.SetListener(this);

		// 下拉刷新上拉加载更多控件
		refreshView = (PullDownRefreshView) findViewById(R.id.pulldown_refreshview);
		listView = (ListView) refreshView.getChildAt(1);
		
		listAdapter = new NoteForLeaveApplyListViewAdapter(this, nfls, true);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(NoteForLeaveApplyActivity.this, ApplyNoteForLeaveDetailsActivity.class);
				intent.putExtra("nfl", nfls.get(position));
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
						refreshView.setOnLoadState(false, true); // 设置加载状态,参数为isFinished和isRefreshing
						pageindex = 1;
						doLoadMore();
					}
				});
			}
		}, 0);// 这里id只是一个标志,用以区分不同页面上次下拉刷新的时间
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
	 * 测试模拟加载更多数据
	 */
	public void doLoadMore() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<NoteForLeave> nfls = NoteForLeaveHttpUtils.loadNoteForLeaveList(pageindex, employee.getUserid(), 0);
				handler.sendMessage(handler.obtainMessage(1, nfls));
			}
		}).start();
		
		
		
	}

	
	/**
	 * 数据加载成功
	 */
	public void onSuccess2(List<NoteForLeave> nfls) {
		loadingHelper.HideLoading(8);
		if (refreshView.getRefreshState()) {
			if (listAdapter != null)
				listAdapter.clear();
			refreshView.finishRefreshing();// 刷新完成隐藏下拉headview
		}
		refreshView.setOnLoadState(false, false);
		refreshView.initListFootView(listAdapter); // 初始化加载更多的footview
		listAdapter.addItems(nfls);
		if ((nfls == null || nfls.size() == 0) && pageindex == 1) {
			loadingHelper.ShowEmptyData();
			refreshView.removeListFootView(); // 移除加载更多的footview
			return;
		}
		if (nfls == null || nfls.size() < DataSizePerPage) {
			Toast.makeText(this, R.string.loading_data_finished, Toast.LENGTH_SHORT).show();
			refreshView.removeListFootView();
		}
	}

	/**
	 * 数据加载失败
	 */
	public void onFail() {
		loadingHelper.ShowError("显示网络出错信息!");
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

	public static void  notifyChanged() {
		
		listAdapter.myNotifyChanged(nfls);
	}
	
	public static void removeItem(NoteForLeave nfl) {
		nfls.remove(nfl);
		Log.i("a", "size = " + nfls.size());
	}
}
