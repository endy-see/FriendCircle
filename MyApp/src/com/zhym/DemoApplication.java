package com.zhym;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

/**
 * Created by lenovo on 2015/8/31.
 */
public class DemoApplication extends Application {
    public static Context context;
    private static DemoApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        instance = this;
    }

    public static DemoApplication getInstance() {
        return instance;
    }
    public  void saveUsername(String username) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public  void savePassword(String password) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("password", password);
        editor.commit();
    }

    //保存的是头像的名称。因为剪裁的时候就已经直接将图片存在/myapp/imageName.jpg了。。
    public void saveAvatar(String imageAbsPath) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("avatar", imageAbsPath);
        editor.commit();
    }

    public  String getUsername() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString("username", "zhym");
    }

    public  String  getPassword() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString("password","happy");
    }

    public String getAvatar() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getString("avatar", "imageAbsPath");
    }

    public boolean isLogin() {
        if(!getUsername().equals("") && !getPassword().equals(""))
            return true;
        return false;
    }



    public String getAppName(int pid) {
        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> iterator = list.iterator();
        PackageManager pm = getApplicationContext().getPackageManager();

        while(iterator.hasNext()) {
            //获取应用程序信息
            ActivityManager.RunningAppProcessInfo info = iterator.next();
            try {
                if (info.pid == pid) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    Log.e("DemoApplication:getAppName:", "pid:" + info.pid + " ProcessName:" + info.processName + " Label:" + c.toString());
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    //退出登陆
    public static void logout() {
        instance.saveUsername("");
        instance.savePassword("");

//        int pid = android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
       // System.exit(0);


//        String appName = instance.getAppName(pid);
//        Log.e("DemoApplication:logout:appName",appName);
//        if(appName.equals("com.example.myapp")) {
//
//        }

    }
}
