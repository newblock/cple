package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.GroupMembersEntity;
import com.qcx.mini.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Member;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class ItemGroupMemberAdapter extends BaseRecyclerViewAdapter<GroupMembersEntity.GroupMember, ItemGroupMemberAdapter.ItemGroupMemberViewHolder> {

    private ItemMemberClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemGroupMemberAdapter(Context context) {
        super(context);
    }

    public ItemGroupMemberAdapter(Context context, List<GroupMembersEntity.GroupMember> datas) {
        super(context, datas);
    }

    public void setListener(ItemMemberClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public ItemGroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemGroupMemberViewHolder(inflater.inflate(R.layout.item_group_member, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemGroupMemberViewHolder holder, int position) {

        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemGroupMemberViewHolder extends BaseRecycleViewHolder {
        private GroupMembersEntity.GroupMember member;
        private ImageView iv_ranking;
        private ImageView iv_icon;
        private ImageView iv_attention;
        private TextView tv_name;
        private TextView tv_describe;


        public ItemGroupMemberViewHolder(View itemView) {
            super(itemView);
            iv_ranking = itemView.findViewById(R.id.item_group_member_ranking_img);
            iv_icon = itemView.findViewById(R.id.item_group_member_icon);
            iv_attention = itemView.findViewById(R.id.item_group_member_attention_img);
            tv_name = itemView.findViewById(R.id.item_group_member_name);
            tv_describe = itemView.findViewById(R.id.item_group_member_describe);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (member != null) listener.onMemberClick(member.getPhone());
                    }
                }
            });
            iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (member != null) listener.onIconClick(member.getPhone());
                    }
                }
            });
            iv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (member != null) listener.onAttentionClick(member, iv_attention);
                    }
                }
            });
        }

        public void bindData(Object data) {
            member = (GroupMembersEntity.GroupMember) data;
            Picasso.with(context)
                    .load(member.getPicture())
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .placeholder(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(member.getNickName());
            String describe = member.getCar() + " " + member.getLastTimeOnline();
            tv_describe.setText(describe);
            if (member.isAttention()) {
                iv_attention.setImageResource(R.mipmap.btn_followed_nomal);
            } else {
                iv_attention.setImageResource(R.mipmap.btn_follow_nomal);
            }

        }

        public void bindData(Object data, @NonNull Params params) {
            bindData(data);
            int rankingImgId = 0;
            if (params.getPosition() == 0) {
                iv_ranking.setVisibility(View.VISIBLE);
                rankingImgId = R.mipmap.icon_noone;
            } else if (params.getPosition() == 1) {
                iv_ranking.setVisibility(View.VISIBLE);
                rankingImgId = R.mipmap.icon_notwo;
            } else if (params.getPosition() == 2) {
                iv_ranking.setVisibility(View.VISIBLE);
                rankingImgId = R.mipmap.icon_nothree;
            } else {
                iv_ranking.setVisibility(View.GONE);
            }
            if (rankingImgId != 0) {
                iv_ranking.setImageResource(rankingImgId);
            }
        }
    }

    public interface ItemMemberClickListener {
        void onMemberClick(long phone);

        void onAttentionClick(GroupMembersEntity.GroupMember member, ImageView attentionImageView);

        void onIconClick(long phone);
    }
}
