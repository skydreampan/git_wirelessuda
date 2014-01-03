package com.wirelessuda.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wirelessuda.R;
import com.wirelessuda.model.RSSFeed;
import com.wirelessuda.model.RSSHandler;

public class NoticeindexActivity extends Activity{

	private final static String rsshost = "http://jsglxt.suda.edu.cn/feed.action?type=1";
	private List<HashMap<String, Object>> list;
	private ListView lvNews;
	private URL url;
	private ProgressBar pb1;
	private Button goback;
	//适配器
		BaseAdapter ba = new BaseAdapter() {
			
			public View getView(int position, View convertView, ViewGroup parent) {
		
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.newsindex_item,null); //加载布局文件,定义没一行样式
				
				TextView tvDepart = (TextView)convertView.findViewById(R.id.news_depart);
				TextView tvTitle = (TextView) convertView.findViewById(R.id.news_title);
				TextView tvDate = (TextView) convertView.findViewById(R.id.news_date);
				
				
				tvTitle.setText(list.get(position).get("title").toString()); 
				String date = list.get(position).get("pubdate").toString();
				tvDepart.setText(list.get(position).get("depart").toString());
				tvDate.setText(date);
			
				return convertView;
				
			}
			
			public long getItemId(int position) {
				return 0;
			}
			
			public Object getItem(int arg0) {
				return null;
			}
			
			public int getCount() {
				return list.size();
			}
		};
		Handler myHandler = new Handler(){
			
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0:		
					lvNews.setAdapter(ba);  
					pb1.setVisibility(View.GONE); 
					break;
				}
			//	super.handleMessage(msg);
			}
		};
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.noticeindex);
		
		goback = (Button)findViewById(R.id.noticeindex_goback);
		lvNews = (ListView) findViewById(R.id.news_list2_lvNews);
		pb1 = (ProgressBar)findViewById(R.id.pb1);
		getRSSList();
/*		new Thread(new Runnable() {
			@Override
			public void run() {
				// 需要进行的逻辑处理
				runOnUiThread(new Runnable() {
					public void run() {
						try {
							url = new URL(rsshost);
							InputStream rsstream = url.openStream();
							SAXParserFactory factory = SAXParserFactory.newInstance();
							SAXParser parser = factory.newSAXParser();
							XMLReader reader = parser.getXMLReader();
							RSSHandler handler = new RSSHandler();
							reader.setContentHandler(handler);
							reader.parse(new InputSource(rsstream));
							RSSFeed feed = handler.getFeed();
							list= feed.getItems();
							myHandler.sendEmptyMessage(0);
						} catch (Exception e) {
							Log.e("Exception",e.toString());
						} 
					}
				});
			}
		}).start();*/
		
		lvNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final String title = list.get(position).get("title").toString();
				final String link = list.get(position).get("link").toString();
				Log.i("RSS", "title = "+title);
				Intent intent = new Intent(NoticeindexActivity.this, Noticeindex_WebView.class);
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				startActivity(intent);
			}
		});
		
		goback.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				NoticeindexActivity.this.finish();
			}
		});
	}
	
	public void getRSSList(){
		
		new Thread(){
			
			public void run() {
				//Looper.prepare();  //使线程拥有自己的消息列队
				try {
					url = new URL(rsshost);
				
				
				InputStream rsstream = url.openStream();
				//本地解析rss.xml
			//	rsstream = readLocalXml();
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				RSSHandler handler = new RSSHandler();
				reader.setContentHandler(handler);
				reader.parse(new InputSource(rsstream));
				
				RSSFeed feed = handler.getFeed();
				
			   list= feed.getItems();
			   
			   /*
				Looper.loop();
				Looper.myLooper().quit();
				*/
			   myHandler.sendEmptyMessage(0);
			 //  Looper.myLooper().quit();
			   
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public InputStream readLocalXml() throws IOException{
		return this.getAssets().open("rssxml.xml");
	}
	
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if(keyCoder == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			this.finish();
		}
		return true;
	}
}