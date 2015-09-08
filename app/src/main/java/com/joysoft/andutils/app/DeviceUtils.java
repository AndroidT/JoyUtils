package com.joysoft.andutils.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.joysoft.andutils.common.BitmapUtils;
import com.joysoft.andutils.lg.Lg;

/**
 * 硬件工具类
 * @author fengmiao
 *
 */
public class DeviceUtils {
	
	/**
	 * 获取手机信息
	 * @param context
	 * @return
	 */
	public static String getInfo(Context context) {
		String model = android.os.Build.MODEL;
		String device = android.os.Build.DEVICE;
		String brand = android.os.Build.BRAND;
		String product = android.os.Build.PRODUCT;
		String display = android.os.Build.DISPLAY;
		String manufacture = android.os.Build.MANUFACTURER;

		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		float density = dm.density;

		StringBuilder sb = new StringBuilder();
		String finalInfo = sb.append("MODEL " + model)
				.append("\nDEVICE " + device).append("\nBRAND " + brand)
				.append("\nPRODUCT " + product).append("\nDISPLAY " + display)
				.append("\nMANUFACTURE " + manufacture)
				.append("\nSCREEN_WIDTH " + screenWidth)
				.append("\nSCREEN_HEIGHT " + screenHeight)
				.append("\nDENSITY " + density).toString();
		return finalInfo;
	}
	
	/**
	 * 蓝牙mac
	 * @return
	 */
	public static final String getBluetoothMac() {
		BluetoothAdapter adapter = null;
		String bluetoothMac = null;
		try {
			adapter = BluetoothAdapter.getDefaultAdapter();
			bluetoothMac = adapter.getAddress();
		} catch (Exception e) {
		}
		return bluetoothMac;
	}

	/**
	 * wlanMac
	 * @param context
	 * @return
	 */
	public static final String getWlanMac(Context context) {
		String wlanMac = null;
		try {
			WifiManager wm = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			wlanMac = wm.getConnectionInfo().getMacAddress();
		} catch (Exception e) {

		}
		return wlanMac;
	}
	
	/**
	 * 获取android版本
	 * @return
	 */
	public static float getAndroidVersion() {
		return Float.valueOf(android.os.Build.VERSION.RELEASE);
	}
	
	/**
	 * 获取手机型号
	 * @return
	 */
	public String getDeviceModel () {
		return android.os.Build.MODEL;
	}
	
