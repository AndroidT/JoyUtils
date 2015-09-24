package com.joysoft.andutils.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.joysoft.andutils.lg.Lg;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengmiao on 2015/9/24.
 */
public class StringUtils {

    /**
     * 复制到剪贴板
     * @param context
     * @param content
     */
    public static void copyToClipboard(Context context,String content){
        // 得到剪贴板管理器
        try{
            ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
            // setText()方法被丢弃
            //cmb.setText(text).setText(content.trim());
            cmb.setPrimaryClip(ClipData.newPlainText(null, content.trim()));
        }catch(Exception e){
            Lg.e(e.toString());
            TipUtils.ShowText(context, "复制失败...");
        }
    }

    /**
     * 验证是否是邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {// 验证邮箱的正则表达式
        String format = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*"
                + "@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!TextUtils.isEmpty(email) && email.matches(format)) {
            return true;// 邮箱名合法，返回true
        } else {
            return false;// 邮箱名不合法，返回false
        }
    }

    /**
     * 判断 str == null || 出去空格后size > 0
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (TextUtils.isEmpty(deleteBlank(str)))
            return true;
        return false;

    }

    /**
     * 删除空格、空白、等
     *
     * @param str
     * @return
     */
    public static String deleteBlank(String str) {
        if (str == null)
            str = "";
        return str.replaceAll("\\s*", "");
    }

    /**
     * null string -->> "" string
     *
     * @param str
     * @return
     */
    public static String nullToStr(String str) {
        return str == null ? "" : str;
    }

    /**
     * String 以 以utf-8格式编码
     *
     * @param str
     * @return
     */
    public static String utf8Encode(String str) {
        if (!TextUtils.isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 把string进行utf-8格式转码,转码失败则返回默认string
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!TextUtils.isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * 第一个字母大写 capitalize first letter
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * 删除html标签 有待验证
     */
    public static String delHtml(String str) {
        String info = str.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
                "<[^>]*>", "");
        info = info.replaceAll("[(/>)<]", "");
        return info;
    }


    /**
     * 验证电话号码
     * @param mobiles
     * @return
     */
    public static boolean checkPhoneNum(String mobiles){
//		Pattern p = Pattern.compile("^((13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(mobiles);
//		return m.matches();

        if(mobiles == null)
            return false;

        if(StringUtils.isBlank(mobiles) || mobiles.length() != 11 || !mobiles.startsWith("1"))
            return false;

        return true;
    }


    /**
     * 检测字符串中是否包含汉字
     * @param sequence
     * @return
     */
    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    /**
     * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
     * @param sequence
     * @return
     */
    public static boolean checkNickname(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    }


}
