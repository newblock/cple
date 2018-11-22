package com.qcx.mini.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.Entity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemDriverAndTravelClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/3/22.
 */

public class ItemDriverAndTravelAdapter extends BaseRecyclerViewAdapter<DriverAndTravelEntity, BaseRecycleViewHolder> {
    private Class<? extends BaseRecycleViewHolder> headHolderClass;
    private int headViewId;
    private int headItemsNum;
    private ItemClickListener headListener;
    private List<? extends Entity> headData;
    private BaseRecycleViewHolder.Params params;

    private ItemDriverAndTravelClickListener listener;

    int tag=0;
    public ItemDriverAndTravelAdapter(Context context) {
        super(context);
    }

    public ItemDriverAndTravelAdapter(Context context, List<DriverAndTravelEntity> datas) {
        super(context, datas);
    }

    public void setHeadViewHolder(Class<? extends BaseRecycleViewHolder> holder, int headViewId, int itemsNum) {
        this.headHolderClass = holder;
        this.headViewId = headViewId;
        this.headItemsNum = itemsNum;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    private BaseRecycleViewHolder initHeadViewHolder(Class<? extends BaseRecycleViewHolder> holderClass, int headViewId, ViewGroup parent) {
        Class[] cl = {View.class};
        try {
            Constructor constructor = holderClass.getConstructor(cl);
            View view = inflater.inflate(headViewId, parent,false);
            Object[] params = {view};
            BaseRecycleViewHolder headHolder = (BaseRecycleViewHolder) constructor.newInstance(params);
            headHolder.setHolderContext(context);
            headHolder.setHolederListener(headListener);
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

    public void setHeadData(List<? extends Entity> headData) {
        this.headData = headData;
        notifyDataSetChanged();
    }

    public void setListener(ItemDriverAndTravelClickListener listener) {
        this.listener = listener;
    }

    public void setHeadListener(ItemClickListener headListener) {
        this.headListener = headListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headItemsNum) {
            return ViewType.HEAD_VIEW.getCode();
        } else {
            return ViewType.DRIVER_TRAVEL_VIEW.getCode();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + headItemsNum;
    }

    int i=0;
    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        i++;
        if (viewType == ViewType.HEAD_VIEW.getCode()) {
            return initHeadViewHolder(headHolderClass, headViewId,parent);
        } else {
                ItemDriverAndTravelViewHolder holder = new ItemDriverAndTravelViewHolder(inflater.inflate(R.layout.item_driver_travel_2, parent, false));
                holder.setHolderContext(context);
                holder.setHolederListener(listener);
                holder.tag=(tag++)+"";
                return holder;
//            }
        }
    }

    @Override
    public void onViewRecycled(BaseRecycleViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        if (position < headItemsNum) {
            if(headData!=null&&position<headData.size()){
                params.setPosition(position);
                holder.bindData(headData.get(position),params);
            }else {
                params.setPosition(0);
                holder.bindData(null,params);
            }
        } else {
            if (position - headItemsNum < datas.size()) {
                params.setPosition(position-headItemsNum);
                holder.bindData(getItem(position - headItemsNum),params);
                holder.setHolederListener(listener);
            }
        }
    }

    public enum ViewType {
        DRIVER_TRAVEL_VIEW(0, "行程item"), HEAD_VIEW(1, "头部View");
        private int code;
        private String info;

        ViewType(int code, String info) {
            this.code = code;
            this.info = info;
        }

        public int getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }
    }

}
