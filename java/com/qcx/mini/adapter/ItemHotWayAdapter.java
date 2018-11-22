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
import com.qcx.mini.entity.HotGroupsEntity;
import com.qcx.mini.listener.OnGroupClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class ItemHotWayAdapter extends BaseRecyclerViewAdapter<HotGroupsEntity.HotGroup,ItemHotWayAdapter.ItemHotWayViewHolder> {
    private OnGroupClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemHotWayAdapter(Context context) {
        super(context);
    }

    public ItemHotWayAdapter(Context context, List<HotGroupsEntity.HotGroup> datas) {
        super(context, datas);
    }

    public void setListener(OnGroupClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    @Override
    public ItemHotWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHotWayViewHolder(inflater.inflate(R.layout.item_hot_way,null,false));
    }

    @Override
    public void onBindViewHolder(ItemHotWayViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemHotWayViewHolder extends BaseRecycleViewHolder implements View.OnClickListener{
        private HotGroupsEntity.HotGroup hotGroup;
        private View view;
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_manNum;
        private TextView tv_info;
        private TextView tv_type;
        private ImageView iv_add;
        private TextView tv_distance;
        private TextView tv_price;

        public ItemHotWayViewHolder(View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.item_hot_way_view);
            iv_icon=itemView.findViewById(R.id.item_hot_way_icon);
            tv_name=itemView.findViewById(R.id.item_hot_way_name);
            tv_manNum=itemView.findViewById(R.id.item_hot_way_man_num);
            tv_info=itemView.findViewById(R.id.item_hot_way_ifon);
            iv_add=itemView.findViewById(R.id.item_hot_way_add);
            tv_type=itemView.findViewById(R.id.item_hot_way_type);
            tv_distance=itemView.findViewById(R.id.item_hot_way_distance);
            tv_price=itemView.findViewById(R.id.item_hot_way_price);

            view.setOnClickListener(this);
            iv_add.setOnClickListener(this);
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            try {
                hotGroup= (HotGroupsEntity.HotGroup) data;
                Picasso.with(context)
                        .load(hotGroup.getGroupBanner())
                        .error(R.mipmap.img_me)
                        .placeholder(R.mipmap.img_me)
                        .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                        .into(iv_icon);
                String name=hotGroup.getStartCity()+"-"+hotGroup.getEndCity();
                tv_name.setText(hotGroup.getGroupTitle());
                tv_manNum.setText("人数".concat(String.valueOf(hotGroup.getGroupMemberNum())));
                tv_info.setText(hotGroup.getGroupNotice());
                if(hotGroup.isJoined()){
                    iv_add.setVisibility(View.GONE);
                }else {
                    iv_add.setVisibility(View.VISIBLE);
                }

                if(hotGroup.getDistance()>0){
                    tv_distance.setVisibility(View.VISIBLE);
                    tv_distance.setText("路程".concat(String.valueOf(hotGroup.getDistance()).concat("公里")));
                }else {
                    tv_distance.setVisibility(View.GONE);
                }
                if(hotGroup.getAdvisePrice()>0){
                    tv_price.setVisibility(View.VISIBLE);
                    tv_price.setText("推荐价".concat(String.valueOf(hotGroup.getAdvisePrice())));
                }else {
                    tv_price.setVisibility(View.GONE);
                }

                switch (hotGroup.getGroupType()){
                    case ConstantValue.GroupType.ACROSS_CITY:
                        tv_type.setText("跨城");
                        break;
                    case ConstantValue.GroupType.WORK:
                        tv_type.setText("上下班");
                        break;
                    case ConstantValue.GroupType.SCENIC:
                        tv_type.setText("景点");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_hot_way_view:
                    if (listener!=null){
                        listener.onItemClick(hotGroup.getGroupId(),hotGroup.getGroupType());
                    }
                    break;
                case R.id.item_hot_way_add:
                    if (listener!=null){
                        listener.onAddClick(hotGroup.getGroupId(),iv_add);
                    }
                    break;
            }
        }
    }
}
