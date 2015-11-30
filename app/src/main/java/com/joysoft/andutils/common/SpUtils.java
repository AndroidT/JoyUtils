package com.joysoft.andutils.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

/**
 * SharedPreferences操作类 增删改查
 * 
 * @author fengmiao
 * 
 */
public class SpUtils {

	SharedPreferences sp;
	Context context;
	String TAG = SpUtils.class.getSimpleName();
	Editor ed;

	/**
	 * 
	 * @param context
	 * @param name
	 *            SharedPreferences名字
	 * @param mode
	 *            0--私有模式
	 */
	public SpUtils(Context context, String name, int mode) {
		this.context = context;
		sp = this.context.getSharedPreferences(name, mode);
		ed = sp.edit();
	}

	public void addMess(String key, long Value) {
		ed.putLong(key, Value);
		ed.commit();
	}

	public void addMess(String key, int Value) {
		ed.putInt(key, Value);
		ed.commit();
	}

	public void addMess(String key, float Value) {
		ed.putFloat(key, Value);
		ed.commit();
	}
	
	public void addMess(String key,boolean Value){
		ed.putBoolean(key, Value);
		ed.commit();
	}
	
	public boolean getMessBoolean(String key,boolean defValue){
		return sp.getBoolean(key, defValue);
	}

	public void deteletMess() {
		ed.clear();
		ed.commit();
	}

	public void addMess(String key, Set<String> set) {
		ed.putStringSet(key, set);
		ed.commit();
	}
	
	public String getMessString(String key) {
		return sp.getString(key, "");
	}

	public String getMessString(String key,String defualt) {
		return sp.getString(key, defualt);
	}
	
	public int getMessInt(String key) {
		return sp.getInt(key, -1);
	}

	public float getMessLiht(String key) {
		return sp.getFloat(key, -1);
	}

	public long getMessLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}
	
	public Set<String> getMessSet(String key) {
		return sp.getStringSet(key, new HashSet<String>());
	}

	public void addMess(String key, String Value) {
		ed.putString(key, Value);
		ed.commit();
	}
}
