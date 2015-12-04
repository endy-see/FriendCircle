package com.zhym.friendcircule;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.zhym.adapter.CommonAdapter;
import com.zhym.adapter.ViewHolder;
import com.zhym.bean.ImageBucket;
import com.zhym.myapp.*;

import org.w3c.dom.Text;

import static com.zhym.myapp.R.layout.item_image_bucket;

public class TestPicActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
    BitmapCache cache;
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap source_path_numm_bmp, thumb_path_null_bmp;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
        cache = new BitmapCache();

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		dataList = helper.getImagesBucketList(false);	
		source_path_numm_bmp = BitmapFactory.decodeResource(getResources(),R.drawable.source_path_null);
        thumb_path_null_bmp = BitmapFactory.decodeResource(getResources(), R.drawable.thumb_path_null);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.albums);
//		adapter = new ImageBucketAdapter(TestPicActivity.this, dataList);
//		gridView.setAdapter(adapter);
        gridView.setAdapter(new CommonAdapter<ImageBucket>(TestPicActivity.this, dataList, item_image_bucket) {
            BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
                    if (imageView != null && bitmap != null) {
                        String url = (String) params[0];
                        if (url != null && url.equals(imageView.getTag())) {
                            (imageView).setImageBitmap(bitmap);
                        } else {
                            Log.e("TestPicActivity:url为null", "callback, bmp not match");
                        }
                    } else {
                        Log.e("TestPicActivity:bitmap为null", "callback, thumbBmp null");
                    }
                }

            };
            @Override
            public void convert(ViewHolder holder, ImageBucket imageBucket, TextCallback txCallback) {
                holder.setText(R.id.name, imageBucket.bucketName);
                holder.setText(R.id.count, imageBucket.count+"");
                holder.setVisibility(R.id.isselected, false);

                if(imageBucket.imageList != null && imageBucket.imageList.size() > 0) {
                    String thumbPath = imageBucket.imageList.get(0).thumbnailPath;
                    String sourcePath = imageBucket.imageList.get(0).imagePath;
                    holder.setTag(R.id.image, sourcePath);
                    cache.displayBmp((ImageView) holder.getView(R.id.image), thumbPath, sourcePath, callback);
                } else {
                    holder.setImageBitmap(R.id.image, null);
                    Log.e("TestPicActivity:getView", "no images in bucket " + imageBucket.bucketName);
                }
            }
        });

        try {
            gridView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    /**
                     * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
                     * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
                     */
                    // if(dataList.get(position).isSelected()){
                    // dataList.get(position).setSelected(false);
                    // }else{
                    // dataList.get(position).setSelected(true);
                    // }
                    /**
                     * 通知适配器，绑定的数据发生了改变，应当刷新视图
                     */
                    // adapter.notifyDataSetChanged();
                    Intent intent = new Intent(TestPicActivity.this,
                            ImageGridActivity.class);
                    intent.putExtra(TestPicActivity.EXTRA_IMAGE_LIST,
                            (Serializable) dataList.get(position).imageList);
                    startActivity(intent);
                    finish();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
