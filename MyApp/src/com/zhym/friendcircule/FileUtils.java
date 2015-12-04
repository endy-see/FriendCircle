package com.zhym.friendcircule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
    private final static String CACHE = "/myapp";
	
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/formats/";

//	public static void saveBitmap(Bitmap bm, String imageName) {
//		Log.e("", "保存图片");
//		try {
//			if (!isFileExist("")) {
//				File tempf = createSDDir("");
//			}
//			File f = new File(SDPATH, imageName + ".JPEG");
//			if (f.exists()) {
//				f.delete();
//			}
//			FileOutputStream out = new FileOutputStream(f);
//			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            Log.e("FileUtil", "已经保存的图片路径："+SDPATH+imageName+".JPEG");
//            out.flush();
//			out.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static File createSDDir(String dirName) throws IOException {
//		File dir = new File(SDPATH + dirName);
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//            Log.e("FileUtil:createSDDir:创建SD卡目录","dir.getAbsolutePath():"+dir.getAbsolutePath()+", dir.mkdir():"+dir.mkdir());
//			System.out.println("createSDDir:" + dir.getAbsolutePath());
//			System.out.println("createSDDir:" + dir.mkdir());
//		}
//		return dir;
//	}
	
	public static void delFile(String tmpDirName) {
		File file = new File(getFileDir()+tmpDirName);
		if(file.isFile()){
            Log.e("FileUtils:delFile:PhotoActvity", "------要删除此图片啦------正在使用delFile(fileName)方法！！！-----");
			file.delete();
        }
		file.exists();
	}

    /**
     * 保存图片到/sdcard/myapp/tmpDir（先确定临时目录存在，然后再向其中存入bitmap）
     * 修改：保存图片到指定路径
     * */
    public static void saveImage(Bitmap bitmap, String imageName, String absDir) {
        File absFileDir = new File(absDir);
        if(!absFileDir.exists())
            absFileDir.mkdir();

        FileOutputStream fos;
        File file = new File(absFileDir, imageName+".jpg");
        if(file.exists())                           //确保图片不重复
            file.delete();

        try {
            fos = new FileOutputStream(file);
            if(fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //将发表的说说图片保存到tmp文件夹下
    public static void saveImageToTmpDir(Bitmap bitmap, String imageName) {
        FileOutputStream fos;
        File file = new File(getFileDir()+"/tmp/",imageName+".png");
        if(!file.exists()) {
            try {
                fos = new FileOutputStream(file);
                if(fos != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    Log.e("FileUtile:saveImageToTmpDir","保存图片到tmp中成功！保存的路径="+getFileDir()+"/tmp/imageName.png");
                    fos.flush();
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 当把Bimp.bmp中存储的缩略图都删完或者发布shuoshuo后，
     * 就可以把保存这些缩略图的临时目录删除了
     * 下面方法没起作用，因为SDPATH本身就没有用*********************待改正************
     * */
	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
                file.delete(); // 删除所有文件
            }
			else if (file.isDirectory()) {
                deleteDir(); // 递规的方式删除文件夹
            }
		}
		dir.delete();// 删除目录本身
	}

    /**
     * 获取缓存文件夹目录 如果不存在则创建文件夹
     * */
    public static String getFileDir() {     //fileName:可以是文件名，也可以是文件夹名
        String filePath;
        filePath = getSDPath() + CACHE;
        File file = new File(filePath);
        if(!file.exists()) {        //如果不存在此文件，则新建一个名为fileName的文件夹
            file.mkdir();
        }
        return filePath;
    }

    public static Bitmap getImageFromSDCard(String imageName) {
        String filepath = getFileDir()+"/"+imageName;
        File file = new File(filepath);
        if(file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            return bm;
        } else {
            return null;
        }
    }

    public static Bitmap getImageFromTmpDir(String imageName) {
        String filepath = getFileDir()+"/tmp/"+imageName;
        File file = new File(filepath);
        if(file.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filepath);
            return bm;
        } else {
            Log.e("FileUtil:getImageFromTmpDir","tmp目录"+filepath+"中没有此图片！！！");
            return null;
        }
    }

    /**
     * 获取sd卡的缓存路径， 一般在卡中sdCard就是这个目录
     *
     * @return SDPath
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  //判断sdCard是否存在
        if(sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();      //获取根目录
        } else {
            Log.e("FileUtil:getSDPath","没有内存卡");
        }
        return sdDir.toString();
    }

    public static String getImageNameFromPath(String path) {
        int nameIndex = path.lastIndexOf("/") + 1;
        String imageName = path.substring(nameIndex);
        return imageName;
    }

}
