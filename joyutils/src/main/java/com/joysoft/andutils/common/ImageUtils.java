package com.joysoft.andutils.common;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {
	
	/**
	 * 将图片文件移至相册
	 * 
	 * @param iv
	 *            图片所在的ImageView
	 * @return
	 */
	public static boolean downloadImg(ImageView iv,String name) {

		Drawable drawable = iv.getDrawable();

		BitmapDrawable bd = (BitmapDrawable) drawable;

		Bitmap bitmap = bd.getBitmap();
		
		return downloadImg(bitmap, name);
	}
	
	/**
	 * 将图片文件移至相册
	 * 
	 * @param iv
	 *            图片所在的ImageView
	 * @return
	 */
	public static boolean downloadImg(Bitmap bitmap,String name) {
		
		if (name == null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			Date curDate = new Date(System.currentTimeMillis());
			name = formatter.format(curDate) + ".jpg";
		}
		
		return BitmapUtils.saveBitmap(bitmap, photoPath+"/"+name, 100);
	}

	private static String photoPath = Environment.getExternalStorageDirectory()
			+ "/DCIM/Camera/";
	
	
//	//将图片文件移至相册
//	private static Boolean saveBitmapToCamera(Bitmap bm, String name) {
//
//		File file = null;
//
//		if (name == null) {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//			Date curDate = new Date(System.currentTimeMillis());
//			name = formatter.format(curDate) + ".jpg";
//		}
//
//		file = new File(photoPath, name);
//		if (file.exists()) {
//			file.delete();
//		}
//
//		try {
//			FileOutputStream out = new FileOutputStream(file);
//			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
//			out.flush();
//			out.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
	
	
}
