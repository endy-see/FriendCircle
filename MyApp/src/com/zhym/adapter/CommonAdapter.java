package com.zhym.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.List;

/**
 * Created by lenovo on 2015/10/13.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected TextCallback textcallback = null;
    protected Context mContext;
    protected List<T> data;
    protected LayoutInflater mInflater;
    protected int layoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.data = data;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public static interface TextCallback {
        public void onListen(int count);
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(convertView, mContext, parent, layoutId, position);
        convert(holder, getItem(position), textcallback);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, TextCallback textCallback);
}
