package com.joysoft.andutils;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.VolleyLog;
import com.joysoft.andutils.http.base.VolleyUtils;
import com.joysoft.andutils.lg.Lg;
import com.joysoft.andutils.test.TestListViewFragment;
import com.joysoft.andutils.test.TestRecycleViewFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lg.LogEnable = true;
        VolleyLog.DEBUG = true;

        if(savedInstanceState == null)
            testRecyclerView();

    }

    void testRecyclerView(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_rl,new TestRecycleViewFragment())
                .commit();
    }




}
