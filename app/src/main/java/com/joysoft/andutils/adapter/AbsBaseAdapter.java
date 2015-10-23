package com.joysoft.andutils.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.joysoft.andutils.lg.Lg;

import java.util.ArrayList;
import java.util.List;

/**
 * 继承BaseAdapter的通用类
 * Created by fengmiao on 15/8/31.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter implements IBaseAdapter<T>{

    public List<T> dataList = new ArrayList<T>();

    private final Object mLock = new Object();

    protected Context context;

    public AbsBaseAdapter(Context context){
        this.context = context;
    }


    /**
     * 获取当前视图Id
     * @return
     */
    public abstract int getLayoutID();

    /**
     * 绑定数据
     * @param position
     * @param parent
     */
    public abstract void onBindViewHolder(int position, ViewHolder holder, ViewGroup parent);

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

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(getLayoutID(),parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        onBindViewHolder(position,holder,parent);
        return convertView;
    }


    public class ViewHolder{
        private SparseArray<View> views = new SparseArray<>();
        private View convertView;

        public ViewHolder(View convertView){
            this.convertView = convertView;
        }

        public <T extends View> T getView(int resId){
            View v = views.get(resId);
            if(v == null){
                v = convertView.findViewById(resId);
                views.put(resId,v);
            }
            return (T)v;
        }

        public View getConvertView(){
            return convertView;
        }
    }

    /********************************
     * 数据相关
     ********************************/
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


}
