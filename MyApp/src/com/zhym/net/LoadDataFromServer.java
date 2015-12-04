package com.zhym.net;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;


import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import internal.org.apache.http.entity.mime.MultipartEntity;
import internal.org.apache.http.entity.mime.content.FileBody;
import internal.org.apache.http.entity.mime.content.StringBody;

/**
 * Created by lenovo on 2015/10/26.
 */
public class LoadDataFromServer {
    private String url;
    private Map<String, String> map = null;
    //是否包含数组，默认是不包含
    Context context;

    /*向服务器注册新用户，上传用户信息。Context：当前上下文； URL：用户信息上传到服务器地址； map：用户信息*/
    public LoadDataFromServer(Context context, String url, Map<String, String> map) {
        this.context = context;
        this.url = url;
        this.map = map;
    }

    /*从服务器端获取数据（用户信息）*/
    public void getData(final DataCallback dataCallback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 111 && dataCallback != null) {
                    JSONObject jsonObject = (JSONObject) msg.obj;
                    if(jsonObject != null) {
                        Log.e("LoadDataFromServer:getData:handleMessage：if(jsonObject != null)", "msg.obj="+msg.obj+", 开始执行dataCallback.onDataCallback(jsonObject)");
                        try {
                            dataCallback.onDataCallback(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    String returnData = (String) msg.obj;
//                    if(returnData != null) {
//                        dataCallback.onDataCallback(returnData);
                    } else {
                        Log.e("LoadDataFromServer:getData:handleMessage：else", "jsonObj == null，访问服务器出错！");
                        Toast.makeText(context, "访问服务器出错...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                List<NameValuePair> params = new ArrayList<>();
                Set keys = map.keySet();
                if(keys != null) {
                    Iterator iterator = keys.iterator();
                    while(iterator.hasNext()) {
                        String key = (String) iterator.next();
                        params.add(new BasicNameValuePair(key, map.get(key)));  //将map中所有key-value映射到params中
                    }
                }
                try {
                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);    //连接超时
                client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);            //请求超时
                HttpPost post = new HttpPost(url);
                post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                StringBuilder builder = new StringBuilder();

                HttpResponse response = client.execute(post);
                if(response.getStatusLine().getStatusCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), Charset.forName("UTF-8")));
                    for(String s = reader.readLine(); s!=null; s=reader.readLine()) {
                        builder.append(s);
                    }
                    Log.e("LoadDataFromServer:getData:builder.toString()未经jsonTokener", builder.toString());
                    String builder_BOM = jsonTokener(builder.toString());
                    Log.e("LoadDataFromServer:getData:builder.toString()经过后jsonTokener", builder_BOM.toString());
                    try {
                        JSONObject jsonObject;
                        jsonObject = JSONObject.parseObject(builder_BOM);
                        Log.e("LoadDataFromServer:getData:jsonObject", jsonObject.toString());
                        Message msg = new Message();
                        msg.what = 111;
                        msg.obj = jsonObject;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String jsonTokener(String in) {
        int count = 0;    //记录in中“{”的个数。如果有count>=2,则不用去最后一个了,说明全是数据
        for(int i=0; i<in.length(); i++) {
            if("{".equals(in.charAt(i)))
                count++;
        }

        if(in != null && count == 1) {
            int index = in.lastIndexOf("{");
            in = in.substring(index);
        }
        return in;
    }

    /*网络访问回调接口*/
    public interface DataCallback {
        void onDataCallback(JSONObject data);
    }

}
