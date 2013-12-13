package com.chinaknown.nuoensys;

import java.util.ArrayList;

import com.chinaknown.nuoensys.adapter.MyFragmentViewPagerAdapter;
import com.chinaknown.nuoensys.fragments.FragmentReadReport;
import com.chinaknown.nuoensys.fragments.FragmentSendedReport;
import com.chinaknown.nuoensys.fragments.FragmentWriteReport;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ReportIndexActivity extends FragmentActivity {
	private ViewPager pager;
	private ImageView iv_bottom_line;
	private LinearLayout layout_write, layout_sended, layout_read;
	public static ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private FragmentManager fm = null;
	private Fragment fragment = null;
	private int currIndex = 0;
	private int bottomLineWidth, offset, position_one, position_two ,position_three;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_report);
		fm = this.getSupportFragmentManager();
		findViews();
		initViewPager();
		initWidth();
	}
	
	private void findViews() {
		pager = (ViewPager) this.findViewById(R.id.viewPager);
		pager.setOffscreenPageLimit(0);
		iv_bottom_line = (ImageView) this.findViewById(R.id.iv_bottom_line);
		layout_write = (LinearLayout) this.findViewById(R.id.writeLayout);
		layout_sended = (LinearLayout) this.findViewById(R.id.sendedLayout);
		layout_read = (LinearLayout) this.findViewById(R.id.readLayout);
		
		layout_write.setOnClickListener(new OnClickListenerImpl(0));
		layout_sended.setOnClickListener(new OnClickListenerImpl(1));
		layout_read.setOnClickListener(new OnClickListenerImpl(2));
	}
	
	private void initViewPager() {
		
		Fragment writeFragment = new FragmentWriteReport();
		Fragment sendedFragment = new FragmentSendedReport();
		Fragment readFragment = new FragmentReadReport();
		fragments.add(writeFragment);
		fragments.add(sendedFragment);
		fragments.add(readFragment);
		
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(new MyFragmentViewPagerAdapter(fm, fragments));
		pager.setOnPageChangeListener(new OnPageChangerListenerImpl());
		pager.setCurrentItem(currIndex);
	}
	
	
	private class OnPageChangerListenerImpl implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one , 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, 0, 0, 0);
                }
				break;
			case 1:
				if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
                } 
				break;
			case 2:
				if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
                }
				break;
			}
			
			currIndex = arg0;
//			Log.i("a", "currIndex = " + currIndex);
			animation.setFillAfter(true);
			animation.setDuration(300);
			iv_bottom_line.startAnimation(animation);
			
		}
	}
	
    private void initWidth() {
        bottomLineWidth = iv_bottom_line.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        
		LayoutParams params = new LayoutParams(screenW / 3,
				LinearLayout.LayoutParams.MATCH_PARENT);
		iv_bottom_line.setLayoutParams(params);
        
        offset = 0;
//        Log.i("a", "offset=" + offset);

        position_one = (int) (screenW / 3.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
//        Log.i("a", "screeW = " + screenW + " screenW / 3.0 = " + screenW / 3.0 + " bottomLineWidth / 2 = " + bottomLineWidth / 2);
//        Log.i("a", "position_one = " + position_one + " position_two = " + position_two + " position_three = " + position_three );
    }
    
	private class OnClickListenerImpl implements  OnClickListener {
		private int index = 0;
		public OnClickListenerImpl(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {	
			pager.setCurrentItem(index);
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
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
		}
	}
}
