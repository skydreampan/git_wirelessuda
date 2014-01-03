package com.wirelessuda.activity;


import com.wirelessuda.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 使用动画形式创建，没有使用线程方式
 * @author ppf
 *
 */
public class WelcomeActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//设置无标题栏
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
	    final View view = View.inflate(this,R.layout.welcome, null);
	    setContentView(view);
	    
	    
	    
    
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f); //透明度由低到高
		aa.setDuration(2000);
		view.startAnimation(aa);
		
		aa.setAnimationListener(new AnimationListener() {
			
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}


		});
	}
	/*跳转到开始页面*/
	private void redirectTo() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
