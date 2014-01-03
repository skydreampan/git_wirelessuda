package com.wirelessuda.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ReadHelp {
	
	public static void ShowImg(String uri, ImageView iv) throws IOException {    
		
        FileInputStream fs = new FileInputStream(uri);  
        BufferedInputStream bs = new BufferedInputStream(fs);  
        Bitmap btp = BitmapFactory.decodeStream(bs);  
        iv.setImageBitmap(btp);  
        bs.close();  
        fs.close();  
        btp = null;
}  
}
