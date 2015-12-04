package com.zhym.friendcircule;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhym.Constant;
import com.zhym.myapp.R;

public class PublishedActivity extends Activity {

	private GridView smallGridView;
	private GridAdapter adapter;
	private TextView send_selectimg;
    private EditText shuoshuo;
    private List<Bitmap> bitexample;
    private ImageView testImage;
    private String takePhotoName;
    Map<String, String> photos = new HashMap<>();              //key="shuoshuo", value="absolut path"


    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_fridcir_state);
        testImage = (ImageView) findViewById(R.id.testImage);
        Init();
	}

	public void Init() {
        bitexample = new ArrayList<>();
        shuoshuo = (EditText) findViewById(R.id.shuoshuo);
        smallGridView = (GridView) findViewById(R.id.smallImage_gridview);
		smallGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		smallGridView.setAdapter(adapter);

		smallGridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == Bimp.bmp.size()) {
                    new PopupWindows(PublishedActivity.this, smallGridView);
                } else {
                    Intent intent = new Intent(PublishedActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", position);
                    intent.putExtra("From", "PublishedActivity");
                    startActivity(intent);
                }
            }
        });

        //将每次添加的缩略图集bitexample都放在Bimp.state_photos中，以备每次都加载当时状态下的缩略图集
		send_selectimg = (TextView) findViewById(R.id.send_selectimg);      //即“发送”按钮
		send_selectimg.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                //用LoadDataFromServer上传头像、昵称、说说、评论等（这里不用自定义的类了。。不知道该咋用）
                //用HttpUrlConnection上传所发朋友圈中的照片，多个线程同时传，传的过程中弹出进度圈，显示“正在发表说说”
//                String photos = "";
//                List<String> list = new ArrayList<>();
//                Bimp.drr.add(DemoApplication.getInstance().getAvatar());            //将头像混着照片一块上传
//                for (int i = 0; i < Bimp.drr.size(); i++) {
//                    UploadPhotoToServer task = new UploadPhotoToServer(PublishedActivity.this, Constant.URL_UPDATE_Photos, Bimp.drr.get(i));
//                    task.uploadPhoto(new UploadPhotoToServer.DataCallback() {
//                        @Override
//                        public void onDataCallback(Object data) {
//                            String res = (String) data;
//                            if(data.equals("success")) {
//                                Toast.makeText(PublishedActivity.this, "用HttpURLConnection上传成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(PublishedActivity.this, "用HttpURLConnection上传失败。。失败。。。", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                    //给photos赋值
//                    photos += FileUtils.getImageNameFromPath(Bimp.drr.get(i)) +"&";

//                    String Str = Bimp.drr.get(i).substring(
//                            Bimp.drr.get(i).lastIndexOf("/") + 1,           //Bimp.drr中存的是绝对路径
//                            Bimp.drr.get(i).lastIndexOf("."));
//                    list.add(FileUtils.SDPATH + Str + ".JPEG");
//                }

//                aShuoShuoState.setId("1");
//                aShuoShuoState.setNickName("羊咩咩~");
//                Log.e("****PublishedActivity:Init:send_selectimg****", "&&&&&&&&&&&DemoApplication.getInstance().getAvatar()="+DemoApplication.getInstance().getAvatar());
//                if(DemoApplication.getInstance().getAvatar() != null) {
//                    aShuoShuoState.setAvatar(FileUtils.getImageNameFromPath(DemoApplication.getInstance().getAvatar()));
//                } else {
//                    aShuoShuoState.setAvatar(FileUtils.getFileDir()+"/TMPSNAPSHOT1444042790914.jpg");
//                } //只需要上传头像的名字即可，不需要头像的绝对路径）
//
//                aShuoShuoState.setShuoshuo(shuoshuo.getText().toString());
//                aShuoShuoState.setPhotos(photos);

//                String shuoshuoString = JsonTools.getJsonString("aShuoShuo", aShuoShuoState);
//                Log.e("PublishedActivity:Init:send_selectimg", "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^AShuoShuoClass="+shuoshuoString.toString());
//                Map<String, String> map = new HashMap();
//                map.put("file", DemoApplication.getInstance().getAvatar());                             //这里上传头像是根据头像的绝对路径来上传的
//                map.put("userInfoJson", shuoshuoString);
//                map.put("userID", "3");
//                map.put("nickName", "addTwo");
//                map.put("shuoshuo", shuoshuo.getText().toString());
//                map.put("avatar", DemoApplication.getInstance().getAvatar());
//                map.put("photos", photos);
//                LoadDataFromServer t = new LoadDataFromServer(PublishedActivity.this, Constant.URL_UPDATE_Avatar, map);
//                t.getData(new LoadDataFromServer.DataCallback() {
//                    @Override
//                    public void onDataCallback(JSONObject data) {
//                        Log.e("PublishedActivity:Init:send_selectimg:task", "##############data中userInfo="+data.toString());
//                        try {
//                            int code = data.getInteger("code");
//                            if (code == 1) {
//                                //更新本地所有头像。。
//                                Log.e("PublishedActivity:Init:send_selectimg:task", "%%%%%%%%%%%%%%%%%%%data中userInfo="+data.getString("userInfo"));
//                                Toast.makeText(PublishedActivity.this, "已经成功发表说说，返回上传的用户信息userInfo", Toast.LENGTH_SHORT).show();
//                            } else if (code == 2) {
//                                Toast.makeText(PublishedActivity.this, "上传成功，但是写入数据库时失败，更新失败...", Toast.LENGTH_SHORT).show();
//                            } else if (code == 3) {
//                                Toast.makeText(PublishedActivity.this, "图片上传失败...", Toast.LENGTH_SHORT).show();
//                            } else if(code == 4) {
//                                Toast.makeText(PublishedActivity.this, "给user数据表插入对象成功，但是给pengyouquan数据表插入信息失败...", Toast.LENGTH_SHORT).show();
//                            }else if(code == 5){
//                                Toast.makeText(PublishedActivity.this, "user和pengyouquan都成功！！！！！！！！...", Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                Toast.makeText(PublishedActivity.this, "服务器繁忙请重试...", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........

