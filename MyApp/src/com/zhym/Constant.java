package com.zhym;

import com.zhym.friendcircule.FileUtils;

/**
 * Created by lenovo on 2015/10/27.
 */
public class Constant {
    public static final String savePhotoPath = FileUtils.getSDPath()+"/DCIM/Camera/";           //相机拍摄的照片的存放地址
    public static final String savePhotoThumbPath = "/storage/emulated/0/DCIM/.thumbnails/";    //拍摄的照片对应的缩略图的存放地址


    public static final String URL_UPDATE_Avatar = "http://10.108.216.139/myown/update_avatar.php";
    public static final String URL_UPDATE_Photos = "http://10.108.216.139/myown/proctice.php";
//    public static final String URL_GET_SHUOSHUO = "http://10.108.216.139/myown/load_friendcircule.php";
//    public static final String URL_UPLOAD_DIR = "http://10.108.216.139/myown/upload";
}
