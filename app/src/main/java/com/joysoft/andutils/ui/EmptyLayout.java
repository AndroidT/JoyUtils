package com.joysoft.andutils.ui;

import android.view.View;

/**
 * 占位Layout的接口，
 *
 * Created by fengmiao on 15/8/31.
 */
public interface EmptyLayout {

    /**
     * 网络错误
     */
    public static final int STATE_ERROR_NET = 1;
    /**
     * 没有数据
     */
    public static final int STATE_ERROR_DATA_NULL = 2;

    /**
     * 数据有误
     */
    public static final int STATE_ERROR_DATA_PARSE = 3;

    /**
     * 加载中
     */
    public static final int STATE_LOADING = 4;



    /**
     * 显示占位图片
     *
     */
    public void showEmptyLayout();

    /**
     * 隐藏占位图片
     */
    public void hideEmptyLayout();


    /**
     * 设置没有数据时的状态
     */
    public void setLayoutState(int state);

    public boolean isShowing();

    /**
     * 设置监听
     * @param mListener
     */
    public void setOnLayoutClickListener(View.OnClickListener mListener);


}