	/**
	 * 获取sdk版本
	 * get android os sdk version  2.2 = 8,2.3 = 9,4.2.1 = 17
	 * @return sdk version
	 */
	public static int getSDKVersion(){
		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * 获取androidId
	 * @param context
	 * @return
	 */
	public static final String getAndroidId(Context context) {
		String androidID = null;
		try {
			androidID = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (Exception e) {

		}
		return androidID;
	}

	
	public static final String getPackageName(Context context){
		String packageName = "";
		try{
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//			String className = cn.getClassName();
			packageName = cn.getPackageName();
		}catch(Exception e){
			Lg.e(e.toString());
		}
		return packageName;
	}
	
	/**
	 * IMEI
	 * @param context
	 * @return
	 */
	public static final String getIMEI(Context context) {
		String deviceIMEI = null;
		try {
			TelephonyManager teleManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceIMEI = teleManager.getDeviceId();
		} catch (Exception e) {

		}
		return deviceIMEI;
	}

	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 像素-->dp
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dp(Context context,int px) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int dp = Math.round(px
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

	/**
	 * sp --> px
	 * @param context
	 * @param sp
	 * @return
	 */
	public static int sp2px(Context context,float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		int px = Math.round(sp * scale);
		return px;
	}
	
	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		try{
			DisplayMetrics dm = new DisplayMetrics();
			((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
			return dm.heightPixels;
		}catch(Exception e){
			Lg.e(e.toString());
			return 0;
		}
	}
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		try{
			DisplayMetrics dm = new DisplayMetrics();
			((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
			return dm.widthPixels;
		}catch(Exception e){
			Lg.e(e.toString());
			return 0;
		}
	}
	
	/**
	 * 获取系统状态栏高度
	 * return system bar height
	 * @param context
	 * @return
	 */
	public static int getStatuBarHeight(Context context) {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");  
			Object obj;
			obj = c.newInstance();
			Field field = c.getField("status_bar_height");  
	        int width = Integer.parseInt(field.get(obj).toString());  
	        int height = context.getResources().getDimensionPixelSize(width);
	        return height;
		} catch (Exception e) {
			e.printStackTrace();
			Lg.e("getStatuBarHeight："+e.toString());
		}
		return 0;
	}
	
	/**
	 * 获取系统状态栏高度
	 * return system bar height
	 * @param context
	 * @return
	 */
	public static int getStatuBarHeight2(Context context) {
		Rect frame = new Rect();  
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
        int statusBarHeight = frame.top;  
        return statusBarHeight;
	}
	
	/**
	 * 获取屏幕英寸
	 * @param context
	 * @return
	 */
	public static float getScreenInches(Context context) {
		float screenInches = -1;
		try {
			Resources resources = context.getResources();
			DisplayMetrics dm = resources.getDisplayMetrics();
			double width = Math.pow(dm.widthPixels / dm.xdpi, 2);
			double height = Math.pow(dm.heightPixels / dm.ydpi, 2);
			screenInches = (float) (Math.sqrt(width + height));
		} catch (Exception e) {
		}
		return screenInches;
	}
	
	
	/**
	 * 获取某个view 的宽度和高度 o[0]---width o[1]---height
	 */

	public static int[] getHeghtAndWidth(View view) {
		int o[] = new int[2];
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		o[0] = view.getMeasuredWidth();
		o[1] = view.getMeasuredHeight();
		return o;
	}
	
	/**
	 * 获取屏幕密度
	 * @param context
	 * @return
	 */
	public static int getDensity(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		int density = metrics.densityDpi;
		return density;
	}
	
	/**
	 * 获得屏幕的截图
	 * 
	 * @param activity
	 * 
	 * @return
	 */
	public static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		//图片允许最大空间   单位：KB 
        double maxSize = 100.00; 
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）   
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
        b1.compress(Bitmap.CompressFormat.JPEG, 70, baos); 
        byte[] b = baos.toByteArray(); 
        //将字节换成KB 
        double mid = b.length/1024; 
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩 
        if (mid > maxSize) { 
                //获取bitmap大小 是允许最大大小的多少倍 
                double i = mid / maxSize; 
                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小） 
//                b1 = ImageUtils.zoomBitmap(b1, b1.getWidth() / Math.sqrt(i), 
//                		b1.getHeight() / Math.sqrt(i));
                b1 = BitmapUtils.resizeImage(b1, (int) (b1.getWidth() / Math.sqrt(i)), (int) (b1.getHeight() / Math.sqrt(i)), false);
        } 
		
		return b1;
	}
	
    /** 
     * 获得机身内容总大小 
     *  
     * @return 
     */  
    public String getRomTotalSize(Context context) {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long totalBlocks = stat.getBlockCount();  
        return Formatter.formatFileSize(context, blockSize * totalBlocks);  
    }  
  
    /** 
     * 获得机身可用内存 
     *  
     * @return 
     */  
    public String getRomAvailableSize(Context context) {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks();  
        return Formatter.formatFileSize(context, blockSize * availableBlocks);  
    } 
    
    /**
     * 获取分配的heap大小
     * @param context
     * @return
     */
    public static int getHeapSize(Context context){
    	int size = 0;
    	try{
    		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);  
            size = activityManager.getMemoryClass();  
            Lg.e(((Activity)context).getClass().getSimpleName()+"所在进程"+android.os.Process.myPid()+" 限制内存大小为:"+size+"M");
    	}catch(Exception e){
    		Lg.e(e.toString());
    	}
    	
        return size;
    }
}
