package com.zhym.friendcircule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zhym.myapp.R;
import com.zhym.net.UploadPhotoToServer;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lenovo on 2015/9/11.
 */
public class Test extends Activity{
    private String username = "zhym";
    private String uploadFile = "/sdcard/DCIM/Camera/20151102203744.jpg";
    private String actionUrl = "http://10.108.218.249/myown/proctice.php";
    private TextView mText1;
    private TextView mText2;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mText1 = (TextView) findViewById(R.id.test1);       //文件路径
        mText2 = (TextView) findViewById(R.id.test2);       //上传网址
        mText1.setText(uploadFile);
        mText2.setText(actionUrl);
        mButton = (Button) findViewById(R.id.mButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadPhotoToServer task = new UploadPhotoToServer(Test.this, "http://10.108.218.181/myown/proctice.php","/storage/sdcard1/Pictures/SHOUT.jpg");
                task.uploadPhoto(new UploadPhotoToServer.DataCallback() {
                    @Override
                    public void onDataCallback(Object data) {
                        String res = (String) data;
                        if(data.equals("success")) {
                            Toast.makeText(Test.this, "用HttpURLConnection上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Test.this, "用HttpURLConnection上传失败。。失败。。。", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}
