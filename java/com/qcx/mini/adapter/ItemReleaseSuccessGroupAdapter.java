package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.ReleaseResultEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ItemReleaseSuccessGroupAdapter extends BaseRecyclerViewAdapter<ReleaseResultEntity.JoinedGroup,ItemReleaseSuccessGroupAdapter.ItemReleaseSuccessGroupViewHolder> {

    private boolean is1=true;
    private BaseRecycleViewHolder.Params params;

    public ItemReleaseSuccessGroupAdapter(Context context) {
        super(context);
    }

    public ItemReleaseSuccessGroupAdapter(Context context, List datas) {
        super(context, datas);
    }

    public void setIs1(boolean is1) {
        this.is1 = is1;
    }

    public boolean isIs1() {
        return is1;
    }

    @Override
    public int getItemViewType(int position) {
        if(is1){
            return 1;
        }else {
            return 2;
        }
    }

    @Override
    public ItemReleaseSuccessGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            return new ItemReleaseSuccessGroupViewHolder(inflater.inflate(R.layout.item_release_success_group,parent,false));
        }else {
            return new ItemReleaseSuccessGroupViewHolder(inflater.inflate(R.layout.item_release_success_group_2,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(ItemReleaseSuccessGroupViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemReleaseSuccessGroupViewHolder extends BaseRecycleViewHolder{
        private ReleaseResultEntity.JoinedGroup group;
        private TextView tv_text;
        private ImageView iv_check;
        public ItemReleaseSuccessGroupViewHolder(View itemView) {
            super(itemView);
            tv_text=itemView.findViewById(R.id.item_release_success_group_text);
            iv_check=itemView.findViewById(R.id.item_release_success_group_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(group!=null){
                        group.setCheck(!group.isCheck());
                        bindData(group);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            try {
                group= (ReleaseResultEntity.JoinedGroup) data;
                tv_text.setText(group.getGroupTitle());
                if(!group.isJoined()&&is1){
                    iv_check.setVisibility(View.VISIBLE);
                }else {
                    iv_check.setVisibility(View.GONE);
                }
                if(group.isCheck()){
                    iv_check.setImageResource(R.mipmap.icon_check);
                }else {
                    iv_check.setImageResource(R.mipmap.icon_uncheck);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
