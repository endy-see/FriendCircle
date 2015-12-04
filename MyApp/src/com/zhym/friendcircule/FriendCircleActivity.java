package com.zhym.friendcircule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.zhym.Constant;
import com.zhym.myapp.R;
import com.zhym.myapp.function.MainActivity;
import com.zhym.net.LoadDataFromServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/9/21.
 */
public class FriendCircleActivity extends Activity{
    ListView friends_photo;
    FriendCirculeAdapter adapter = null;
    List<String> shuoshuoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView xiangce;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendcircule);

        xiangce = (ImageView) findViewById(R.id.camera);
        friends_photo = (ListView) findViewById(R.id.friends_states);

        /**从服务器端加载数据库pengyouquan中内容
         * 1.先从服务器端获得朋友圈数据表pengyouquan中的数据条数count
         * 2.循环所有count，并都add到shuoshuoList中
        * */
          shuoshuoList = Bimp.shuoshuo;
        if(adapter == null) {
           Log.e("FriendCircleActivity:onCreate","开始执行new FriendCirculeAdapter,这里的shuoshuoList.size="+shuoshuoList.size());
           adapter = new FriendCirculeAdapter(FriendCircleActivity.this, shuoshuoList);
           } else {
                Log.e("FriendCircleActivity:onCreate","开始更新适配器数据");
                adapter.notifyDataSetChanged();
           }
        friends_photo.setAdapter(adapter);

//                Log.e("FriendCircleActivity:onCreate","给朋友圈列表装适配器");
//                friends_photo.setAdapter(adapter);
//        final Map<String, String> map = new HashMap();
//        map.put("allItems", "allItems");
//        map.put("id", "1");
//        LoadDataFromServer task = new LoadDataFromServer(FriendCircleActivity.this, Constant.URL_GET_SHUOSHUO, map);
//        task.getData(new LoadDataFromServer.DataCallback() {
//            @Override
//            public void onDataCallback(JSONObject data) {
//                map.put("nickname", data.getString("nickname"));
//                map.put("shuoshuo", data.getString("shuoshuo"));
//                map.put("photos", data.getString("photos"));
//                Log.e("FriendCircleActivity:onCreate","已经从服务器端获得所有朋友圈数据，但是具体是啥内容呢？？data="+data.toString());
//                Toast.makeText(FriendCircleActivity.this, "data="+data.toString(),Toast.LENGTH_SHORT).show();
//                shuoshuoList.add(map);
//                Log.e("FriendCircleActivity:onCreate","map="+map+", nickname="+data.getString("nickname")+", shuoshuoList.size="+shuoshuoList.size());
//                if(adapter == null) {
//                    Log.e("FriendCircleActivity:onCreate","开始执行new FriendCirculeAdapter,这里的shuoshuoList.size="+shuoshuoList.size());
//                    adapter = new FriendCirculeAdapter(FriendCircleActivity.this, shuoshuoList);
//                } else {
//                    Log.e("FriendCircleActivity:onCreate","开始更新适配器数据");
//                    adapter.notifyDataSetChanged();
//                }

//                Log.e("FriendCircleActivity:onCreate","给朋友圈列表装适配器");
//                friends_photo.setAdapter(adapter);
//            }
//        });

        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bimp.bmp.clear();
                Bimp.max = 0;
                Bimp.drr.clear();
                startActivity(new Intent(FriendCircleActivity.this, TestPicActivity.class));
            }
        });

    }

    public void back(View v) {
       Intent intent = new Intent(FriendCircleActivity.this, MainActivity.class);
       intent.putExtra("showFragment",2);
       startActivity(intent);
       finish();
    }

}
