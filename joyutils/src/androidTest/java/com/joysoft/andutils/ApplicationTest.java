package com.joysoft.andutils;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.joysoft.andutils.lg.Lg;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
        Lg.e("-------  这是单元测试 ======");
    }
}