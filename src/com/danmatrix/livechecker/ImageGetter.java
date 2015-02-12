package com.danmatrix.livechecker;

import java.io.IOException;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

class ImageGetter extends AsyncTask<String, byte[], Bitmap>{

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap content=null;
		try {
			content=BitmapFactory.decodeStream(new java.net.URL(params[0]).openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	@Override
	protected void onPostExecute(Bitmap result)
	{
		super.onPostExecute(result);
	}

}
