package com.joysoft.andutils.card;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joysoft.andutils.lg.Lg;
import com.joysoft.andutils.test.TestRecylcerAdapter;

/**
 * 卡片基类。
 * 配合RecyclerView使用
 *
 * Created by fengmiao on 15/9/12.
 */
public abstract class BaseCard<T> extends RecyclerView.ViewHolder{

    public BaseCard(View itemview){
        super(itemview);
        ((ViewGroup)itemview).addView(getInnerView((ViewGroup)itemview));

        initView();
    }

    private View getInnerView(ViewGroup parentView){
        try{
            int layoutId = getCardLayout();
            return LayoutInflater.from(parentView.getContext()).inflate(layoutId,parentView,false);
        }catch (Exception e){
            Lg.e(e.toString());
        }
        return null;
    }


    public abstract int getCardLayout();

    public abstract void initView();

    public abstract void onBindData(T data,int position);

    public void onViewAttachedToWindow(){}


    public abstract void onViewDetachedFromWindow();



}
