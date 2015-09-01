package com.joysoft.andutils.http;

/**
 * Created by fengmiao on 15/9/1.
 */
public interface BaseResponse<T> {

    /**
     * 网络故障
     */
    public static final int ERROR_NETWORK = 0;

    /**
     * 网络连接成功，无响应
     */
    public static final int ERROR_DATA_NULL = 1;

    /**
     * 返回数据成功，没有返回正确数据
     */
    public static final int ERROR_DATA_PARSE = 2;

    /**
     * 返回解析之后的数据
     * @param data
     */
    public void onCompleteParse(T data);

    /**
     * 请求失败的回调；
     * @param errorType:
     * <br>                ERROR_NETWORK   网络故障
     * <br>                ERROR_DATA_NULL  没有返回数据
     * <br>                ERROR_DATA_PARSE  数据没有正确返回
     */
    public void onError(int errorType);

}
