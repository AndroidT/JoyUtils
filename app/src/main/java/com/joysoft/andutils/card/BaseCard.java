package com.joysoft.andutils.card;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fengmiao on 15/9/12.
 */
public abstract  class BaseCard<T> extends RecyclerView.ViewHolder{

    public BaseCard(View itemView) {
        super(itemView);
    }


    /**
     * 创建视图
     * @param viewGroup
     */
    public abstract void onCreateView(ViewGroup viewGroup);



    public void onViewAttacthedToWindow(){};

    public abstract  void onBindData(T data,int position);

    public void onDetachedFromWidow(){};

    public abstract void onViewRecycled();




}
