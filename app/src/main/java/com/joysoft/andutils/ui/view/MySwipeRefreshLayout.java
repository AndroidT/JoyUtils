package com.joysoft.andutils.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 刷新View:
 * <br>1、重写{@link #onInterceptTouchEvent(MotionEvent)}防止Refresh与左右滑动冲突
 * Created by fengmiao on 15/9/8.
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    private float mPrevX;
    /**
     * 当用户左右滑动距离小于默认滚动距离时 Refresh不处理
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(event);

    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!isEnabled()) {
            return false;
        }
        return super.onTouchEvent(arg0);
    }


}
