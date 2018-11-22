package com.qcx.mini.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.qcx.mini.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseRecyclerViewAdapter<D,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<D> datas;
    protected LayoutInflater inflater;
    protected Context context;

    protected boolean isScrolling = false;

    public BaseRecyclerViewAdapter(Context context){
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        datas=new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(Context context,List<D> datas){
        this.inflater=LayoutInflater.from(context);
        if(datas!=null)this.datas=datas;
        else this.datas=new ArrayList<>();
    }

    public D getItem(int position){
        return datas.get(position);
    }

    public void setDatas(List<D> datas){
        if(datas!=null){
            this.datas=datas;
        }else {
            this.datas=new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void addDatas(List<D> datas){
        if(datas!=null){
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

    }

    public void remove(List<D> datas){
        if(datas!=null){
            this.datas.removeAll(datas);
//            for(int i=0;i<datas.size();i++){
//                this.datas.remove(datas.get(i));
//            }
        }
        notifyDataSetChanged();
    }

    public void remove(D data){
        if(data!=null){
            datas.remove(data);
        }
        notifyDataSetChanged();
    }

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    public List<D> getData(){
        return datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
