package com.danmatrix.livechecker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

class HttpRequestTask extends AsyncTask<String, String, String> implements Serializable{
	@Override
	protected String doInBackground(String... uri){
		/*HttpClient httpclient=new DefaultHttpClient();
		HttpResponse response;
		String responseString=null;
		try
		{
			response=httpclient.execute(new HttpGet(uri[0]));
			StatusLine statusLine=response.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out=new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				responseString=out.toString();
				out.close();
			}else
			{
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		}catch(ClientProtocolException e){
			//
		}catch(IOException e){
			//
		}
		if(responseString==null){responseString="";}
		return responseString;*/
		String strContents = null;
		try {
			URL url=new URL(uri[0]);
			HttpURLConnection urlconnection=(HttpURLConnection)url.openConnection();
			InputStream is=new BufferedInputStream(urlconnection.getInputStream());
			byte[] contents = new byte[4096];
			int bytesRead=0;
			while((bytesRead = is.read(contents)) !=-1){
				strContents=new String(contents,0,bytesRead);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(strContents==null)
		{
			strContents=new String("");
		}
		return strContents;
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
	}

}
