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
	// Ч��
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
		
		// ��ָ����С����һ��͸����32λλͼ������������һ��canvas����
		sCoverBitmap = Bitmap.createBitmap(
				activity.findViewById(id).getWidth(), activity.findViewById(id)
						.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(sCoverBitmap);
		// ��ָ����view��������view��Ⱦ�����ֻ����ϣ����������һ��activity���ֵ�һ�����գ��������bitmap�Ͼ�����һ��activity�Ŀ���
		activity.findViewById(id).draw(canvas);
		indexActivity = (ReportIndexActivity) activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_reportreceiverlistselect);
		//���ñ���
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

		// �����˾��Բ��֣����Բ�����view�����ϽǴ�(0,0)��ʼ
		@SuppressWarnings("deprecation")
		final android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);

		findViewById(R.id.slideout_placeholder).setLayoutParams(lp);

		// ��Ļ�Ŀ��
		int displayWidth = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		// �ұߵ�λ������60dpת����px
		int sWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, -60, getResources()
						.getDisplayMetrics());
		// �����������ƶ���ƫ����
		final int shift = displayWidth + sWidth;

		// �����ƶ���λ�ƶ��������ƶ�shift���룬y���򲻱�
		// mStartAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
		// 0, TranslateAnimation.ABSOLUTE, shift,
		// TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		mStartAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
				0, TranslateAnimation.ABSOLUTE, -shift,
				TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		// ����ʱ��λ�ƶ���
		// mStopAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
		// 0,
		// TranslateAnimation.ABSOLUTE, -shift,
		// TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		mStopAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, +shift,
				TranslateAnimation.ABSOLUTE, 0, TranslateAnimation.ABSOLUTE, 0);

		// ����ʱ��
		mStartAnimation.setDuration(DURATION_MS);
		// �������ʱͣ���ڽ���λ��
		mStartAnimation.setFillAfter(true);
		mStartAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				// ��������ʱ�ص�
				// ��imageview�̶���λ�ƺ��λ��
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
