package com.zhym.myapp.function;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhym.myapp.R;
import com.zhym.DemoApplication;

/**
 * Created by lenovo on 2015/9/7.
 */
public class SettingInfo extends Activity {
    private DemoApplication applicaiton = new DemoApplication();
    private Button bn_logout, systemTime, testOnClick;
    TextView tv;
    int level, scale;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tv = (TextView) findViewById(R.id.test);
                    tv.setText(tv.getText().toString()+level*100/scale+"%");
                    break;
                case 1:
                    Bundle b = msg.getData();
                    String tmpMsg = (String) b.get("xx");
                    gotoCheck(tmpMsg);
                default:
                    finish();
                    break;
            }
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                level = intent.getIntExtra("level", 0);        //获取当前电量大小
                scale = intent.getIntExtra("scale", 100);       //手机总电量大小
                handler.sendEmptyMessage(0);                    //发送消息改变TextView值
                Toast.makeText(SettingInfo.this, "当前手机电量为："+level*100/scale+"%", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null) {
            String tmpMsg = bundle.getString("change");
            Bundle bundle1 = new Bundle();
            bundle1.putString("xx", tmpMsg);
            Message message = new Message();
            message.what = 1;
            message.setData(bundle1);
            handler.sendMessage(message);
        } else {
            setContentView(R.layout.setinfo);

            tv = (TextView) findViewById(R.id.test);
            bn_logout = (Button) findViewById(R.id.logout);
            bn_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(SettingInfo.this)
                                    .setTitle("退出")
                                    .setMessage("确定要退出登陆？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            logout();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).show();
                        }
                    });

                }
            });
        }
    }

    public void checkSystemTime(View view) {
       registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));       //注册访问电池剩余电量的系统BroadcastReceiver
    }

    public void testOnClick(View view) {
        tv = (TextView) findViewById(R.id.test);
        tv.setText("正在测试onClick!!!");
    }

    public void gotoCheck(String msg) {
        setContentView(R.layout.receive_sms);
        EditText sms_from = (EditText) findViewById(R.id.sms_from);
        EditText sms_content = (EditText) findViewById(R.id.sms_content);
        String[] tmpMsg = msg.split("\\|");
        sms_from.setText(tmpMsg[0]);
        sms_content.setText(tmpMsg[1]);
    }

    private void logout() {
        applicaiton.saveUsername("");
        applicaiton.savePassword("");

        Intent intent = new Intent();
        intent.setClass(SettingInfo.this,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }
}
