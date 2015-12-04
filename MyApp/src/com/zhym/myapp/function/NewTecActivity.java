package com.zhym.myapp.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhym.custom.VDHBlogActivity;
import com.zhym.friendcircule.Test;
import com.zhym.myapp.R;

/**
 * Created by lenovo on 2015/10/29.
 */
public class NewTecActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtec);
        Button custom_layout = (Button) findViewById(R.id.custom_layout);
        Button upload_image = (Button) findViewById(R.id.uploadImage);

        custom_layout.setOnClickListener(this);
        upload_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.custom_layout:
                startActivity(new Intent(NewTecActivity.this, VDHBlogActivity.class));
                break;
            case R.id.uploadImage:
                startActivity(new Intent(NewTecActivity.this, Test.class));
                break;
        }
    }
}
