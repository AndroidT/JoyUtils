package com.joysoft.andutils.test.card;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joysoft.andutils.R;
import com.joysoft.andutils.card.BaseCard;
import com.joysoft.andutils.lg.Lg;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

/**
 * Created by fengmiao on 2015/9/29.
 */
public class TestImageCard extends BaseCard {

    TextView mTestView;
    ImageView imageView;

    public TestImageCard(View itemView){
        super(itemView);
    }

    @Override
    public int getCardLayout() {
        return R.layout.test_item_imagecard;
    }

    @Override
    public void initView() {
        mTestView = (TextView)itemView.findViewById(R.id.test_tv);
        imageView = (ImageView)itemView.findViewById(R.id.test_img);
    }

    @Override
    public void onBindData(Object data, int position) {
        try {
            Glide.with(itemView.getContext()).load(((JSONObject)data).get("avatar")).into(imageView);
        }catch (Exception e){
            Lg.e(e.toString());
        }

        mTestView.setText("位置是:"+position);
    }

    @Override
    public void onViewDetachedFromWindow() {

    }
}
