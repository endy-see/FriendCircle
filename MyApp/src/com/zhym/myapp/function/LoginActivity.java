package com.zhym.myapp.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhym.myapp.R;
import com.zhym.DemoApplication;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by lenovo on 2015/8/31.
 */
public class LoginActivity extends Activity {
    private EditText username, password;
    private TextView isSuccess;
    private Button login;
    private String result;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Log.e("LoginActivity:onCreate", "begin init()");
        init();
        Log.e("LoginActivity:onCreate", "end init()");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            login();
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
                            if(result.equals("exist")) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else if("error".equals(result)){
                                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                            } else {
                                Log.e("LoginActivity:thread:run:else","result is not exist & error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(result.equals("exist")) {
                    DemoApplication.getInstance().saveUsername(username.getText().toString());
                    DemoApplication.getInstance().savePassword(password.getText().toString());

                    username.setText("");
                    password.setText("");
                    isSuccess.setText(result);
                } else if(result.equals("error")) {
                    isSuccess.setText(result);
     //              Toast.makeText(getApplicationContext(), "此用户不存在，请先注册！", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };

    }

    private void init() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        isSuccess = (TextView) findViewById(R.id.isSuccess);
        login = (Button) findViewById(R.id.login);

    }

    private void login() {
        HttpClient client = new DefaultHttpClient();
        Log.e("LoginActivity:login():HttpGet","建立Get请求");
        HttpGet get = new HttpGet("http://10.108.217.56/myown/login.php?username="+username.getText().toString()
                +"&password="+password.getText().toString());
        try {
            Log.e("LoginActivity:login():response","begin response");
            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
                Log.e("LoginActivity:login():response:result",result);
            } else {
                result = "请求失败";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