//                FileUtils.deleteDir();
                Bimp.shuoshuo.add(shuoshuo.getText().toString());                   //将发表的说说存储到全局静态变量中
                String photoNameStr = null;
                for(int i=0; i<Bimp.drr.size(); i++) {
                    String name = Bimp.drr.get(i).substring(Bimp.drr.get(i).lastIndexOf("/")+1);

                    photoNameStr += name + "&";
                }
                Log.e("PublishedActivity:Init", "说说中所有图片名字组成的字符串photoNameStr:"+photoNameStr);
//                photos.put(bitexample.size()+"", photoNameStr) ;                  //key="shuoshuo", value="图片路径"
//                Bimp.state_photos.put(bitexample.size()+"", photoNameStr);
                Bimp.allPhotosStr.add(photoNameStr);
                startActivity(new Intent(PublishedActivity.this, FriendCircleActivity.class));
            }
        });
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {                              //如果适配到最后一张图片（尚未达到9张）
				holder.image.setImageBitmap(BitmapFactory.decodeResource(   //则最后一张图片设置为“+”（即添加照片的图片）
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

        /**将在相册中选择的图片（路径暂时保存在Bimp.drr中）  一起加载到待发朋友圈页面。
         * 如果是后来又通过相机添加的图片，1.原图保存，并将原图路径保存在bmp.drr中 2.缩略图保存 3.原图与缩略图对应
         * */
		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {      //如果max等于选择缩略图的最大数量，直接更新
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
                            String path = Bimp.drr.get(Bimp.max);
                            Bitmap bm = Bimp.revitionImageSize(path);
                            Bimp.bmp.add(bm);
                            bitexample.add(bm);
//                                Bimp.state_photos.put(Bimp.state_photos.size()+"", Bimp.bmp);
                            String imageName = path.substring(
                                    path.lastIndexOf("/") + 1,
                                    path.lastIndexOf("."));
//                            FileUtils.saveBitmap(bm, "" + newStr);
                            //在/storage/sdcard1/myapp下新建临时文件夹tmp，用于保存临时的图片文件
                            Log.e("PublishedActivity:loading", "--------------将选中的待发朋友圈的图片保存在临时文件夹tmp中------------");

                            //改进1：临时路径完全不需要（因为图片路径和缩略图路径都都有了，直接将上传的图片的文件名组成字符串传递给服务器端）、原图片上传到服务器端
                            FileUtils.saveImage(bm, imageName, FileUtils.getFileDir()+"/tmp");
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Button takePhoto = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			camera.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    photo();                    //本身是一个跳往相机的intent,拍完照后有startActivityForResult
                    dismiss();
                }
            });
			takePhoto.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PublishedActivity.this, TestPicActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
			cancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

		}
	}

	private static final int TAKE_PICTURE = 2;
    //调用手机照相功能，并将拍后的照片存到相册
	public void photo() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoName = getFormatSystemTime()+".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constant.savePhotoPath, takePhotoName)));
		startActivityForResult(cameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("PublishedActivity:onActivityResult", "resultCode="+resultCode);
		switch (requestCode) {
		case TAKE_PICTURE:
            //拍完照后，1.将原图路径加入Bimp.drr，以备点击缩略图查看时显示   2.此处不需要剪裁（只有在换头像时为避免图片过大才剪裁），只需要将图片缩略成缩略图，并将缩略图存储在存储卡的缩略图文件夹，并与原图对应（同名）
			if (Bimp.drr.size() < 9) {
				Bimp.drr.add(Constant.savePhotoPath+takePhotoName);
			}

            //制作缩略图并将缩略图保存在路径：/storage/emulated/0/DCIM/.thumbnails/***.jpg
            Bitmap bmp = Bimp.revitionImageSize(Constant.savePhotoPath+takePhotoName);
            FileUtils.saveImage(bmp, takePhotoName, Constant.savePhotoThumbPath);
            break;

		}
	}

    public String getFormatSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

}
