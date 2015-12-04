package com.zhym.friendcircule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhym.Constant;
import com.zhym.DemoApplication;
import com.zhym.myapp.R;
import com.zhym.net.UploadPhotoToServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2015/9/21.
 * 这个适配器可能还要改，因为我想将每条朋友圈状态（即ArrayList中的item）封装成一个独立的类
 */
public class FriendCirculeAdapter extends BaseAdapter{
//    UploadPhotoToServer loadPhotos;
    Activity activity;
//    List<Map<String, String>> shuoList;
    List<String> shuoList;
    String shuo = null;
    LocalAdapter grid_adapter;
    Holder holder;

//    public FriendCirculeAdapter(Activity activity, List<Map<String, String>> shoList) {
//        this.activity = activity;
//        this.shuoList = shoList;
//        Log.e("FriendCirculeAdapter:构造函数", "传入的说说数据列表shuoList="+shoList.size());
//    }

    public FriendCirculeAdapter(Activity activity, List<String> ashuoshuo) {
        this.activity = activity;
        this.shuoList = ashuoshuo;
    }

    @Override
    public int getCount() {
        return shuoList.size();
    }

    @Override
    public String getItem(int position) {
        return shuoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("FriendCirculeAdapter:getView", "在getView中，准备初始化页面");
        if(convertView == null) {
            convertView = View.inflate(activity, R.layout.item_friend_state, null);
            holder = new Holder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.shuoshuo = (TextView) convertView.findViewById(R.id.shuoshuo);
            holder.photos = (NoScrollGridView) convertView.findViewById(R.id.friend_photos);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        try {
            shuo = getItem(position);
            holder.avatar.setImageResource(R.drawable.avatar1);
//            holder.nickname.setText(map.get("nickname"));
//            holder.shuoshuo.setText(map.get("shuoshuo"));
            holder.nickname.setText("meiyangyang");
            holder.shuoshuo.setText(shuo);
//            Log.e("FriendCirculeAdapter:getView", "传入到adapter中的数据：nickname="+map.get("nickname")+", shuoshuo="+map.get("shuoshuo"));
            String photos_name_str = Bimp.allPhotosStr.get(position).substring(4);
            String[] photos_arr = photos_name_str.split("&");
            grid_adapter = new LocalAdapter(activity.getApplicationContext(), photos_arr);
            Log.e("FriendCirculeAdapter:getView","图片名字字符串：photos_name_str="+photos_name_str);
            holder.photos.setAdapter(grid_adapter);
            holder.nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Toast.makeText(activity, "在nickname上点击了一下。。。", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    class Holder {
        private ImageView avatar;
        private TextView nickname;
        private TextView shuoshuo;
        private NoScrollGridView photos;
    }

    class Holder2 {
        private ImageView photo;
    }

    public class LocalAdapter extends BaseAdapter {         //这是发表的朋友圈列表中的每个item中的GridView，是上传的照片。构造函数中的key是指当前item的id。因为每个item可能都有GridView
        LayoutInflater inflater;
        String[] photos;

//        public LocalAdapter(Context context, String[] photos) {
//            inflater = LayoutInflater.from(context);
//            this.photos = photos;
//            if(Bimp.state_photos.containsKey(state_photos_key)) {
//                photos = Bimp.state_photos.get(state_photos_key);
//            } else {
//                Log.e("FriendCirculeAdapter:LocalAdapter:构造函数","Bimp.state_photos中不包含此key...");
//            }
//       }
        public LocalAdapter(Context context, String[] photos) {
            inflater = LayoutInflater.from(context);
            this.photos = photos;
        }

        @Override
        public int getCount() {
            return photos.length;
//            return Bimp.bmp.size();
        }

        @Override
        public Bitmap getItem(int position) {
            Bitmap bp = FileUtils.getImageFromTmpDir(photos[position]);
            Log.e("FriendCirculeAdapter:LocalAdapter:getItem","bitmap图片名字：photos[position]="+photos[position]+", bitmap="+bp);
            return bp;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder2 holder2;
            if(convertView == null) {
                holder2 = new Holder2();
                convertView = inflater.inflate(R.layout.item_published_grida, null);
                holder2.photo = (ImageView) convertView.findViewById(R.id.item_grida_image);
                convertView.setTag(holder2);
            } else {
                holder2 = (Holder2) convertView.getTag();
            }

            //从缓存中加载发表的说说图片
            Bitmap bitmap = getItem(position);
            holder2.photo.setImageBitmap(bitmap);
            holder2.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, PhotoActivity.class);
                    intent.putExtra("ID", position);
                    intent.putExtra("From", "FriendCirculeAdapter");
                    activity.startActivity(intent);
                }
            });

            //下面是从服务器端加载发表的说说。因为要对外做展示，无法连接服务器，所以这里暂时注销
//            loadPhotos = new UploadPhotoToServer(activity, Constant.URL_UPLOAD_DIR+"/"+photos[position], null);
//            loadPhotos.uploadPhoto(new UploadPhotoToServer.DataCallback() {
//                @Override
//                public void onDataCallback(Object data) {
//                    Bitmap bitmap = (Bitmap) data;
//                    holder2.photo.setImageBitmap(bitmap);
//                    holder2.photo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(activity, PhotoActivity.class);
//                            intent.putExtra("ID", position);
//                            intent.putExtra("From", "FriendCirculeAdapter");
//                            activity.startActivity(intent);
//                        }
//                    });
//                }
//            });
            return convertView;
        }

    }

}
