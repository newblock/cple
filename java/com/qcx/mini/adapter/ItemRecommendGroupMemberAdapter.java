package com.qcx.mini.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.GroupMembersEntity;
import com.qcx.mini.entity.RecommendGroupMembersEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ItemRecommendGroupMemberAdapter extends BaseRecyclerViewAdapter<RecommendGroupMembersEntity.Member, ItemRecommendGroupMemberAdapter.ItemRecommendGroupMemberViewHolder> {
    private OnItemRecommendGroupMemberClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemRecommendGroupMemberAdapter(Context context) {
        super(context);
    }

    public ItemRecommendGroupMemberAdapter(Context context, List<RecommendGroupMembersEntity.Member> datas) {
        super(context, datas);
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public ItemRecommendGroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemRecommendGroupMemberViewHolder(inflater.inflate(R.layout.item_group_member, parent, false));
    }

    public void setListener(OnItemRecommendGroupMemberClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ItemRecommendGroupMemberViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemRecommendGroupMemberViewHolder extends BaseRecycleViewHolder {
        private RecommendGroupMembersEntity.Member member;
        private ImageView iv_ranking;
        private ImageView iv_icon;
        private ImageView iv_attention;
        private TextView tv_name;
        private TextView tv_describe;

        public ItemRecommendGroupMemberViewHolder(View itemView) {
            super(itemView);
            iv_ranking = itemView.findViewById(R.id.item_group_member_ranking_img);
            iv_icon = itemView.findViewById(R.id.item_group_member_icon);
            iv_attention = itemView.findViewById(R.id.item_group_member_attention_img);
            tv_name = itemView.findViewById(R.id.item_group_member_name);
            tv_describe = itemView.findViewById(R.id.item_group_member_describe);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onItemClick(member,iv_ranking);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data,Params params ) {
            member = (RecommendGroupMembersEntity.Member) data;
            int rankingImgId = 0;
            if(member.isSelected()){
                iv_ranking.setImageResource(R.mipmap.icon_check);
            }else {
                iv_ranking.setImageResource(R.mipmap.icon_uncheck);
            }
            Picasso.with(context)
                    .load(member.getPicture())
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .placeholder(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(member.getNickName());
            String describe = member.getCar() + " " + member.getLastTimeOnline();
            tv_describe.setText(describe);
            iv_attention.setVisibility(View.GONE);

        }
    }

    public interface OnItemRecommendGroupMemberClickListener{
        void onItemClick(RecommendGroupMembersEntity.Member member,ImageView iv_selected);
    }
}
