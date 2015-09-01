package com.joysoft.andutils.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.CommonAdapter;
import com.joysoft.andutils.ui.EmptyLayout;
import com.joysoft.andutils.ui.FooterLayout;

import java.util.Map;

/**
 * Created by fengmiao on 15/8/31.
 */
public abstract class BaseListFragment extends  BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener{


    public abstract  String getUrl();
    public abstract Map getParams();

    // 没有状态
    public static final int LISTVIEW_ACTION_NONE = -1;
    // 更新状态，不显示toast
    public static final int LISTVIEW_ACTION_UPDATE = 0;
    // 初始化时，加载缓存状态
    public static final int LISTVIEW_ACTION_INIT = 1;
    // 刷新状态，显示toast
    public static final int LISTVIEW_ACTION_REFRESH = 2;
    // 下拉到底部，获取下一页的状态
    public static final int LISTVIEW_ACTION_SCROLL = 3;

    public enum MessageData {
        MESSAGE_STATE_ERROR, MESSAGE_STATE_EMPTY, MESSAGE_STATE_MORE, MESSAGE_STATE_FULL
    }

    // 当前数据状态，如果是已经全部加载，则不再执行滚动到底部就加载的情况
    protected MessageData mMessageState = MessageData.MESSAGE_STATE_MORE;

    static final int STATE_NONE = -1;
    static final int STATE_LOADING = 0;
    static final int STATE_LOADED = 1;

    // 当前加载状态
    protected int mState = STATE_NONE;

    // UI状态
    protected int mListViewAction = LISTVIEW_ACTION_NONE;



    protected  SwipeRefreshLayout mSwipeRefreshLayout;

    protected ListView mListView;

    protected EmptyLayout mEmptyLayout;

    protected FooterLayout mFooterLayout;

    protected CommonAdapter<?> mAdapter;


    private  int mPageSize = 10;


    @Override
    public void initViews(View root) {

    }


    /**
     * 设置
     */
    protected void setUpSwipeLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);

        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    /**
     * 设置顶部正在加载的状态
     */
    private void setSwipeRefreshLoadingState(){
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(true);
            //防止多次刷新
            mSwipeRefreshLayout.setEnabled(false);
        }

    }

    /** 设置顶部加载完毕的状态 **/
    private void setSwipeRefreshLoadedState(){
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }



    @Override
    public void onRefresh() {
        if(mState == STATE_LOADING)
            return;

        mListView.setSelection(0);

        setSwipeRefreshLoadingState();
    }


    /**
     * 加载下一页数据
     */
    protected void onLoadNextPage(){
        int pageIndex = mAdapter.getCount() / mPageSize + 1;
        getData(pageIndex, LISTVIEW_ACTION_SCROLL);
    }

    private  void getData(final int index,final int action){

        mListViewAction = action;

        boolean cacheEnable = false;

        /*
		 * 判断是否使用缓存:
		 * 	1、不是使用缓存则列表每页都没有缓存
		 * 	2、使用缓存的话 只缓存第一页(pageSize)条数据
		 *
		 */
        cacheEnable = useCache() ? index <= 1 : false;



    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

        if(mAdapter  == null || mAdapter.getCount() == 0)
            return;

        // footer 没有设置或 是隐藏状态 则直接返回
        if(mFooterLayout == null || !mFooterLayout.isVisible())
            return;

        /*
          以下条件均返回不处理
          1、数据已经加载完毕
          2、数据为空
          3、正在加载
         */
        if(mMessageState == MessageData.MESSAGE_STATE_FULL
                || mMessageState == MessageData.MESSAGE_STATE_EMPTY
                || mState == STATE_LOADING){
            return;
        }


        //判断是否滚动到底部
        boolean scrollEnd = false;

        try{
            if(absListView.getPositionForView(mFooterLayout.getFooterView())
                    == absListView.getLastVisiblePosition())
                scrollEnd = true;
        }catch (Exception e){
                scrollEnd = false;
        }

        if(scrollEnd)
            onLoadNextPage();
    }

    public void setPageSize(int pageSize){
        this.mPageSize = pageSize;
    }

    /**
     * 是否使用缓存
     * @return
     */
    protected boolean useCache(){
        return true;
    }
}
