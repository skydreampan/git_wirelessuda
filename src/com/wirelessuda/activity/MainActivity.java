package com.wirelessuda.activity;


import com.wirelessuda.layout.GalleryFlow;
import com.wirelessuda.layout.ImageAdapter;
import com.wirelessuda.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener{

	ImageButton mImgBtnNews,mImgBtnGateway,mImgBtnCard,mImgBtnNotice;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/*
		//��ȡSharedPreferences����Ҫ������
		SharedPreferences preferences = getSharedPreferences("startCount",MODE_PRIVATE);
        int count = preferences.getInt("count",0); //�ڶ������������ֵ�����ھͷ���0
        //�жϳ����ǵڼ������У�����ǵ�һ����������ת������ҳ��
        if (count == 0) {
           Intent intent = new Intent(this,FirstStartActivity.class);
            startActivity(intent);
            finish();
        }
        Editor editor = preferences.edit();
        editor.putInt("count", ++count);
        editor.commit();
		*/
		
	   //����gallery ����ͼƬ
		Integer[] images = { R.drawable.bus, R.drawable.calendar,
				 
                R.drawable.card, R.drawable.gateway, R.drawable.news,
 
                R.drawable.settings, R.drawable.weather};  //����ͼƬ����
 
        ImageAdapter adapter = new ImageAdapter(this, images);
 
        adapter.createReflectedImages();
 
        GalleryFlow galleryFlow = (GalleryFlow) findViewById(R.id.Gallery01);
 
        galleryFlow.setAdapter(adapter);   
        
        mImgBtnNews = (ImageButton) findViewById(R.id.imageButtonNews);
        mImgBtnGateway = (ImageButton) findViewById(R.id.imageButtonGateway);
        mImgBtnCard = (ImageButton) findViewById(R.id.imageButtonCard);
        mImgBtnNotice=(ImageButton) findViewById(R.id.imageButtonNotice);
        
        mImgBtnNews.setOnClickListener(this);
        mImgBtnGateway.setOnClickListener(this);
        mImgBtnCard.setOnClickListener(this);
        mImgBtnNotice.setOnClickListener(this);
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		menu.add(menu.NONE,menu.FIRST + 1, 1, "bus").setIcon(R.drawable.bus);
		menu.add(menu.NONE,menu.FIRST + 2, 2, "calendar").setIcon(R.drawable.calendar);
		menu.add(menu.NONE,menu.FIRST + 3, 3, "card").setIcon(R.drawable.card);
		menu.add(menu.NONE,menu.FIRST + 4, 4, "gateway").setIcon(R.drawable.gateway);
		menu.add(menu.NONE,menu.FIRST + 5, 5, "news").setIcon(R.drawable.news);
		menu.add(menu.NONE,menu.FIRST + 6, 6, "weather").setIcon(R.drawable.weather);
		menu.add(menu.NONE,menu.FIRST + 7, 7, "settings").setIcon(R.drawable.settings);
		return true;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v == mImgBtnNews){
			Intent  newsIntent = new Intent(this,NewsindexActivity.class);
			startActivity(newsIntent);
		}
		if(v == mImgBtnGateway){
			Intent  intent = new Intent(this,GatewayActivity.class);
			startActivity(intent);
		}
		if(v == mImgBtnCard){
			Intent  intent = new Intent(this,CardActivity.class);
			startActivity(intent);
		}
		if(v == mImgBtnNotice){
			Intent  newsIntent = new Intent(this,NoticeindexActivity.class);
			startActivity(newsIntent);
		}
	}
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	dialog();
    	
    }
    //�˳��Ի���
    protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this); //���ؼ�����ȡ��
		builder.setMessage("ȷ���˳���")
		.setCancelable(false)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			//�˳����򣬶���淽���˳���finish����
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			/*	Intent intent = new Intent(Intent.ACTION_MAIN);  //�ȵ�home���棬�ٽ�������
	            intent.addCategory(Intent.CATEGORY_HOME);  
	            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
	            startActivity(intent);  */
	            android.os.Process.killProcess(Process.myPid());  
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}); //�����Ի������
		builder.create().show();
		
    }

}
