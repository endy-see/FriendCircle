package com.zhym.friendcircule;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.*;
import android.util.Log;

import com.zhym.bean.ImageBucket;
import com.zhym.bean.ImageItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.*;

public class AlbumHelper {
    Context context;
    ContentResolver resolver;
    private static AlbumHelper helper = null;

    HashMap<String, String> thumbnailList = new HashMap<>();            //缩略图列表
    HashMap<String, ImageBucket> bucketList = new HashMap<>();          //专辑列表

    List<ImageItem> allImages = new ArrayList<>();                      //用于"修改头像"时显示所有图片
    private HashSet<String> mImageBucket = new HashSet<>();             //临时的辅助类，用于防止同一个文件夹的多次扫描

    boolean hasBuildImagesBucketList = false;

    private AlbumHelper() {};
    public static AlbumHelper getHelper() {
        if(helper == null) {
            helper = new AlbumHelper();
        }
        return helper;
    }

    public void init(Context cx) {
        this.context = cx;
        resolver = context.getContentResolver();
    }

    /**
     * 获取所有图片项
     * */
    public List<ImageItem> getAllImages() {
        return allImages;
    }

    /**
     * 获取图片集：接口供外面调用
     * */
    public List<ImageBucket> getImagesBucketList(boolean refresh) {
        List<ImageBucket> tmpList = new ArrayList<>();

        if(refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        Iterator<Entry<String, ImageBucket>> iterator = bucketList.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, ImageBucket> entry = iterator.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    /**
     * 图片集列表：将各个图片集中所有图片的path都放到各图片集中
     * */
    public void buildImagesBucketList() {
        //构建缩略图列表，以便后面将原图与缩略图对应
        getThumbnailList();

        //访问所有原图存放路径，获取所有原图并处理
        String[] columns = new String[] {
                Media._ID,                          //单个图片在存储中对应的唯一标识
                Media.DATA,                         //单个图片的存放路径
                Media.DISPLAY_NAME,                 //单个图片的名字+格式，如wo.png
                Media.TITLE,                        //单个图片的名字，如wo
                Media.SIZE,                         //单个图片的大小
                Media.BUCKET_ID,                    //图片集在存储中对应的唯一标识
                Media.BUCKET_DISPLAY_NAME };          //图片集的名字
        Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        while(cursor.moveToNext()) {
            String img_id = cursor.getString(cursor.getColumnIndex(columns[0]));
            String img_path = cursor.getString(cursor.getColumnIndex(columns[1]));
            String img_name = cursor.getString(cursor.getColumnIndex(columns[2]));
            String img_title = cursor.getString(cursor.getColumnIndex(columns[3]));
            String img_size = cursor.getString(cursor.getColumnIndex(columns[4]));
            String bucket_id = cursor.getString(cursor.getColumnIndex(columns[5]));
            String bucket_name = cursor.getString(cursor.getColumnIndex(columns[6]));

            //作用：给“相册”提供数据源。图片集、图片集中的所有图片建立对应关系
            //实现：获取图片所属的图片集，并将图片集相同的所有图片都归到该图片集的imageList中
            ImageBucket bucket = bucketList.get(bucket_id);
            if(bucket == null) {
                bucket = new ImageBucket();
                bucket.bucketName = bucket_name;
                bucket.imageList = new ArrayList<>();
                bucketList.put(bucket_id, bucket);
            }
            ImageItem imgItem = new ImageItem();
            imgItem.imageId = img_id;
            imgItem.imagePath = img_path;
            imgItem.thumbnailPath = thumbnailList.get(img_id);
            if(!bucket.imageList.contains(imgItem)) {               //去除重复元素
                allImages.add(imgItem);     //给“修改头像”提供所有图片
                bucket.imageList.add(imgItem);
                bucket.count++;
            }
        }
        cursor.close();

    }

    /**
     *缩略图列表：从本地缩略图文件夹中获取其所有缩略图
     *作用范围：仅在本类中,供下面的buildImagesBucketList调用，故可以定义成private
     * */
    private void getThumbnailList() {
        String[] projection = {Thumbnails.IMAGE_ID, Thumbnails.DATA};
        Cursor cursor = resolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while(cursor.moveToNext()) {
           // cursor.getColumnIndex(Thumbnails._ID);
            int image_id = cursor.getInt(cursor.getColumnIndex(projection[0]));
            String image_path = cursor.getString(cursor.getColumnIndex(projection[1]));
            Log.e("AlbumHelper:getThumbnailList","缩略图路径="+image_path);
            thumbnailList.put(image_id + "", image_path);
        }
        cursor.close();
    }

 }