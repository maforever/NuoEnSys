package com.chinaknown.nuoensys;

import java.util.ArrayList;

import com.chinaknown.nuoensys.adapter.GuideActivityPagerViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity {

	private ViewPager viewPager = null;
	private LinearLayout point_layout = null;
	private ArrayList<View> views = new ArrayList<View>();
	private LayoutInflater inflater = null;
	private View view = null;
	private ImageView[] guide_points = null;
	private ImageView point = null;
	private GuideActivityPagerViewAdapter adapter = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        findViews();
        initPagerViews();
        initPointLayout();
        
        adapter = new GuideActivityPagerViewAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				for(int i=0; i<guide_points.length; i++) {
					guide_points[i].setBackgroundResource(R.drawable.pagectr_inactive);
					if(arg0 == i) {
						guide_points[i].setBackgroundResource(R.drawable.pagectr_active);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
    }
    private void findViews() {
    	viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        point_layout = (LinearLayout) this.findViewById(R.id.point_layout);
    }
    private void initPagerViews() {
    	view = new View(this);
    	view.setBackgroundResource(R.drawable.guide1);
    	views.add(view);
    	view = new View(this);
    	view.setBackgroundResource(R.drawable.guide2);
    	views.add(view);
    	view = new View(this);
    	view.setBackgroundResource(R.drawable.guide3);
    	views.add(view);
    	view = new View(this);
    	view.setBackgroundResource(R.drawable.guide4);
    	views.add(view);
    	view = inflater.inflate(R.layout.activity_guide_lastpage, null);
    	views.add(view);
    	view.findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
				startActivity(intent);
				GuideActivity.this.finish();
				overridePendingTransition(R.anim.activity_up, R.anim.activity_steady);
			}
		});
    }
    
    private void initPointLayout() {
    	guide_points = new ImageView[views.size()];
    	for(int i=0; i<views.size(); i++) {
    		point = new ImageView(this);
    		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 20);
    		lp.setMargins(10, 0, 10, 0);
    		point.setLayoutParams(lp);
    		guide_points[i] = point;
    		if(i==0) {
    			point.setBackgroundResource(R.drawable.pagectr_active);
    		}else {
    			point.setBackgroundResource(R.drawable.pagectr_inactive);
    		}
    		point_layout.addView(point);
    	}
    }
}
