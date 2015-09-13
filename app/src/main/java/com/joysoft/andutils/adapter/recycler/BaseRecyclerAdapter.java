package com.joysoft.andutils.adapter.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.joysoft.andutils.adapter.IBaseAdapter;
import com.joysoft.andutils.lg.Lg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengmiao on 15/9/12.
 */
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter implements IBaseAdapter<T> {

    public List<T> dataList = new ArrayList<T>();
    private final Object mLock = new Object();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public  void addData(T object){
        synchronized (mLock){

            if(object == null){
                Lg.d("---- 插入一条空数据  --- return");
                return;
            }
            if(dataList != null)
                dataList.add(object);

            notifyDataSetChanged();
        }

    }

    @Override
    public  void addDataList(List<T> mList){
        synchronized (mLock){

            if(mList != null){
                dataList.addAll(mList);
            }
            else{
                Lg.d("---- 插入一条空数据  --- return");
                return;
            }

            notifyDataSetChanged();
        }

    }

    @Override
    public void addDataList(List<T> mList,int index){
        synchronized (mLock){

            if(mList != null){
                dataList.addAll(index,mList);
            }
            else{
                Lg.d("---- 插入一条空数据  --- return");
                return;
            }

            notifyDataSetChanged();
        }

    }

    @Override
    public  void clearDataList(){
        synchronized (mLock){
            if(dataList != null)
                dataList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public  void removeData(int position){
        synchronized (mLock){
            if(dataList != null)
                dataList.remove(position);
        }
    };

    @Override
    public List<T> getDataList(){
        return dataList;
    };

    @Override
    public  void recycleData(){

        if(dataList != null){
            dataList.clear();
        }
        dataList = null;

    };

    @Override
    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public int getTotalCount() {
        return getItemCount();
    }

}
