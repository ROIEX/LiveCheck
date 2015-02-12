package com.danmatrix.livechecker;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StreamAdapter extends ArrayAdapter<StreamModel>{
	private final Context context;
	private final List<StreamModel> values;
	
	public StreamAdapter(Context context, List<StreamModel> values){
		super(context, R.layout.listlayout, values);
		this.context=context;
		this.values=values;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView=inflater.inflate(R.layout.listlayout, parent,false);
		TextView livestatus=(TextView) rowView.findViewById(R.id.text);
		TextView description=(TextView) rowView.findViewById(R.id.description);
		TextView channel=(TextView) rowView.findViewById(R.id.channelname);
		TextView viewers=(TextView) rowView.findViewById(R.id.viewers);
		TextView StreamProvider=(TextView) rowView.findViewById(R.id.StreamProvider);
		if(values.get(position).getClass().getSimpleName().compareTo("TwitchModel")==0){
			StreamProvider.setText("Twitch");
		}else if(values.get(position).getClass().getSimpleName().compareTo("JustinModel")==0){
			StreamProvider.setText("Justin.TV");
		}else if(values.get(position).getClass().getSimpleName().compareTo("CaveTubeModel")==0){
			StreamProvider.setText("CaveTube");
		}
		
		ImageView viewericon=(ImageView)rowView.findViewById(R.id.viewer_icon);
		if(values.get(position).isLive())
		{
			viewericon.setVisibility(View.VISIBLE);
			livestatus.setText("LIVE");
			channel.setText(values.get(position).getChannelName());
			description.setText(values.get(position).getDescription());
			viewers.setText(String.valueOf(values.get(position).getViewers()));
		}else
		{
			viewericon.setVisibility(View.GONE);
			livestatus.setText("OFFLINE");
			channel.setText(values.get(position).getChannelName());
		}
		if(values.get(position).isImageAvailable()==true){
			ImageView image=(ImageView) rowView.findViewById(R.id.icon);
			image.setImageBitmap(values.get(position).GetImage());
		}
		return rowView;
	}
	
}
