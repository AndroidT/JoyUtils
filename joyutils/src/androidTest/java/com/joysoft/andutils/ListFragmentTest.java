package com.joysoft.andutils;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

/**
 * Created by fengmiao on 15/9/12.
 */
public class ListFragmentTest extends ActivityUnitTestCase<MainActivity>{

    private Intent mIntent;

    public ListFragmentTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mIntent = new Intent(getInstrumentation().getTargetContext(),MainActivity.class);
    }

    @MediumTest
    public void testStart(){
        startActivity(mIntent,null,null);
    }
}
