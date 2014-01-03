package com.wirelessuda.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.wirelessuda.R;
import com.wirelessuda.activity.CardApplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Cardindex_querybalance extends Activity {
	
	private TextView tv1;
	private SharedPreferences setting;
	private CardApplication app;
//	private HashMap<String, Object> map;
	private List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	private ListView lv;
	private URL url;
	private ProgressDialog pd;
	
	BaseAdapter ba = new BaseAdapter() {
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.list,null); //加载布局文件,定义没一行样式
			
			TextView tv1 = (TextView)convertView.findViewById(R.id.list_tv1);
			TextView tv2 = (TextView)convertView.findViewById(R.id.list_tv2);
			TextView tv3 = (TextView)convertView.findViewById(R.id.list_tv3);
			tv1.setText(list.get(position).get("s1").toString());
			tv2.setText(list.get(position).get("s2").toString());
			tv3.setText(list.get(position).get("s3").toString());
			tv1.setTextColor(Color.BLACK);
			tv2.setTextColor(Color.BLACK);
			tv3.setTextColor(Color.BLACK);
			
			return convertView;
			
		}
		
		public long getItemId(int position) {
			return 0;
		}
		
		public Object getItem(int arg0) {
			return null;
		}
		
		public int getCount() {
			return list.size();
		}
	};
	
	Handler myHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:		
				lv.setAdapter(ba);  
				break;
			}
		//	super.handleMessage(msg);
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cardindex_querybalance);
		pd = ProgressDialog.show(Cardindex_querybalance.this, "请稍候", "正在获取信息...",true,true);
		setting = getSharedPreferences("userInfo", 0);
		app = (CardApplication)getApplicationContext();
		cardindex_querybalance_getInfo();
		tv1 = (TextView)findViewById(R.id.cardindex_querybalance_tv1);
		lv = (ListView)findViewById(R.id.cardindex_querybalance_lv);
		
		tv1.setText("您的卡内余额： "+ Float.parseFloat(app.getBalance())/100 +" 元");
	}
	
	public void cardindex_querybalance_getInfo(){
		new Thread(){
			public void run() {
				try {
					url=new URL("http://weixin.suda.edu.cn/servlet/TodayTrjnLogBrows?account=" + app.getAccount());
					HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
					httpConn.setConnectTimeout(10000);
					httpConn.setReadTimeout(10000);
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
					pd.dismiss();
					JSONTokener jsonParser = new JSONTokener(content);
					JSONObject card = (JSONObject) jsonParser.nextValue();
					String result = card.getString("result");
					if(result.equals("0")){
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("s1", "今日还未消费。");
						map.put("s2", "");
						map.put("s3", "");
						list.add(map);
						myHandler.sendEmptyMessage(0);
						return;
					}
					else{
				//		List<HashMap<String, Object>> l = JSONToList(result);
						JSONArray l = new JSONArray(result); 
						for(int i=0; i<l.length(); i++){
							HashMap<String, Object> map = new HashMap<String, Object>();
							jsonParser = new JSONTokener(l.get(i).toString());
							JSONObject info = (JSONObject) jsonParser.nextValue();
					//		String s= (i+1) + "、 消费时间：" + info.getString("jndatetime")+ " 消费地点：" + info.getString("sysname1") + 
					//				" 消费金额：" + info.getString("FTranAmt") + "元 消费之后卡内余额：" + info.getString("FCardBalance") + "元";
							map.put("s1", "消费时间： " + info.getString("jndatetime"));
							map.put("s2", "消费地点： " + info.getString("sysname1"));
							map.put("s3", "消费金额： " + info.getString("FTranAmt") + "元");
							list.add(map);
						}
						myHandler.sendEmptyMessage(0);
					}
					   
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Exception", e.toString());
					Toast.makeText(Cardindex_querybalance.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		}.start();
	}
	
	/** 
     * JSON转map 
     * tips：支持多组相同JSON数据的传输 
     * @param string 
     * <A href='\"http://www.eoeandroid.com/home.php?mod=space&uid=7300\"' target='\"_blank\"'>@return</A> List<Map<String,String>> 
     * @author DustFinger 
     *  
     */
	/*
    @SuppressWarnings({ "unchecked" }) 
    public static List<HashMap<String,Object>> JSONToList(String string){ 
            try { 
              	JSONArray Data_jsonArray = new JSONArray(string); 
              	if( !Data_jsonArray.isNull(0)){ 
                 	JSONObject jobj = Data_jsonArray.getJSONObject(0); 
                 	@SuppressWarnings("rawtypes") 
                 	ArrayList keys = new ArrayList() ; 
                 	int keys_posi = 0; 
                  	Iterator<String> IT = jobj.keys(); 
                 	while(IT.hasNext()){ 
                     	keys.add(keys_posi, IT.next()); 
                      	System.out.println(keys.get(keys_posi)); 
                     	keys_posi ++; 
                   	} 
                  	List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(); 
                  	for(int i = 0; i < Data_jsonArray.length(); i ++){ 
                      	HashMap<String, Object> map = new HashMap<String,Object>(); 
                     	for(int j = 0; j < keys.size(); j++){ 
                        	map.put(keys.get(j).toString(), Data_jsonArray.getJSONObject(i).getString(keys.get(j).toString())); 
                     	} 
                     	list.add(map); 
                  	} 
                	return list; 
             	} 
            	return null; 
            } catch (Exception e) { 
             	e.printStackTrace();
              	Log.e("json", "json转list出错."); 
              	return null; 
        	} 
    }
    */
	
}
