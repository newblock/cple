package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.listener.ItemClickListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2018/7/27.
 */

public class SimpleRecyclerViewAdapter<D> extends BaseRecyclerViewAdapter<D,BaseRecycleViewHolder> {
    private final static int VIEW_TYPE_HEAD = 0;
    private final static int VIEW_TYPE_ITEM = 1;

    private Class<? extends BaseRecycleViewHolder> holderClass;
    private int layoutId;
    private ItemClickListener listener;
    public BaseRecycleViewHolder.Params params;

    private Object headData;
    private Class<? extends BaseRecycleViewHolder> headHolderClass;
    private int headLayoutId;
    private ItemClickListener headListener;

    public SimpleRecyclerViewAdapter(Context context, Class<? extends BaseRecycleViewHolder> holderClass, int layoutId) {
        super(context);
        this.holderClass = holderClass;
        this.layoutId = layoutId;
        this.params=new BaseRecycleViewHolder.Params();
    }

    public void setHeadView(@NonNull Class<? extends BaseRecycleViewHolder> headHolderClass, int headLayoutId, @Nullable ItemClickListener listener) {
        this.headHolderClass = headHolderClass;
        this.headLayoutId = headLayoutId;
        this.headListener = listener;
    }

    public void setHeadData(Object headData) {
        this.headData = headData;
        notifyDataSetChanged();
    }

    public Object getHeadData() {
        return headData;
    }

    @Override
    public int getItemCount() {
        return headHolderClass == null ? datas.size() : datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (headHolderClass != null && position == 0) {
            return VIEW_TYPE_HEAD;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecycleViewHolder viewHolder;
        if (viewType == VIEW_TYPE_HEAD) {
            viewHolder = createViewHolder(headHolderClass, headLayoutId, headListener, parent);
        } else {
            viewHolder = createViewHolder(holderClass, layoutId, listener, parent);
        }
        return viewHolder;
    }

    private BaseRecycleViewHolder createViewHolder(Class<? extends BaseRecycleViewHolder> holderClass, int layoutId, ItemClickListener listener, ViewGroup parent) {
        View view = inflater.inflate(layoutId, parent, false);
        Class[] cl = {View.class};
        BaseRecycleViewHolder viewHolder = null;
        try {
            Constructor constructor = holderClass.getConstructor(cl);
            viewHolder = (BaseRecycleViewHolder) constructor.newInstance(view);
            viewHolder.setHolderContext(context);
            viewHolder.setHolederListener(listener);
            viewHolder.tag = holderClass.getSimpleName();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        if (holder != null) {
            if(headHolderClass!=null&&position==0){
                params.setPosition(position);
                holder.bindData(headData,params);
            }else if(headHolderClass!=null){
                params.setPosition(position-1);
                holder.bindData(getItem(position-1),params);
            } else{
                params.setPosition(position);
                holder.bindData(getItem(position),params);
            }
        }
    }
}
