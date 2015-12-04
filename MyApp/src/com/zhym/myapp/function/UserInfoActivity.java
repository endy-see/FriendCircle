package com.zhym.myapp.function;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONException;
import com.zhym.DemoApplication;
import com.zhym.friendcircule.FileUtils;
import com.zhym.image.AllImageActivity;
import com.zhym.myapp.R;
import com.zhym.Constant;
import com.zhym.net.LoadDataFromServer;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2015/9/7.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener{
    public static final int REQUEST_IMAGE = 2;
    public static final int REQUEST_CUT = 3;
    private ImageView avatar;
    private String imageName;
    private String returnData = "随意给了个初始化值。。";
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        test = (TextView) findViewById(R.id.test);

        findViewById(R.id.item_avatar).setOnClickListener(this);
        avatar = (ImageView) findViewById(R.id.iv_avatar);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.item_avatar:
                startActivityForResult(new Intent(UserInfoActivity.this, AllImageActivity.class),REQUEST_IMAGE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取返回数据，并处理
        try {
            if(resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_IMAGE:
                        //获取传过来的图片，并将布局中的头像设置为传来的图片
                        if(data != null) {
                            FileUtils.getFileDir();                              //确定要保存头像的SDCard目录存在
                            imageName = FileUtils.getImageNameFromPath(data.getStringExtra("imagepath"));
                            Bitmap bimp = FileUtils.getImageFromSDCard(imageName);    //传过来的图片所在的绝对路径

                            if(bimp != null) {
                               avatar.setImageBitmap(bimp);
                               if(avatar.getDrawable() == null)
                                   avatar.setImageResource(R.drawable.avatar1);
                               updateAvatarInServer(imageName);
                            } else {
                               startImageZoom(data.getStringExtra("imagepath"), 480);
                            }
                        }
                        break;
                    case REQUEST_CUT:
                        Bitmap bmp = FileUtils.getImageFromSDCard(imageName);
                        avatar.setImageBitmap(bmp);
                        if(avatar.getDrawable() == null)
                            avatar.setImageResource(R.drawable.avatar1);
                        updateAvatarInServer(imageName);
                        break;

                    default:
                        break;
                }

            } else {
                Log.e("UserInfoActivity:onActivityResult:resultCode", resultCode+"");
                Toast.makeText(getApplicationContext(),"onActivityResult在传输过程中出错了。。。", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startImageZoom(String path, int size) {
        Uri uri = Uri.fromFile(new File(path));

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");        //uri:被剪裁图片的路径
        intent.putExtra("aspectX", 1);          //aspectX, aspectY是宽高比例
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", size);       //outputX, outputY是剪裁图片的宽高
        intent.putExtra("outputY",size);
//        intent.putExtra("output", Uri.fromFile(new File("/sdcard/myapp/Picture_01_Shark.jpg")));
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FileUtils.getFileDir(), imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CUT);

    }

    private void updateAvatarInServer(final String imageName) {
        Map<String, String> map = new HashMap();
        if(FileUtils.getImageFromSDCard(imageName) != null) {       //能够取到图片，说明此imageName存在
            map.put("file", FileUtils.getFileDir()+"/"+imageName);       //将“file”和文件路径对应
            map.put("image", imageName);
        } else {
            return;
        }
        map.put("userID", "1");

        //上传多张图片要用for循环、多线程或AsyncTask.一个线程传一张图片，不要开太多线程
        LoadDataFromServer task = new LoadDataFromServer(UserInfoActivity.this, Constant.URL_UPDATE_Avatar, map);
        task.getData(new LoadDataFromServer.DataCallback() {
            @Override
            public void onDataCallback(JSONObject data) {
              try {
                  Log.e("------------UserInfoActivity:updateAvatarInServer:onDataCallback:code----","--------data="+data+"code="+data.getInteger("code")+"image1="+data.getString("image1")+"image2="+data.getString("image2")+"---------");
//                  int code = data.getInteger("1");
                  int code = data.getInteger("code");
                if(code == 1) {
                    //更新本地所有头像。。
                    DemoApplication.getInstance().saveAvatar(FileUtils.getFileDir()+"/"+imageName);
                    Toast.makeText(UserInfoActivity.this, "更新成功...并且已将头像路径保存到Preference中以供其他程序获取",Toast.LENGTH_SHORT).show();
                } else if(code == 2) {
                    Toast.makeText(UserInfoActivity.this, "上传成功，但是写入数据库时失败，更新失败...",Toast.LENGTH_SHORT).show();
                } else if(code == 3){
                    Toast.makeText(UserInfoActivity.this, "图片上传失败...",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserInfoActivity.this, "服务器繁忙请重试...",Toast.LENGTH_SHORT).show();
                }

              } catch (JSONException e) {
                  Toast.makeText(UserInfoActivity.this, "数据解析错误...",Toast.LENGTH_SHORT).show();
                  e.printStackTrace();
              }
            }

//            @Override
//            public void onDataCallback(String returnData) {
//                String result = returnData;
//                Log.e("------------UserInfoActivity:updateAvatarInServer:onDataCallback----","--------result="+result+"---------");
//
//            }
        });
    }

}
