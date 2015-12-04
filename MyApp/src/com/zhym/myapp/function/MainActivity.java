package com.zhym.myapp.function;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.zhym.myapp.R;
import com.zhym.DemoApplication;
import com.zhym.myapp.fragment.Fragment1;
import com.zhym.myapp.fragment.Fragment2;
import com.zhym.myapp.fragment.Fragment3;
import com.zhym.myapp.fragment.Fragment4;



/**
 * Created by lenovo on 2015/8/31.
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private DemoApplication application;

    private FragmentTabHost mTabHost;
    private RadioGroup mRadioGroup;
    private Class[] fragments = {Fragment1.class, Fragment2.class, Fragment3.class, Fragment4.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        application = DemoApplication.getInstance();

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        for(int i=0; i<fragments.length; i++) {
            TabHost.TabSpec mTabSpec = mTabHost.newTabSpec(i+"").setIndicator(i+"");
            mTabHost.addTab(mTabSpec, fragments[i], null);
        }

        int show = getIntent().getIntExtra("showFragment",0);

        mRadioGroup = (RadioGroup) findViewById(R.id.tab_rg);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.tab_rb_1:
                        mTabHost.setCurrentTab(0);
                        break;
                    case R.id.tab_rb_2:
                        mTabHost.setCurrentTab(1);
                        break;
                    case R.id.tab_rb_3:
                        mTabHost.setCurrentTab(2);
                        break;

                    case R.id.tab_rb_4:
                        mTabHost.setCurrentTab(3);
                        break;
                    default:
                        break;
                }
            }
        });

        mTabHost.setCurrentTab(show);

    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
