package com.zhym.friendcircule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.GridView;

import com.zhym.adapter.CommonAdapter;
import com.zhym.adapter.ViewHolder;
import com.zhym.bean.ImageItem;
import com.zhym.myapp.R;

public class ImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
    BitmapCache cache;
    HashMap<String, String> current_select_image = new HashMap<>();         //之所以选择Map这种数据结构，主要是因为存在删除指定（路径）文件。这个如果直接用ArrayList是无法做到的
    CommonAdapter commonAdapter;
	List<ImageItem> dataList;
	GridView gridView;
	Button finished;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);

//		helper = AlbumHelper.getHelper();
//		helper.init(getApplicationContext());
        cache = new BitmapCache();
		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);

		initView();
		finished = (Button) findViewById(R.id.finished);
		finished.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
//                Collection<String> c = current_select_image.values();
                ArrayList<String> list = new ArrayList<>(current_select_image.values());            //将Map中的值不重复得存在ArrayList中
                Log.e("ImageGridActivity:onCreate:finished", "发表：将set值赋给Arraylist list.size()="+list.size());

//                Iterator<String> it = c.iterator();
//                for (; it.hasNext(); ) {
//                    list.add(it.next());
//                }
                for (int i = 0; i < list.size(); i++) {
                    if (Bimp.drr.size() < 9) {
                        Log.e("ImageGridActivity:onCreate:finished", "Bitmp.drr.add(list.get(i))="+list.get(i));
                        Bimp.drr.add(list.get(i));                      //将确定选择的原图片路径保存到静态变量中，以备“PublishedActivity.java”使用
                    }
                }

                if (Bimp.act_bool) {
                    Intent intent = new Intent(ImageGridActivity.this,PublishedActivity.class);
                    startActivity(intent);
                    //Bimp.act_bool = false;
                }
                finish();
            }

        });
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
        gridView = (GridView) findViewById(R.id.albums);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        commonAdapter = new CommonAdapter<ImageItem>(ImageGridActivity.this, dataList, R.layout.item_image_grid) {
            private int selectTotal = 0;

            BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                    if (imageView != null && bitmap != null) {
                        String url = (String) params[0];
                        if (url != null && url.equals(imageView.getTag())) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Log.e("ImageGridActivity", "callback, bmp not match");
                        }
                    } else {
                        Log.e("ImageGridActivity", "callback, bmp null");
                    }
                }
            };

            @Override
            public void convert(final ViewHolder holder, final ImageItem imageItem, final TextCallback textCallback) {
                holder.setTag(R.id.image, imageItem.imagePath);
                cache.displayBmp((ImageView) holder.getView(R.id.image), imageItem.thumbnailPath, imageItem.imagePath, callback);
                if (imageItem.isSelected) {
                    holder.setVisibility(R.id.isselected, true);
                    holder.setImageResource(R.id.isselected, R.drawable.icon_photo_select);
                    holder.setBackgroundRes(R.id.item_image_grid_text, R.drawable.mask_margin_line);
                } else {
                    holder.setVisibility(R.id.isselected, false);
                    holder.setBackgroundColor(R.id.item_image_grid_text, 0x00000000);
                }

                holder.setOnClickListenere(R.id.image, new OnClickListener() {
                    @Override
                    public void onClick(View v) {                                   //因为此时的变动比较大，所以用临时Map：current_select_image增加选择的图片或去掉取消选择的图片
                        String path = imageItem.imagePath;
                        if ((Bimp.drr.size() + selectTotal) < 9) {                  //小于9张时，既可以继续添加，又可以取消原来已添加的；每选择或取消一张图片，都有执行一次接口方法（相当于一个监听器）来动态改变选择数目
                            imageItem.isSelected = !imageItem.isSelected;
                            if (imageItem.isSelected) {
                                holder.setVisibility(R.id.isselected, true);
                                holder.setImageResource(R.id.isselected, R.drawable.icon_photo_select);
                                holder.setBackgroundRes(R.id.item_image_grid_text, R.drawable.mask_margin_line);
                                selectTotal++;
                                if (textCallback != null)
                                    textCallback.onListen(selectTotal);

                                current_select_image.put(path, path);
                            } else if (!imageItem.isSelected) {
                                holder.setVisibility(R.id.isselected, false);
                                holder.setBackgroundColor(R.id.item_image_grid_text, 0x000000);
                                selectTotal--;
                                if (textCallback != null)
                                    textCallback.onListen(selectTotal);

                                current_select_image.remove(path);
                            }
                        } else if ((Bimp.drr.size() + selectTotal) >= 9) {          //数量达到9张以后，只能对已选中的图片进行“取消选择”的操作；否则向主线程发送消息提示不能再添加
                            if (imageItem.isSelected) {
                                holder.setVisibility(R.id.isselected, false);
                                imageItem.isSelected = !imageItem.isSelected;
                                selectTotal--;
                                current_select_image.remove(path);
                            } else {
                                Message message = Message.obtain(mHandler, 0);
                                message.sendToTarget();
                            }
                        }
                    }

                });
            }

        };
        commonAdapter.setTextCallback(new CommonAdapter.TextCallback(){
            @Override
            public void onListen(int count) {
                finished.setText("完成" + "(" + count + ")");
            }
        });
        gridView.setAdapter(commonAdapter);


//        gridView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                adapter.notifyDataSetChanged();
//            }
//
//        });

    }

}
