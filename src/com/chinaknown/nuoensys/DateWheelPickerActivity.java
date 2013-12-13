package com.chinaknown.nuoensys;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinaknown.nuoensys.utils.AppConstants;
import com.chinaknown.nuoensys.wheeldatepicker.NumericWheelAdapter;
import com.chinaknown.nuoensys.wheeldatepicker.OnWheelChangedListener;
import com.chinaknown.nuoensys.wheeldatepicker.WheelView;

public class DateWheelPickerActivity extends Activity {
	private static int START_YEAR = 1990, END_YEAR = 2100;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	private Calendar calendar;
	String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
	String[] months_little = { "4", "6", "9", "11" };
	private TextView btn_sure,btn_cancel;
	final List<String> list_big = Arrays.asList(months_big);
	final List<String> list_little = Arrays.asList(months_little);
	private int resultCode = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.datewheelpicker_layout);
		
		resultCode = this.getIntent().getIntExtra("resultCode", 0);
		
		findView();
		adjustView();
		setListener();
		setDate();
	}
	
	
	private void findView() {
		// TODO Auto-generated method stub
		// 年
		wv_year = (WheelView) findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setLabel("年");// 添加文字

		// 月
		wv_month = (WheelView) findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setLabel("月");

		// 日
		
		wv_day = (WheelView) findViewById(R.id.day);
		// 判断大小月及是否闰年,用来确定"日"的数据
		wv_day.setLabel("日");

		// 时
		wv_hours = (WheelView) findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setLabel("时");

		// 分
		wv_mins = (WheelView) findViewById(R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setLabel("分");

		btn_sure = (TextView) findViewById(R.id.sure);
		btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = getSetCalendar();
				String dateStr = getFormatTime(c);
				String dateYMD = getFormatYMD(c);
				Intent intent = new Intent();
				intent.putExtra("date", dateStr);
				intent.putExtra("dateYMD", dateYMD);
				DateWheelPickerActivity.this.setResult(resultCode, intent);
				DateWheelPickerActivity.this.finish();
			}
		});
		btn_cancel = (TextView) findViewById(R.id.cancel);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DateWheelPickerActivity.this.finish();
			}
		});
	}
	
	private void adjustView() {
		// TODO Auto-generated method stub
		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;

		textSize = pixelsToDip(this.getResources(), 13);
		wv_day.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
	}
	
	public static int pixelsToDip(Resources res, int pixels) {
		final float scale = res.getDisplayMetrics().density;
		return (int) (pixels * scale + 0.5f);
	}

	
	
	
	public void setDate() {
		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		wv_month.setCurrentItem(month);
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setCurrentItem(day - 1);
		wv_hours.setCurrentItem(hour);
		wv_mins.setCurrentItem(minute);
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
	}
	
	
	// 添加"年"监听
	private final OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			int year_num = newValue + START_YEAR;
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big
					.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(wv_month
					.getCurrentItem() + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				if ((year_num % 4 == 0 && year_num % 100 != 0)
						|| year_num % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
		}
	};
	// 添加"月"监听
	private final OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			int month_num = newValue + 1;
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month_num))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month_num))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
						.getCurrentItem() + START_YEAR) % 100 != 0)
						|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
		}
	};
	
	public Calendar getSetCalendar() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.set(wv_year.getCurrentItem() + START_YEAR, wv_month.getCurrentItem(),
				wv_day.getCurrentItem() + 1, wv_hours.getCurrentItem(),
				wv_mins.getCurrentItem());
		return c;
	}
	
	public static String getFormatTime(Calendar c) {
		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);
		// 设置日期的显示
		Calendar calendar = c;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return year + "-" + decimal.format(month + 1) + "-"
				+ decimal.format(day) + " " + decimal.format(hour) + ":"
				+ decimal.format(minute);

	}
	
	
	public static String getFormatYMD(Calendar c) {
		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);
		// 设置日期的显示
		Calendar calendar = c;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return year + "-" + decimal.format(month + 1) + "-"
				+ decimal.format(day);

	}
}
