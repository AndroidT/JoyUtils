package com.joysoft.andutils.http.base;

import com.android.volley.Request;
import com.joysoft.andutils.http.base.ResponseHandler;

/**
 * 网络请求接口
 *  <br>CommonRequest封装的为Volley
 *  通常只有 GET方式 的请求才设置重试次数
 *
 * Created by fengmiao on 15/9/1.
 */
public interface BaseRequest<Params,TAG>{

    /**
     * 执行Post请求
     */
    public void postRequest(String url,Params params,ResponseHandler response);

    /**
     *
     * @param url
     * @param params
     * @param response
     * @param useCache
     */
    public void postRequest(String url,Params params,ResponseHandler response,boolean useCache);

    /**
     *
     * @param url
     * @param params
     * @param response
     * @param useCache
     * @param retryPolicy 是否重试
     */
    public void postRequest(String url,Params params,ResponseHandler response,boolean useCache,boolean retryPolicy);

    /**
     * 执行Get方式请求
     */
    public void getRequest(String url,Params params,ResponseHandler response);

    /**
     *
     * @param url
     * @param params
     * @param response
     * @param useCache
     */
    public void getRequest(String url,Params params,ResponseHandler response,boolean useCache);


    /**
     *
     * @param url
     * @param params
     * @param response
     * @param useCache
     *@param retryPolicy 是否重试
     */
    public void getRequest(String url,Params params,ResponseHandler response,boolean useCache,boolean retryPolicy);

    /**
     *
     * @param method
     * @param url
     * @param params
     */
    public void withReuqest(int method,String url,Params params,ResponseHandler response);

    /**
     *
     * @param url
     * @param params
     * @param response
     * @param useCache
     */
    public void withReuqest(int method,String url,Params params,ResponseHandler response,boolean useCache);

    /**
     *
     * @param url
     * @param params
     * @param response
     * @param useCache
     * @param retryPolicy 是否重试
     */
    public void withReuqest(int method,String url,Params params,ResponseHandler response,boolean useCache,boolean retryPolicy);

    public void cancelRequest(Request request);

    public void cacelAll(TAG tag);
}
