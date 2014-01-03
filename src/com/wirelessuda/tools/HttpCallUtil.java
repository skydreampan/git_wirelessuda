package com.wirelessuda.tools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class HttpCallUtil {
	
	/**
	 * post 请求
	 * @param url
	 * @param name 
	 * @param psw
	 * @param data
	 * @return
	 */
	public String getConnectionPost(String url, String name, String psw, List data){
		String sb = new String();
		HttpPost request = null;
		HttpResponse response = null;
		List<NameValuePair> list = null;
		try {
			if(url != null){
				request = new HttpPost(url);
				if(data != null){
					list = putParam(data);
				}
				else if(name != null && psw !=null){
					list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("username", name));
					list.add(new BasicNameValuePair("password", psw));
				}
				request.setEntity(new UrlEncodedFormEntity(list,HTTP.UTF_8)); //统一编码
				response = new DefaultHttpClient().execute(request); //?
				
				if(response.getStatusLine().getStatusCode() == 200){
					String temp = EntityUtils.toString(response.getEntity());
					if(temp.length() > 0){
						sb = temp.trim().toString();
					}
					else {
						sb = "error response data length";
					}
					
				}
				else {
					sb = "error response code:"+response.getStatusLine().getStatusCode();
				}
			}
			else {
				return null;
			}
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		
		}
		return sb;
	}
	/**
	 * 传递给服务器的数据，用mpa 进行封装
	 * @param data
	 * @return
	 */
	public List putParam(List data){
		Log.v("map 3 == ==", data.size()+"");
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(data != null){
			for (int i = 0; i < data.size(); i++) {
				Log.v("==" + data.get(i).toString(), data.get(i).toString());
				list.add(new BasicNameValuePair(data.get(i).toString(), data.get(i).toString()));
				
			}
		}
		return list;
	}
	/**
	 * get 请求
	 * @param url
	 * @return
	 */
	public String getConnectionGet(String url){
		String str = new String();
		HttpGet request = null;
		HttpResponse response = null;
		try {
			if(url != null){
				request = new HttpGet(url);
				response = new DefaultHttpClient().execute(request);
				if(response.getStatusLine().getStatusCode() == 200){
					String temp = EntityUtils.toString(response.getEntity());
					if(temp.length() >=0){
						str = temp.substring(0, temp.length()-1);
					}
					else {
						str = "error response data length" + response.getStatusLine().getStatusCode();
					}
				}
				else {
					str = "error response :"+ response.getStatusLine().getStatusCode();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 获取图片
	 * @param url
	 * @param iv
	 */
	public void getConnectionImage(String url, ImageView iv){
		URL imageUrl = null;
		HttpURLConnection conn = null;
		try {
			if(url != null){
				imageUrl = new URL(url);
				conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream in = conn.getInputStream();
				Bitmap bp = BitmapFactory.decodeStream(in);
				if(iv != null){
					iv.setImageBitmap(bp);
				}
				
			}
			else {
				;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
