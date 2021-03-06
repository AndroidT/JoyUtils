package com.joysoft.andutils.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joysoft.andutils.activity.ActivityPageManager;
import com.joysoft.andutils.load.ImageLoader;

/**
 * Created by fengmiao on 15/8/30.
 */
public abstract class BaseFragment extends Fragment{


    /**
     * 获取要加载的视图Id
     * @return
     */
    public abstract  int getLayoutId();

    /**
     * 初始化View
     * @param root
     */
    public abstract  void initViews(View root);

    /**
     * dealloc无用对象
     */
    public abstract  void recycle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, attachToRoot());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ActivityPageManager.unbindReferences(getView());

        recycle();
    }

    @Override
    public void onDestroy() {
        if(cleanCache())
            ImageLoader.clearMemory(getActivity());
        super.onDestroy();
    }

    protected boolean cleanCache(){return true;}

    protected boolean attachToRoot(){
        return false;
    }
}

