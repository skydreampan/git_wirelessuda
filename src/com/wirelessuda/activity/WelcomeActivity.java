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
 * ʹ�ö�����ʽ������û��ʹ���̷߳�ʽ
 * @author ppf
 *
 */
public class WelcomeActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//�����ޱ�����
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
	    final View view = View.inflate(this,R.layout.welcome, null);
	    setContentView(view);
	    
	    
	    
    
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f); //͸�����ɵ͵���
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
	/*��ת����ʼҳ��*/
	private void redirectTo() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
