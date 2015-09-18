package com.joysoft.andutils.adapter.recycler;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joysoft.andutils.card.BaseCard;

import java.util.ArrayList;

/**
 * Created by fengmiao on 2015/9/18.
 */
public abstract class CardAdapter extends BaseRecyclerAdapter{

    ArrayList<Class<BaseCard>> mCardList = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        if(isFooter(position))
            return TYPE_FOOTER;
        else
            return getCardType(position);
    }

    public abstract int getCardType(int position);

    public abstract void setupCardList(ArrayList params);


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        return super.onCreateViewHolder(viewGroup, type);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup viewGroup, int type) {
        return super.onCreateViewHolder(viewGroup,type);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((BaseCard)holder).onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((BaseCard)holder).onViewDetachedFromWindow();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
