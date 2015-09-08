package com.joysoft.andutils.http.base;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.*;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.joysoft.andutils.lg.Lg;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 重写缓存key：
 * 如果请求带有参数，以url+params为CacheKey
 * Created by fengmiao on 15/9/7.
 */
public class JsonMapRequest extends Request<JSONObject> {

    private  Listener<JSONObject> mListener = null;

    public JsonMapRequest(int method, String url, Listener<JSONObject> listener,
                      ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public String getCacheKey() {
        try {
            if(getParams() == null)
                return getUrl();
            return getUrl()+getParams().toString();
        } catch (AuthFailureError e) {
            e.printStackTrace();
            return getUrl();
        }
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            long start = System.currentTimeMillis();
            Lg.d("-----  开始数据包装 parseNetworkResponse -------- ");
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            //设置强制磁盘缓存有效 1年
            Cache.Entry entry  =  HttpHeaderParser.parseCacheHeaders(response);
            entry.ttl = System.currentTimeMillis() + 365 * 24 * 60 *60 * 1000;

            Response mResponse = Response.success(new JSONObject(jsonString),entry);

            Lg.d("---- parseNetworkResponse 结束  耗时:"+(System.currentTimeMillis() - start)+"ms");
            return mResponse;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}