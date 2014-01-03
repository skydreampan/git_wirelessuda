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

public class Cardindex_changepass extends Activity {
	
	private TextView tv1;
	private EditText oldPass, newPass, repeatPass;
	private SharedPreferences setting;
	private int ScreenWIDTH;
	private ProgressDialog pd;
	private CardApplication app;
	private Button submit, cancel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardindex_changepass);
		
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		//��ȡ��ǰ�豸����Ļ���
		ScreenWIDTH = dMetrics.widthPixels;
		
		setting = getSharedPreferences("userInfo", 0);
		app = (CardApplication)getApplicationContext();
		tv1 = (TextView)findViewById(R.id.cardindex_changepass_tv1);
		oldPass = (EditText)findViewById(R.id.cardindex_changepass_oldPass);
		newPass = (EditText)findViewById(R.id.cardindex_changepass_newPass);
		repeatPass = (EditText)findViewById(R.id.cardindex_changepass_repeatPass);
		submit = (Button)findViewById(R.id.cardindex_changepass_submit);
		cancel = (Button)findViewById(R.id.cardindex_changepass_cancel);
		
		oldPass.setText("");
		newPass.setText("");
		repeatPass.setText("");
		
		//����Ӧ��Ļ���
		tv1.setWidth(ScreenWIDTH/2);
		
		submit.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				if(oldPass.getText().toString().equals("")){
		    		Toast.makeText(Cardindex_changepass.this, "ԭ���벻��Ϊ�գ�", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else if(newPass.getText().toString().equals("")){
		    		Toast.makeText(Cardindex_changepass.this, "�����벻��Ϊ�գ�", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else if(!newPass.getText().toString().equals(repeatPass.getText().toString())){
		    		Toast.makeText(Cardindex_changepass.this, "2���������벻һ�£�", Toast.LENGTH_LONG).show();
					return;
		    	}
		    	else {
		    		changePassSubmit(oldPass.getText().toString(),newPass.getText().toString());
		    	}
			}
		});
		
		cancel.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				oldPass.setText("");
				newPass.setText("");
				repeatPass.setText("");
			}
		});
	}

	private void changePassSubmit(final String oldPass, final String newPass){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// ��Ҫ���е��߼�����
				runOnUiThread(new Runnable() {
					public void run() {
						pd = ProgressDialog.show(Cardindex_changepass.this, "���Ժ�", "�����ύ����...",true,true);
						String url="http://weixin.suda.edu.cn/servlet/ModifyPsd?account=" + setting.getString("account", "") + "&password=" + 
								oldPass + "&newpassword=" + newPass;
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
								Toast.makeText(Cardindex_changepass.this, "�����޸ĳɹ��������µ�¼��", Toast.LENGTH_LONG).show();
								SharedPreferences.Editor editor = setting.edit();
								editor.clear();
								editor.commit();
								app.setLoginFlag("");
								app.setAccount("");
								app.setUsername("");
								Intent  newsIntent = new Intent(Cardindex_changepass.this,CardActivity.class);
								startActivity(newsIntent);
								Cardindex_changepass.this.finish();
							}
							else if(result.equals("�������")){
								Toast.makeText(Cardindex_changepass.this, "�Բ���ԭ�������", Toast.LENGTH_LONG).show();
							}
							else{
								Toast.makeText(Cardindex_changepass.this, "�Բ��������޸�ʧ�ܣ�", Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							Log.v("Exception",e.toString());
							pd.dismiss();
							Toast.makeText(Cardindex_changepass.this, "�����������", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}).start();
	}
}
