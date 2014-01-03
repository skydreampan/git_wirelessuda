package com.wirelessuda.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.wirelessuda.R;
import com.wirelessuda.model.RSSFeed;
import com.wirelessuda.model.RSSHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsindexActivity extends Activity {

	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ViewGroup buttonsLine;
	private Button button01, button02, button03, button04, goback;
	private Button[] buttons;
	private ListView lv01, lv02, lv03, lv04;
	private int flag02 = 1, flag03 = 1, flag04 = 1;
	private List<HashMap<String, Object>> list, list1,list2,list3,list4;
	private View page01, page02, page03, page04;
	private ProgressBar pb1,pb2,pb3,pb4;
	private LayoutInflater inflater;
	Handler myHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		// 每页de界面
		page01 = inflater.inflate(R.layout.newsindex_page01, null);
		page02 = inflater.inflate(R.layout.newsindex_page02, null);
		page03 = inflater.inflate(R.layout.newsindex_page03, null);
		page04 = inflater.inflate(R.layout.newsindex_page04, null);
		pageViews.add(page01); // lee
		pageViews.add(page02); // lee
		pageViews.add(page03); // lee
		pageViews.add(page04); 
		lv01 = (ListView) page01.findViewById(R.id.lv01);
		lv02 = (ListView) page02.findViewById(R.id.lv02);
		lv03 = (ListView) page03.findViewById(R.id.lv03);
		lv04 = (ListView) page04.findViewById(R.id.lv04);
		pb1 = (ProgressBar) page01.findViewById(R.id.pb1);
		pb2 = (ProgressBar) page02.findViewById(R.id.pb2);
		pb3 = (ProgressBar) page03.findViewById(R.id.pb3);
		pb4 = (ProgressBar) page04.findViewById(R.id.pb4);
	//	list = new ArrayList<HashMap<String, Object>>();
		getRSSList(0);
	//	Toast.makeText(NewsindexActivity.this, list1.toString(), Toast.LENGTH_LONG).show();
		myHandler = new Handler(){
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0:	
					lv01.setAdapter(new newsListAdapter1(page01.getContext()));  
					break;
				case 1:	
					lv02.setAdapter(new newsListAdapter2(page02.getContext()));  
					break;
				case 2:	
					lv03.setAdapter(new newsListAdapter3(page03.getContext()));  
					break;
				case 3:	
					lv04.setAdapter(new newsListAdapter4(page04.getContext())); 
					break;
				}
		//		super.handleMessage(msg);
			}
		};
