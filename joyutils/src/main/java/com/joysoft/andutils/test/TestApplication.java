package com.joysoft.andutils.test;

import android.app.Application;

import com.joysoft.andutils.lg.Lg;

/**
 * Created by fengmiao on 2015/9/25.
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Lg.LogEnable = true;

    }
}
