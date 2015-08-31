package com.joysoft.andutils.app;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.joysoft.andutils.lg.Lg;

/**
 * 系统及硬件常用工具
 * @author fengmiao
 *
 */
public class SystemUtils {
	// 亮度值
	public static final int MAX_BRIGHTNESS = 255;
	public static final int MIN_BRIGHTNESS = 0;

	/**
	 * 判断是否在主线程
	 * 
	 * @return
	 */
	public final static boolean isMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}
	
	/**
	 * 状态栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int height = 0;
		if (context == null) {
			return height;
		}
		Resources resources = context.getResources();
		int resId = resources.getIdentifier("status_bar_height", "dimen",
				"android");
		if (resId > 0) {
			height = resources.getDimensionPixelSize(resId);
		}
		return height;
	}

	/**
	 * 获取屏幕亮度
	 * 
	 * @return
	 */
	public static int getSysScreenBrightness(Context context) {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return screenBrightness;
	}

	/**
	 * 设置屏幕亮度 1 auto, 0 manual
	 * 
	 * @param context
	 * @param brightnessMode
	 */
	public static void setBrightnessMode(Context context, int brightnessMode) {
		try {
			Settings.System.putInt(context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static boolean isRooted() {
		String binaryName = "su";
		boolean rooted = false;
		String[] places = { "/sbin/", "/system/bin/", "/system/xbin/",
				"/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/",
				"/system/bin/failsafe/", "/data/local/" };
		for (String where : places) {
			if (new File(where + binaryName).exists()) {
				rooted = true;
				break;
			}
		}
		return rooted;
	}

	/**
	 * 锁屏
	 * 
	 * @param context
	 */
	public static void lockScreen(Context context) {
		DevicePolicyManager deviceManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		deviceManager.lockNow();
	}

	// <uses-permission android:name="android.permission.INJECT_EVENTS" />
	public final static void inputKeyEvent(int keyCode) {
		try {
			runRootCmd("input keyevent " + keyCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String runCmd(String cmd) {
		if (TextUtils.isEmpty(cmd)) {
			return null;
		}
		Process process = null;
		String result = null;

		String[] commands = { "/system/bin/sh", "-c", cmd };

		try {
			process = Runtime.getRuntime().exec(commands);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			InputStream errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');

			InputStream inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}

			byte[] data = baos.toByteArray();
			result = new String(data);

			Lg.d("runCmd result " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void runRootCmd(String cmd) {
		if (TextUtils.isEmpty(cmd)) {
			return;
		}
		Process process;
		try {
			process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(
					process.getOutputStream());
			os.writeBytes(cmd + " ;\n");
			os.flush();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			InputStream errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');

			InputStream inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}

			byte[] data = baos.toByteArray();
			String result = new String(data);

			Lg.d("runRootCmd result " + result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 滑动距离
	 * @param e1
	 * @param e2
	 * @return
	 */
	public static int getDistance(MotionEvent e1, MotionEvent e2) {
		float x = e1.getX() - e2.getX();
		float y = e1.getY() - e2.getY();
		return (int) Math.sqrt(x * x + y * y);
	}
	
	public static long getMaxMemory() {
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		Lg.d("application max memory " + maxMemory);
		return maxMemory;
	}
	
	public static boolean isDebuggable(Context context) {
		boolean debuggable = ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
		return debuggable;
	}
	
	
	/**
	 * app目录
	 * @param context
	 * @return
	 */
	public static String getApplicaitonDir(Context context) {
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();
		String applicationDir = null;
		try {
			PackageInfo p = packageManager.getPackageInfo(packageName, 0);
			applicationDir = p.applicationInfo.dataDir;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return applicationDir;
	}
	
	/**
	 * ？应用重启？
	 * @param context
	 * @param clazz
	 */
	public static void restartApplication(Context context,Class<?> clazz) {
		Intent intent = new Intent(context, clazz);
		int pendingIntentId = 198964;
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				pendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC, System.currentTimeMillis() + 500,
				pendingIntent);
		System.exit(0);
	}
	
	public static int getUid(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_ACTIVITIES);
			return ai.uid;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// 获得可用的内存
    public static long getMeM_UNUSED(Context mContext) {
        long MEM_UNUSED;
	// 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
	// 创建ActivityManager.MemoryInfo对象  

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

	// 取得剩余的内存空间 

        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }
	
}
