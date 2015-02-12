package com.danmatrix.livechecker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.google.android.gms.ads.*;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.ListActivity;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;


public class MainActivity extends ListActivity {
	StreamModel obj;
	ProgressBar pBar;
	ArrayList<StreamModel> list=new ArrayList<StreamModel>();
	ArrayList<StreamModel> displayedlist=new ArrayList<StreamModel>();
	StreamAdapter adapter;
	SharedPreferences mSettings;
	int updateinterval=60;
	boolean onlyLive=false;
	boolean DeleteMode=false;
	Handler mainTimerHandler=new Handler();
	Runnable mainTimerRunnable=new Runnable()
	{
		@Override
		public void run(){
			for(StreamModel o:list){
				o.update();
			}
			updaterTimerRunnable.run();
			mainTimerHandler.postDelayed(mainTimerRunnable, updateinterval*1000);
		}
	};
	Handler updaterTimerHandler=new Handler();
	Runnable updaterTimerRunnable=new Runnable()
	{
		@Override
		public void run(){
			pBar.setVisibility(View.VISIBLE);
			//if(!list.isEmpty()){
			for(StreamModel o:list){
					if(o.postUpdate()!=true){
						updaterTimerHandler.postDelayed(updaterTimerRunnable, 3000);
						return;
					}
			}
			for(StreamModel o:list){
				if(onlyLive==true){
					if(o.isLive()==true){
							if(!displayedlist.contains(o)){
								displayedlist.add(o);
							}
						}else{
							if(displayedlist.contains(o)){
								displayedlist.remove(o);
							}
						}
					}else{
						if(!displayedlist.contains(o)){
							displayedlist.add(o);
						}
					}
				}
			//}
			adapter.notifyDataSetChanged();
			pBar.setVisibility(View.GONE);
		}
	};
	Handler addChannelTimerHandler=new Handler();
	Runnable addChannelTimerRunnable=new Runnable(){
		@Override
		public void run(){
			if(obj.postUpdate()==true){
				list.add(obj);
				adapter.notifyDataSetChanged();
				updaterTimerHandler.removeCallbacks(updaterTimerRunnable);
				updaterTimerRunnable.run();
			}else{
				addChannelTimerHandler.postDelayed(addChannelTimerRunnable,1000);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(1);
		setContentView(R.layout.activity_main);
		pBar=(ProgressBar)findViewById(R.id.progressBar1);
		FileInputStream fis;
		try {
			fis = this.openFileInput("data.dat");
			ObjectInputStream is = new ObjectInputStream(fis);
			ArrayList<StreamModel> simpleClass = (ArrayList<StreamModel>)is.readObject();
			is.close();
		    if(simpleClass!=null){
		    	for(StreamModel o:simpleClass){
		    		try {
						obj=o.getClass().newInstance();
						obj.setChannelName(o.getChannelName());
						list.add(obj);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		
		    	}
		    	//list=(ArrayList<StreamModel>)simpleClass.clone();
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mSettings=getSharedPreferences("mysettings", Context.MODE_PRIVATE);
		if(mSettings.contains("onlyLive")){
			onlyLive=mSettings.getBoolean("onlyLive", false);
		}
		if(mSettings.contains("updateinterval")){
			updateinterval=mSettings.getInt("updateinterval", 60);
		}
		adapter=new StreamAdapter(this, displayedlist);
		setListAdapter(adapter);
		mainTimerRunnable.run();
	}
	private class OnReadyListener implements AddDialog.ReadyListener{
		@Override
		public void ready(String name, int selection){
			switch(selection){
			case 0:
				obj=new TwitchModel();
				obj.setChannelName(name);
				obj.update();
				addChannelTimerRunnable.run();
				break;
			case 1:
				obj=new JustinModel();
				obj.setChannelName(name);
				obj.update();
				addChannelTimerRunnable.run();
				break;
			case 2:
				obj=new CaveTubeModel();
				obj.setChannelName(name);
				obj.update();
				addChannelTimerRunnable.run();
			default:
				break;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.additem:
			AddDialog dialog=new AddDialog(MainActivity.this, "", new OnReadyListener());
			dialog.show();
			break;
		case R.id.deleteitem:
			if(item.isChecked()==true){
				item.setIcon(R.drawable.button1);
				item.setChecked(false);
				DeleteMode=false;
			}else{
				item.setIcon(R.drawable.button1checked);
				item.setChecked(true);
				DeleteMode=true;
			}
			break;
		case R.id.action_settings:
			Intent intent=new Intent(this, SettingsActivity.class);
			intent.putExtra("isLive", onlyLive);
			intent.putExtra("updateinterval", updateinterval);
			startActivityForResult(intent, 1);
			break;
		case R.id.sortbyviewers:
			adapter.sort(new ViewersComparator());
			adapter.notifyDataSetChanged();
			break;
		case R.id.sortbychannelname:
			adapter.sort(new ChannelNameComparator());
			adapter.notifyDataSetChanged();
			break;
		case R.id.refresh:
			mainTimerHandler.removeCallbacks(mainTimerRunnable);
			mainTimerRunnable.run();
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		if(DeleteMode==true){
			StreamModel obj=displayedlist.get(position);
			list.remove(obj);
			adapter.remove(obj);
			adapter.notifyDataSetChanged();
		}else{
			StreamModel obj=displayedlist.get(position);
			if(obj.getClass().getSimpleName().compareTo("TwitchModel")==0){
				Uri uri=Uri.parse("http://www.twitch.tv"+obj.getChannelName());
				Intent intent=new Intent(Intent.ACTION_VIEW,uri);
				startActivity(intent);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(data==null){return;}
		onlyLive=data.getBooleanExtra("isLive", false);
		int selection=data.getIntExtra("updateinterval", 0);
		if(selection==0){
			updateinterval=60;
		}else if(selection==1){
			updateinterval=300;
		}else if(selection==2){
			updateinterval=600;
		}else if(selection==3){
			updateinterval=1800;
		}else if(selection==4){
			updateinterval=3600;
		}
	}
	
	@Override
	protected void onStop() {
		FileOutputStream fos;
		try {
			fos = this.openFileOutput("data.dat", MainActivity.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(list);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Editor editor=mSettings.edit();
		editor.putInt("updateinterval", updateinterval);
		editor.putBoolean("onlyLive", onlyLive);
		editor.commit();
		super.onStop();
	}
}