//		lv01.setAdapter(new newsListAdapter(this));
//		lv02.setAdapter(new newsListAdapter(this));
//		lv03.setAdapter(new newsListAdapter(this));

		// 按钮栏
		buttons = new Button[pageViews.size()];
		buttonsLine = (ViewGroup) inflater.inflate(R.layout.newsindex, null);
		button01 = (Button) buttonsLine.findViewById(R.id.pre_one_button);
		button02 = (Button) buttonsLine.findViewById(R.id.pre_two_button);
		button03 = (Button) buttonsLine.findViewById(R.id.pre_three_button);
		button04 = (Button) buttonsLine.findViewById(R.id.pre_four_button);
		goback = (Button)buttonsLine.findViewById(R.id.newsindex_goback);
		
		buttons[0] = button01;
		buttons[1] = button02;
		buttons[2] = button03;
		buttons[3] = button04;
		button01.setOnClickListener(new GuideButtonClickListener(0));
		button02.setOnClickListener(new GuideButtonClickListener(1));
		button03.setOnClickListener(new GuideButtonClickListener(2));
		button04.setOnClickListener(new GuideButtonClickListener(3));

		viewPager = (ViewPager) buttonsLine.findViewById(R.id.guidePages);
		setContentView(buttonsLine);

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		goback.setOnClickListener(new android.view.View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				NewsindexActivity.this.finish();
			}
		});

		
		lv01.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final String title = list1.get(position).get("title").toString();
				final String link = list1.get(position).get("link").toString();
				Intent intent = new Intent(NewsindexActivity.this, Newsindex_WebView.class);
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				startActivity(intent);
			}
		});
		
		lv02.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final String title = list2.get(position).get("title").toString();
				final String link = list2.get(position).get("link").toString();
				Intent intent = new Intent(NewsindexActivity.this, Newsindex_WebView.class);
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				startActivity(intent);
			}
		});
		
		lv03.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final String title = list3.get(position).get("title").toString();
				final String link = list3.get(position).get("link").toString();
				Intent intent = new Intent(NewsindexActivity.this, Newsindex_WebView.class);
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				startActivity(intent);
			}
		});
		
		lv04.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final String title = list4.get(position).get("title").toString();
				final String link = list4.get(position).get("link").toString();
				Intent intent = new Intent(NewsindexActivity.this, Newsindex_WebView.class);
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				startActivity(intent);
			}
		});
	}

	// 列表适配
	public class newsListAdapter1 extends BaseAdapter implements ListAdapter {
		private Context mContext = null;
		private LayoutInflater mInflater = null;

		public newsListAdapter1(Context c) {
			mContext = c;
			mInflater = LayoutInflater.from(this.mContext);
		}

		@Override
		public int getCount() {
			return list1.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.newsindex_item, null);
				// 初始化组件
				holder.title = (TextView) convertView
						.findViewById(R.id.news_title);
				holder.depart = (TextView) convertView
						.findViewById(R.id.news_depart);
				holder.date = (TextView) convertView
						.findViewById(R.id.news_date);
				
		/*		convertView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						final String title = list1.get(position).get("title").toString();
						final String link = list1.get(position).get("link").toString();
						Intent intent = new Intent(NewsindexActivity.this, NewsItemWebViewActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("link", link);
						startActivity(intent);
					}});
				
				convertView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_UP:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_CANCEL:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						}
						return false;
					}
				});*/
				
				convertView.setBackgroundResource(R.drawable.button_unselected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		//	Toast.makeText(NewsindexActivity.this, list1.toString(), Toast.LENGTH_LONG).show();
			holder.title.setText(list1.get(position).get("title").toString());
			holder.depart.setText(list1.get(position).get("depart").toString());
			holder.date.setText(list1.get(position).get("pubdate").toString());
			pb1.setVisibility(View.GONE);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView depart;
			TextView date;
		}
	}
	
	public class newsListAdapter2 extends BaseAdapter implements ListAdapter {
		private Context mContext = null;
		private LayoutInflater mInflater = null;

		public newsListAdapter2(Context c) {
			mContext = c;
			mInflater = LayoutInflater.from(this.mContext);
		}

		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.newsindex_item, null);
				// 初始化组件
				holder.title = (TextView) convertView
						.findViewById(R.id.news_title);
				holder.depart = (TextView) convertView
						.findViewById(R.id.news_depart);
				holder.date = (TextView) convertView
						.findViewById(R.id.news_date);
				
	/*			convertView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						final String title = list2.get(position).get("title").toString();
						final String link = list2.get(position).get("link").toString();
						Intent intent = new Intent(NewsindexActivity.this, NewsItemWebViewActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("link", link);
						startActivity(intent);
					}});*/
				
				convertView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_UP:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_CANCEL:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						}
						return false;
					}
				});
				
				convertView.setBackgroundResource(R.drawable.button_unselected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		//	Toast.makeText(NewsindexActivity.this, list.toString(), Toast.LENGTH_LONG).show();
			holder.title.setText(list2.get(position).get("title").toString());
			holder.depart.setText(list2.get(position).get("depart").toString());
			holder.date.setText(list2.get(position).get("pubdate").toString());
			pb2.setVisibility(View.GONE);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView depart;
			TextView date;
		}
	}

	public class newsListAdapter3 extends BaseAdapter implements ListAdapter {
		private Context mContext = null;
		private LayoutInflater mInflater = null;

		public newsListAdapter3(Context c) {
			mContext = c;
			mInflater = LayoutInflater.from(this.mContext);
		}

		@Override
		public int getCount() {
			return list3.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.newsindex_item, null);
				// 初始化组件
				holder.title = (TextView) convertView
						.findViewById(R.id.news_title);
				holder.depart = (TextView) convertView
						.findViewById(R.id.news_depart);
				holder.date = (TextView) convertView
						.findViewById(R.id.news_date);
				
		/*		convertView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						final String title = list3.get(position).get("title").toString();
						final String link = list3.get(position).get("link").toString();
						Intent intent = new Intent(NewsindexActivity.this, NewsItemWebViewActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("link", link);
						startActivity(intent);
					}});*/
				
				convertView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_UP:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_CANCEL:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						}
						return false;
					}
				});
				
				convertView.setBackgroundResource(R.drawable.button_unselected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		//	Toast.makeText(NewsindexActivity.this, list.toString(), Toast.LENGTH_LONG).show();
			holder.title.setText(list3.get(position).get("title").toString());
			holder.depart.setText(list3.get(position).get("depart").toString());
			holder.date.setText(list3.get(position).get("pubdate").toString());
			pb3.setVisibility(View.GONE);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView depart;
			TextView date;
		}
	}

	public class newsListAdapter4 extends BaseAdapter implements ListAdapter {
		private Context mContext = null;
		private LayoutInflater mInflater = null;

		public newsListAdapter4(Context c) {
			mContext = c;
			mInflater = LayoutInflater.from(this.mContext);
		}

		@Override
		public int getCount() {
			return list4.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.newsindex_item, null);
				// 初始化组件
				holder.title = (TextView) convertView
						.findViewById(R.id.news_title);
				holder.depart = (TextView) convertView
						.findViewById(R.id.news_depart);
				holder.date = (TextView) convertView
						.findViewById(R.id.news_date);
				
		/*		convertView.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						final String title = list4.get(position).get("title").toString();
						final String link = list4.get(position).get("link").toString();
						Intent intent = new Intent(NewsindexActivity.this, NewsItemWebViewActivity.class);
						intent.putExtra("title", title);
						intent.putExtra("link", link);
						startActivity(intent);
					}});*/
				
				convertView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_UP:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						case MotionEvent.ACTION_CANCEL:
							v.setBackgroundResource(R.drawable.button_unselected);
							break;
						}
						return false;
					}
				});
				
				convertView.setBackgroundResource(R.drawable.button_unselected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
		//	Toast.makeText(NewsindexActivity.this, list.toString(), Toast.LENGTH_LONG).show();
			holder.title.setText(list4.get(position).get("title").toString());
			holder.depart.setText(list4.get(position).get("depart").toString());
			holder.date.setText(list4.get(position).get("pubdate").toString());
			pb4.setVisibility(View.GONE);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			TextView depart;
			TextView date;
		}
	}
	
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setBackgroundResource(R.drawable.button_unselected);
				if (arg0 == i) {
					buttons[i].setBackgroundResource(R.drawable.button_selected);
					if(i==1 && flag02==1){
						getRSSList(1);
						flag02=0;
					}
					if(i==2 && flag03==1){
						getRSSList(2);
						flag03=0;
					}
					if(i==3 && flag04==1){
						getRSSList(3);
						flag04=0;
					}
				}
			}
		}
	}

	class GuideButtonClickListener implements OnClickListener {
		private int index = 0;

		public GuideButtonClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index, true);
		}
	}
	
	public void getRSSList(final int x) {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(
							"http://jsglxt.suda.edu.cn/feed.action?type="+(x+2));

					InputStream rsstream = url.openStream();
					// 本地解析rss.xml
					// rsstream = readLocalXml();
					SAXParserFactory factory = SAXParserFactory.newInstance();
					SAXParser parser = factory.newSAXParser();
					XMLReader reader = parser.getXMLReader();
					RSSHandler handler = new RSSHandler();
					reader.setContentHandler(handler);
					reader.parse(new InputSource(rsstream));

					RSSFeed feed = handler.getFeed();
					
					if(x==0){
						list1 = feed.getItems();
					}
					if(x==1){
						list2 = feed.getItems();
					}
					if(x==2){
						list3 = feed.getItems();
					}
					if(x==3){
						list4 = feed.getItems();
					}
					
					myHandler.sendEmptyMessage(x);
					/*
					 * myHandler.sendEmptyMessage(1);
					 * myHandler.sendEmptyMessage(2);
					 * myHandler.sendEmptyMessage(3);
					 * myHandler.sendEmptyMessage(4);
					 */
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Exception", e.toString());
					e.printStackTrace();
				}
			}
		}.start();
	}

	public InputStream readLocalXml() throws IOException {
		return this.getAssets().open("rssxml.xml");
	}

}
