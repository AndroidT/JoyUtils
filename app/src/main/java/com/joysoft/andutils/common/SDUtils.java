package com.joysoft.andutils.common;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

/**
 * 
 * sd卡工具类
 * @author fengmiao
 *
 */
public class SDUtils {
	
	 /** 
     * 获得SD卡总大小 
     *  
     * @return 
     */  
    public String getSDTotalSize(Context context) {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return Formatter.formatFileSize(context, blockSize * totalBlocks);  
    }  
  
	/**
	 * 获取Sd卡剩余空间
	 * 
	 * @return
	 */
	public static long getAvaiableSpace() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);

			return availableSpare;
		}
		return -1;
	}

	/**
	 * 判断当前sd卡是否有效使用
	 * 
	 * @return trueΪ
	 */
	public static boolean isSDAvailable(int minSDSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		if (!isAvaiableSpace(minSDSize)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断当前sd卡剩余空间是否够用
	 * @param sizeMb
	 * @return
	 */
	private static boolean isAvaiableSpace(int sizeMb) {
		return getAvaiableSpace() > sizeMb;
	}
}
