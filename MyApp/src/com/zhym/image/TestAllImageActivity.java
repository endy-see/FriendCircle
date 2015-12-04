package com.zhym.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhym.friendcircule.AlbumHelper;
import com.zhym.friendcircule.BitmapCache;
import com.zhym.bean.ImageBucket;
import com.zhym.bean.ImageItem;
import com.zhym.myapp.R;

import java.util.List;

/**
 * Created by lenovo on 2015/9/25.
 */
public class TestAllImageActivity extends Activity{
    GridView gv_allImg;
    Spinner sp_albumList;

    AllImageAdapter imgAdapter;
    SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_allimage);


            AlbumHelper helper = AlbumHelper.getHelper();
            helper.init(getApplicationContext());
            helper.buildImagesBucketList();

//            sp_albumList = (Spinner) findViewById(R.id.sp_albumList);
            spinnerAdapter = new SpinnerAdapter(TestAllImageActivity.this, R.layout.myspinner, R.id.sp_tv_name, helper.getImagesBucketList(false));
            sp_albumList.setAdapter(spinnerAdapter);



            List<ImageItem> list = helper.getAllImages();
//            gv_allImg = (GridView) findViewById(R.id.gv_allImg);
            gv_allImg.setSelector(new ColorDrawable(Color.TRANSPARENT));
            imgAdapter = new AllImageAdapter(TestAllImageActivity.this, list);
            gv_allImg.setAdapter(imgAdapter);

//        sp_albumList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//               // String selectValue = parent.getItemAtPosition(position).toString();
//
////                Log.e("setOnItemSelectedListener:",selectValue);
////                Toast.makeText(AllImageActivity.this, selectValue, Toast.LENGTH_SHORT);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


//        return_test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("yes", test.getText());
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

    }

    class AllImageAdapter extends BaseAdapter{
        private List<ImageItem> imageList;
        LayoutInflater mInflater;
        BitmapCache cache;

        public  AllImageAdapter(Context context, List<ImageItem> list) {
            this.imageList = list;
            mInflater = LayoutInflater.from(context);
            cache = new BitmapCache();
        }

        BitmapCache.ImageCallback callBack = new BitmapCache.ImageCallback() {
            @Override
            public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                if(imageView != null && bitmap != null) {
                    String url = (String) params[0];
                    if(!TextUtils.isEmpty(url) && url.equals(imageView.getTag()))
                        imageView.setImageBitmap(bitmap);
                    else
                        Log.e("url == null或", "url个imageView.getTag()不相等");
                } else {
                    Log.e("bitmap为空", "从BitmapCache返回的Bitmap为null");
                }
            }
        };

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public ImageItem getItem(int position) {
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            if(convertView == null) {
                convertView = View.inflate(TestAllImageActivity.this, R.layout.item_image_grid, null);
                holder.imgView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            if(position == 0) {
                Bitmap bimp = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
                holder.imgView.setImageBitmap(bimp);
            } else {
                ImageItem item = getItem(position);
                holder.imgView.setTag(item.imagePath);
                //显示缩略图
                cache.displayBmp(holder.imgView, item.thumbnailPath, item.imagePath, callBack);
            }
            return convertView;
        }

        class Holder {
            private ImageView imgView;
        }

    }

    class SpinnerAdapter extends ArrayAdapter {
        int resource;
        int tv_albumName;
        int count = 0;
        boolean newValue = false;
        String curValue;

        BitmapCache cache;
        BitmapCache.ImageCallback callBack = new BitmapCache.ImageCallback() {
            @Override
            public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                if(imageView != null && bitmap != null) {
                    String url = (String) params[0];
                    if(url != null && url.equals(imageView.getTag())) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Log.e("AllImageActivity:SpinnerAdapter:ImageCallback", "url is null 0r bitmap is null,please check it");
                    }
                } else {
                    Log.e("AllImageActivity:callback:bitmap为null", "callback, thumbBmp null");
                }

            }
        };

        public SpinnerAdapter(Context context, int resource, int textViewResourceId, List<ImageBucket> objects) {
            super(context, resource, textViewResourceId, objects);
            this.resource = resource;
            this.tv_albumName = textViewResourceId;
            cache = new BitmapCache();
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            try {
                newValue = true;
                Holder holder = null;
                if(convertView == null) {
                    holder = new Holder();
                    convertView = View.inflate(getContext(), resource, null);
//                    LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    convertView = inflater.inflate(resource, null);
                    holder.thumbView = (ImageView) convertView.findViewById(R.id.sp_iv);
                    holder.thumbView.setVisibility(View.VISIBLE);
                    holder.albumName = (TextView) convertView.findViewById(R.id.sp_tv_name);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }

                ImageBucket bucket = (ImageBucket) getItem(position);
                setCurValue(bucket.bucketName);
                Log.e("getDropDownView:","选择了列表项："+bucket.bucketName);
                holder.albumName.setText(bucket.bucketName);
                String thumbPath = bucket.imageList.get(0).thumbnailPath;
                String sourcePath = bucket.imageList.get(0).imagePath;
                holder.thumbView.setTag(sourcePath);
                cache.displayBmp(holder.thumbView, thumbPath, sourcePath, callBack);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);
            TextView title = (TextView) convertView.findViewById(R.id.sp_tv_name);
            title.setTextColor(Color.rgb(255, 255, 255));
            if(newValue == false) {
                title.setText("所有图片");
                Log.e("getView:","第一次执行getView");
            }
            else {
                title.setText(getCurValue());
                Log.e("getView:","后来执行getView:"+curValue);
            }

            return convertView;

        }

        public void setCurValue(String curValue) {
            this.curValue = curValue;
        }

        public String getCurValue() {
            return curValue;
        }

        class Holder {
            private ImageView thumbView;
            private TextView albumName;
            private TextView photoCount;
        }
    }
}
