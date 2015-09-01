package com.joysoft.andutils.http;

import android.content.Context;

/**
 * 网络请求接口
 *
 *  通常只有 GET方式 的请求才设置重试次数
 *
 * Created by fengmiao on 15/9/1.
 */
public interface BaseRequest<Params,Tag> {

    /**
     * 初始化请求
     * @param context
     * @return
     */
    public BaseRequest with(Context context);

    /**
     * 设置是否缓存
     * @param shouldCache
     * @return
     */
    public BaseRequest setShouldCache(boolean shouldCache);

    public BaseRequest setUrl(String url);

    /**
     * 设置参数
     * @param params
     * @return
     */
    public BaseRequest setParams(Params params);


    /**
     * 设置重试机制:
     * @param timeOut  超时时间
     * @param retryCount  重试次数
     * @return
     */
    public BaseRequest setRetryPolicy(int timeOut,int retryCount);


    /**
     * 执行Post请求
     */
    public void postRequest();

    /**
     * 执行Get方式请求
     */
    public void getRequest();

    /**
     * Put方式请求
     */
    public void putRequest();

    public void cancelRequest(Tag tag);

    public void cacelAll();
}
