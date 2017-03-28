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
	 * 判断当前可用空间   没有sd卡时使用机身分配的空间
	 * 
	 * @return trueΪ
	 */
	public static boolean isSDAvailable(int minSDSize) {
		if (!isAvaiableSpace(minSDSize)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断当前app可用剩余空间是否够用
	 * @param sizeMb
	 * @return
	 */
	private static boolean isAvaiableSpace(int sizeMb) {
		return getAvaiableSpace() > sizeMb;
	}

	/**
	 * 获取当前app可用空间
	 *
	 * @return  单位 M
	 */
	public static long getAvaiableSpace() {
		String sdCard = "";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			sdCard = Environment.getExternalStorageDirectory().getPath();
		} else {
			sdCard = Environment.getDataDirectory().getPath();
		}
		StatFs statFs = new StatFs(sdCard);
		long blockSize = statFs.getBlockSize();
		long blocks = statFs.getAvailableBlocks();
		long spare = (blocks * blockSize) / (1024 * 1024);
		//Lg.d("calcAvailableSpare:" + spare);
		return spare;
	}


}
