package com.joysoft.andutils.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;

import com.joysoft.andutils.R;
import com.joysoft.andutils.common.TipUtils;
import com.joysoft.andutils.lg.Lg;

import java.util.List;

/**
 * app帮助类
 * Created by fengmiao on 15/8/30.
 */
public class AppHelper {

    /**
     * 获取当前应用版本信息
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取VersionCode
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            int version = packInfo.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 应用评价
     *
     * @param context
     */
    public static void appraiseApp(Context context) {
        try {
            Uri uri = null;
            if (isMarketInstalled(context)) {
                Lg.e(context.getApplicationInfo().packageName);
                uri = Uri.parse("market://details?id="
                        + context.getApplicationInfo().packageName);
            } else {
                uri = Uri.parse("http://play.google.com/store/apps/details?id="
                        + context.getApplicationInfo().packageName);

            }
            Intent intent_aboutus = new Intent(Intent.ACTION_VIEW, uri);
            intent_aboutus.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// xy
            context.startActivity(intent_aboutus);
        } catch (Exception e) {
            TipUtils.ShowText(context, "没有应用可执行此操作");
        }
    }

    /**
     * 手机是否安装了市场
     *
     * @param context
     * @return
     */
    private static boolean isMarketInstalled(Context context) {
        Intent market = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://search?q=dummy"));
        PackageManager manager = context.getPackageManager();
        List<ResolveInfo> list = manager.queryIntentActivities(market, 0);
        if (list.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * 根据Uri安装apk
     * @param context
     * @param uri
     */
    public void installApk(Context context,Uri uri) {
        if (uri.toString().endsWith(".apk")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
//                 intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            intent.setDataAndType(uri,"application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }

    /**
     * 卸载应用
     * @param context
     * @param packageName
     */
    public static void uninstallApp(Context context, String packageName) {
        boolean installed = installedApp(context, packageName);
        if (!installed) {
            TipUtils.ShowText(context, "package_not_installed");
            return;
        }

        boolean isRooted = SystemUtils.isRooted();
        if (isRooted) {
            SystemUtils.runRootCmd("pm uninstall " + packageName);
        } else {
            Uri uri = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Intent.ACTION_DELETE, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * ？判断是否安装app
     * @param context
     * @param packageName
     * @return
     */
    public static boolean installedApp(Context context, String packageName) {
        PackageInfo packageInfo = null;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos == null) {
            return false;
        }
        for (int index = 0; index < packageInfos.size(); index++) {
            packageInfo = packageInfos.get(index);
            final String name = packageInfo.packageName;
            if (packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建桌面快捷方式(需要权限：com.android.launcher.permission.INSTALL_SHORTCUT)
     *
     * @param activity
     *            ：Actitivy
     * @param iconResId
     *            ：应用logo的资源id
     * @param appnameResId
     *            ：应用名字的资源id
     */
    public static void createShortCut(Activity activity, int iconResId,int appnameResId,Class<?> homeActivity) {

        try{
            Intent shortcutintent = new Intent(
                    "com.android.launcher.action.INSTALL_SHORTCUT");
            // 不允许重复创建
            shortcutintent.putExtra("duplicate", false);
            // 需要显示的名称
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                    activity.getString(appnameResId));
            // 快捷图片
            Parcelable icon = Intent.ShortcutIconResource.fromContext(
                    activity.getApplicationContext(), iconResId);
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            // 点击快捷图片，运行的程序主入口
            Intent intent = new Intent(activity.getApplicationContext(), homeActivity);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            // 发送广播
            activity.sendBroadcast(shortcutintent);
        }catch(Exception e){
            Lg.e(e.toString());
            TipUtils.ShowText(activity, "操作失败...");
        }
    }

    /**
     * 判断是否已创建快捷方式(需要权限com.android.launcher.permission.READ_SETTINGS)
     *
     * @return
     */
    public static boolean hasShortcut(Context context) {
        boolean isInstallShortcut = false;
        try{
            final ContentResolver cr = context.getContentResolver();
            final String AUTHORITY = getAuthorityFromPermission(context,
                    "com.android.launcher.permission.READ_SETTINGS");
            final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                    + "/favorites?notify=true");
            Cursor c = cr.query(CONTENT_URI,
                    new String[] { "title", "iconResource" }, "title=?",
                    new String[] { context.getString(R.string.app_name) }, null);
            if (c != null && c.getCount() > 0) {
                isInstallShortcut = true;
                c.close();
            }
        }catch(Exception e){
            Lg.e(e.toString());
        }
        return isInstallShortcut;
    }

    private static String getAuthorityFromPermission(Context context,
                                                     String permission) {
        if (permission == null)
            return null;
        List<PackageInfo> packs = context.getPackageManager()
                .getInstalledPackages(PackageManager.GET_PROVIDERS);
        if (packs != null) {
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission))
                            return provider.authority;
                        if (permission.equals(provider.writePermission))
                            return provider.authority;
                    }
                }
            }
        }
        return null;
    }

}
