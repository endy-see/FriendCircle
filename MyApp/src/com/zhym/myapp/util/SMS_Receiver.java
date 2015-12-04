package com.zhym.myapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.zhym.myapp.function.SettingInfo;

/**
 * Created by lenovo on 2015/10/28.
 */
public class SMS_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            StringBuilder sb = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                Object[] objects = (Object[]) bundle.get("pdus");      //获取短信信息
                SmsMessage[] smsMessages = new SmsMessage[objects.length];
                for(int i=0; i<objects.length; i++) {
                    smsMessages[i] = SmsMessage.createFromPdu((byte[]) objects[i]);
                }
                for(int i=0; i<objects.length; i++) {
                    sb.append(smsMessages[i].getDisplayOriginatingAddress());       //电话号码
                    sb.append("|");
                    sb.append(smsMessages[i].getMessageBody());
                }
                Toast.makeText(context, "接收短信验证信息！", Toast.LENGTH_SHORT).show();
                //创建Intent对象
                Intent tmpIntent = new Intent(context, SettingInfo.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("change", sb.toString().trim());
                tmpIntent.putExtras(myBundle);
                tmpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(tmpIntent);
            }
        }

    }
}
