package com.joysoft.andutils.fragment;

import com.joysoft.andutils.adapter.IBaseAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fengmiao on 15/9/12.
 */
public class RecylerViewFragment extends BaseRefreshFragment{

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public HashMap<String, String> getParams(int index) {
        return null;
    }

    @Override
    public IBaseAdapter getAdapter() {
        return null;
    }

    @Override
    public List getContentList(Object result, int index) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void recycle() {

    }
}
