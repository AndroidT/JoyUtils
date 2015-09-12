package com.joysoft.andutils.adapter;

import com.joysoft.andutils.common.Data;
import com.joysoft.andutils.lg.Lg;

import java.util.List;

/**
 * Created by fengmiao on 15/9/12.
 */
public interface IBaseAdapter<T> {


    public  void addData(T object);

    public  void addDataList(List<T> mList);

    public void addDataList(List<T> mList,int index);

    public  void clearDataList();

    public  void removeData(int position);

    public List<T> getDataList();


    public  void recycleData();

    /**
     * 判断当前adapter数据长度是否为0
     * @return
     */
    public boolean isEmpty();

    /**
     * 获取当前Adapter的item数量
     *
     * @return  见getCount；
     */
    public int getTotalCount();
}
