package com.joysoft.andutils.test;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.AbsBaseAdapter;

import org.w3c.dom.Text;

/**
 * Created by fengmiao on 2015/10/27.
 */
public class TestAbsAdapter extends AbsBaseAdapter {

    public TestAbsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutID() {
        return R.layout.test_my_text_view;
    }

    @Override
    public void onBindViewHolder(int position, ViewHolder holder, ViewGroup parent) {
        TextView textView = (TextView) holder.getView(R.id.test_tv);
        textView.setText(position+"");
    }
}
