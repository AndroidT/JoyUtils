package com.joysoft.andutils.ui;

import android.view.View;

/**
 * Created by fengmiao on 15/8/31.
 */
public interface FooterLayout {

    /**
     * 显示 “加载更多” 时的状态
     */
    public static final int STATE_MORE = 1;
    /**
     *  显示 “没有数据时” 时的状态
     */
    public static final int STATE_ERROR_DATA_NULL = 2;

    /**
     * 数据解析失败
     * 显示 “数据有误或加载数据出错” 时的状态
     */
    public static final int STATE_ERROR_DATA_PARSE = 3;

    /**
     * 加载中
     */
    public static final int STATE_LOADING = 4;

    /**
     * 已经加载全部
     */
    public static final int STATE_COMPLETE = 5;



    public   int mLayoutState = -1;

    /**
     * 设置当前View的状态值
     * 这个方法内只更新View的数据，不操作是否visible
     * @param State
     */
    public void setLayoutState(int State);


    /**
     * 显示footer
     */
    public void showLayout();

    /**
     * 隐藏footer
     */
    public void hideLayout();

    /**
     * 判断当前footer是否可见
     * @return
     */
    public boolean isVisible();

    /**
     * 返回当前底部视图
     * @return
     */
    public  View getFooterView();
}
