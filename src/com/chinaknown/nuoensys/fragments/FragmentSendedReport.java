package com.chinaknown.nuoensys.fragments;

import java.util.ArrayList;
import java.util.List;
import com.chinaknown.nuoensys.NewDetailActivity;
import com.chinaknown.nuoensys.NewsIndexActivity;
import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.ReportDetailsActivity;
import com.chinaknown.nuoensys.ReportIndexActivity;
import com.chinaknown.nuoensys.adapter.NewsIndexListViewAdapter;
import com.chinaknown.nuoensys.adapter.ReportReceiverListAdapter;
import com.chinaknown.nuoensys.adapter.ReportSendedListViewAdapter;
import com.chinaknown.nuoensys.dialog.DialogAlertBill;
import com.chinaknown.nuoensys.dialog.DialogAlertListener;
import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.model.Report;
import com.chinaknown.nuoensys.pulldownlistview.LoadingHelper;
import com.chinaknown.nuoensys.pulldownlistview.LoadingListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.onLoadMoreListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.pullToRefreshListener;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.MyApplication;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentSendedReport extends Fragment implements LoadingListener{
	PullDownRefreshView refreshView = null;
	LoadingHelper loadingHelper = null;
	public int pageindex = 1;
	public int DataSizePerPage = 10;

	private Intent intent;
	View view = null;
	ListView listView = null;
	public ReportSendedListViewAdapter listAdapter;
	List<Report> reports = new ArrayList<Report>();
	private ReportIndexActivity index;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				final List<Report> reports = (List<Report>) msg.obj;
				Log.i("a", "size = " + reports.size());
				this.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						onSuccess2(reports);
					}
				}, 1500);
				
				break;
			}
		};
	};
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("a", "FragmentSendedReport onCreate");
		index = (ReportIndexActivity) getActivity();
//		init(view);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("a", "FragmentSendedReport createView");
		view = inflater.inflate(R.layout.fragment_senedreport, container, false);
		init(view);
		return view;
	}
	
	
	

	private void init(View view) {
		// 可自定义的网络加载挡板和数据为空的view
		loadingHelper = new LoadingHelper(getActivity(), view.findViewById(R.id.loading_prompt_linear), view.findViewById(R.id.loading_empty_prompt_linear));
		loadingHelper.ShowLoading();
		loadingHelper.SetListener(this);

		// 下拉刷新上拉加载更多控件
		refreshView = (PullDownRefreshView) view.findViewById(R.id.pulldown_refreshview);
		listView = (ListView) refreshView.getChildAt(1);

		listAdapter = new ReportSendedListViewAdapter(getActivity(), reports, true);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(getActivity(), ReportDetailsActivity.class);
				intent.putExtra("r", reports.get(position));
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
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
				MyApplication myApp = (MyApplication) getActivity().getApplication();
				List<Report> reports = HttpUtils.loadSendedReports(pageindex, myApp.getLoginer().getUserid());
				handler.sendMessage(handler.obtainMessage(1, reports));
			}
		}).start();
		
	}

	
	/**
	 * 数据加载成功
	 */
	public void onSuccess2(List<Report> reports) {

		loadingHelper.HideLoading(8);
		if (refreshView.getRefreshState()) {
			if (listAdapter != null)
				listAdapter.clear();
			refreshView.finishRefreshing();// 刷新完成隐藏下拉headview
		}
		refreshView.setOnLoadState(false, false);
		refreshView.initListFootView(listAdapter); // 初始化加载更多的footview
		if ((reports == null || reports.size() == 0) && pageindex == 1) {
			loadingHelper.ShowEmptyData();
			refreshView.removeListFootView(); // 移除加载更多的footview
			return;
		}
		listAdapter.addItems(reports);
		if (reports == null || reports.size() < DataSizePerPage) {
			Toast.makeText(index, R.string.loading_data_finished, Toast.LENGTH_SHORT).show();
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

	
	
//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//		reports.clear();
//		Log.i("a", "FragmentSendedReport onDestroyView");
//		
//	}
//	
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		Log.i("a", "FragmentSendedReport onDetach");
//	}
//	
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		Log.i("a", "FragmentSendedReport onAttach");
//	}
//	
//	@Override
//	public void onResume() {
//		super.onResume();
//		Log.i("a", "FragmentSendedReport onResume");
//	}
//	
//	@Override
//	public void onPause() {
//		super.onPause();
//		Log.i("a", "FragmentSendedReport onPause");
//	}

}


















