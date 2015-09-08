package com.joysoft.andutils.http.base;

/**
 *
 * 网络请求响应处理类
 * 使用方法参照子类
 * <br>setResultData方法内 对数据进行判断  分别调用onCompleteParse 和 onError
 * Created by fengmiao on 15/9/1.
 */
public abstract class ResponseHandler<ContentType,ResultData> {
    /**
     * 返回解析之后的数据
     * @param responseData
     */
    public abstract void onCompleteParse(ContentType responseData);

    /**
     * 请求失败的回调；
     * @param errorType:
     * <br>                ERROR_NETWORK   网络故障
     * <br>                ERROR_DATA_NULL  没有返回数据
     * <br>                ERROR_DATA_PARSE  数据没有正确返回
     */
    public abstract void onError(ResponseState errorType);


    public   void setResultData(ResultData resultData){};

    public  boolean isOk(){return false;};

    public  int getResultCode(){ return -1;};

}
