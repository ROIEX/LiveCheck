package com.danmatrix.livechecker;

import java.io.Serializable;
import java.lang.Thread.State;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask.Status;

class TwitchModel implements StreamModel, Serializable {

	private String channel_name;
	private String logo_url;
	private String description;
	private int viewers=0;
	transient ImageGetter test=new ImageGetter();
	transient private RequestTask task=null;
	transient private Bitmap image;
	private boolean live=false;
	private boolean isImage=false;
	private String extra_data=null;
	transient private postUpdateRunnable myRunnable=new postUpdateRunnable();
	transient private Thread myThread;
	public boolean isLive() {
		// TODO Auto-generated method stub
		return live;
	}

	public int getViewers() {
		// TODO Auto-generated method stub
		return viewers;
	}

	public void update() {
		task=null;
		test=null;
		live=false;
		logo_url=null;
		description=null;
		isImage=false;
		myThread=null;
		task=new RequestTask();//.execute("https://api.twitch.tv/kraken/"+channel_name);
		task.execute("https://api.twitch.tv/kraken/streams/"+getChannelName());
		myThread=new Thread(myRunnable);
		myThread.start();
	}

	public String getLogoURL() {
		// TODO Auto-generated method stub
		return logo_url;
	}

	public void setChannelName(String ChannelName) {
		// TODO Auto-generated method stub
		channel_name=ChannelName;
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
	public String getDescription() {
		return description;
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
					if(!response.equals("")){
						JSONObject obj=new JSONObject(response);
						if(obj.isNull("stream"))
						{
							live=false;
							isImage=false;
							viewers=0;
							image=null;
							logo_url=null;
							return;
						}else{
							live=true;
							obj=obj.getJSONObject("stream");
							viewers=obj.getInt("viewers");
							logo_url=obj.getJSONObject("preview").getString("small");
							description=obj.getJSONObject("channel").getString("status");
							}
					}else{
						live=false;
						isImage=false;
						viewers=0;
						image=null;
						logo_url=null;
						return;
					}
				if(logo_url!=null){
					test=new ImageGetter();
					test.execute(logo_url);
					image=test.get();
					isImage=true;
					return;
				}else{
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




/*
 * String response;
		try {
			if(task.getStatus()!=Status.FINISHED)return false;
			if(workfinished!=true){
				response=task.get();
				if(!response.equals("")){
					JSONObject obj=new JSONObject(response);
					if(obj.isNull("stream"))
					{
						live=false;
						workfinished=true;
						viewers=0;
						return true;
					}else{
						live=true;
						obj=obj.getJSONObject("stream");
						viewers=obj.getInt("viewers");
						logo_url=obj.getJSONObject("preview").getString("small");
						description=obj.getJSONObject("channel").getString("status");
						workfinished=true;
						}
				}
			}
			if(workfinished==true){
			ImageGetter test=new ImageGetter();
			if(logo_url!=null){
				test.execute(logo_url);
				if(test.getStatus()==Status.FINISHED)return false;
				image=test.get();
				isImage=true;
				workfinished=false;
				return true;
			}else{
				return true;
			}
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
		return false;
 */

/*
try{
		if(task.getStatus()==Status.FINISHED && workfinished==false){
				String response=task.get();
				if(!response.equals("")){
					data=new JSONObject(response);
					if(data.isNull("stream")){
						viewers=0;
						live=false;
						workfinished=true;
						isImage=false;
					}else{
						live=true;
						data=data.getJSONObject("stream");
						viewers=data.getInt("viewers");
						logo_url=data.getJSONObject("preview").getString("small");
						description=data.getJSONObject("channel").getString("status");
					}
				}else{
					viewers=0;
					live=false;
					workfinished=true;
					isImage=false;
				}
			}
		if(task.getStatus()==Status.FINISHED && workfinished==true){
			ImageGetter test=new ImageGetter();
			if(isImage==false){
				test.execute(logo_url);
				if(test.getStatus()==Status.FINISHED)return false;
				image=test.get();
				isImage=true;
				workfinished=false;
				return true;
			}else{
				return true;
			}
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
		return false;
 */
