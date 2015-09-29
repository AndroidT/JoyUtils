package com.joysoft.andutils.common;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.joysoft.andutils.lg.Lg;

/**
 * 检查网络状态的工具类
 * @author fengmiao
 *
 */
public class NetUtils {
	
	/**
	 *打开设置网页界面
	 */
	public static void openSettingNet(Context context) {
        try{
        	Intent intent=null;
            //判断手机系统的版本  即API大于10 就是3.0或以上版本
            if(android.os.Build.VERSION.SDK_INT>10){
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
               Lg.d("api level 10");
            }else{
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
                Lg.d("api level less 10");
            }
            context.startActivity(intent);
        }catch(Exception e){
        	TipUtils.ShowText(context, "操作失败");
        }
    }
	
	/**
	 * 检查当前网络状态是否有效
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isAvailable() && info.isConnected()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 判断当前是否是wifi连接
     * Judge network  whether Wifi connectivity
     * @param context
     * @return
     */
	public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
        	int type = networkInfo.getType();
        	if (networkInfo.isAvailable() && networkInfo.isConnected() && type == ConnectivityManager.TYPE_WIFI ) {
        		return true;
        	}
        }
		return false;
	}
	
	/**
     * 判断当前网络是否是移动流量连接
     * @param context
     * @return
     */
    public static  boolean  isMobile(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
        	int type = networkInfo.getType();
        	if (networkInfo.isAvailable() && networkInfo.isConnected() && type == ConnectivityManager.TYPE_MOBILE) {
        		return true;
        	}
        }
        return false;
    }
    
    
    /**
     * 打开wifi
     * Whether open wifi
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }
}


