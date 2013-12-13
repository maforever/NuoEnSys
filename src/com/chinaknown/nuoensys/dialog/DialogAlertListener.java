package com.chinaknown.nuoensys.dialog;

import android.app.Dialog;

public interface DialogAlertListener
{
	public void onDialogCreate(Dialog dlg, Object param);

	public void onDialogOk(Dialog dlg, Object param);

	public void onDialogCancel(Dialog dlg, Object param);
	
	public void onDialogSave(Dialog dlg, Object param);
	
	public void onDialogUnSave(Dialog dlg, Object param);
}
