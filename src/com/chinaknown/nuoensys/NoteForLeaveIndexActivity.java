package com.chinaknown.nuoensys;

import com.chinaknown.nuoensys.adapter.NoteForLeaveIndexGridViewAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class NoteForLeaveIndexActivity extends Activity {
	private TranslateAnimation left, right;
	private ImageView runImage;
	private ImageView leftmenuHandler = null;
	private TextView tv_page = null;
	private String[] firstPageTitle = new String[] { "写假条", "申请中的假条", "批准的假条",
			"拒绝的假条" };
	private String[] secondPageTitle = new String[] { "申请列表", "批准过的假条", "拒绝过的假条",
			"预留" };
	private int[] firstPageImageIds = new int[] { R.drawable.btn_setting,
			R.drawable.btn_setting, R.drawable.btn_setting,
			R.drawable.btn_setting };
	private int[] secondPageImageIds = new int[] { R.drawable.btn_setting,
			R.drawable.btn_setting, R.drawable.btn_setting,
			R.drawable.btn_setting };
	private NoteForLeaveIndexGridViewAdapter firstAdapter, secondAdapter;
	private ViewFlipper viewFlipper = null;
	private GridView firstGridView, secondGridView = null;
	private Animation flipperLeftInAnim, flipperLeftOutAnim,
			flipperRightInAnim, flipperRightOutAnim = null;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_noteforleave_index);
		findViews();
		runAnimation();
		
		GridViewAdapter();
		
	}

	private void findViews() {
		runImage = (ImageView) this.findViewById(R.id.run_image);
		tv_page = (TextView) this.findViewById(R.id.tv_page);
		viewFlipper = (ViewFlipper) this.findViewById(R.id.views);
		viewFlipper.setFocusable(true);
		firstGridView = (GridView) this.findViewById(R.id.firstGridView);
		secondGridView = (GridView) this.findViewById(R.id.secondGridView);

		firstGridView.setOnTouchListener(new FlipperOnTouchListener());
		secondGridView.setOnTouchListener(new FlipperOnTouchListener());
		viewFlipper.setOnTouchListener(new FlipperOnTouchListener());

		firstGridView.setOnItemClickListener(new MyOnItemClickListener1());
		secondGridView.setOnItemClickListener(new MyOnItemClickListener2());

		leftmenuHandler = (ImageView) this.findViewById(R.id.handler);
		leftmenuHandler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});
	}
	
	private void GridViewAdapter() {
		firstAdapter = new NoteForLeaveIndexGridViewAdapter(NoteForLeaveIndexActivity.this, firstPageTitle, firstPageImageIds);
		secondAdapter = new NoteForLeaveIndexGridViewAdapter(NoteForLeaveIndexActivity.this, secondPageTitle, secondPageImageIds);
		firstGridView.setAdapter(firstAdapter);
		secondGridView.setAdapter(secondAdapter);
	}

	public void runAnimation() {

		flipperLeftInAnim = AnimationUtils.loadAnimation(
				NoteForLeaveIndexActivity.this, R.anim.filpper_left_in);
		flipperLeftOutAnim = AnimationUtils.loadAnimation(
				NoteForLeaveIndexActivity.this, R.anim.flipper_left_out);
		flipperRightInAnim = AnimationUtils.loadAnimation(
				NoteForLeaveIndexActivity.this, R.anim.flipper_right_in);
		flipperRightOutAnim = AnimationUtils.loadAnimation(
				NoteForLeaveIndexActivity.this, R.anim.flipper_right_out);

		right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f, Animation.RELATIVE_TO_PARENT, 0f);
		right.setDuration(25000);
		left.setDuration(25000);
		right.setFillAfter(true);
		left.setFillAfter(true);

		right.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(left);
			}
		});
		left.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(right);
			}
		});
		runImage.startAnimation(right);
	}

	private class FlipperOnTouchListener implements OnTouchListener {
		float x1 = 0f, x2 = 0f;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x1 = event.getX();
				break;
			case MotionEvent.ACTION_UP:
				x2 = event.getX();
				if (x1 - x2 < -50) {
					if (viewFlipper.getDisplayedChild() != 0) {
						viewFlipper.setInAnimation(flipperLeftInAnim);
						viewFlipper.setOutAnimation(flipperLeftOutAnim);
						viewFlipper.showPrevious();
						tv_page.setText(1 + "");
						tv_page.startAnimation(AnimationUtils.loadAnimation(
								NoteForLeaveIndexActivity.this,
								R.anim.scale_out));
					}
					return true;
					// Log.i("a", "viewFlipper childId = " +
					// viewFlipper.getDisplayedChild());
				} else if (x1 - x2 > 50) {
					if (viewFlipper.getDisplayedChild() != 1) {
						viewFlipper.setInAnimation(flipperRightInAnim);
						viewFlipper.setOutAnimation(flipperRightOutAnim);
						viewFlipper.showNext();
						tv_page.setText(2 + "");
						tv_page.startAnimation(AnimationUtils.loadAnimation(
								NoteForLeaveIndexActivity.this,
								R.anim.scale_out));
					}
					// Log.i("a", "viewFlipper childId = " +
					// viewFlipper.getDisplayedChild());
					return true;
				}
				break;
			}
			return false;
		}
	}

	private class MyOnItemClickListener1 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
//				Toast.makeText(NoteForLeaveIndexActivity.this,
//						firstPageTitle[position], 0).show();
				intent = new Intent(NoteForLeaveIndexActivity.this, NoteForLeaveInputActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				break;
			case 1:
//				Toast.makeText(NoteForLeaveIndexActivity.this,
//						firstPageTitle[position], 0).show();
				
				intent = new Intent(NoteForLeaveIndexActivity.this, NoteForLeaveApplyActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				
				break;
			case 2:
				Toast.makeText(NoteForLeaveIndexActivity.this,
						firstPageTitle[position], 0).show();
				break;
			case 3:
				Toast.makeText(NoteForLeaveIndexActivity.this,
						firstPageTitle[position], 0).show();
				break;
			}
		}
	}

	private class MyOnItemClickListener2 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0:
//				Toast.makeText(NoteForLeaveIndexActivity.this,
//						secondPageTitle[position], 0).show();
				intent = new Intent(NoteForLeaveIndexActivity.this, NoteForLeaveHandleListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
				break;
			case 1:
				Toast.makeText(NoteForLeaveIndexActivity.this,
						secondPageTitle[position], 0).show();
				break;
			case 2:
				Toast.makeText(NoteForLeaveIndexActivity.this,
						secondPageTitle[position], 0).show();
				break;
			case 3:
				Toast.makeText(NoteForLeaveIndexActivity.this,
						secondPageTitle[position], 0).show();
				break;
			}
		}
	}
	
	private void back() {
		NoteForLeaveIndexActivity.this.finish();
		overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			back();
		}
		return true;
	}
}
