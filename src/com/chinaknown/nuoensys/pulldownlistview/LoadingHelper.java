package com.chinaknown.nuoensys.pulldownlistview;


import com.chinaknown.nuoensys.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingHelper {

	LinearLayout promptLayout;
	ProgressBar promptProgress;
	TextView promptText;
	ImageView promptRefresh;

	// 数据为空时加载的view
	LinearLayout emptyLayout;
	TextView emptyText;
	ImageView emptyRetryBtn;

	enum State {
		Loading, EMPTY, Error, Hide,
	}

	State cuState;
	LoadingListener mlistener;

	public void SetListener(LoadingListener listener) {
		mlistener = listener;
	}

	public void SetLoadingTxt(String info) {
		promptText.setText(info);
	}

	
	public LoadingHelper(Context context, View rootView, View emptyView) {
		promptLayout = (LinearLayout) rootView.findViewById(R.id.loading_prompt_linear);
		promptProgress = (ProgressBar) rootView.findViewById(R.id.loading_prompt_progress);
		promptText = (TextView) rootView.findViewById(R.id.loading_prompt_text);
		promptRefresh = (ImageView) rootView.findViewById(R.id.prompt_refresh);
		emptyLayout = (LinearLayout) emptyView.findViewById(R.id.loading_empty_prompt_linear);
		emptyText = (TextView) emptyView.findViewById(R.id.loading_empty_text);
		emptyRetryBtn = (ImageView) emptyView.findViewById(R.id.loading_empty_refresh);
		emptyRetryBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cuState == State.EMPTY) {
					if (mlistener != null) {
						mlistener.OnRetryClick();
					}
				}
			}
		});

		promptLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (cuState == State.EMPTY) {
					if (mlistener != null) {
						mlistener.OnRetryClick();
						return;
					}
				}
				if (cuState != State.Error) {
					return;
				}

				if (mlistener != null) {
					// 重试按钮
					mlistener.OnRetryClick();
				}
			}
		});
		HideLoading(4);

	}

	public void ShowLoading() {
		cuState = State.Loading;
		emptyLayout.setVisibility(View.GONE);
		promptLayout.setVisibility(View.VISIBLE);
		promptText.setText(R.string.loading_show_start);
		promptProgress.setVisibility(View.VISIBLE);
		promptRefresh.setVisibility(View.INVISIBLE);

	}

	public void ShowError(String info) {
		cuState = State.Error;
		promptLayout.setVisibility(View.VISIBLE);
		promptText.setText(info);
		promptRefresh.setVisibility(View.VISIBLE);
		promptProgress.setVisibility(View.INVISIBLE);
	}

	public void ShowEmptyData() {
		cuState = State.EMPTY;
		emptyLayout.setVisibility(View.VISIBLE);
		emptyText.setText(R.string.loading_show_empty);
	}

	/**
	 * 4为INVISIBLE,8为GONE
	 * 
	 * @param hide
	 */
	public void HideLoading(int hide) {
		cuState = State.Hide;
		promptLayout.setVisibility(hide);
	}

}
