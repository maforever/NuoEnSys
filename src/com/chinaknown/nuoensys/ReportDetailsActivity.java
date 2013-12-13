package com.chinaknown.nuoensys;

import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.model.Report;
import com.chinaknown.nuoensys.utils.URLAddress;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class ReportDetailsActivity extends Activity {
	private TextView titleTv, creatorTv,departmentTv, dutyTv, receiverTv, createDateTv;
	private WebView wv;
	private Report r;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_reportdetails);
		findViews();
		
		r = (Report) this.getIntent().getSerializableExtra("r");
		initViews();
	}
	
	private void findViews() {
		titleTv = (TextView) this.findViewById(R.id.title);
		creatorTv = (TextView) this.findViewById(R.id.creator);
		receiverTv = (TextView) this.findViewById(R.id.receiver);
		createDateTv = (TextView) this.findViewById(R.id.createDate);
		departmentTv = (TextView) this.findViewById(R.id.department);
		dutyTv = (TextView) this.findViewById(R.id.duty);
		wv = (WebView) this.findViewById(R.id.webView);
	}
	
	private void initViews() {
		titleTv.setText(r.getTitle());
		creatorTv.setText(r.getCreator());
		departmentTv.setText(r.getDepartment());
		dutyTv.setText(r.getDuty());
		receiverTv.setText(r.getToName());
		createDateTv.setText(r.getCreateDate());
		String srcContent = r.getContent();
		srcContent = srcContent.replace("/NuoEnSys", URLAddress.BASIC_URL + "/NuoEnSys");
		String content = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/></head><body padding:0; margin: 0px>"+ srcContent +"</body></html>";
		Log.i("a", "html = " + content);
		wv.loadDataWithBaseURL("about:blank", content, "text/html", "utf-8", null);
	}
	
	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
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
}



















