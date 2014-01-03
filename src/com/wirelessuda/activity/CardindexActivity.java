package com.wirelessuda.activity;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wirelessuda.R;
import com.wirelessuda.activity.CardApplication;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class CardindexActivity extends Activity{
	
	private Button button01, button02, button03, button04, goback, logout, c_submit, c_cancel, d_submit, d_cancel;
	private Button[] buttons;
	private ImageView photo;
	private View c_view, d_view;
	private TextView sno, name,u_account ,u_sno, u_name, u_idnumber, u_pidname, u_iddept, u_peoplename, u_sexname, u_cardId, u_tv1, c_tv1, q_tv1, d_tv1, d_tv2;
	private EditText c_oldPass, c_newPass, c_repeatPass, d_pass;
	private SharedPreferences setting;
	private CardApplication app;
	private InputMethodManager imm;
	private LayoutInflater inflater;
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ViewGroup buttonsLine;
	private View page01, page02, page03, page04;
	private ProgressBar c_pb, q_pb, d_pb;
	private int ScreenWIDTH, flag = 1;
	private List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private ListView q_lv;
	
	BaseAdapter ba = new BaseAdapter() {
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list,null); //加载布局文件,定义没一行样式
			
			TextView tv1 = (TextView)convertView.findViewById(R.id.list_tv1);
			TextView tv2 = (TextView)convertView.findViewById(R.id.list_tv2);
			TextView tv3 = (TextView)convertView.findViewById(R.id.list_tv3);
			tv1.setText(list.get(position).get("s1").toString());
			tv2.setText(list.get(position).get("s2").toString());
			tv3.setText(list.get(position).get("s3").toString());
			tv1.setTextColor(Color.BLACK);
			tv2.setTextColor(Color.BLACK);
			tv3.setTextColor(Color.BLACK);
			
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
	
	Handler c_handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				Toast.makeText(CardindexActivity.this, "密码修改成功，请重新登录！", Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = setting.edit();
				editor.clear();
				editor.commit();
				app.setLoginFlag("");
				app.setAccount("");
				app.setUsername("");
				Intent  newsIntent = new Intent(CardindexActivity.this,CardActivity.class);
				startActivity(newsIntent);
				CardindexActivity.this.finish(); 
				break;
			case 1:
				c_view.setVisibility(View.GONE);
	    		c_pb.setVisibility(View.GONE);
				Toast.makeText(CardindexActivity.this, "对不起，原密码错误！", Toast.LENGTH_LONG).show();
				break;
			case 2:
				c_view.setVisibility(View.GONE);
	    		c_pb.setVisibility(View.GONE);
				Toast.makeText(CardindexActivity.this, "对不起，密码修改失败！", Toast.LENGTH_LONG).show();
				break;
			case 3:
				c_view.setVisibility(View.GONE);
	    		c_pb.setVisibility(View.GONE);
				Toast.makeText(CardindexActivity.this, "网络出错啦！", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
	Handler q_handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				q_lv.setAdapter(ba);  
				q_pb.setVisibility(View.GONE);
				break;
			}
		}
	};
	
	Handler d_handler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:	
				d_view.setVisibility(View.GONE);
	    		d_pb.setVisibility(View.GONE);
				Toast.makeText(CardindexActivity.this, "一卡通挂失成功，解除挂失请带好个人证件去校园卡部或者网络中心！", Toast.LENGTH_LONG).show();
				d_pass.setText("");
				break;
			case 1:	
				d_view.setVisibility(View.GONE);
	    		d_pb.setVisibility(View.GONE);
	    		Toast.makeText(CardindexActivity.this, "一卡通已挂失，解除挂失请带好个人证件去校园卡部或者网络中心！", Toast.LENGTH_LONG).show();
				d_pass.setText("");
				break;
			case 2:	
				d_view.setVisibility(View.GONE);
	    		d_pb.setVisibility(View.GONE);
	    		Toast.makeText(CardindexActivity.this, "对不起，密码错误！", Toast.LENGTH_LONG).show();
				d_pass.setText("");
				break;
			case 3:	
				d_view.setVisibility(View.GONE);
	    		d_pb.setVisibility(View.GONE);
	    		Toast.makeText(CardindexActivity.this, "对不起，一卡通挂失失败！", Toast.LENGTH_LONG).show();
				d_pass.setText("");
				break;
			case 4:	
				d_view.setVisibility(View.GONE);
	    		d_pb.setVisibility(View.GONE);
	    		Toast.makeText(CardindexActivity.this, "网络出错啦！", Toast.LENGTH_LONG).show();
				d_pass.setText("");
				break;
			}
		}
	};

    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		ScreenWIDTH = dMetrics.widthPixels;
		setting = getSharedPreferences("userInfo", 0);
		app = (CardApplication)getApplicationContext();
		inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		
		page01 = inflater.inflate(R.layout.cardindex_userinfo, null);
		page02 = inflater.inflate(R.layout.cardindex_changepass, null);
		page03 = inflater.inflate(R.layout.cardindex_querybalance, null);
		page04 = inflater.inflate(R.layout.cardindex_doloss, null);
		pageViews.add(page01); 
		pageViews.add(page02); 
		pageViews.add(page03); 
		pageViews.add(page04); 
		//userinfo中的对象
		u_account = (TextView)page01.findViewById(R.id.cardindex_userinfo_account);
		u_sno = (TextView)page01.findViewById(R.id.cardindex_userinfo_sno);
		u_name = (TextView)page01.findViewById(R.id.cardindex_userinfo_name);
		u_idnumber = (TextView)page01.findViewById(R.id.cardindex_userinfo_idnumber);
		u_pidname = (TextView)page01.findViewById(R.id.cardindex_userinfo_pidname);
		u_iddept = (TextView)page01.findViewById(R.id.cardindex_userinfo_iddept);
		u_peoplename = (TextView)page01.findViewById(R.id.cardindex_userinfo_peoplename);
		u_sexname = (TextView)page01.findViewById(R.id.cardindex_userinfo_sexname);
		u_cardId = (TextView)page01.findViewById(R.id.cardindex_userinfo_cardId);
		u_tv1 = (TextView)page01.findViewById(R.id.cardindex_userinfo_tv1);
		u_tv1.setWidth(ScreenWIDTH/2);
		//changepass中的对象
		c_tv1 = (TextView)page02.findViewById(R.id.cardindex_changepass_tv1);
		c_oldPass = (EditText)page02.findViewById(R.id.cardindex_changepass_oldPass);
		c_newPass = (EditText)page02.findViewById(R.id.cardindex_changepass_newPass);
		c_repeatPass = (EditText)page02.findViewById(R.id.cardindex_changepass_repeatPass);
		c_submit = (Button)page02.findViewById(R.id.cardindex_changepass_submit);
		c_cancel = (Button)page02.findViewById(R.id.cardindex_changepass_cancel);
		c_pb = (ProgressBar)page02.findViewById(R.id.cardindex_changepass_pb);
		c_view = (View)page02.findViewById(R.id.cardindex_changepass_view);
		c_oldPass.setText("");
		c_newPass.setText("");
		c_repeatPass.setText("");
		c_tv1.setWidth(ScreenWIDTH/2);
		//querybalance中的对象
		q_tv1 = (TextView)page03.findViewById(R.id.cardindex_querybalance_tv1);
		q_lv = (ListView)page03.findViewById(R.id.cardindex_querybalance_lv);
		q_pb = (ProgressBar)page03.findViewById(R.id.cardindex_querybalance_pb);
		q_tv1.setText("您的卡内余额： "+ Float.parseFloat(app.getBalance())/100 +" 元");
		//doloss中的对象
		d_tv1 = (TextView)page04.findViewById(R.id.cardindex_doloss_tv2);
		d_pass = (EditText)page04.findViewById(R.id.cardindex_doloss_pass);
		d_submit = (Button)page04.findViewById(R.id.cardindex_doloss_submit);
		d_cancel = (Button)page04.findViewById(R.id.cardindex_doloss_cancel);
		d_pb = (ProgressBar)page04.findViewById(R.id.cardindex_doloss_pb);
		d_view = (View)page04.findViewById(R.id.cardindex_doloss_view);
		d_tv2 = (TextView)page04.findViewById(R.id.cardindex_doloss_tv3);
		c_oldPass.setText("");
		d_pass.setText("");
		d_tv1.setWidth(ScreenWIDTH/2);
		
		buttons = new Button[pageViews.size()];
		buttonsLine = (ViewGroup) inflater.inflate(R.layout.cardindex, null);
		button01 = (Button) buttonsLine.findViewById(R.id.cardindex_userInfo);
		button02 = (Button) buttonsLine.findViewById(R.id.cardindex_changePass);
		button03 = (Button) buttonsLine.findViewById(R.id.cardindex_queryBalance);
		button04 = (Button) buttonsLine.findViewById(R.id.cardindex_lossReport);
		goback = (Button)buttonsLine.findViewById(R.id.cardindex_goback);
		logout = (Button)buttonsLine.findViewById(R.id.cardindex_logout);
		photo = (ImageView)buttonsLine.findViewById(R.id.cardindex_photo);
		name = (TextView)buttonsLine.findViewById(R.id.cardindex_name);
		sno = (TextView)buttonsLine.findViewById(R.id.cardindex_sno);
		//pageview的构造
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
		
		if(!setting.getString("photo", "").equals("")){
			String url = setting.getString("photo", "");
			new ImageDownloader().download(url, photo);
		}
		if(!setting.getString("name", "").equals("")){
			name.setText(setting.getString("name", ""));
		}
		if(!setting.getString("sno", "").equals("")){
			sno.setText(setting.getString("sno", ""));
		}
		//填充userinfo
		if(!setting.getString("account", "").equals("")){
			u_account.setText(setting.getString("account", ""));
		}
		if(!setting.getString("sno", "").equals("")){
			u_sno.setText(setting.getString("sno", ""));
		}
		if(!setting.getString("name", "").equals("")){
			u_name.setText(setting.getString("name", ""));
		}
		if(!setting.getString("idnumber", "").equals("")){
			u_idnumber.setText(setting.getString("idnumber", ""));
		}
		if(!setting.getString("pidname", "").equals("")){
			u_pidname.setText(setting.getString("pidname", ""));
		}
		if(!setting.getString("iddept", "").equals("")){
			u_iddept.setText(setting.getString("iddept", ""));
		}
		if(!setting.getString("peoplename", "").equals("")){
			u_peoplename.setText(setting.getString("peoplename", ""));
		}
		if(!setting.getString("sexname", "").equals("")){
			u_sexname.setText(setting.getString("sexname", ""));
		}
		if(!setting.getString("cardId", "").equals("")){
			u_cardId.setText(setting.getString("cardId", ""));
		}
		//填充changepass
		c_submit.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				if(c_oldPass.getText().toString().equals("")){
		    		Toast.makeText(CardindexActivity.this, "原密码不能为空！", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else if(c_newPass.getText().toString().length()!=6){
		    		Toast.makeText(CardindexActivity.this, "新密码必须是6位！", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else if(!c_newPass.getText().toString().equals(c_repeatPass.getText().toString())){
		    		Toast.makeText(CardindexActivity.this, "2次密码输入不一致！", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else {
		    		c_view.setVisibility(View.VISIBLE);
		    		c_pb.setVisibility(View.VISIBLE);
		    		c_submit(c_oldPass.getText().toString(),c_newPass.getText().toString());
		    	}
			}
		});
		c_cancel.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				c_oldPass.setText("");
				c_newPass.setText("");
				c_repeatPass.setText("");
			}
		});
		//填充doloss
		if(app.getFlag().substring(0,3).equals("001")){
			d_view.setVisibility(View.VISIBLE);
			d_tv2.setVisibility(View.VISIBLE);
		}
		d_submit.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				if(d_pass.getText().toString().equals("")){
		    		Toast.makeText(CardindexActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else {
		    		d_view.setVisibility(View.VISIBLE);
		    		d_pb.setVisibility(View.VISIBLE);
		    		d_submit(d_pass);
		    	}
			}
		});
		d_cancel.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				d_pass.setText("");
			}
		});
		
        goback.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				CardindexActivity.this.finish();
			}
		});
		
		logout.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(CardindexActivity.this);
				builder.setMessage("确认退出登录吗？");
				builder.setTitle("提示");
				builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SharedPreferences.Editor editor = setting.edit();
						editor.putString("password", "");
						editor.commit();
						app.setLoginFlag("");
						app.setAccount("");
						app.setUsername("");
						Intent  intent = new Intent(CardindexActivity.this,CardActivity.class);
						startActivity(intent);
						CardindexActivity.this.finish();
					}
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
    private void c_submit(final String oldPass, final String newPass){
		new Thread() {
			@Override
			public void run() {
				try {
							String url="http://weixin.suda.edu.cn/servlet/ModifyPsd?account=" + setting.getString("account", "") + "&password=" + 
								oldPass + "&newpassword=" + newPass;
						
							URL url1 = new URL(url);
							HttpURLConnection httpConn = (HttpURLConnection) url1.openConnection();
							httpConn.setConnectTimeout(10000);
							httpConn.setReadTimeout(10000);
							httpConn.setDoInput(true);
							httpConn.setDoOutput(true);
							// 此方法在正式链接之前设置才有效。
							httpConn.setRequestMethod("POST");
							httpConn.setUseCaches(false);
							// 正式创建链接
							httpConn.connect();
							InputStream inStream = httpConn.getInputStream();
							String encoding = httpConn.getContentEncoding();
							int read = -1;
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while ((read = inStream.read()) != -1) {
								baos.write(read);
							}
							byte[] data = baos.toByteArray();
							baos.close();
							String content = null;
							if (encoding != null) {
								content = new String(data, encoding).trim();
							} else {
								content = new String(data).trim();
							}
							JSONTokener jsonParser = new JSONTokener(content);
							JSONObject card = (JSONObject) jsonParser.nextValue();
							String result = card.getString("result");
							jsonParser = new JSONTokener(result);
							JSONObject info = (JSONObject) jsonParser.nextValue();
							result = info.getString("resultin");
							if(result.equals("操作成功")){
			/*					Toast.makeText(Cardindex_changepass.this, "密码修改成功，请重新登录！", Toast.LENGTH_LONG).show();
								SharedPreferences.Editor editor = setting.edit();
								editor.clear();
								editor.commit();
								app.setLoginFlag("");
								app.setAccount("");
								app.setUsername("");
								Intent  newsIntent = new Intent(CardindexActivity.this,CardActivity.class);
								startActivity(newsIntent);
								CardindexActivity.this.finish();*/
								c_handler.sendEmptyMessage(0);
							}
							else if(result.equals("密码错误")){
			//					Toast.makeText(Cardindex_changepass.this, "对不起，原密码错误！", Toast.LENGTH_LONG).show();
								c_handler.sendEmptyMessage(1);
							}
							else{
			//					Toast.makeText(Cardindex_changepass.this, "对不起，密码修改失败！", Toast.LENGTH_LONG).show();
								c_handler.sendEmptyMessage(2);
							}
						} catch (Exception e) {
							Log.v("Exception",e.toString());
			//				Toast.makeText(Cardindex_changepass.this, "网络出错啦！", Toast.LENGTH_LONG).show();
							c_handler.sendEmptyMessage(3);
						}
					}
			
		}.start();
	}
    
    public void q_getInfo(){
		new Thread(){
			public void run() {
				try {
					URL url=new URL("http://weixin.suda.edu.cn/servlet/TodayTrjnLogBrows?account=" + app.getAccount());
					HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
					httpConn.setConnectTimeout(10000);
					httpConn.setReadTimeout(10000);
					httpConn.setDoInput(true);
					httpConn.setDoOutput(true);
					// 此方法在正式链接之前设置才有效。
					httpConn.setRequestMethod("POST");
					httpConn.setUseCaches(false);
					// 正式创建链接
					httpConn.connect();
					InputStream inStream = httpConn.getInputStream();
					String encoding = httpConn.getContentEncoding();
					int read = -1;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((read = inStream.read()) != -1) {
						baos.write(read);
					}
					byte[] data = baos.toByteArray();
					baos.close();
					String content = null;
					if (encoding != null) {
						content = new String(data, encoding).trim();
					} else {
						content = new String(data).trim();
					}
					JSONTokener jsonParser = new JSONTokener(content);
					JSONObject card = (JSONObject) jsonParser.nextValue();
					String result = card.getString("result");
					if(result.equals("0")){
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("s1", "今日还未消费。");
						map.put("s2", "");
						map.put("s3", "");
						list.add(map);
						q_handler.sendEmptyMessage(0);
						return;
					}
					else{
						JSONArray l = new JSONArray(result); 
						for(int i=0; i<l.length(); i++){
							HashMap<String, Object> map = new HashMap<String, Object>();
							jsonParser = new JSONTokener(l.get(i).toString());
							JSONObject info = (JSONObject) jsonParser.nextValue();
							map.put("s1", "消费时间： " + info.getString("jndatetime"));
							map.put("s2", "消费地点： " + info.getString("sysname1"));
							map.put("s3", "消费金额： " + info.getString("FTranAmt") + "元");
							list.add(map);
						}
						q_handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Exception", e.toString());
	//				Toast.makeText(Cardindex_querybalance.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		}.start();
	}
    
    private void d_submit(final EditText pass){
		new Thread() {
			@Override
			public void run() {
						String url="http://weixin.suda.edu.cn/servlet/AccountDoLoss?account=" + setting.getString("account", "") + "&password=" + 
								pass.getText().toString() ;
						try {
							URL url1 = new URL(url);
							HttpURLConnection httpConn = (HttpURLConnection) url1.openConnection();
							httpConn.setConnectTimeout(10000);
							httpConn.setReadTimeout(10000);
							httpConn.setDoInput(true);
							httpConn.setDoOutput(true);
							// 此方法在正式链接之前设置才有效。
							httpConn.setRequestMethod("POST");
							httpConn.setUseCaches(false);
							// 正式创建链接
							httpConn.connect();
							InputStream inStream = httpConn.getInputStream();
							String encoding = httpConn.getContentEncoding();
							int read = -1;
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							while ((read = inStream.read()) != -1) {
								baos.write(read);
							}
							byte[] data = baos.toByteArray();
							baos.close();
							String content = null;
							if (encoding != null) {
								content = new String(data, encoding).trim();
							} else {
								content = new String(data).trim();
							}
							JSONTokener jsonParser = new JSONTokener(content);
							JSONObject card = (JSONObject) jsonParser.nextValue();
							String result = card.getString("result");
							jsonParser = new JSONTokener(result);
							JSONObject info = (JSONObject) jsonParser.nextValue();
							result = info.getString("resultin");
							if(result.equals("操作成功")){
								d_handler.sendEmptyMessage(0);
						//		Toast.makeText(CardindexActivity.this, "一卡通挂失成功，解除挂失请带好个人证件去校园卡部或者网络中心！", Toast.LENGTH_LONG).show();
						//		pass.setText("");
							}
							else if(result.equals("持卡人已挂失")){
								d_handler.sendEmptyMessage(1);
						//		Toast.makeText(CardindexActivity.this, "一卡通已挂失，解除挂失请带好个人证件去校园卡部或者网络中心！", Toast.LENGTH_LONG).show();
						//		pass.setText("");
							}
							else if(result.equals("密码错误")){
								d_handler.sendEmptyMessage(2);
						//		Toast.makeText(CardindexActivity.this, "对不起，密码错误！", Toast.LENGTH_LONG).show();
						//		pass.setText("");
							}
							else {
								d_handler.sendEmptyMessage(3);
						//		Toast.makeText(CardindexActivity.this, "对不起，一卡通挂失失败！", Toast.LENGTH_LONG).show();
						//		pass.setText("");
							}
						} catch (Exception e) {
							Log.v("Exception",e.toString());
							d_handler.sendEmptyMessage(4);
					//		Toast.makeText(CardindexActivity.this, "网络出错啦！", Toast.LENGTH_LONG).show();
						}
					
			}
		}.start();
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
					if(i==2 && flag==1){
						q_getInfo();
						flag=0;
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			 this.finish();
		 }
		 return true;
	}
	
	private static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient.newInstance("linux初学三月");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.e("cwjDebug", "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			getRequest.abort();
			Log.e("Debug", "Error while retrieving bitmap from " + url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int byt = read();
					if (byt < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	public class ImageDownloader {
		public void download(String url, ImageView imageView) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			task.execute(url);
		}
	}

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private final WeakReference<ImageView> imageViewReference; 
		// 使用WeakReference解决内存问题
		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// 实际的下载线程，内部其实是concurrent线程，所以不会阻塞
	        return downloadBitmap(params[0]);

		}

		@Override
		protected void onPostExecute(Bitmap bitmap) { // 下载完后执行的
			if (isCancelled()) {
				bitmap = null;
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap); // 下载完设置imageview为刚才下载的bitmap对象
				}
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 // TODO Auto-generated method stub
		 imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		 if(event.getAction() == MotionEvent.ACTION_DOWN){
			 if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
				 imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			 }
		 }
		 return super.onTouchEvent(event);
	 }
}
