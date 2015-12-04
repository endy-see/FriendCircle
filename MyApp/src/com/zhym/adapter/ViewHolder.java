package com.zhym.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhym.friendcircule.BitmapCache;

import java.io.File;

/**
 * Created by lenovo on 2015/10/13.
 */
public class ViewHolder {
    private Context mContext;
    private SparseArray<View> mViews;
    private View mConvertView;
    private int mLayoutId;              //可能有多个布局
    private int mPosition;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mPosition = position;
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(View convertView, Context context, ViewGroup parent, int layoutId, int position) {
        if(convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;        //确定是当前position的Item要可视化到界面
            return holder;
        }
    }

    public int getPosition() {
        return mPosition;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    //通过getViewID，从MViews中获取控件
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if(view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    //下面设置Item布局中可能出现的控件的值
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setImageByUrl(int viewId, String path) {
        ImageView iv = getView(viewId);
        iv.setImageURI(Uri.fromFile(new File(path)));
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View v = getView(viewId);
        v.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View v = getView(viewId);
        v.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setVisibility(int viewId, boolean visible) {
        View v = getView(viewId);
        v.setVisibility(visible ? View.VISIBLE :View.GONE);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View v = getView(viewId);
        v.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View v = getView(viewId);
        v.setTag(key, tag);
        return this;
    }

    public ViewHolder setOnClickListenere(int viewId, View.OnClickListener listener) {
        View v = getView(viewId);
        v.setOnClickListener(listener);
        return this;
    }
}
