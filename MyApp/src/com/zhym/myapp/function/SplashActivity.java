package com.zhym.myapp.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhym.DemoApplication;

/**
 * Created by lenovo on 2015/9/1.
 */
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(DemoApplication.getInstance().isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }
}
