package com.joysoft.andutils.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joysoft.andutils.adapter.CommonAdapter;
import com.joysoft.andutils.ui.EmptyLayout;
import com.joysoft.andutils.ui.FooterLayout;

/**
 * Created by fengmiao on 15/8/31.
 */
public abstract class BaseListFragment extends  BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener{

    protected  SwipeRefreshLayout mSwipeRefreshLayout;

    protected ListView mListView;

    protected EmptyLayout mEmptyLayout;

    protected FooterLayout mFooterLayout;

    protected CommonAdapter<?> mAdapter;


}
