package com.joysoft.andutils.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by fengmiao on 2015/10/27.
 */
public class BaseRecyclerHolder extends RecyclerView.ViewHolder{

    private SparseArray<View> views = new SparseArray<>();

    public BaseRecyclerHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T getView(int resId){
        View v = views.get(resId);
        if(v == null){
            v = itemView.findViewById(resId);
            views.put(resId,v);
        }
        return (T)v;
    }

    public View getConvertView(){
        return itemView;
    }

}
