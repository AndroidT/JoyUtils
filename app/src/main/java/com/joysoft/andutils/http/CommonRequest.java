package com.joysoft.andutils.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.joysoft.andutils.http.base.BaseRequest;
import com.joysoft.andutils.http.base.JsonMapRequest;
import com.joysoft.andutils.http.base.ResponseHandler;
import com.joysoft.andutils.http.base.ResponseState;
import com.joysoft.andutils.http.base.VolleyUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用的请求接口
 *  <br> 1、使用post提交数据时  参数类型为Map
 *  <br> 2、返回的数据为Json数据
 */
public class CommonRequest implements BaseRequest<HashMap<String,String>,String> {


    public static CommonRequest instance;
    public static RequestQueue requestQueue;

    private CommonRequest() {}



    public static CommonRequest with(Context context) {

        if(context == null)
            throw new IllegalArgumentException("the context cannot be null");

        if (instance == null){
            synchronized (CommonRequest.class) {
                CommonRequest temp = instance;
                if (temp == null) {
                    temp = new CommonRequest();
                    instance = temp;
                }
            }
        }
        if(requestQueue == null){
            requestQueue = VolleyUtils.getInstance(context);
        }
        return instance;
    }

    @Override
    public void postRequest(String url, HashMap<String,String> params, ResponseHandler response) {
        postRequest(url,params,response,true);
    }

    @Override
    public void postRequest(String url, HashMap<String,String> params, ResponseHandler response, boolean useCache) {
        postRequest(url,params,response,useCache, false);
    }

    @Override
    public void postRequest(String url, HashMap<String,String> params, ResponseHandler response, boolean useCache, boolean retryPolicy) {
        withReuqest(Request.Method.POST,url,params,response,useCache,false);
    }

    @Override
    public void getRequest(String url, HashMap<String,String> params, ResponseHandler response) {
        getRequest(url,params,response,true);
    }

    @Override
    public void getRequest(String url, HashMap<String,String> params, ResponseHandler response, boolean useCache) {
        getRequest(url,params,response,useCache,true);
    }

    @Override
    public void getRequest(String url, HashMap<String,String> params, ResponseHandler response, boolean useCache,boolean retryPolicy) {
        withReuqest(Request.Method.GET,url,params,response,useCache,retryPolicy);
    }

    @Override
    public void withReuqest(int method, String url, HashMap<String,String> params,ResponseHandler response) {
        withReuqest(method,url,params,response,true,true);
    }

    @Override
    public void withReuqest(int method,String url, HashMap<String,String> params, ResponseHandler response, boolean useCache) {
        withReuqest(method,url,params,response,useCache,true);
    }

    @Override
    public void withReuqest(int method,String url, final HashMap<String,String> params, final ResponseHandler responseHandler, boolean useCache, boolean retryPolicy) {

        JsonMapRequest request = new JsonMapRequest(
                method, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseHandler.setResultData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseHandler.onError(ResponseState.ERROR_NETWORK);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setTag(url);
        if(retryPolicy)
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 2, 1.0f));
        else
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 1.0f));

        request.setShouldCache(useCache);
        requestQueue.add(request);
    }

    @Override
    public void cancelRequest(Request request) {
        request.cancel();
    }

    @Override
    public void cacelAll(String tag) {
        requestQueue.cancelAll(tag);
    }
}

