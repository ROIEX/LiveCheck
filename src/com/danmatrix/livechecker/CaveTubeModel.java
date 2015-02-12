package com.danmatrix.livechecker;

import java.io.Serializable;
import java.lang.Thread.State;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.net.Uri;

public class CaveTubeModel implements StreamModel, Serializable {
	
	private String channel_name;
	private String logo_url;
	private String description;
	private int viewers=0;
	transient ImageGetter test=new ImageGetter();
	transient private HttpRequestTask task=null;
	transient private Bitmap image;
	private boolean live=false;
	private boolean isImage=false;
	private String extra_data=null;
	transient private postUpdateRunnable myRunnable=new postUpdateRunnable();
	transient private Thread myThread;
	
	@Override
	public void setChannelName(String ChannelName) {
		// TODO Auto-generated method stub
		channel_name=ChannelName;
	}

	@Override
	public boolean isLive() {
		// TODO Auto-generated method stub
		return live;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public int getViewers() {
		// TODO Auto-generated method stub
		return viewers;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		task=null;
		test=null;
		live=false;
		logo_url=null;
		description=null;
		isImage=false;
		myThread=null;
		task=new HttpRequestTask();//.execute("https://api.twitch.tv/kraken/"+channel_name);
		task.execute("http://gae.cavelis.net/live_url?user="+Uri.encode(getChannelName()));
		myThread=new Thread(myRunnable);
		myThread.start();
	}

	@Override
	public boolean postUpdate() {
		// TODO Auto-generated method stub
		if(myThread.getState()==State.TERMINATED){
			return true;
		}
		return false;
	}

	@Override
	public String getLogoURL() {
		// TODO Auto-generated method stub
		return logo_url;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return channel_name;
	}

	@Override
	public boolean isImageAvailable() {
		// TODO Auto-generated method stub
		return isImage;
	}

	@Override
	public Bitmap GetImage() {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public String getExtraData() {
		// TODO Auto-generated method stub
		return extra_data;
	}
	
	class postUpdateRunnable implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String response;
			try {
					response=task.get();
					response=new JSONObject(response).getString("stream_name");
					HttpRequestTask finalRequest=new HttpRequestTask();
					finalRequest.execute("http://gae.cavelis.net/api/summary?devkey=B0CF360BA120411DB4D2DE6AEB045805&stream_name="+response);
					String finalResponse=finalRequest.get();
					if(!finalResponse.equals("")){
						JSONObject obj=new JSONObject(finalResponse);
						live=true;
						viewers=obj.getInt("listener");
						String desc=obj.getString("desc");
						if(desc!=""){
							description=obj.getString("title")+desc;
						}else{
							description=obj.getString("title");
						}
					}else{
						live=false;
						isImage=false;
						viewers=0;
						image=null;
						logo_url=null;
						return;
					}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			//} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
	}

}
