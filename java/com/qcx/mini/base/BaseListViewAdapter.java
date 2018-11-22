package com.qcx.mini.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    public List<T> datas;
    public LayoutInflater inflater;
    public BaseListViewAdapter(Context context){
        inflater=LayoutInflater.from(context);
        datas=new ArrayList<>();
    }
    public BaseListViewAdapter(Context context,List<T> datas){
        inflater=LayoutInflater.from(context);
        this.datas=datas;
    }

    public void setDatas(List<T> datas){
        if (datas==null) datas=new ArrayList<>();
        this.datas=datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas){
        if(datas==null||datas.size()==0) return;
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


//    public abstract View getCView(int position, View convertView, ViewGroup parent);
}
