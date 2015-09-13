package com.joysoft.andutils.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.IBaseAdapter;
import com.joysoft.andutils.ui.IEmptyLayout;
import com.joysoft.andutils.ui.view.EmptyLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fengmiao on 15/9/12.
 */
public abstract class RecylerViewFragment extends BaseRefreshFragment{

    public RecyclerView mRecyclerView;

    @Override
    protected void initConfigView(View root) {
        super.initConfigView(root);

        //SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.fragment_listrefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);


        //RecyclerView
        mRecyclerView = (RecyclerView)root.findViewById(R.id.fragment_recylerview);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter((RecyclerView.Adapter)mAdapter);

        //EmptyView
        mEmptyLayout = (EmptyLayout)root.findViewById(R.id.fragement_emptyLayout);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mState = STATE_LOADING;
                mEmptyLayout.setLayoutState(IEmptyLayout.STATE_LOADING);
                loadList(1, LISTVIEW_ACTION_REFRESH);
            }
        });
    }


    public abstract RecyclerView.LayoutManager getLayoutManager();

}
