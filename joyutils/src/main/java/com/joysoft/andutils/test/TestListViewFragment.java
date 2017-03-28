package com.joysoft.andutils.test;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joysoft.andutils.R;
import com.joysoft.andutils.adapter.AbsBaseAdapter;
import com.joysoft.andutils.adapter.IBaseAdapter;
import com.joysoft.andutils.fragment.ListViewFragment;
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
public class TestListViewFragment extends ListViewFragment{


    String path = "http://apiv2.9dxz.cn:81/content/getContentList";
    @Override
    public String getUrl() {
        return path;
    }

    @Override
    public Object getParams(int index) {
        HashMap<String,String> parmas = new HashMap<>();
        parmas.put("pagenum",index+"");
        parmas.put("pagesize","10");
        parmas.put("type","1");
        parmas.put("idtype","master");
        parmas.put("categoryId","11");
        return parmas;
    }

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

    @Override
    public IBaseAdapter getAdapter() {
//        return mAdapter == null ? new TestAbsAdapter(getActivity()) : mAdapter;
        if(mAdapter != null)
            return mAdapter;

        return  new AbsBaseAdapter(getActivity()) {
            @Override
            public int getLayoutID() {
                return  R.layout.test_my_text_view;
            }

            @Override
            public void onBindViewHolder(int position, ViewHolder holder, ViewGroup parent) {
                TextView textView = (TextView) holder.getView(R.id.test_tv);
                textView.setText(position+"");
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_listrefresh;
    }
}
