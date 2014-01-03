package com.wirelessuda.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wirelessuda.model.ImageNew;



public class FilterString {
	
	 
   public static String filter(String a)
	 {
	  String b=new String();
	  int i,j,t;
	  for(i=0;i<a.length();i++)
	  if(a.charAt(i)!='\\' )
	  b=b+a.charAt(i);
	  return b;
	 }
   public static ArrayList<ImageNew> List (String jsonStr) throws JSONException {
	   /******************* 解析 ***********************/
	           JSONArray jsonArray = null;
	   // 初始化list数组对象
	           ArrayList<ImageNew> list = new ArrayList<ImageNew>();
	           JSONObject json = new JSONObject(jsonStr);
	           
	           jsonArray = json.getJSONArray("news");
	           for (int i = 0; i < jsonArray.length(); i++) {
	        	   		JSONObject jsonObject = jsonArray.getJSONObject(i);
	        	   		String jsonString = jsonObject.getString("new"+i);
	        	   		JSONObject jsonObj2 = new JSONObject(jsonString);
	        	   		String photoLink = jsonObj2.getString("photo");
	        	   		String detail = jsonObj2.getString("detail");
						
	        	   		ImageNew imageNew = new ImageNew();
	        	   		imageNew.setPhotoLink(photoLink);
	        	   		imageNew.setDetail(detail);
	        	   		list.add(imageNew);
						}
	   return list;
	       }
}
