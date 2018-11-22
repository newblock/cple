package com.qcx.mini.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.listener.ItemClickListener;

/**
 * Created by Administrator on 2018/3/30.
 *
 */

public abstract class BaseRecycleViewHolder<T> extends RecyclerView.ViewHolder {
    public String tag=" ";
    protected Context holderContext;

    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
    }

//    public  void bindData(T data,int position){
//        bindData(data,position,0);
//    }
//
//    public void bindData(T data){
//        bindData(data,0,0);
//    }
//
//    public abstract void bindData(T data,int position,int viewType);

    public abstract void bindData(T data,@NonNull Params params);

    public void bindData(T data){
        bindData(data,new Params());
    }



    public void setHolderContext(Context context) {
        this.holderContext = context;
    }

    public void setHolederListener(ItemClickListener holederListener) {}

    public static class Params{
        private int position;
        private int viewType;
        private Object data;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
