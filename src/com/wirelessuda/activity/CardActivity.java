package com.wirelessuda.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.wirelessuda.R;
import com.wirelessuda.activity.CardApplication;
import com.wirelessuda.activity.NewsindexActivity.newsListAdapter1;
import com.wirelessuda.activity.NewsindexActivity.newsListAdapter2;
import com.wirelessuda.activity.NewsindexActivity.newsListAdapter3;
import com.wirelessuda.activity.NewsindexActivity.newsListAdapter4;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CardActivity extends Activity {

	private EditText account, password;
	private TextView tv1, tv2, tv3;
	private String username, pass, autoflag = "0";
	SharedPreferences setting;
	private CardApplication app;
	private CheckBox autologin;
	private Button login, goback;
	private InputMethodManager imm;
	private ProgressBar pb1;
	Handler myHandler;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_login);

		pb1 = (ProgressBar) findViewById(R.id.card_pb1);
		login = (Button) findViewById(R.id.cardLogin);
		goback = (Button) findViewById(R.id.card_goback);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		account = (EditText) findViewById(R.id.cardAccount);
		password = (EditText) findViewById(R.id.cardPass);
		autologin = (CheckBox) findViewById(R.id.cardAutoLogin);

		pb1.setVisibility(View.GONE);
		setting = getSharedPreferences("userInfo", 0);
		username = setting.getString("username", "");
		pass = setting.getString("password", "");
		app = (CardApplication) getApplicationContext();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		if (app.getLoginFlag().equals("1")) {
			Intent intent = new Intent(CardActivity.this,
					CardindexActivity.class);
			startActivity(intent);
			CardActivity.this.finish();
			return;
		}

		if (!username.equals("")) {
			account.setText(username);
		}

		if (!pass.equals("")) {
			password.setText(pass);
		}

		if (!username.equals("") && !pass.equals("")) {
			loading();
			loginThread();
		}

		autologin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					autoflag = "1";
				} else {
					autoflag = "0";
				}
			}
		});

		login.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				if (account.getText().toString().equals("")) {
					Toast.makeText(CardActivity.this, "账号不能为空！",
							Toast.LENGTH_LONG).show();
					account.setFocusable(true);
					return;
				} else if (password.getText().toString().equals("")) {
					Toast.makeText(CardActivity.this, "密码不能为空！",
							Toast.LENGTH_LONG).show();
					password.setFocusable(true);
					return;
				} else {
					loading();
					loginThread();
				}
			}
		});

		goback.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				CardActivity.this.finish();
			}
		});

		myHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					ending();
					Toast.makeText(CardActivity.this, "账号或者密码错误！",
							Toast.LENGTH_LONG).show();
					break;
				case 1:
					ending();
					Toast.makeText(CardActivity.this, "网络错误！",
							Toast.LENGTH_LONG).show();
					break;
				}
				// super.handleMessage(msg);
			}
		};
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			CardActivity.this.finish();
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	private void loginThread() {
		new Thread() {
			public void run() {
				try {
					String url = "";
					String flag = "";
					if (!username.equals(account.getText().toString())) {
						url = "http://weixin.suda.edu.cn/servlet/GetUserAllDetail?username="
								+ account.getText().toString()
								+ "&password="
								+ password.getText().toString()
								+ "&logintype=2&usertype=1";
						flag = "1";
					} else {
						url = "http://weixin.suda.edu.cn/servlet/LoginToCard?username="
								+ account.getText().toString()
								+ "&password="
								+ password.getText().toString()
								+ "&logintype=2&usertype=1";
						flag = "0";
					}
					// Toast.makeText(CardActivity.this, url,
					// Toast.LENGTH_LONG).show();
					// 得到Json解析成功之后数据
					String status = "";
					String account1 = "";
					URL url1 = new URL(url);
					HttpURLConnection httpConn = (HttpURLConnection) url1
							.openConnection();
					httpConn.setConnectTimeout(30000);
					httpConn.setReadTimeout(30000);
					httpConn.setDoInput(true);
					httpConn.setDoOutput(true);
					// 此方法在正式链接之前设置才有效。
					httpConn.setRequestMethod("POST");
					httpConn.setUseCaches(false);
					// 正式创建链接
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

					JSONTokener jsonParser = new JSONTokener(content);
					JSONObject card = (JSONObject) jsonParser.nextValue();
					String result = card.getString("result");
					// Toast.makeText(CardActivity.this, result,
					// Toast.LENGTH_LONG).show();
					jsonParser = new JSONTokener(result);
					JSONObject info = (JSONObject) jsonParser.nextValue();
					status = info.getString("status");
					// Toast.makeText(CardActivity.this, status,
					// Toast.LENGTH_LONG).show();
					if (status.equals("0")) {
						SharedPreferences.Editor editor = setting.edit();
						if (flag.equals("1")) {
							String photo = info.getString("photo");
							editor.putString("username", account.getText()
									.toString());
							editor.putString("photo", photo);
							// editor.putString("account", account1);
							String userDTO = info.getString("userDTO");
							String cardUserDTO = info.getString("cardUserDTO");

							jsonParser = new JSONTokener(userDTO);
							info = (JSONObject) jsonParser.nextValue();
							String cardId = info.getString("cardId");
							editor.putString("cardId", cardId);
							String sno = info.getString("sno");
							editor.putString("sno", sno);
							String identityID = info.getString("identityID");
							editor.putString("identityID", identityID);
							String userType = info.getString("userType");
							editor.putString("userType", userType);
							String name = info.getString("name");
							editor.putString("name", name);
							account1 = info.getString("account");
							app.setAccount(account1);
							editor.putString("account", account1);
							String deptCode = info.getString("deptCode");
							editor.putString("deptCode", deptCode);

							jsonParser = new JSONTokener(cardUserDTO);
							info = (JSONObject) jsonParser.nextValue();
							String iddept = info.getString("iddept");
							editor.putString("iddept", iddept);
							String pidname = info.getString("pidname");
							editor.putString("pidname", pidname);
							String idnumber = info.getString("idnumber");
							editor.putString("idnumber", idnumber);
							String peoplename = info.getString("peoplename");
							editor.putString("peoplename", peoplename);
							String sexname = info.getString("sexname");
							editor.putString("sexname", sexname);
							String flag1 = info.getString("flag");
							app.setFlag(flag1);
							String balance = info.getString("balance");
							app.setBalance(balance);
							String preTmpBalance = info
									.getString("preTmpBalance");
							app.setPreTmpBalance(preTmpBalance);
							String tmpBalance = info.getString("tmpBalance");
							app.setTmpBalance(tmpBalance);
						} else {
							account1 = info.getString("account");
							app.setAccount(account1);
							getUserPartDetail();
						}
						if (pass.equals("")) {
							if (autoflag.equals("1")) {
								editor.putString("password", password.getText()
										.toString());
							} else {
								editor.putString("password", "");
							}
						}
						editor.commit();
						app.setUsername(account.getText().toString());
						app.setLoginFlag("1");
						// Toast.makeText(CardActivity.this, app.getUsername(),
						// Toast.LENGTH_LONG).show();
						// Toast.makeText(CardActivity.this, app.getAccount(),
						// Toast.LENGTH_LONG).show();
						// Toast.makeText(CardActivity.this, app.getLoginFlag(),
						// Toast.LENGTH_LONG).show();
						Intent intent = new Intent(CardActivity.this,
								CardindexActivity.class);
						startActivity(intent);
						CardActivity.this.finish();
						return;
					} else {
						myHandler.sendEmptyMessage(0);
						return;
					}
				} catch (Exception e) {
					myHandler.sendEmptyMessage(1);
					return;
				}
			}
		}.start();
	}

	private void getUserPartDetail() {
		try {
			URL url = new URL(
					"http://weixin.suda.edu.cn/servlet/GetUserPartDetail?account="
							+ app.getAccount());
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(30000);
			httpConn.setReadTimeout(30000);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			// 此方法在正式链接之前设置才有效。
			httpConn.setRequestMethod("POST");
			httpConn.setUseCaches(false);
			// 正式创建链接
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
			JSONTokener jsonParser = new JSONTokener(content);
			JSONObject card = (JSONObject) jsonParser.nextValue();
			String result = card.getString("result");
			jsonParser = new JSONTokener(result);
			JSONObject info = (JSONObject) jsonParser.nextValue();
			String status = info.getString("status");
			if (status.equals("0")) {
				app.setBalance(info.getString("balance"));
				app.setFlag(info.getString("flag"));
				app.setPreTmpBalance("preTmpBalance");
				app.setTmpBalance("tmpBalance");
			} else {
				Toast.makeText(CardActivity.this, "网络错误！", Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(CardActivity.this, "网络错误！", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void loading() {
		pb1.setVisibility(View.VISIBLE);
		tv1.setVisibility(View.GONE);
		tv2.setVisibility(View.GONE);
		tv3.setVisibility(View.GONE);
		account.setVisibility(View.GONE);
		password.setVisibility(View.GONE);
		autologin.setVisibility(View.GONE);
		login.setVisibility(View.GONE);
	}

	private void ending() {
		pb1.setVisibility(View.GONE);
		tv1.setVisibility(View.VISIBLE);
		tv2.setVisibility(View.VISIBLE);
		tv3.setVisibility(View.VISIBLE);
		account.setVisibility(View.VISIBLE);
		password.setVisibility(View.VISIBLE);
		autologin.setVisibility(View.VISIBLE);
		login.setVisibility(View.VISIBLE);
	}
}
