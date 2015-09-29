package com.joysoft.andutils.test;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.IBaseAdapter;
import com.joysoft.andutils.fragment.RecylerViewFragment;
import com.joysoft.andutils.lg.Lg;
import com.joysoft.andutils.test.card.TestCardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fengmiao on 15/9/13.
 */
public class TestRecycleViewFragment extends RecylerViewFragment{

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }


    String path = "http://apiv2.9dxz.cn:81/content/getContentList";
    @Override
    public String getUrl() {
        return path;
    }

    @Override
    public HashMap<String, String> getParams(int index) {
        HashMap<String,String> parmas = new HashMap<>();
        parmas.put("pagenum",index+"");
        parmas.put("pagesize","10");
        parmas.put("type","1");
        parmas.put("idtype","master");
        parmas.put("categoryId","11");
        return parmas;
    }

    @Override
    public IBaseAdapter getAdapter() {
//        return new TestRecylcerAdapter();
        return mAdapter == null ? new TestCardAdapter() : mAdapter;
    }

    /**
     * 解析列表数据
     *
     * @param result
     * @param index
     * @return result
     */
    @Override
    public List getContentList(Object result, int index) {

        List<JSONObject> mList = new ArrayList<>();

        try{

            JSONArray array = (JSONArray)result;

            for(int i = 0; i < array.length(); i++){
                mList.add(array.getJSONObject(i));
            }

        }catch (Exception e){
            Lg.e(e.toString());
        }


        return mList;
    }

    /**
     * 获取要加载的视图Id
     *
     * @return
     */
    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_recyclrefresh;
    }

    /**
     * dealloc无用对象
     */
    @Override
    public void recycle() {

    }
}
