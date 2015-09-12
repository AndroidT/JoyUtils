package com.joysoft.andutils.adapter;

import android.database.DataSetObserver;
import android.widget.BaseAdapter;

import com.joysoft.andutils.common.Data;
import com.joysoft.andutils.lg.Lg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * Created by fengmiao on 15/8/31.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter implements IBaseAdapter<T>{

    public List<T> dataList = new ArrayList<T>();

    private final Object mLock = new Object();

    @Override
    public  void addData(T object){
        synchronized (mLock){

            if(Data.isNull(object)){
                Lg.d("---- 插入一条空数据  --- return");
                return;
            }
            if(Data.isValid(dataList))
                dataList.add(object);

            notifyDataSetChanged();
        }

    }

    @Override
    public  void addDataList(List<T> mList){
        synchronized (mLock){

            if(Data.isValid(mList) && Data.isValid(dataList)){
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

            if(Data.isValid(mList) && Data.isValid(dataList)){
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
            if(Data.isValid(dataList))
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
        return getCount() == 0;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getTotalCount() {
        return getCount();
    }
}
