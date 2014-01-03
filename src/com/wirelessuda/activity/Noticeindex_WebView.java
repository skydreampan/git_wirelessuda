package com.wirelessuda.activity;

import com.wirelessuda.R;
import com.wirelessuda.R.layout;
import com.wirelessuda.R.menu;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Noticeindex_WebView extends Activity {
	private WebView wv;  
	private ProgressBar pb1;
    private Handler mHandler = new Handler();  
    private Button goback;
       
    public void onCreate(Bundle icicle) { 
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);       
        setContentView(R.layout.noticeindex_web); 
    
        wv = (WebView) findViewById(R.id.wvNews);
        goback = (Button)findViewById(R.id.news_item_web_view_goback);
        pb1 = (ProgressBar)findViewById(R.id.pb1);
        
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(0);
		WebSettings webSettings = wv.getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		String url1 = "http://jsglxt.suda.edu.cn/noticeDetail.action?detailUrl=";
		String link1 = getIntent().getStringExtra("link");
//		Toast.makeText(Noticeindex_WebView.this, link1, Toast.LENGTH_LONG).show();
		url1 += link1;
		//本地，xml
		//url1 = "file:///android_asset/index.html ";
		wv.loadUrl(url1);
		//加载数据
		WebSettings settings = wv.getSettings();
		//这方法可以让你的页面适应手机屏幕的分辨率，完整的显示在屏幕上，可以放大缩小
		settings.setUseWideViewPort(true); 
		settings.setLoadWithOverviewMode(true); 
		wv.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
//					wv.setVisibility(view.VISIBLE);
					pb1.setVisibility(view.GONE);
				}
			}
		});
		
		goback.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				Noticeindex_WebView.this.finish();
			}
		});
    }
		
    //返回按钮
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
//    	Intent intent=new Intent(this, ReadNoticeActivity.class);   
 //       startActivity(intent);   
        this.finish();
    	super.onBackPressed();
    }
}
