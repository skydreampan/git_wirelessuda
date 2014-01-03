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

public class Newsindex_WebView extends Activity {        
    private WebView wv;  
    private Button goback;
    private ProgressBar pb1;
    private Handler mHandler = new Handler();       
       
    public void onCreate(Bundle icicle) {   
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(icicle);       
        setContentView(R.layout.newsindex_web); 
        
        wv = (WebView) findViewById(R.id.wvNews);
        goback = (Button)findViewById(R.id.news_item_web_view_goback);
        pb1 = (ProgressBar)findViewById(R.id.pb1);
        
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(0);
		WebSettings webSettings = wv.getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		String url1 = "http://jsglxt.suda.edu.cn/feedDetail.action?detailUrl=";
		String link1 = getIntent().getStringExtra("link");
		url1 += link1;
		wv.loadUrl(url1);
		WebSettings settings = wv.getSettings();
		//这方法可以让你的页面适应手机屏幕的分辨率，完整的显示在屏幕上，可以放大缩小
		settings.setUseWideViewPort(true); 
		settings.setLoadWithOverviewMode(true); 
		
		wv.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
			/*	if (newProgress == 100) {
					NewsItemWebViewActivity.this.setTitle("加载完成");
				} else {
					NewsItemWebViewActivity.this.setTitle("加载中.......");
				}*/
				if (newProgress == 100) {
					wv.setVisibility(view.VISIBLE);
					pb1.setVisibility(view.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
		
		goback.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				Newsindex_WebView.this.finish();
			}
		});
        /*
        mWebView = (WebView) findViewById(R.id.wvNews);       
        WebSettings webSettings = mWebView.getSettings();       
        webSettings.setJavaScriptEnabled(true);       
        mWebView.addJavascriptInterface(new Object() {       
            public void clickOnAndroid() {       
                mHandler.post(new Runnable() {       
                    public void run() {       
                        mWebView.loadUrl("javascript:wave()");       
                    }       
                });       
            }       
        }, "demo");    
        String url1 = "http://jsglxt.suda.edu.cn/feedDetail.action?detailUrl=";
		String link1 = getIntent().getStringExtra("link");
		url1 += link1;
		pd = ProgressDialog.show(NewsItemWebViewActivity.this, "请稍候", "正在刷新...",true,true);
        mWebView.loadUrl(url1);
        
        pd.dismiss();
        */
    }
    //返回按钮
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    //	Intent intent=new Intent(this, NewsindexActivity.class);   
    //    startActivity(intent);   
    	this.finish();
    	super.onBackPressed();
    }
    
} 
