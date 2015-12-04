package com.zhym.friendcircule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bimp {
	public static int max = 0;
	public static boolean act_bool = true;
//    public static Map<String, String> state_photos = new HashMap<>();
	public static List<Bitmap> bmp = new ArrayList<>();
    public static List<String> shuoshuo = new ArrayList<>();
    public static List<String> allPhotosStr = new ArrayList<>();

	
	public static List<String> drr = new ArrayList<>();                     //存放图片的绝对路径

    //图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static Bitmap revitionImageSize(String path){
        BufferedInputStream in = null;
        Bitmap bitmap = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));

        BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);          //null
		in.close();
		int i = 0;
		while (true) {
			if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {  //相比较256,1000会更加清晰
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}
