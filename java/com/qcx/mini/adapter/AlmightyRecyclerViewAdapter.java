package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class AlmightyRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecycleViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;

    private List<Class<? extends BaseRecycleViewHolder>> holders;
    private List<List<? extends Object>> datas;
    private List<Integer> layouts;
    private List<ItemClickListener> listeners;
    private BaseRecycleViewHolder.Params params;

    public AlmightyRecyclerViewAdapter(Context context){
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
        holders=new ArrayList<>();
        datas=new ArrayList<>();
        layouts=new ArrayList<>();
        listeners=new ArrayList<>();
    }

    public void addData(Class<? extends BaseRecycleViewHolder> holder,List<? extends Object> data,int layout,ItemClickListener listener){
        holders.add(holder);
        datas.add(data);
        layouts.add(layout);
        listeners.add(listener);
        notifyDataSetChanged();
    }

    public void changeData(int holderPosition,Class<? extends BaseRecycleViewHolder> holder,List<? extends Object> data,int layout,ItemClickListener listener){
        holders.set(holderPosition,holder);
        datas.set(holderPosition,data);
        layouts.set(holderPosition,layout);
        listeners.set(holderPosition,listener);
        notifyDataSetChanged();
    }
    public void addData(int position,Class<? extends BaseRecycleViewHolder> holder,List<? extends Object> data,int layout,ItemClickListener listener){
        holders.add(position,holder);
        datas.add(position,data);
        layouts.add(position,layout);
        listeners.add(position,listener);
        notifyDataSetChanged();
    }

    public void clear(){
        holders.clear();
        datas.clear();
        layouts.clear();
        listeners.clear();
        notifyDataSetChanged();
    }

    public void clear(int position){
        holders.remove(position);
        datas.remove(position);
        layouts.remove(position);
        listeners.remove(position);
        notifyDataSetChanged();
    }
    public void clear(Class<? extends BaseRecycleViewHolder> holder){
        for(int i=0;i<holders.size();i++){
            if(holder.isAssignableFrom(holders.get(i))){
                holders.remove(i);
                datas.remove(i);
                layouts.remove(i);
                listeners.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public int getHolderCount(){
        return holders.size();
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int type=0;
        int count=0;
        for(int i=0;i<datas.size();i++){
            count+=datas.get(i).size();
            if(position<count){
                return type;
            }else {
                type++;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        int count=0;
        for(int i=0;i<datas.size();i++){
            List<? extends Object> content=datas.get(i);
            if(content!=null){
                count+=content.size();
            }
        }
        return count;
    }

    public Object getItem(int position){
        int count=0;
        for(int i=0;i<datas.size();i++){
            List<? extends Object> data=datas.get(i);
            if(data!=null){
                for(int j=0;j<data.size();j++){
                    if(position==count){
                        return data.get(j);
                    }
                    count++;
                }
            }else {
                count++;
            }
        }
        return null;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            return  getHeadViewHolder(parent,holders.get(viewType),layouts.get(viewType),listeners.get(viewType));
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.e("AlmightyRecyclerViewAdapter onCreateViewHolder is error");
            return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        if(holder!=null){
            holder.bindData(getItem(position),params);
        }
    }


    private BaseRecycleViewHolder getHeadViewHolder(ViewGroup parent,Class<? extends BaseRecycleViewHolder> holderClass, int viewId,ItemClickListener listener) {
        Class[] cl = {View.class};
        try {
            Constructor constructor = holderClass.getConstructor(cl);
            View view = mInflater.inflate(viewId, parent,false);
            Object[] params = {view};
            BaseRecycleViewHolder headHolder = (BaseRecycleViewHolder) constructor.newInstance(params);
            headHolder.setHolderContext(mContext);
            headHolder.setHolederListener(listener);
            headHolder.tag=holderClass.getSimpleName();
            return headHolder;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
