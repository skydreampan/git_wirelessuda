package com.wirelessuda.activity;

import com.wirelessuda.R;
import com.wirelessuda.R.layout;
import com.wirelessuda.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BusActivity extends Activity {


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bus, menu);
		return true;
	}

}
