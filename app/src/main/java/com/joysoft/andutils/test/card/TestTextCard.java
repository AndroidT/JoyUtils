package com.joysoft.andutils.test.card;

import android.view.View;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.card.BaseCard;

/**
 * Created by fengmiao on 2015/9/28.
 */
public class TestTextCard extends BaseCard {

    TextView mtextView;

    public TestTextCard(View itemView){
        super(itemView);
    }

    @Override
    public int getCardLayout() {
        return R.layout.test_item;
    }

    @Override
    public void initView() {
        mtextView = (TextView)itemView.findViewById(R.id.test_tv);
    }

    @Override
    public void onBindData(Object data, int position) {
        mtextView.setText("位置:"+position);
    }

    @Override
    public void onViewDetachedFromWindow() {

    }
}
