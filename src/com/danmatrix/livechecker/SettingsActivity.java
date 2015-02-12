package com.danmatrix.livechecker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class SettingsActivity extends Activity {
	boolean isLive=false;
	int interval=0;
	CheckBox cbLive;
	Spinner intervalspinner;
	Button saveButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		cbLive=(CheckBox)findViewById(R.id.onlyLiveBox);
		intervalspinner=(Spinner)findViewById(R.id.IntervalSpinner);
		saveButton=(Button)findViewById(R.id.savebutton);
		saveButton.setOnClickListener(new SaveButtonListener());
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			cbLive.setChecked(extras.getBoolean("isLive"));
			interval=extras.getInt("updateinterval");
			if(interval==60){
				intervalspinner.setSelection(0);
			}else if(interval==300){
				intervalspinner.setSelection(1);
			}else if(interval==600){
				intervalspinner.setSelection(2);
			}else if(interval==1800){
				intervalspinner.setSelection(3);
			}else if(interval==3600){
				intervalspinner.setSelection(4);
			}
		}
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public class SaveButtonListener implements View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.putExtra("isLive", cbLive.isChecked());
			intent.putExtra("updateinterval", intervalspinner.getSelectedItemPosition());
			setResult(RESULT_OK, intent);
			finish();
		}
		
	}
}


/*
 			Intent intent=new Intent();
			intent.putExtra("isLive", cbLive.isChecked());
			intent.putExtra("updateinterval", intervalspinner.getSelectedItemPosition());
			setResult(RESULT_OK, intent);
 */
