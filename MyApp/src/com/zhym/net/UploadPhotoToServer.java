package com.zhym.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhym.friendcircule.FileUtils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2015/11/5.
 */
public class UploadPhotoToServer {
    Context context;
    String imageAbsPath;
    String actionUrl = "";
    public UploadPhotoToServer(Context context, String url, String imageAbsPath) {
        this.context = context;
        this.actionUrl = url;
        this.imageAbsPath = imageAbsPath;
    }

    public void uploadPhoto(final DataCallback dataCallback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 110 && dataCallback != null) {
                    String result = (String) msg.obj;
                    if(result != null) {
                        Log.e("UploadPhotoToServer:uploadPhoto", "result=msg.obj="+msg.obj+", 开始执行dataCallback.onDataCallback(String)");
                        dataCallback.onDataCallback(result);                //将从新线程（网络上传、下载）中从服务端获得的信息设置为回调函数的dataCallback.onDataCallback的传入参数。当在其他程序需要服务端返回的数据即result时，只需要需要实现回调函数即可从其参数中获得所需结果
                    } else {
                        Log.e("UploadPhotoToServer:uploadPhoto", "访问服务器出错。。");

                    }
                } else if(msg.what == 111 & dataCallback != null) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if(bitmap != null) {
                        Log.e("UploadPhotoToServer:uploadPhoto","成功从服务器端下载说说中的图片！，给回调函数dataCallback.onDataCallback(bitmap)设置参数");
                        dataCallback.onDataCallback(bitmap);
                    }
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                String end = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                try {
                    URL url = new URL(actionUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    Message msg = new Message();

                    if(imageAbsPath != null) {          //图片路径不为null，视为上传图片，否则是下载图片
                        con.setDoInput(true);
                        con.setDoOutput(true);
                        con.setUseCaches(false);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Connection", "Keep-Alive");
                        con.setRequestProperty("Charset", "UTF-8");
                        con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                        dos.writeBytes(twoHyphens + boundary + end);
                        dos.writeBytes("Content-Disposition:form-data;" +
                                "name=\"file\";filename=\"" +
                                FileUtils.getImageNameFromPath(imageAbsPath) + "\"" + end);
                        dos.writeBytes(end);
                        Log.e("UploadPhotoToServer:uploadPhoto", "*******************3333333333333");
                        FileInputStream fis = new FileInputStream(imageAbsPath);
                        int bufferSize = 8192;
                        byte[] buffer = new byte[bufferSize];

                        int length;
                        while ((length = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, length);
                        }
                        dos.writeBytes(end);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + end);

                        fis.close();
                        dos.flush();

                        InputStream is = con.getInputStream();
                        int ch;
                        StringBuffer sb = new StringBuffer();
                        while ((ch = is.read()) != -1) {
                            sb.append((char) ch);
                        }

                        msg.what = 110;
                        msg.obj = sb.toString();
                        handler.sendMessage(msg);

                    /* 显示网页响应内容 */
                        Log.e("UploadPhotoToServer:uploadPhoto", "上传成功" + sb.toString().trim());
                        dos.close();
                    } else {                      //下载图片
                        con.connect();
                        InputStream is = con.getInputStream();
                        Bitmap bimp = BitmapFactory.decodeStream(is);
                        msg.what = 111;
                        msg.obj = bimp;
                        handler.sendMessage(msg);
                    }

                } catch (Exception e) {
             /* 显示异常信息 */
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public interface DataCallback {
        void onDataCallback(Object data);
    }
}
