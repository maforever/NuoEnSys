package com.chinaknown.nuoensys;

import java.util.List;

import com.chinaknown.nuoensys.adapter.ReportReceiverListAdapter;
import com.chinaknown.nuoensys.fragments.FragmentWriteReport;
import com.chinaknown.nuoensys.model.Employee;
import com.chinaknown.nuoensys.utils.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ReportReceiverSelectedActivity extends Activity {

	private TextView titleNameTv;
	public static String value;
	private ListView listView;
	// 效果
	private ImageView mCover;
	private Animation mStartAnimation;
	private Animation mStopAnimation;
	private static final int DURATION_MS = 400;
	private static Bitmap sCoverBitmap = null;
	private static ReportIndexActivity indexActivity;
	
	private List<Employee> employees;
	private ReportReceiverListAdapter adapter;
	private Employee receiver;
	
	public static void prepare(Activity activity, int id) {
		if (sCoverBitmap != null) {
			sCoverBitmap.recycle();
		}
		
		// 用指定大小生成一张透明的32位位图，并用它构建一张canvas画布
		sCoverBitmap = Bitmap.createBitmap(
				activity.findViewById(id).getWidth(), activity.findViewById(id)
						.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(sCoverBitmap);
		// 将指定的view包括其子view渲染到这种画布上，在这就是上一个activity布局的一个快照，现在这个bitmap上就是上一个activity的快照
		activity.findViewById(id).draw(canvas);
		indexActivity = (ReportIndexActivity) activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_reportreceiverlistselect);
		//设置标题
		titleNameTv = (TextView) this.findViewById(R.id.titleName);
		String titleNameStr = this.getIntent().getStringExtra("titleName");
		titleNameTv.setText(titleNameStr);
		
		MyApplication mApp = (MyApplication) this.getApplicationContext();
		employees = mApp.getReportReceivers();
		
//		for(Employee e : employees) {
//			Log.i("a", "android " + e.toString());
//		}
		
		
		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new ReportReceiverListAdapter(this, employees, R.layout.reportreceiver_list_item);
		listView.setAdapter(adapter);
		adapter.setSelected(0);
		receiver = employees.get(0);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setSelected(position);
				receiver = employees.get(position);
			}
		});
		
		
		initAnim();
		mCover = (ImageView) findViewById(R.id.slidedout_cover);
		mCover.setImageBitmap(sCoverBitmap);
		mCover.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				close();
			}
		});

		open();
	}

	public void initAnim() {

		// 采用了绝对布局，绝对布局是view的左上角从(0,0)开始
		@SuppressWarnings("deprecation")
		final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);

		findViewById(R.id.slideout_placeholder).setLayoutParams(lp);

		// 屏幕的宽度
		int displayWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		// 右边的位移量，60dp转换成px
		int sWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, -60, getResources()
						.getDisplayMetrics());
		// 将快照向右移动的偏移量
		final int shift = displayWidth + sWidth;

		// 向右移动的位移动画向右移动shift距离，y方向不变
		// mStartAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
		// 0, TranslateAnimation.ABSOLUTE, shift,
		// TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		mStartAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
				0, TranslateAnimation.ABSOLUTE, -shift,
				TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		// 回退时的位移动画
		// mStopAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
		// 0,
		// TranslateAnimation.ABSOLUTE, -shift,
		// TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		mStopAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, +shift,
				TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		// 持续时间
		mStartAnimation.setDuration(DURATION_MS);
		// 动画完成时停留在结束位置
		mStartAnimation.setFillAfter(true);
		mStartAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				// 动画结束时回调
				// 将imageview固定在位移后的位置
				mCover.setAnimation(null);
				@SuppressWarnings("deprecation")
				final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,
						-shift, 0);
				mCover.setLayoutParams(lp);
			}
		});

		mStopAnimation.setDuration(DURATION_MS);
		mStopAnimation.setFillAfter(true);
		mStopAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				finish();
				overridePendingTransition(0, 0);
			}
		});

	}

	public void open() {
		mCover.startAnimation(mStartAnimation);
	}

	public void close() {
		FragmentWriteReport fwr = (FragmentWriteReport) indexActivity.fragments.get(0);
		fwr.sendToTv.setText(receiver.getDuty()+"-"+receiver.getRealName());
		fwr.toEmployee = receiver;
		mCover.startAnimation(mStopAnimation);
	}
	
	
	
}
