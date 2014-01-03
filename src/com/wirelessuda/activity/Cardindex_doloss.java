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
		//��ȡ��ǰ�豸����Ļ���
		ScreenWIDTH = dMetrics.widthPixels;
		
		setting = getSharedPreferences("userInfo", 0);
		app = (CardApplication)getApplicationContext();
		tv1 = (TextView)findViewById(R.id.cardindex_doloss_tv2);
		pass = (EditText)findViewById(R.id.cardindex_doloss_pass);
		submit = (Button)findViewById(R.id.cardindex_doloss_submit);
		cancel = (Button)findViewById(R.id.cardindex_doloss_cancel);
		
		pass.setText("");
		
		//����Ӧ��Ļ���
		tv1.setWidth(ScreenWIDTH/2);
		
		submit.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				if(pass.getText().toString().equals("")){
		    		Toast.makeText(Cardindex_doloss.this, "���벻��Ϊ�գ�", Toast.LENGTH_LONG).show();
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
				// ��Ҫ���е��߼�����
				runOnUiThread(new Runnable() {
					public void run() {
						pd = ProgressDialog.show(Cardindex_doloss.this, "���Ժ�", "�����ύ����...",true,true);
						String url="http://weixin.suda.edu.cn/servlet/AccountDoLoss?account=" + setting.getString("account", "") + "&password=" + 
								pass.getText().toString() ;
						try {
							URL url1 = new URL(url);
							HttpURLConnection httpConn = (HttpURLConnection) url1.openConnection();
							httpConn.setConnectTimeout(10000);
							httpConn.setReadTimeout(10000);
							httpConn.setDoInput(true);
							httpConn.setDoOutput(true);
							// �˷�������ʽ����֮ǰ���ò���Ч��
							httpConn.setRequestMethod("POST");
							httpConn.setUseCaches(false);
							// ��ʽ��������
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
							if(result.equals("�����ɹ�")){
								Toast.makeText(Cardindex_doloss.this, "һ��ͨ��ʧ�ɹ��������ʧ����ø���֤��ȥУ԰���������������ģ�", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
							else if(result.equals("�ֿ����ѹ�ʧ")){
								Toast.makeText(Cardindex_doloss.this, "һ��ͨ�ѹ�ʧ�������ʧ����ø���֤��ȥУ԰���������������ģ�", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
							else if(result.equals("�������")){
								Toast.makeText(Cardindex_doloss.this, "�Բ����������", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
							else {
								Toast.makeText(Cardindex_doloss.this, "�Բ���һ��ͨ��ʧʧ�ܣ�", Toast.LENGTH_LONG).show();
								pass.setText("");
								return;
							}
						} catch (Exception e) {
							Log.v("Exception",e.toString());
							pd.dismiss();
							Toast.makeText(Cardindex_doloss.this, "�����������", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}).start();
	}
}
