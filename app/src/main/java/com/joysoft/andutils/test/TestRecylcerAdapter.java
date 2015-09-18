package com.joysoft.andutils.test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.recycler.BaseRecyclerAdapter;
import com.joysoft.andutils.lg.Lg;

import org.json.JSONObject;

/**
 * Created by fengmiao on 15/9/13.
 */
public class TestRecylcerAdapter extends BaseRecyclerAdapter<JSONObject>{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public ViewHolder(View v){
            super(v);
            mTextView = (TextView)v.findViewById(R.id.test_tv);
        }
    }

    /**
     * 获取一个新的ViewHolder
     *
     * @param viewGroup
     * @param type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup viewGroup, int type) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_my_text_view,viewGroup,false);
        return new ViewHolder(v);
    }

    /**
     * 绑定数据
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindData(RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolder)viewHolder).mTextView.setText("位置是:" + position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
