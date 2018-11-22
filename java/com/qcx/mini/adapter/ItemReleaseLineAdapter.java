package com.qcx.mini.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.viewHolder.ItemReleaseLineViewHolder;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.ReleaseLineInfoEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ItemReleaseLineAdapter extends BaseRecyclerViewAdapter<ReleaseLineInfoEntity,BaseRecycleViewHolder> {
    private final int BOTTOME_VIEW=1;
    private final int CONTENT_VIEW=0;
    private OnReleaseLineItemClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemReleaseLineAdapter(Context context) {
        super(context);
    }

    public ItemReleaseLineAdapter(Context context, List<ReleaseLineInfoEntity> datas) {
        super(context, datas);
    }

    public void setListener(OnReleaseLineItemClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==datas.size()){
            return BOTTOME_VIEW;
        }else {
            return CONTENT_VIEW;
        }
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==BOTTOME_VIEW){
            ItemReleaseLineBottomViewHolder holder=new ItemReleaseLineBottomViewHolder(inflater.inflate(R.layout.item_release_line_bottom,parent,false));
           holder.setHolederListener(listener);
            return holder;
        }else {
            ItemReleaseLineViewHolder holder=new ItemReleaseLineViewHolder(inflater.inflate(R.layout.item_release_line,parent,false));
            holder.setHolederListener(listener);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        if(position<datas.size()){
            holder.bindData(getItem(position),params);
        }
    }

    public static class ItemReleaseLineBottomViewHolder extends BaseRecycleViewHolder{
        private OnReleaseLineItemClickListener clickListener;
        public ItemReleaseLineBottomViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.item_release_line_bottom_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener!=null){
                        clickListener.onAddClick();
                    }
                }
            });
            itemView.findViewById(R.id.item_release_line_bottom_manger).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener!=null){
                        clickListener.onManger();
                    }
                }
            });

        }

        @Override
        public void setHolederListener(ItemClickListener holederListener) {
            clickListener= (OnReleaseLineItemClickListener) holederListener;
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {

        }
    }

    public interface OnReleaseLineItemClickListener extends ItemClickListener{
        void onInfoClick(ReleaseLineInfoEntity line);
        void onReleaseClick(ReleaseLineInfoEntity line,TextView releaseView);
        void onAddClick();
        void onManger();
    }
}
