package com.joysoft.andutils.test.card;

import com.joysoft.andutils.adapter.recycler.CardAdapter;

import java.util.ArrayList;

/**
 * Created by fengmiao on 2015/9/28.
 */
public class TestCardAdapter extends CardAdapter {
    /**
     * 子类依据position 来判断 当前应该加载那种类型的卡片
     *
     * @param position
     * @return type 需要加载的卡片在mCardList中的位置
     */
    @Override
    public int getCardType(int position) {
        return  (position % 2 == 0 ? 1 : 0);
    }

    /**
     * 设置一共需要加载多少种卡片
     *
     * @param mCardList
     */
    @Override
    public void setupCardList(ArrayList mCardList) {
        mCardList.add(TestTextCard.class);
        mCardList.add(TestImageCard.class);
    }
}
