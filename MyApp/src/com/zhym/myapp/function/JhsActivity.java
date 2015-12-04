package com.zhym.myapp.function;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.zhym.myapp.*;
import com.zhym.myapp.fragment.TabFragment1;
import com.zhym.myapp.fragment.TabFragment2;
import com.zhym.myapp.fragment.TabFragment3;
import com.zhym.myapp.fragment.TabFragment4;

/**
 * Created by lenovo on 2015/9/6.
 */
public class JhsActivity extends FragmentActivity implements ActionBar.TabListener{

    private TabFragment1 mFragment1= new TabFragment1();
    private TabFragment2 mFragment2= new TabFragment2();
    private TabFragment3 mFragment3= new TabFragment3();
    private TabFragment4 mFragment4= new TabFragment4();


    private static final int TAB_INDEX_ONE = 0;
    private static final int TAB_INDEX_TWO = 1;
    private static final int TAB_INDEX_THREE = 2;
    private static final int TAB_INDEX_FOUR = 3;
    private static final int TAB_INDEX_TOTAL = 4;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jhs);

        init();
    }

    private void init() {
        //配置ActionBar
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        actionBar.setHomeButtonEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.jhs);
        actionBar.setTitle("聚划算");

        //ViewPager装上监听，并由ActionBar控制
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch(state) {
                    case ViewPager.SCROLL_STATE_IDLE:          //未拖动页面时
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:      //正在拖动页面时
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:      //加载完毕
                        break;
                    default:
                        break;
                }
            }
        });

        //ActionBar添加Tab,并装监听器
        for(int i=0; i<mViewPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(mViewPagerAdapter.getPageTitle(i)).setTabListener(this));
        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case TAB_INDEX_ONE:
                    return mFragment1;
                case TAB_INDEX_TWO:
                    return mFragment2;
                case TAB_INDEX_THREE:
                    return mFragment3;
                case TAB_INDEX_FOUR:
                    return mFragment4;
            }
            throw new IllegalStateException("this position:"+position+" is illegal");
        }

        @Override
        public int getCount() {
            return TAB_INDEX_TOTAL;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabLabel = null;
            switch (position) {
                case TAB_INDEX_ONE:
                    tabLabel = getString(R.string.tab_1);
                    break;
                case TAB_INDEX_TWO:
                    tabLabel = getString(R.string.tab_2);
                    break;
                case TAB_INDEX_THREE:
                    tabLabel = getString(R.string.tab_3);
                    break;
                case TAB_INDEX_FOUR:
                    tabLabel = getString(R.string.tab_4);
                    break;
            }
            return tabLabel;
        }
    }


}
