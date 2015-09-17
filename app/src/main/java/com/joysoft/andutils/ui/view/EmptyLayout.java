package com.joysoft.andutils.ui.view;

import android.content.Context;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.ui.IEmptyLayout;

/**
 * Created by fengmiao on 15/9/12.
 */
public class EmptyLayout extends FrameLayout implements IEmptyLayout{

    ImageView imageView;
    TextView textView;
    ContentLoadingProgressBar progressBar;
    LinearLayout linearLayout;

    public EmptyLayout(Context context){
        this(context, null);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init(){
        linearLayout = (LinearLayout)findViewById(R.id.ll_error);
        imageView = (ImageView)findViewById(R.id.img_error);
        textView = (TextView)findViewById(R.id.tv_error);
        progressBar = (ContentLoadingProgressBar)findViewById(R.id.common_progressbar);
        imageView.setImageResource(R.drawable.default_empty_img);
    }

    /**
     * 显示占位图片
     */
    @Override
    public void showEmptyLayout() {
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏占位图片
     */
    @Override
    public void hideEmptyLayout() {
        setVisibility(GONE);
    }

    /**
     * 设置没有数据时的状态
     *
     * @param state
     */
    @Override
    public void setLayoutState(int state) {

        //加载中...
        if(STATE_LOADING == state){
            linearLayout.setVisibility(GONE);
            progressBar.show();
            return;
        }

        if(STATE_ERROR_NET == state){
            textView.setText(R.string.load_error_Net);
        }

        if(STATE_ERROR_DATA_NULL == state){
            textView.setText(R.string.load_empty);
        }

        if(STATE_ERROR_DATA_PARSE == state){
            textView.setText(R.string.load_error);
        }

        progressBar.hide();
        linearLayout.setVisibility(VISIBLE);


    }


    @Override
    public boolean isShowing() {
        return getVisibility() == VISIBLE;
    }


    /**
     * 设置监听
     *
     * @param mListener
     */
    @Override
    public void setOnLayoutClickListener(OnClickListener mListener) {
            if(imageView != null)
                imageView.setOnClickListener(mListener);
    }
}
