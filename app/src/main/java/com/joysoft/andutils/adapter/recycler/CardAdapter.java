package com.joysoft.andutils.adapter.recycler;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joysoft.andutils.card.BaseCard;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * 通用的 卡片Adapter
 *
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


    /**
     * 子类依据position 来判断 当前应该加载那种类型的卡片
     * @param position
     * @return type 需要加载的卡片在mCardList中的位置
     */
    public abstract int getCardType(int position);

    /**
     * 设置一共需要加载多少种卡片
     * @param params
     */
    public abstract void setupCardList(ArrayList params);


    /**
     * 在这里初始化卡片
     *
     * @param viewGroup
     * @param type 当前卡片在 mCardList中的下标
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup viewGroup, int type) {
//        return super.onCreateViewHolder(viewGroup,type);

        Class cardCls = mCardList.get(type);
//        cardCls.getConstructor()
        return null;
    }

    /**
     * 绑定数据
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindData(RecyclerView.ViewHolder viewHolder, int position) {

    }


//    /**
//     * 当卡片 attach 到 recyclerview上时被调用:
//     *  <br>可以在 此处初始化状态
//     * @param holder
//     */
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
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
