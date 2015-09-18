package com.joysoft.andutils.ui.view;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.ui.IFooterLayout;

/**
 * Created by fengmiao on 15/9/12.
 */
public class FooterView extends LinearLayout implements IFooterLayout{

    ProgressBar progressBar;
    TextView textView;

    public FooterView(Context context){
        this(context, null);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    void init(){
        progressBar = (ProgressBar)findViewById(R.id.listview_foot_progress);
        textView = (TextView)findViewById(R.id.common_foot_more);
    }

    /**
     * 设置当前View的状态值
     * 这个方法内只更新View的数据，不操作是否visible
     *
     * @param state
     */
    @Override
    public void setLayoutState(final int state) {

       this.post(new Runnable() {
           @Override
           public void run() {
               if(getContext() == null || progressBar == null)
                   return;

               int textRes = R.string.load_more;

               if(STATE_LOADING == state){
                   progressBar.setVisibility(View.VISIBLE);
                   textRes = R.string.load_ing;
                   textView.setText(textRes);
                   return;
               }

               if(STATE_ERROR_DATA_NULL == state){
                   textRes = R.string.load_empty;
               }

               if(STATE_COMPLETE == state){
                   textRes = R.string.load_full;
               }

               if(STATE_ERROR_DATA_PARSE == state)
                   textRes = R.string.load_error;

               if(STATE_MORE == state){
                   textRes = R.string.load_more;
               }

               progressBar.setVisibility(View.GONE);
               textView.setText(textRes);
           }
       });

    }

    /**
     * 显示footer
     */
    @Override
    public void showLayout() {
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏footer
     */
    @Override
    public void hideLayout() {
        setVisibility(GONE);
    }

    /**
     * 判断当前footer是否可见
     *
     * @return
     */
    @Override
    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    /**
     * 返回当前底部视图
     *
     * @return
     */
    @Override
    public View getView() {
        return this;
    }

    /**
     * 设置监听
     *
     * @param mListener
     */
    @Override
    public void setOnLayoutClickListener(OnClickListener mListener) {
        this.setOnClickListener(mListener);
    }
}
