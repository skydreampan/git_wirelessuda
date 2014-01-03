package com.wirelessuda.activity;

import com.wirelessuda.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class Cardindex_userinfo extends Activity {
	
	private TextView account, sno, name, idnumber, pidname, iddept, peoplename, sexname, cardId, tv1;
	private SharedPreferences setting;
	private int ScreenWIDTH;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardindex_userinfo);
		
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		//获取当前设备的屏幕宽度
		ScreenWIDTH = dMetrics.widthPixels;
		setting = getSharedPreferences("userInfo", 0);
		account = (TextView)findViewById(R.id.cardindex_userinfo_account);
		sno = (TextView)findViewById(R.id.cardindex_userinfo_sno);
		name = (TextView)findViewById(R.id.cardindex_userinfo_name);
		idnumber = (TextView)findViewById(R.id.cardindex_userinfo_idnumber);
		pidname = (TextView)findViewById(R.id.cardindex_userinfo_pidname);
		iddept = (TextView)findViewById(R.id.cardindex_userinfo_iddept);
		peoplename = (TextView)findViewById(R.id.cardindex_userinfo_peoplename);
		sexname = (TextView)findViewById(R.id.cardindex_userinfo_sexname);
		cardId = (TextView)findViewById(R.id.cardindex_userinfo_cardId);
		tv1 = (TextView)findViewById(R.id.cardindex_userinfo_tv1);
		
		//自适应屏幕宽度
		tv1.setWidth(ScreenWIDTH/2);
		
		if(!setting.getString("account", "").equals("")){
			account.setText(setting.getString("account", ""));
		}
		if(!setting.getString("sno", "").equals("")){
			sno.setText(setting.getString("sno", ""));
		}
		if(!setting.getString("name", "").equals("")){
			name.setText(setting.getString("name", ""));
		}
		if(!setting.getString("idnumber", "").equals("")){
			idnumber.setText(setting.getString("idnumber", ""));
		}
		if(!setting.getString("pidname", "").equals("")){
			pidname.setText(setting.getString("pidname", ""));
		}
		if(!setting.getString("iddept", "").equals("")){
			iddept.setText(setting.getString("iddept", ""));
		}
		if(!setting.getString("peoplename", "").equals("")){
			peoplename.setText(setting.getString("peoplename", ""));
		}
		if(!setting.getString("sexname", "").equals("")){
			sexname.setText(setting.getString("sexname", ""));
		}
		if(!setting.getString("cardId", "").equals("")){
			cardId.setText(setting.getString("cardId", ""));
		}
	}

}
