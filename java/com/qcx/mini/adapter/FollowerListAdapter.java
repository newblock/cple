package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.FollowerEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */

public class FollowerListAdapter extends BaseRecyclerViewAdapter<FollowerEntity.Follower, FollowerListAdapter.FollowerListViewHolder> {
    private FollowerOnClickListener listener;

    public FollowerListAdapter(Context context) {
        super(context);
    }

    public FollowerListAdapter(Context context, List<FollowerEntity.Follower> datas) {
        super(context, datas);
    }

    public void setListener(FollowerOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public FollowerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowerListViewHolder(inflater.inflate(R.layout.item_follower, parent, false));
    }

    @Override
    public void onBindViewHolder(FollowerListViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    class FollowerListViewHolder extends RecyclerView.ViewHolder {
        FollowerEntity.Follower follower;
        private ImageView iv_icon, iv_status, iv_sex;
        private TextView tv_name, tv_info;

        FollowerListViewHolder(View itemView) {
            super(itemView);
            iv_icon=itemView.findViewById(R.id.item_follower_icon);
            iv_status=itemView.findViewById(R.id.item_follower_status);
            iv_sex=itemView.findViewById(R.id.item_follower_sex);
            tv_name=itemView.findViewById(R.id.item_follower_name);
            tv_info=itemView.findViewById(R.id.item_follower_info);
            iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&follower!=null){
                        listener.onAttentionBtnClick(follower.getAttentionStatus()!=0,follower.getPhone(),iv_status,follower);
                    }
                }
            });
            iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onIconClick(follower.getPhone());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onIconClick(follower.getPhone());
                    }
                }
            });
        }

        void bindData(final FollowerEntity.Follower follower){
            this.follower=follower;
            tv_name.setText(follower.getNickName());
            String info=follower.getCar()+" "+follower.getLastTimeOnline();
            tv_info.setText(info);
            Picasso.with(context)
                    .load(follower.getPicture())
                    .resize(200,200)
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
            iv_status.setVisibility(View.VISIBLE);
            switch (follower.getAttentionStatus()){
                case 0:
                    iv_status.setImageResource(R.mipmap.btn_follow);
                    break;
                case 1:
                    iv_status.setImageResource(R.mipmap.btn_followed);
                    break;
                case 2:
                    iv_status.setImageResource(R.mipmap.btn_followed_each);
                    break;
                default:
                    iv_status.setVisibility(View.GONE);
                    break;
            }
            if (follower.getSex() == 1) {
                iv_sex.setImageResource(R.mipmap.img_men);
            } else if (follower.getSex() == 2) {
                iv_sex.setImageResource(R.mipmap.img_women);
            } else {
                iv_sex.setVisibility(View.GONE);
            }
        }
    }

    public interface FollowerOnClickListener{
        void onAttentionBtnClick(boolean isAttention, long phone, ImageView iv_status, FollowerEntity.Follower follower);
        void onIconClick(long phone);
    }
}
