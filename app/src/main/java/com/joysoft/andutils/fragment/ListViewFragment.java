package com.joysoft.andutils.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.ui.IEmptyLayout;
import com.joysoft.andutils.ui.view.EmptyLayout;
import com.joysoft.andutils.ui.view.FooterView;

import java.util.List;

/**
 * 使用ListView刷新基类
 *
 * Created by fengmiao on 15/9/7.
 */
public abstract class ListViewFragment extends  BaseRefreshFragment implements
        AbsListView.OnScrollListener,AdapterView.OnItemClickListener{

    public ListView mListView;

    @Override
    protected void initConfigView(View root) {
        super.initConfigView(root);

        //EmptyView
        mEmptyLayout = (EmptyLayout)root.findViewById(R.id.fragement_emptyLayout);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mState == STATE_LOADING)
                    return;

                mState = STATE_LOADING;
                mEmptyLayout.setLayoutState(IEmptyLayout.STATE_LOADING);
                loadList(1, LISTVIEW_ACTION_REFRESH);
            }
        });


        //SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.fragment_listrefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);


        //ListView
        mListView = (ListView)root.findViewById(R.id.fragment_listview);
        mListView.setOnScrollListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter((BaseAdapter) mAdapter);

    }

    @Override
    protected void initHeaderFooterView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_footer_layout,null);

        mFooterLayout = (FooterView)view.findViewById(R.id.commom_footview);

        mFooterLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mState != STATE_LOADING){
                    onLoadNextPage();
                }
            }
        });
        mListView.addFooterView(mFooterLayout.getView());
    }


    @Override
    public List getContentList(Object result, int index) {
        return null;
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

        if(mFooterLayout == null)
            return;

        //判断是否滚动到底部
        boolean scrollEnd = false;
        try{
            if(absListView.getPositionForView(mFooterLayout.getView())
                    == absListView.getLastVisiblePosition())
                scrollEnd = true;
        }catch (Exception e){
            scrollEnd = false;
        }

        if(scrollEnd)
           onScrollLoad();
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void recycle() {

    }

}
