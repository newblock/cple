package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.SearchTravelEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemSearchGroupClickListener;
import com.qcx.mini.utils.LogUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/4/9.
 */

public class ItemSearchGroupViewHolder extends BaseRecycleViewHolder implements View.OnClickListener {
    private SearchTravelEntity.SearchGroupEntity group;
    private ItemSearchGroupClickListener listener;
    private TextView tv_title;
    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_manNum;
    private TextView tv_describe;
    private ImageView iv_join;
    private View v_bottom;
    private View v_noData;
    private View v_noTravelText1;
    private View v_noTravelText2;
    private View v_noTravelRelease;
    private View v_group;

    public ItemSearchGroupViewHolder(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.item_search_group_title);
        iv_icon = itemView.findViewById(R.id.item_search_group_icon);
        tv_name = itemView.findViewById(R.id.item_search_group_name);
        tv_manNum = itemView.findViewById(R.id.item_search_group_man_num);
        tv_describe = itemView.findViewById(R.id.item_search_group_describe);
        iv_join = itemView.findViewById(R.id.item_search_group_add);
        v_bottom = itemView.findViewById(R.id.item_search_group_bottomView);
        v_noData = itemView.findViewById(R.id.item_search_group_noData);
        v_noTravelText1 = itemView.findViewById(R.id.item_search_group_no_travel_text1);
        v_noTravelText2 = itemView.findViewById(R.id.item_search_group_no_travel_text2);
        v_noTravelRelease = itemView.findViewById(R.id.item_search_group_no_travel_release_view);
        v_group = itemView.findViewById(R.id.item_search_group_view);

        v_group.setOnClickListener(this);
        iv_join.setOnClickListener(this);
        v_noTravelRelease.setOnClickListener(this);
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener = (ItemSearchGroupClickListener) holederListener;
    }

    @Override
    public void bindData(Object data, @NonNull Params params) {
        if (data == null) {
            showNullView();
            return;
        }
        try {
            group = (SearchTravelEntity.SearchGroupEntity) data;
            bindData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindData() {
        if (group.getGroupId() == 0) {
            showNullView();
        } else {
            iv_icon.setVisibility(View.VISIBLE);
            tv_name.setVisibility(View.VISIBLE);
            tv_manNum.setVisibility(View.VISIBLE);
            tv_describe.setVisibility(View.VISIBLE);
            v_noData.setVisibility(View.GONE);
            if (group.isLastItem()) {
                v_bottom.setVisibility(View.VISIBLE);
            } else {
                v_bottom.setVisibility(View.GONE);
            }

            Picasso.with(holderContext)
                    .load(group.getGroupImg())
                    .resize(ConstantValue.ICON_RESIZE, ConstantValue.ICON_RESIZE)
                    .error(R.mipmap.img_me)
                    .placeholder(R.mipmap.img_me)
                    .into(iv_icon);
            tv_name.setText(group.getGroupTitle());
            tv_manNum.setText("人数".concat(String.valueOf(group.getPeopleNum())));
            tv_describe.setText(group.getGroupNotice());
            if (group.isInGroup()) {
                iv_join.setVisibility(View.GONE);
            } else {
                iv_join.setVisibility(View.VISIBLE);
            }
        }
        if (group.isFirstItem()) {
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.GONE);
        }

        if (group.isHavesTravel()) {
            v_noTravelRelease.setVisibility(View.VISIBLE);
            v_noTravelText1.setVisibility(View.VISIBLE);
            v_noTravelText2.setVisibility(View.VISIBLE);
        } else {
            v_noTravelRelease.setVisibility(View.GONE);
            v_noTravelText1.setVisibility(View.GONE);
            v_noTravelText2.setVisibility(View.GONE);
        }

    }

    private void showNullView() {
        tv_title.setVisibility(View.VISIBLE);
        iv_icon.setVisibility(View.GONE);
        iv_join.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        tv_manNum.setVisibility(View.GONE);
        tv_describe.setVisibility(View.GONE);
        v_bottom.setVisibility(View.VISIBLE);
        v_noData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_search_group_view:
                if (listener != null) {
                    listener.onGroupClick(group.getGroupId(), group.getGroupType());
                }
                break;
            case R.id.item_search_group_add:
                if (listener != null) {
                    listener.onJoinClick(group.getGroupId(), iv_join);
                }
                break;
            case R.id.item_search_group_no_travel_release_view:
                if (listener != null) {
                    listener.onReleaseTravelClick();
                }
                break;
        }
    }
}
