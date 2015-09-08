package com.joysoft.andutils.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 自定义listview解决内部view横向滑动时手势冲突问题
 * Created by fengmiao on 15/9/8.
 */
public class CustomListView extends ListView {

    private float xDistance, yDistance, xLast, yLast;

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;
                if(xDistance > yDistance){
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
}
