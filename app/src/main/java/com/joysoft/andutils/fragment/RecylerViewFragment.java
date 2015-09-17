package com.joysoft.andutils.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.recycler.BaseRecyclerAdapter;
import com.joysoft.andutils.lg.Lg;
import com.joysoft.andutils.ui.IEmptyLayout;
import com.joysoft.andutils.ui.view.EmptyLayout;
import com.joysoft.andutils.ui.view.FooterView;

/**
 * Created by fengmiao on 15/9/12.
 */
public abstract class RecylerViewFragment extends BaseRefreshFragment{

    public RecyclerView mRecyclerView;

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

        //RecyclerView
        mRecyclerView = (RecyclerView)root.findViewById(R.id.fragment_recylerview);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter((RecyclerView.Adapter) mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mFooterLayout == null)
                    return;

                //判断是否滚动到底部
                boolean scrollEnd = false;
                try {
                    if (recyclerView.getChildLayoutPosition(mFooterLayout.getView()) == recyclerView.getChildCount())
                        scrollEnd = true;
                } catch (Exception e) {
                    scrollEnd = false;
                }

                if (scrollEnd)
                    onScrollLoad();
            }
        });

    }


    @Override
    protected void initHeaderFooterView() {
        super.initHeaderFooterView();
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

        ((BaseRecyclerAdapter)mAdapter).addFooter(view);
//        mRecyclerView.getLayoutManager().
    }

    public abstract RecyclerView.LayoutManager getLayoutManager();

}
