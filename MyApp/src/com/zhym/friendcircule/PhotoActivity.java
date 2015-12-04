package com.zhym.friendcircule;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhym.myapp.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends Activity {
    List<View> listViews = null;
    ViewPager viewPager;
    MyViewPagerAdapter adapter;
    RelativeLayout btn_bg_rl;
    int count = 0;

    List<Bitmap> bmp = new ArrayList<>();
    List<String> drr= new ArrayList<>();
    List<String> del= new ArrayList<>();
    int max = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);
        init();

        adapter = new MyViewPagerAdapter(listViews);
        viewPager.setAdapter(adapter);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        String from = intent.getStringExtra("From");
        if(from.equals("FriendCirculeAdapter"))         //如果是在“朋友圈”列表中查看用户发布的照片，则不需要显示编辑按钮
            btn_bg_rl.setVisibility(View.GONE);

        viewPager.setCurrentItem(id);

        //页面滑动监听
        viewPager.setOnPageChangeListener(pageChangeListener);

        //按钮点击事件见下面的button_click方法

    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btn_bg_rl = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        btn_bg_rl.setBackgroundColor(0x70000000);

        //给数据赋值
        for(int i=0; i<Bimp.bmp.size(); i++) {
            bmp.add(Bimp.bmp.get(i));
        }

        for(int i=0; i<Bimp.drr.size(); i++) {
            drr.add(Bimp.drr.get(i));
        }

        max = Bimp.max;

        //初始化视图列表
        for(int i=0; i<bmp.size(); i++) {
            initListView(bmp.get(i));
        }
    }

    private void initListView(Bitmap bitmap) {
        if(listViews == null) {
            listViews = new ArrayList<>();
        }
        ImageView iv_photo = new ImageView(this);
        iv_photo.setImageBitmap(bitmap);
        iv_photo.setBackgroundColor(0xff000000);
        iv_photo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        listViews.add(iv_photo);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int i) {
            count = i;
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void button_click(View view) {
        switch(view.getId()) {
            case R.id.photo_bt_exit:
                finish();
                break;
            case R.id.photo_bt_del:
                if(listViews.size() == 1) {
                    Bimp.bmp.clear();;
                    Bimp.drr.clear();
                    Bimp.max = 0;
                    FileUtils.deleteDir();
                    finish();
                } else {
                    String imageName = drr.get(count).substring(drr.get(count).lastIndexOf("/") + 1, drr.get(count).lastIndexOf("."));
                    bmp.remove(count);
                    drr.remove(count);
                    del.add(imageName);
                    max--;
                    viewPager.removeAllViews();
                    listViews.remove(count);
                    adapter.setListViews(listViews);
                    viewPager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.photo_bt_enter:
                Bimp.bmp = bmp;
                Bimp.drr = drr;
                Bimp.max = max;
                for(int i=0; i<del.size(); i++) {
                    FileUtils.delFile(del.get(i) + ".png");
                }
                finish();
                break;
            default:
                break;
        }

    }

    class MyViewPagerAdapter extends PagerAdapter {
        private List<View> listViews;
        private int size;

        public MyViewPagerAdapter(List<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(List<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0:listViews.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(listViews.get(position % size), 0);
            return listViews.get(position % size);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(listViews.get(position % size));
            Log.e("PhotoActivity:MyViewPagerAdapter:destoryItem","position"+position);
        }
        @Override
        public int getCount() {
            Log.e("PhotoActivity:MyViewPagerAdapter:getCount","size="+size);
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

    }
}