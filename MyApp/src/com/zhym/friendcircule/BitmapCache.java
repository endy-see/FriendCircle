package com.zhym.friendcircule;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * Created by lenovo on 2015/9/28.
 */
public class BitmapCache {
    HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<>();
    Handler handler = new Handler();

    public void put(String path, Bitmap bitmap) {
        if(!TextUtils.isEmpty(path) && bitmap != null) {
            imageCache.put(path, new SoftReference<>(bitmap));
        }
    }

    public void displayBmp(final ImageView imageView, final String thumbPath, final String sourcePath, final ImageCallback callback) {
        final String path;
        final boolean isThumb;
        //确定缩略图路径
        if(!TextUtils.isEmpty(thumbPath)) {
            path = thumbPath;
            isThumb = true;
        } else if(!TextUtils.isEmpty(sourcePath)) {
            path = sourcePath;
            isThumb = false;
        } else {
            Log.e("BitmapCache:displayBmp","路径不存在！请检查。。");
            return;
        }

        //判断缓存imageCache中是否有key=path(此步可以省略。。，开始new Thread同样效果。。）
        if(imageCache.containsKey(path)) {
            SoftReference<Bitmap> sr = imageCache.get(path);
            Bitmap bmp = sr.get();
            if(bmp != null && callback != null) {
                callback.imageLoad(imageView, bmp, sourcePath);
            }
        }

//        imageView.setImageBitmap(null);

        //在新线程中要完成的任务：1.构建缓存 2.handler将缓存中的缩略图一起设置到各个ImageView
        new Thread(new Runnable() {
            Bitmap thumb;

            @Override
            public void run() {
                try {
                    if(isThumb) {   //通过revitionImageSize(...)方法返回的bitmap其实都不为空
                        thumb = (BitmapFactory.decodeFile(thumbPath) != null) ? BitmapFactory.decodeFile(thumbPath):
                                (revitionImageSize(sourcePath) != null ?  revitionImageSize(sourcePath) : TestPicActivity.thumb_path_null_bmp);
                    } else {
                        thumb = (revitionImageSize(sourcePath) != null) ?  revitionImageSize(sourcePath) : TestPicActivity.source_path_numm_bmp;
                    }

                    //将缩略图路径和缩略图对应起来放入缓存
                    put(path, thumb);
//                    imageCache.put(path, new SoftReference<>(thumb));

                    if(callback != null) {
                        /*
                        将下面这一段注释掉以后出现异常：
                        System.err﹕ at com.zhym.friendcircule.BitmapCache$1.run(BitmapCache.java:79),
                        即run()中的callback.imageLoad(...)
                        出错原因：不能在非主线程中给控件imageView赋值thumb
                        解决：在非主线程中刷新UI，需要使用Handler
                        * */

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.imageLoad(imageView, thumb, sourcePath);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //麻蛋，真相了。。。下面的循环其实完全不起作用。。这整个一段只需要一句话就可以实现一样的效果：
    //即 return BitmapFactory.decodeFile(sourcePath);
    public Bitmap revitionImageSize(String sourcePath) throws IOException {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(sourcePath)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options); //此处由于设置了inJustDecodeBounds = true，故bmp=null
            Bitmap bitmap ;
            int i = 0;
            while(true) {
                if(options.outWidth >> i <=256 && options.outHeight >> i <= 256) {
//                    int yuanWidth = options.outWidth;
//                    int yuanHeight = options.outHeight;
//                    int width = options.outWidth >> i;
//                    int height = options.outHeight >> i;
//                    Log.e(sourcePath, "原宽高="+yuanWidth+"*"+yuanHeight+", 现宽高="+width+"*"+height+", i="+i);
                    in = new BufferedInputStream(new FileInputStream(new File(sourcePath)));
                    options.inSampleSize = (int) Math.pow(2, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i++;
            }

        return bitmap;

    }

    public interface ImageCallback {
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
    }

}
