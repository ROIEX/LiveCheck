package com.danmatrix.livechecker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddDialog extends Dialog{
	public interface ReadyListener{
		public void ready(String name, int selection);
	}
	private ReadyListener readyListener;
	EditText etName;
	Spinner ChannelSpinner;
	String name;
	
	public AddDialog(Context context, String name,ReadyListener readyListener){
		super(context);
		this.name=name;
		this.readyListener=readyListener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adddialog);
		setTitle(R.string.addchannel);
		Button okbutton=(Button)findViewById(R.id.okbutton);
		okbutton.setOnClickListener(new OKListener());
		Button cancelButton=(Button)findViewById(R.id.cancelbutton);
		cancelButton.setOnClickListener(new CancelListener());
		etName=(EditText)findViewById(R.id.editText1);
		ChannelSpinner=(Spinner)findViewById(R.id.ChannelSpinner);
	}
	
	private class OKListener implements View.OnClickListener{
		@Override
		public void onClick(View v){
			readyListener.ready(String.valueOf(etName.getText()),ChannelSpinner.getSelectedItemPosition() );
			AddDialog.this.dismiss();
		}
	}
	
	private class CancelListener implements View.OnClickListener{
		@Override
		public void onClick(View v){
			AddDialog.this.dismiss();
		}
	}
}