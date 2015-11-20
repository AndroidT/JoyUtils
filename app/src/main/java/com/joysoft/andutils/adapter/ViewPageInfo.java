package com.joysoft.andutils.adapter;

import android.os.Bundle;

public final class ViewPageInfo {

	public final String tag;
    public final Class<?> clss;
    public final Bundle args;
    public final String title;
    public final int iconResId;

    public ViewPageInfo(String _title, String _tag, Class<?> _class, Bundle _args){
        this(_title,0,_tag, _class, _args);
    }

    public ViewPageInfo(String _title,int _iconResId, String _tag, Class<?> _class, Bundle _args) {
    	title = _title;
        tag = _tag;
        clss = _class;
        args = _args;
        iconResId = _iconResId;
    }
}
