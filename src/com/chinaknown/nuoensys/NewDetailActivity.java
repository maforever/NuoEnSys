package com.chinaknown.nuoensys;

import com.chinaknown.nuoensys.model.CompanyNew;
import com.chinaknown.nuoensys.utils.URLAddress;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

public class NewDetailActivity extends Activity {
	private TextView titleTv, createDateTv;//, contentTv;
	private WebView wv;
	private CompanyNew cn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_newdetails);
		
		findViews();
		cn = (CompanyNew) this.getIntent().getSerializableExtra("cn");
		initViews();
	}
	
	private void findViews() {
		titleTv = (TextView) this.findViewById(R.id.title);
		createDateTv = (TextView) this.findViewById(R.id.createDate);
//		contentTv = (TextView) this.findViewById(R.id.content);
		wv = (WebView) this.findViewById(R.id.content);
		WebSettings settings = wv.getSettings(); 
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);  
	}
	
	private void initViews() {
		titleTv.setText(cn.getTitle());
		createDateTv.setText(cn.getCreateDate());
		String srcContent = cn.getContent();
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
