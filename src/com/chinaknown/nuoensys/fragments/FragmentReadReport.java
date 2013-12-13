package com.chinaknown.nuoensys.fragments;

import java.util.ArrayList;
import java.util.List;

import com.chinaknown.nuoensys.R;
import com.chinaknown.nuoensys.ReportDetailsActivity;
import com.chinaknown.nuoensys.ReportIndexActivity;
import com.chinaknown.nuoensys.adapter.ReportSendedListViewAdapter;
import com.chinaknown.nuoensys.model.Report;
import com.chinaknown.nuoensys.pulldownlistview.LoadingHelper;
import com.chinaknown.nuoensys.pulldownlistview.LoadingListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.onLoadMoreListener;
import com.chinaknown.nuoensys.pulldownlistview.PullDownRefreshView.pullToRefreshListener;
import com.chinaknown.nuoensys.utils.HttpUtils;
import com.chinaknown.nuoensys.utils.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentReadReport extends Fragment implements LoadingListener{

	PullDownRefreshView refreshView;
	LoadingHelper loadingHelper;
	public int pageindex = 1;
	public int DataSizePerPage = 10;

	private Intent intent;

	ListView listView;
	public ReportSendedListViewAdapter listAdapter;
	List<Report> reports = new ArrayList<Report>();
	private ReportIndexActivity index;
	private String flag;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				final List<Report> reports = (List<Report>) msg.obj;
				
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
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("a", "FragmentReadReport createView");
		View view = inflater.inflate(R.layout.fragment_readreport, container, false);
		init(view);
		return view;
	}
	

	private void init(View view) {
		// ���Զ����������ص��������Ϊ�յ�view
		loadingHelper = new LoadingHelper(getActivity(), view.findViewById(R.id.loading_prompt_linear), view.findViewById(R.id.loading_empty_prompt_linear));
		loadingHelper.ShowLoading();
		loadingHelper.SetListener(this);

		// ����ˢ���������ظ���ؼ�
		refreshView = (PullDownRefreshView) view.findViewById(R.id.pulldown_refreshview);
		listView = (ListView) refreshView.getChildAt(1);
		listAdapter = new ReportSendedListViewAdapter(getActivity(), reports, false);
		listView.setAdapter(listAdapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(getActivity(), ReportDetailsActivity.class);
				intent.putExtra("r", reports.get(position));
				intent.putExtra("flag", "read");
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
				MyApplication myApp = (MyApplication) getActivity().getApplication();
				List<Report> reports = HttpUtils.loadReadReports(pageindex, myApp.getLoginer().getUserid());
				handler.sendMessage(handler.obtainMessage(1, reports));
			}
		}).start();
		
		
		
	}

	
	/**
	 * ���ݼ��سɹ�
	 */
	public void onSuccess2(List<Report> reports) {
		loadingHelper.HideLoading(8);
		if (refreshView.getRefreshState()) {
			if (listAdapter != null)
				listAdapter.clear();
			refreshView.finishRefreshing();// ˢ�������������headview
		}
		refreshView.setOnLoadState(false, false);
		refreshView.initListFootView(listAdapter); // ��ʼ�����ظ����footview
		if ((reports == null || reports.size() == 0) && pageindex == 1) {
			loadingHelper.ShowEmptyData();
			refreshView.removeListFootView(); // �Ƴ����ظ����footview
			return;
		}
		listAdapter.addItems(reports);
		if (reports == null || reports.size() < DataSizePerPage) {
			Toast.makeText(index, R.string.loading_data_finished, Toast.LENGTH_SHORT).show();
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

	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		reports.clear();
		Log.i("a", "FragmentReadReport onDestroyView");
		
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.i("a", " FragmentReadReport onDetach");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i("a", "FragmentReadReport onAttach");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("a", "FragmentReadReport onResume");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i("a", "FragmentReadReport onPause");
	}

	
}
