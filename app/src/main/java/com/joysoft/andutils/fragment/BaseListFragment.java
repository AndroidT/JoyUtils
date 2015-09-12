package com.joysoft.andutils.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.BaseAbstractAdapter;
import com.joysoft.andutils.ui.EmptyLayout;

import java.util.List;

/**
 * 列表刷新基类
 *
 * Created by fengmiao on 15/9/7.
 */
public  abstract class BaseListFragment extends  BaseRefreshFragment{

    @Override
    protected void initConfigView(View root) {
        super.initConfigView(root);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_LOADING;
                mEmptyLayout.setLayoutState(EmptyLayout.STATE_LOADING);
                loadList(1, LISTVIEW_ACTION_REFRESH);
            }
        });

    }

    @Override
    protected void initHeaderFooterView(LayoutInflater inflater) {
        super.initHeaderFooterView(inflater);
    }


    @Override
    public BaseAbstractAdapter getAdapter() {
        return null;
    }

    @Override
    public List getContentList(Object result, int index) {
        return null;
    }


    @Override
    public void recycle() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
