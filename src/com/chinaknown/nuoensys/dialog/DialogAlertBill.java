package com.chinaknown.nuoensys.dialog;

import com.chinaknown.nuoensys.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogAlertBill extends Dialog implements OnClickListener{

	private TextView btnOk;
	private TextView btnCancel;
	private DialogAlertListener listener;
	private String title;
	private Object param;

	public DialogAlertBill(Context context, DialogAlertListener listener, String title)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.title = title;
	}

	public DialogAlertBill(Context context, DialogAlertListener listener, String title, Object param)
	{
		super(context, R.style.dialog);
		this.listener = listener;
		this.title = title;
		this.param = param;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_sendedreport);
		setCancelable(false);
		setCanceledOnTouchOutside(false);

		btnOk = (TextView) findViewById(R.id.sure);
		btnOk.setOnClickListener(this);
		btnCancel = (TextView) findViewById(R.id.cancel);
		btnCancel.setOnClickListener(this);

		TextView txtTitle = (TextView) findViewById(R.id.title);
		txtTitle.setText(title);

		if (listener != null)
		{
			listener.onDialogCreate(this, param);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v == btnOk)
			onBtnOk();
		else if (v == btnCancel)
			onBtnCancel();
	}

	private void onBtnOk()
	{
		cancel();
		if (listener != null)
		{
			listener.onDialogOk(this, param);
		}
	}

	private void onBtnCancel()
	{
		cancel();
		if (listener != null)
		{
			listener.onDialogCancel(this, param);
		}
	}
}
