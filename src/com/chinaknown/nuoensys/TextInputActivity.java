package com.chinaknown.nuoensys;


import com.chinaknown.nuoensys.utils.AppConstants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TextInputActivity extends Activity {
	private Intent intent;
	private TextView titleTv, editNumTv;
	private EditText contentEt;
	private String title;
	private int resultCode = 0;
	private int editNum = 0; // 可编辑的文本字数
	private String content; // 输入的文本内容
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_text_input);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		findViews();
		initDatas();
		initViews();

	}

	private void findViews() {
		titleTv = (TextView) this.findViewById(R.id.title);
		editNumTv = (TextView) this.findViewById(R.id.editNum);
		contentEt = (EditText) this.findViewById(R.id.content);
		contentEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Log.i("a", "count = " + count);
				content = contentEt.getText().toString();
				if (content.length() <= editNum) {
					editNumTv.setText((editNum - content.length()) + "");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void initDatas() {
		title = this.getIntent().getStringExtra("title");
		resultCode = this.getIntent().getIntExtra("resultCode", 0);
		editNum = this.getIntent().getIntExtra("editNum", 0);
		content = this.getIntent().getStringExtra("content");
	}

	private void initViews() {
		titleTv.setText(title);
		contentEt.setText(content);
		int contentLength = content.length();
		Log.i("a", "contentLength = " + contentLength);
		editNumTv.setText((editNum - contentLength) + "");

		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter.LengthFilter(editNum);
		contentEt.setFilters(filters);

	}

	public void btnClick(View view) {
		switch (view.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.ok:
			content = contentEt.getText().toString();
			if (content == null || "".equals(content)) {
				Toast.makeText(TextInputActivity.this, "您还没有输入任何内容,无法保存", 0)
						.show();
				return;
			}

			intent = this.getIntent();
			intent.putExtra("content", content);
			this.setResult(resultCode, intent);
			this.finish();
			overridePendingTransition(R.anim.activity_steady,
					R.anim.activity_down);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			back();
			break;
		}
		return true;
	}

	private void back() {
		intent = new Intent();
		this.setResult(AppConstants.EDIT_NOCHANGE_BACK, intent);
		this.finish();
		overridePendingTransition(R.anim.activity_steady, R.anim.activity_down);
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
