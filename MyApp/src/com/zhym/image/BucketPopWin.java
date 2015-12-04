package com.zhym.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhym.adapter.CommonAdapter;
import com.zhym.adapter.ViewHolder;
import com.zhym.bean.ImageBucket;
import com.zhym.friendcircule.AlbumHelper;
import com.zhym.myapp.R;

import java.util.List;

/**
 * Created by lenovo on 2015/10/20.
 */
public class BucketPopWin extends PopupWindow {
    private Context mContext;
    private View mContentView;
    private ListView mBucketList;
    AlbumHelper helper;
    private List<ImageBucket> mDatas;
    private OnImageBucketSelected mBucketSelected;

    public BucketPopWin(Activity context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.popuplist, null);
        helper = AlbumHelper.getHelper();
        helper.init(context);
        helper.buildImagesBucketList();
        mDatas = helper.getImagesBucketList(false);


        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mScreenHeight = metrics.heightPixels;

        this.setContentView(mContentView);      //设置PopupWindow的View
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight((int) (mScreenHeight * 0.7));
        this.setFocusable(true);                //时至弹出窗体可点击
        this.setOutsideTouchable(true);         //点back键和其他地方使其消失，设置了这个才能触发OnDismisslistener,使其他控件变化等操作
        this.update();                          //刷新状态
        this.setBackgroundDrawable(new ColorDrawable(0xffffff));
        this.setAnimationStyle(R.style.anim_popup_imagebucket);

        initView(context);
        initEvents();
    }

    private void initView(Activity context) {
        mBucketList = (ListView) mContentView.findViewById(R.id.popup_bucket_list);
        mBucketList.setAdapter(new CommonAdapter<ImageBucket>(context, mDatas, R.layout.item_image_txt) {

            @Override
            public void convert(ViewHolder holder, ImageBucket imageBucket, TextCallback textCallback) {
                holder.setText(R.id.item_bucket_name, imageBucket.bucketName);
                holder.setText(R.id.item_bucket_count, imageBucket.count+"张");
                holder.setImageByUrl(R.id.first_image_in_bucket, imageBucket.imageList.get(0).imagePath);
            }
        });

        TextView view = (TextView) mContentView.findViewById(R.id.test);
        view.setText("哈哈，有列表项哦！");
    }

    public interface OnImageBucketSelected {
        void selected(ImageBucket bucket);
    }

    public void setOnImageBucketSelected(OnImageBucketSelected bucketSelected) {
        this.mBucketSelected = bucketSelected;
    }

    private void initEvents() {
        mBucketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mBucketSelected != null) {
                    mBucketSelected.selected(mDatas.get(position));
                }
            }
        });
    }
}
