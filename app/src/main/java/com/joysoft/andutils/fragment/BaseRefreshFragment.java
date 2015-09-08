package com.joysoft.andutils.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joysoft.andutils.adapter.CommonAdapter;
import com.joysoft.andutils.http.ApiResponseHandler;
import com.joysoft.andutils.http.CommonRequest;
import com.joysoft.andutils.http.base.ResponseState;
import com.joysoft.andutils.lg.Lg;
import com.joysoft.andutils.ui.EmptyLayout;
import com.joysoft.andutils.ui.FooterLayout;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fengmiao on 15/8/31.
 */
public abstract class BaseRefreshFragment extends  BaseFragment implements
        SwipeRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener{

    // 没有状态
    public static final int LISTVIEW_ACTION_NONE = -1;
    // 初始化时，加载缓存状态
    public static final int LISTVIEW_ACTION_INIT = 1;
    // 刷新状态，显示toast
    public static final int LISTVIEW_ACTION_REFRESH = 2;
    // 下拉到底部，获取下一页的状态
    public static final int LISTVIEW_ACTION_SCROLL = 3;
    // UI状态
    protected int mListViewAction = LISTVIEW_ACTION_NONE;

    static final int STATE_NONE = -1;
    static final int STATE_LOADING = 0;
    static final int STATE_LOADED = 1;
    // 当前加载状态
    protected int mState = STATE_NONE;


    public enum MessageData {
        MESSAGE_STATE_ERROR, MESSAGE_STATE_EMPTY, MESSAGE_STATE_MORE, MESSAGE_STATE_FULL
    }
    // 当前数据状态，如果是已经全部加载，则不再执行滚动到底部就加载的情况
    protected MessageData mMessageState = MessageData.MESSAGE_STATE_MORE;


    protected  SwipeRefreshLayout mSwipeRefreshLayout;

    protected ListView mListView;

    protected EmptyLayout mEmptyLayout;

    protected FooterLayout mFooterLayout;

    protected CommonAdapter<?> mAdapter;

    /**
     * 当前列表一页需要几条数据
     */
    private  int PageSize = 10;

    //判断子类是否调用 super.initView
    private boolean callInitView = false;

    public abstract  String getUrl();
    public abstract HashMap<String,String> getParams(int index);
    public abstract CommonAdapter getAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initHeaderFooterView(inflater);
        mAdapter = getAdapter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 初始化footer和header
     * @param inflater
     */
    protected void initHeaderFooterView(LayoutInflater inflater) {

    }


    @Override
    public void initViews(View root) {
        if(mAdapter == null)
            throw new IllegalArgumentException("the mAdapter cannot be null");

        callInitView = true;

        initConfigView(root);

        if(mState == STATE_LOADED && mAdapter.isEmpty()){
            setFooterNoMoreState();
        }else if(mState == STATE_LOADED && mAdapter.getCount() < PageSize){
            setFooterFullState();
        }

        if(mListViewAction == LISTVIEW_ACTION_REFRESH)
            setSwipeRefreshLoadingState();

        loadList(1, LISTVIEW_ACTION_INIT);
    }

    /**
     * 初始化和配置 一系列的view
     *      SwipeRefreshView
     *      mEmptyLayout
     *      mListView
     *
     */
    protected void initConfigView(View root) {

    }

    @Override
    public void onRefresh() {
        if(mState == STATE_LOADING)
            return;

        mListView.setSelection(0);

        setSwipeRefreshLoadingState();
    }

    /**
     * 加载指定某一页的数据
     * @param page
     * @param action
     */
    protected  void loadList(int page,int action){
        mListViewAction = action;
        getData(page, mListViewAction);
    }


    /**
     * 加载下一页数据
     */
    protected void onLoadNextPage(){
        int pageIndex = mAdapter.getCount() / PageSize + 1;
        getData(pageIndex, LISTVIEW_ACTION_SCROLL);
    }

    private  void getData(final int index,final int action){

        if(!callInitView)
            throw new IllegalStateException("-- you must call super method in initViews(root) ----");

        mListViewAction = action;

        boolean cacheEnable;

        /*
		 * 判断是否使用缓存:
		 * 	 就算有缓存 也只缓存第一页(pageSize)条数据
		 *
		 */
        cacheEnable = useCache() && index <= 1;

        //加载数据前 更新View的状态
        beforeLoading(mListViewAction);

        String url = getUrl();

        HashMap<String,String> params = getParams(index);

        if(Lg.LogEnable){

            if(params == null){
                Lg.e("请求地址:"+url);
                return;
            }

            StringBuffer paramsStr = new StringBuffer();
            paramsStr.append("?");
            for(String key : params.keySet()){
                paramsStr.append(key)
                        .append("=")
                        .append(params.get(key))
                        .append("&");
            }
            Lg.e("地址为:\n"+(url+paramsStr));
        }

        CommonRequest.with(getActivity()).postRequest(url, params, new ApiResponseHandler() {
            @Override
            public void onCompleteParse(JSONObject responseData) {
                if(getActivity() == null)
                    return;
                refreshData(responseData,index,action);
            }

            @Override
            public void onError(ResponseState errorType) {
                if(getActivity() == null)
                    return;
                updateState(MessageData.MESSAGE_STATE_ERROR,errorType,action);
            }
        }, cacheEnable);

    }

    /**
     * 刷新数据
     * @param result
     * @param index
     * @param action
     */
    void refreshData(Object result,int index,int action){

        //获取列表数据
        List content = getContentList(result,index);


        //判断当前数据加载状态
        if(content == null || content.size() == 0){
            if (mAdapter.getCount() == 0)
                mMessageState = MessageData.MESSAGE_STATE_EMPTY;
            else
                mMessageState = MessageData.MESSAGE_STATE_FULL;
        }else{
            if(content.size() < PageSize)
                mMessageState = MessageData.MESSAGE_STATE_FULL;
            else
                mMessageState = MessageData.MESSAGE_STATE_MORE;
        }

        //更新视图数据的状态
        updateState(mMessageState, null, action);

        //刷新ListView
        if(index == 1){
            mAdapter.clearDataList();
            mAdapter.addDataList(content);
        }else{
            mAdapter.addDataList(content);
        }

    }

    /**
     * 解析列表数据
     * @param result
     * @param index
     * @return result
     */
    public abstract List getContentList(Object result,int index);

    /**
     * 更新当前列表的状态
     * @param messageData
     * @param errorType
     * @param action
     */
    void updateState(MessageData messageData,ResponseState errorType,int action){
        mMessageState = messageData;

        // 加载结束
        mState = STATE_LOADED;
        if (action == LISTVIEW_ACTION_INIT) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }

        // 如果动作是下拉刷新，则将刷新中的状态去掉
        if (action == LISTVIEW_ACTION_REFRESH) {
            setSwipeRefreshLoadedState();
        }

        // 更新全局的状态
        if (mListViewAction == action) {
            mListViewAction = LISTVIEW_ACTION_NONE;
        }

        //加载数据失败时
        if(mMessageState == MessageData.MESSAGE_STATE_ERROR){

            if(mAdapter.getCount() == 0 && mEmptyLayout != null){

                if(errorType == ResponseState.ERROR_DATA_NULL)
                    mEmptyLayout.setLayoutState(EmptyLayout.STATE_ERROR_DATA_NULL);
                else if(errorType == ResponseState.ERROR_DATA_PARSE)
                    mEmptyLayout.setLayoutState(EmptyLayout.STATE_ERROR_DATA_PARSE);
                if(errorType == ResponseState.ERROR_NETWORK)
                    mEmptyLayout.setLayoutState(EmptyLayout.STATE_ERROR_NET);

                mEmptyLayout.showEmptyLayout();
                return;
            }

        }

        //加载的数据为空
        if(mMessageState == MessageData.MESSAGE_STATE_EMPTY){
            if(mAdapter.getCount() == 0 && mEmptyLayout != null){
                mEmptyLayout.setLayoutState(EmptyLayout.STATE_ERROR_DATA_NULL);
                mEmptyLayout.showEmptyLayout();
                return;
            }else{
                setFooterNoMoreState();
            }
        }

        if(mEmptyLayout != null)
            mEmptyLayout.hideEmptyLayout();

        //数据加载完毕
        if(mMessageState == MessageData.MESSAGE_STATE_FULL){
            setFooterFullState();
        }

        //还有更多数据
        if(mMessageState == MessageData.MESSAGE_STATE_MORE)
            setFooterHasMoreState();
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

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    public void setPageSize(int pageSize){
        this.PageSize = pageSize;
    }

    /**
     * 是否使用缓存
     * @return
     */
    protected boolean useCache(){
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        callInitView = false;
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

    // 准备加载数据
    public void beforeLoading(int action) {
        // 开始加载
        mState = STATE_LOADING;
        if (action == LISTVIEW_ACTION_REFRESH) {
            setSwipeRefreshLoadingState();
            if(mEmptyLayout != null && mEmptyLayout.isShowing()){
                mEmptyLayout.setLayoutState(EmptyLayout.STATE_LOADING);
            }
        } else if (action == LISTVIEW_ACTION_SCROLL) {
            setFooterLoadingState();
        }
    }


    /** 设置顶部加载完毕的状态 **/
    private void setSwipeRefreshLoadedState(){
        if(mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }


    /**
     * 设置底部已加载全部的状态
     */
    void setFooterFullState(){
        if(mFooterLayout != null){
            mFooterLayout.setLayoutState(FooterLayout.STATE_COMPLETE);
        }
    }

    void setFooterLoadingState(){
        if(mFooterLayout != null)
            mFooterLayout.setLayoutState(FooterLayout.STATE_LOADING);
    }

    /**
     * 设置底部 无数据状态
     */
    void setFooterNoMoreState(){
        if(mFooterLayout != null)
            mFooterLayout.setLayoutState(FooterLayout.STATE_ERROR_DATA_NULL);
    }

    /**
     * 设置底部  有更多数据 的状态
     */
    void setFooterHasMoreState(){
        if(mFooterLayout != null)
            mFooterLayout.setLayoutState(FooterLayout.STATE_MORE);
    }

    /**
     * 设置底部有  加载出错的 状态
     */
    void setFooterErrorState(){
        if(mFooterLayout != null)
            mFooterLayout.setLayoutState(FooterLayout.STATE_ERROR_DATA_PARSE);
    }
}
