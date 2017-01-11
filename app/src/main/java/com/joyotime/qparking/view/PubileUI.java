package com.joyotime.qparking.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.ansai.uparking.R;


public class PubileUI
{
	public static Dialog MsgDialog(Context context,String msg)
	{
		Dialog progressDialog=null;
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.dialog);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		TextView msgshow = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
		msgshow.setText(msg);
		return progressDialog;
	}
	public static Dialog PopuplayerDialog(Context context)
	{
		Dialog progressDialog=null;
		progressDialog = new Dialog(context, R.style.progress_dialog);
		progressDialog.setContentView(R.layout.popdialog);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		return progressDialog;
	}
}
