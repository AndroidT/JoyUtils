package com.joysoft.andutils.http.base;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fengmiao on 15/9/7.
 */
public class VolleyUtils {

    private static VolleyUtils instance = null;
    private static RequestQueue requestQueue;

    private VolleyUtils(){}

    public static RequestQueue getInstance(Context context){
        if (requestQueue == null) {
            synchronized (VolleyUtils.class) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(context);
                    requestQueue.start();
                }
            }

        }
        return requestQueue;
    }

    public void cancelAll(String tag){
        requestQueue.cancelAll(tag);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);
    }
}
