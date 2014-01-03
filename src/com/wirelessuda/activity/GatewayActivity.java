package com.wirelessuda.activity;

import com.wirelessuda.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class GatewayActivity extends Activity{
	
	private WebView wv;
	private View v;
	private Button goback;
	private ProgressBar pb1;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gateway);
		
		wv = (WebView)findViewById(R.id.webView1);
		goback = (Button)findViewById(R.id.gateway_goback);
		v = (View)findViewById(R.id.gateway_view);
		pb1 = (ProgressBar)findViewById(R.id.gateway_pb);
		
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(0);
		WebSettings webSettings = wv.getSettings();
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		wv.loadUrl("http://wg.suda.edu.cn/");
		//加载数据
		wv.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					v.setVisibility(View.GONE);
					pb1.setVisibility(view.GONE);
				}
			}
		});
		//这个是当网页上的连接被点击的时候
		wv.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
				wv.loadUrl(url);
				v.setVisibility(View.VISIBLE);
				pb1.setVisibility(view.VISIBLE);
				return true;
			}
		});
		
		goback.setOnClickListener(new android.view.View.OnClickListener() { 
			public void onClick(View v) {
				GatewayActivity.this.finish();
			}
		});
	}
	
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (wv.canGoBack() && keyCoder == KeyEvent.KEYCODE_BACK) {
			wv.goBack();
			return true;
		}
		else if(keyCoder == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			GatewayActivity.this.finish();
		}
		return false;
	}
/*
	protected void exitDialog() {
		AlertDialog.Builder builder = new Builder(GatewayActivity.this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		  builder.setNegativeButton("确认", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.dismiss();
				   GatewayActivity.this.finish();
			   }
		  });
		  builder.setPositiveButton("取消", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.dismiss();
			   }
		  });
		  builder.create().show();
	}*/
}
