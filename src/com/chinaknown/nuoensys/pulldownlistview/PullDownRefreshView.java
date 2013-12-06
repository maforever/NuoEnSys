package com.chinaknown.nuoensys.pulldownlistview;


import com.chinaknown.nuoensys.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class PullDownRefreshView extends LinearLayout implements OnTouchListener {

	/**
	 * ����״̬
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * �ͷ�����ˢ��״̬
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * ����ˢ��״̬
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * ˢ����ɻ�δˢ��״̬
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;

	/**
	 * ����ͷ���ع����ٶ�
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * һ���ӵĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * һСʱ�ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * һ��ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * һ�µĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * һ��ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * �ϴθ���ʱ����ַ���������������ΪSharedPreferences�ļ�ֵ
	 */
	private static final String UPDATED_AT = "updated_at";

	/**
	 * ����ˢ�µĻص��ӿ�
	 */
	private pullToRefreshListener mListener;

	/**
	 * ���ڴ洢�ϴθ���ʱ��
	 */
	private SharedPreferences preferences;

	/**
	 * ����ͷ��View
	 */
	private View header;

	/**
	 * ��Ҫȥ����ˢ�µ�ListView
	 */
	public ListView listView;

	/**
	 * ˢ��ʱ��ʾ�Ľ�����
	 */
	private ProgressBar progressBar;

	/**
	 * ָʾ�������ͷŵļ�ͷ
	 */
	private ImageView arrow;

	/**
	 * ָʾ�������ͷŵ���������
	 */
	private TextView description;

	/**
	 * �ϴθ���ʱ�����������
	 */
	private TextView updateAt;

	/**
	 * ����ͷ�Ĳ��ֲ���
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * �ϴθ���ʱ��ĺ���ֵ
	 */
	private long lastUpdateTime;

	/**
	 * Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ��ʹ��id��������
	 */
	private int mId = -1;

	/**
	 * listview���������ĵ�һ��ɼ��������
	 */
	private int firstItem, visibleItem, totalItem;

	/**
	 * ����listview�������ؼ���
	 */
	private onLoadMoreListener mloadMoreListener;

	/**
	 * ���ó�ʼ���ݼ���״̬Ϊδ���
	 */
	public boolean misFinished = false;
	/**
	 * ���ó�ʼ���ݼ���״̬Ϊ����ˢ��
	 */
	public boolean misRefreshing = false;

	/**
	 * ����ͷ�ĸ߶�
	 */
	private int hideHeaderHeight;

	/**
	 * ��ǰ����ʲô״̬����ѡֵ��STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING �� STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = STATUS_REFRESH_FINISHED;;

	/**
	 * ��¼��һ�ε�״̬��ʲô����������ظ�����
	 */
	private int lastStatus = currentStatus;

	/**
	 * ��ָ����ʱ����Ļ������
	 */
	private float yDown;

	/**
	 * �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ������ֵ��
	 */
	private int touchSlop;

	/**
	 * �Ƿ��Ѽ��ع�һ��layout������onLayout�еĳ�ʼ��ֻ�����һ��
	 */
	private boolean loadOnce;

	/**
	 * ��ǰ�Ƿ����������ֻ��ListView������ͷ��ʱ�����������
	 */
	private boolean ableToPull;

	public int pageindex = 1;
	public int DataSizePerPage = 10;

	public View listFootView;
	public RefreshingTask refreshTask;

	/**
	 * ����ˢ�¿ؼ��Ĺ��캯������������ʱ��̬���һ������ͷ�Ĳ��֡�
	 * 
	 * @param context
	 * @param attrs
	 */

	private Context context;

	public PullDownRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (Activity) context;
		init();
	}

	public PullDownRefreshView(Context context) {
		super(context);
		this.context = (Activity) context;
		init();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		header = LayoutInflater.from(context).inflate(R.layout.pulldown_headview, null, true);
		progressBar = (ProgressBar) header.findViewById(R.id.pulldown_progress_bar);
		arrow = (ImageView) header.findViewById(R.id.pulldown_arrow);
		description = (TextView) header.findViewById(R.id.pulldown_description);
		updateAt = (TextView) header.findViewById(R.id.pulldown_updated_at);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		refreshUpdatedAtValue();
		setOrientation(VERTICAL);
		addView(header, 0);
	}

	/**
	 * ����һЩ�ؼ��Եĳ�ʼ�����������磺������ͷ����ƫ�ƽ������أ���ListViewע��touch�¼���
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			listView = (ListView) getChildAt(1);
			listView.setOnTouchListener(this);
			listView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if (isRefreshing())
						return;
					if (firstItem + visibleItem == totalItem && !misFinished && scrollState != SCROLL_STATE_FLING) {
						mloadMoreListener.onLoadMore();
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					if (isRefreshing())
						return;
					if (misFinished)
						return;
					firstItem = firstVisibleItem;
					visibleItem = visibleItemCount;
					totalItem = totalItemCount;
				}

			});
			loadOnce = true;
		}
	}

	/**
	 * ������ˢ�¿ؼ�ע��һ����������
	 * 
	 * @param loadMoreListener
	 *            ��������ʵ�֡�
	 */
	public void setOnLoadMoreListener(onLoadMoreListener loadMoreListener) {
		mloadMoreListener = loadMoreListener;
	}

	/**
	 * �������ݼ���״̬�Ƿ�������
	 * 
	 * @param isFinished
	 */
	public void setOnLoadState(boolean isFinished, boolean isRefreshing) {
		misFinished = isFinished;
		misRefreshing = isRefreshing;
	}

	/**
	 * ��������ˢ��״̬
	 * 
	 * @return
	 */
	public boolean getRefreshState() {
		return misRefreshing;
	}

	/**
	 * ����ˢ��ʱȥ���������ظ����listFootView
	 * 
	 * @return
	 */
	public boolean isRefreshing() {
		if (misRefreshing) {
			if (listFootView != null) {
				listView.removeFooterView(listFootView);
				listFootView = null;
			}
			return true;
		}
		return false;
	}

	/**
	 * ��ListView������ʱ���ã����д����˸�������ˢ�µľ����߼���
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		setIsAbleToPull(event);
		if (ableToPull) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float yMove = event.getRawY();
				int distance = (int) (yMove - yDown);
				// �����ָ���»�״̬����������ͷ����ȫ���صģ������������¼�
				if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != STATUS_REFRESHING) {
					if (headerLayoutParams.topMargin > 0) {
						currentStatus = STATUS_RELEASE_TO_REFRESH;
					} else {
						currentStatus = STATUS_PULL_TO_REFRESH;
					}
					// ͨ��ƫ������ͷ��topMarginֵ����ʵ������Ч��
					headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				break;
			case MotionEvent.ACTION_UP:
			default:
				if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
					// ����ʱ������ͷ�����ˢ��״̬����ȥ��������ˢ�µ�����
					refreshTask = new RefreshingTask();
					refreshTask.execute();
				} else if (currentStatus == STATUS_PULL_TO_REFRESH) {
					// ����ʱ���������״̬����ȥ������������ͷ������
					new HideHeaderTask().execute();
				}
				break;
			}
			// ʱ�̼ǵø�������ͷ�е���Ϣ
			if (currentStatus == STATUS_PULL_TO_REFRESH || currentStatus == STATUS_RELEASE_TO_REFRESH) {
				updateHeaderView();
				// ��ǰ�������������ͷ�״̬��Ҫ��ListViewʧȥ���㣬���򱻵������һ���һֱ����ѡ��״̬
				listView.setPressed(false);
				listView.setFocusable(false);
				listView.setFocusableInTouchMode(false);
				lastStatus = currentStatus;
				// ��ǰ�������������ͷ�״̬��ͨ������true���ε�ListView�Ĺ����¼�
				return true;
			}
		}
		return false;
	}

	/**
	 * ������ˢ�¿ؼ�ע��һ����������
	 * 
	 * @param listener
	 *            ��������ʵ�֡�
	 * @param id
	 *            Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ�� �벻ͬ������ע������ˢ�¼�����ʱһ��Ҫ���벻ͬ��id��
	 */
	public void setOnRefreshListener(pullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * �����е�ˢ���߼���ɺ󣬼�¼����һ�£��������ListView��һֱ��������ˢ��״̬��
	 */
	public void finishRefreshing() {
		currentStatus = STATUS_REFRESH_FINISHED;
		preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
		new HideHeaderTask().execute();
	}

	/**
	 * ���ݵ�ǰListView�Ĺ���״̬���趨 {@link #ableToPull}
	 * ��ֵ��ÿ�ζ���Ҫ��onTouch�е�һ��ִ�У����������жϳ���ǰӦ���ǹ���ListView������Ӧ�ý���������
	 * 
	 * @param event
	 */
	private void setIsAbleToPull(MotionEvent event) {
		View firstChild = listView.getChildAt(0);
		if (firstChild != null) {
			int firstVisiblePos = listView.getFirstVisiblePosition();
			if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
				if (!ableToPull) {
					yDown = event.getRawY();
				}
				// ����׸�Ԫ�ص��ϱ�Ե�����븸����ֵΪ0����˵��ListView���������������ʱӦ����������ˢ��
				ableToPull = true;
			} else {
				if (headerLayoutParams.topMargin != hideHeaderHeight) {
					headerLayoutParams.topMargin = hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				ableToPull = false;
			}
		} else {
			// ���ListView��û��Ԫ�أ�ҲӦ����������ˢ��
			ableToPull = true;
		}
	}

	/**
	 * ��������ͷ�е���Ϣ��
	 */
	private void updateHeaderView() {
		if (lastStatus != currentStatus) {
			if (currentStatus == STATUS_PULL_TO_REFRESH) {
				description.setText(R.string.pulldown_to_refresh);
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
				description.setText(R.string.pulldown_release_to_refresh);
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_REFRESHING) {
				description.setText(R.string.pulldown_refreshing);
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
			}
			refreshUpdatedAtValue();
		}
	}

	/**
	 * ���ݵ�ǰ��״̬����ת��ͷ��
	 */
	private void rotateArrow() {
		float pivotX = arrow.getWidth() / 2f;
		float pivotY = arrow.getHeight() / 2f;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		if (currentStatus == STATUS_PULL_TO_REFRESH) {
			fromDegrees = 180f;
			toDegrees = 360f;
		} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
			fromDegrees = 0f;
			toDegrees = 180f;
		}
		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		arrow.startAnimation(animation);
	}

	/**
	 * ˢ������ͷ���ϴθ���ʱ�������������
	 */
	private void refreshUpdatedAtValue() {
		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = getResources().getString(R.string.pulldown_not_updated_yet);
		} else if (timePassed < 0) {
			updateAtValue = getResources().getString(R.string.pulldown_time_error);
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = getResources().getString(R.string.pulldown_updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "����";
			updateAtValue = String.format(getResources().getString(R.string.pulldown_updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "Сʱ";
			updateAtValue = String.format(getResources().getString(R.string.pulldown_updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "��";
			updateAtValue = String.format(getResources().getString(R.string.pulldown_updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "����";
			updateAtValue = String.format(getResources().getString(R.string.pulldown_updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "��";
			updateAtValue = String.format(getResources().getString(R.string.pulldown_updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}

	/**
	 * ����ˢ�µ������ڴ������л�ȥ�ص�ע�����������ˢ�¼�������
	 * 
	 * @author wisdomhu
	 */
	class RefreshingTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (mListener != null) {
				mListener.onRefresh();
			}
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= 0) {
					topMargin = 0;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			currentStatus = STATUS_REFRESHING;
			publishProgress(0);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			updateHeaderView();
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

	}

	/**
	 * ��������ͷ�����񣬵�δ��������ˢ�»�����ˢ����ɺ󣬴����񽫻�ʹ����ͷ�������ء�
	 * 
	 * @author wisdomhu
	 */
	class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= hideHeaderHeight) {
					topMargin = hideHeaderHeight;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			return topMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer topMargin) {
			headerLayoutParams.topMargin = topMargin;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
		}
	}

	/**
	 * ʹ��ǰ�߳�˯��ָ���ĺ�������
	 * 
	 * @param time
	 *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ˢ�µļ�������ʹ������ˢ�µĵط�Ӧ��ע��˼���������ȡˢ�»ص���
	 * 
	 * @author wisdomhu
	 */
	public interface pullToRefreshListener {

		/**
		 * ˢ��ʱ��ȥ�ص��˷������ڷ����ڱ�д�����ˢ���߼���ע��˷����������߳��е��õģ� ����Բ������߳������к�ʱ������
		 */
		void onRefresh();

	}

	/**
	 * ����ˢ�µļ�������ʹ������ˢ�µĵط�Ӧ��ע��˼���������ȡˢ�»ص���
	 * 
	 * @author wisdomhu
	 * 
	 */
	public interface onLoadMoreListener {
		void onLoadMore();
	}

	/**
	 * ��ʼ��listview��footview�ؼ�
	 */
	public void initListFootView(BaseAdapter adapter) {
		if (listFootView != null)
			return;
		listFootView = ((Activity) context).getLayoutInflater().inflate(R.layout.loading_foot_view, null);
		listView.addFooterView(listFootView);
		listView.setAdapter(adapter);
	}

	/**
	 * �Ƴ�listview��footview�ؼ�
	 */
	public void removeListFootView() {
		if (listFootView != null) {
			listView.removeFooterView(listFootView);
			listFootView = null;
		}
		setOnLoadState(true, false);
	}
}