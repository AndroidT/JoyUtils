package com.joysoft.andutils.test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.recycler.BaseRecyclerAdapter;

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        if(!isFooter(position))
          ((ViewHolder)viewHolder).mTextView.setText("位置是:"+position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
//        super.onCreateViewHolder(viewGroup,type);


        if(type == TYPE_FOOTER)
            return  super.onCreateViewHolder(viewGroup,type);

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_my_text_view,null);
        return new ViewHolder(v);
    }
}
