package com.joysoft.andutils.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.joysoft.andutils.lg.Lg;

/**
 * 查看网络状态和监听网络状态变化
 * @author fengmiao
 *
 */
public class NetworkUtil {
	public static final String TAG = NetworkUtil.class.getName();
	
	public interface NetworkListener{
		public void onNetworkChanged(NetworkType ot, NetworkType nt);
	}
	
	public static enum NetworkType{
		WIFI_FAST, MOBILE_FAST, MOBILE_MIDDLE, MOBILE_SLOW, NONE,
	}
	
	private NetworkType type;
	private NetworkListener listener;
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateNetwork(context);
		}
	};
	
	public NetworkUtil(Context context) {
		type = NetworkType.NONE;
		updateNetwork(context);

		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.getApplicationContext().registerReceiver(receiver, filter);
	}
	
	public final synchronized NetworkType getNetworkType() {
		return type;
	}
	
	public final void setListener(NetworkListener l) {
		listener = l;
	}

	public final void unRegisterReceiver(Context context){
		this.listener = null;
		context.getApplicationContext().unregisterReceiver(receiver);
	}
	
	private final void updateNetwork(Context context) {
		NetworkInfo networkInfo = getNetworkInfo(context);
		NetworkType t = type;
		type = checkType(networkInfo);
		if (type != t && listener != null) {
			listener.onNetworkChanged(t, type);
		}
		Lg.d("network [type] " + type);
	}

	private final synchronized NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return manager.getActiveNetworkInfo();
	}

	private static NetworkType checkType(NetworkInfo info) {
		if (info == null || !info.isConnected()) {
			return NetworkType.NONE;
		}

		int type = info.getType();
		int subType = info.getSubtype();
		if ((type == ConnectivityManager.TYPE_WIFI)
				|| (type == ConnectivityManager.TYPE_ETHERNET)) {
			return NetworkType.WIFI_FAST;
		}

		if (type == ConnectivityManager.TYPE_MOBILE) {
			switch (subType) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return NetworkType.MOBILE_SLOW; // 2G

			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return NetworkType.MOBILE_MIDDLE;// 3G

			case TelephonyManager.NETWORK_TYPE_LTE:
				return NetworkType.MOBILE_FAST; // 4G
			}
		}

		return NetworkType.NONE;
	}
}
