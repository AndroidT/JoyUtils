package com.joysoft.andutils.http.base;

/**
 * Created by fengmiao on 15/9/7.
 */
public enum ResponseState {
    /**
     * 网络故障
     */
    ERROR_NETWORK,
    /**
     * 网络连接成功，无响应
     */
    ERROR_DATA_NULL,
    /**
     * 返回数据成功，没有返回正确数据
     */
    ERROR_DATA_PARSE
}
