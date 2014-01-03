package com.wirelessuda.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.wirelessuda.R;
import com.wirelessuda.activity.CardApplication;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Cardindex_doloss extends Activity {
	
	private TextView tv1;
	private EditText pass;
	private SharedPreferences setting;
	private int ScreenWIDTH;
	private ProgressDialog pd;
	private CardApplication app;
	private Button submit, cancel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardindex_doloss);
		
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		//获取当前设备的屏幕宽度
		ScreenWIDTH = dMetrics.widthPixels;
		
		setting = getSharedPreferences("userInfo", 0);
		app = (CardApplication)getApplicationContext();
		tv1 = (TextView)findViewById(R.id.cardindex_doloss_tv2);
		pass = (EditText)findViewById(R.id.cardindex_doloss_pass);
		submit = (Button)findViewById(R.id.cardindex_doloss_submit);
		cancel = (Button)findViewById(R.id.cardindex_doloss_cancel);
		
		pass.setText("");
		
		//自适应屏幕宽度
		tv1.setWidth(ScreenWIDTH/2);
		
		submit.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				if(pass.getText().toString().equals("")){
		    		Toast.makeText(Cardindex_doloss.this, "密码不能为空！", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else {
		    		dolossSubmit(pass);
		    	}
			}
		});
		
		cancel.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				pass.setText("");
			}
		});
	}

	private void dolossSubmit(final EditText pass){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 需要进行的逻辑处理
				runOnUiThread(new Runnable() {
					public void run() {
						pd = ProgressDialog.show(Cardindex_doloss.this, "请稍候", "正在提交请求...",true,true);
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
							pd.dismiss();
							JSONTokener jsonParser = new JSONTokener(content);
							JSONObject card = (JSONObject) jsonParser.nextValue();
							String result = card.getString("result");
							jsonParser = new JSONTokener(result);
							JSONObject info = (JSONObject) jsonParser.nextValue();
							result = info.getString("resultin");
							if(result.equals("操作成功")){
								Toast.makeText(Cardindex_doloss.this, "一卡通挂失成功，解除挂失请带好个人证件去校园卡部或者网络中心！", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
							else if(result.equals("持卡人已挂失")){
								Toast.makeText(Cardindex_doloss.this, "一卡通已挂失，解除挂失请带好个人证件去校园卡部或者网络中心！", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
							else if(result.equals("密码错误")){
								Toast.makeText(Cardindex_doloss.this, "对不起，密码错误！", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
							else {
								Toast.makeText(Cardindex_doloss.this, "对不起，一卡通挂失失败！", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
						} catch (Exception e) {
							Log.v("Exception",e.toString());
							pd.dismiss();
							Toast.makeText(Cardindex_doloss.this, "网络出错啦！", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}).start();
	}
}
