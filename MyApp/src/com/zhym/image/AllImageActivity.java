package com.zhym.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhym.adapter.CommonAdapter;
import com.zhym.adapter.ViewHolder;
import com.zhym.bean.ImageItem;
import com.zhym.friendcircule.BitmapCache;
import com.zhym.image.BucketPopWin.OnImageBucketSelected;
import com.zhym.bean.ImageBucket;
import com.zhym.myapp.R;

/**
 * Created by lenovo on 2015/10/20.
 */
public class AllImageActivity extends Activity implements OnImageBucketSelected, View.OnClickListener, OnItemClickListener{
    private BucketPopWin popWin;
    private Button finished;
    private RelativeLayout mBottomLy;
    private TextView choose_imagebucket, bucket_total_count;
    private GridView gv_allImg;
    private BitmapCache cache;
    private ImageBucket mBucket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allimage);
        init();

        mBottomLy.setOnClickListener(this);
        gv_allImg.setOnItemClickListener(this);
        finished.setOnClickListener(this);
    }

    private void init() {
        finished = (Button) findViewById(R.id.finished);
        mBottomLy = (RelativeLayout) findViewById(R.id.bottom_ly);
        choose_imagebucket = (TextView) findViewById(R.id.choose_imagebucket);
        bucket_total_count = (TextView) findViewById(R.id.bucket_total_count);
        gv_allImg = (GridView) findViewById(R.id.gv_allImg);
        cache = new BitmapCache();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bottom_ly:
                popWin = new BucketPopWin(AllImageActivity.this);
                popWin.showAsDropDown(mBottomLy, 0, 0);             //从底部弹出
                //设置弹出时的背景效果
                final WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);

                //消失的时候背景效果也要消失
                popWin.setOnDismissListener(new PopupWindow.OnDismissListener(){
                    @Override
                    public void onDismiss() {
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                    }
                });

                popWin.setOnImageBucketSelected(this);

                break;

//            case R.id.finished:
//                setResult(RESULT_OK, new Intent().putExtra("info", "暂时把test撤销了，谅解哈~"));
//                finish();
//                break;

            default:
                break;

        }
    }

    @Override
    public void selected(ImageBucket bucket) {
        mBucket = bucket;               //用于保存在PopupWindow中点击相册，以便向AllImageActivity.java返回图片路径时在OnItemClicked中获取该ImageItem
        //实现BucketPopWin中的接口，响应接口的事件
        gv_allImg.setAdapter(new CommonAdapter<ImageItem>(AllImageActivity.this, bucket.imageList, R.layout.item_image_grid) {
            BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                    if (imageView != null && bitmap != null) {
                        String url = (String) params[0];
                        if (url != null && url.equals(imageView.getTag())) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Log.e("ImageGridActivity", "callback, bmp not match");
                        }
                    } else {
                        Log.e("ImageGridActivity", "callback, bmp null");
                    }
                }
            };
            @Override
            public void convert(ViewHolder holder, ImageItem imageItem, TextCallback textCallback) {
                holder.setTag(R.id.image, imageItem.imagePath);
                cache.displayBmp((ImageView) holder.getView(R.id.image), imageItem.thumbnailPath, imageItem.imagePath, callback);
            }
        });

        choose_imagebucket.setText(bucket.bucketName);
        bucket_total_count.setText(bucket.count+"张");
        popWin.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageItem imageItem = mBucket.imageList.get(position);                  //从数据源ImageBucket中获取所点击位置的ImageItem项，并获取其存储路径信息
        setResult(RESULT_OK, new Intent().putExtra("imagepath", imageItem.imagePath));
        finish();
    }

}
