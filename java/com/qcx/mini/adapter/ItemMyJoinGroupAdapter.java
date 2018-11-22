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
import com.qcx.mini.entity.WayListEntity;
import com.qcx.mini.listener.OnGroupClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class ItemMyJoinGroupAdapter extends BaseRecyclerViewAdapter<WayListEntity.GroupItem,BaseRecycleViewHolder> {

    private OnGroupClickListener listener;
    private BaseRecycleViewHolder.Params params;

    public ItemMyJoinGroupAdapter(Context context) {
        super(context);
    }

    public ItemMyJoinGroupAdapter(Context context, List<WayListEntity.GroupItem> datas) {
        super(context, datas);
    }

    public void setListener(OnGroupClickListener listener) {
        this.listener = listener;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
    }

    @Override
    public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHotWayViewHolder(inflater.inflate(R.layout.item_hot_way,null,false));
    }

    @Override
    public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemHotWayViewHolder extends BaseRecycleViewHolder implements View.OnClickListener{
        private WayListEntity.GroupItem item;
        private View view;
        private ImageView iv_icon;
        private TextView tv_name;
        private TextView tv_manNum;
        private TextView tv_info;
        private TextView tv_type;
        private TextView tv_distance;
        private TextView tv_price;
        private ImageView iv_add;

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
                item= (WayListEntity.GroupItem) data;
                Picasso.with(context)
                        .load(item.getGroupBanner())
                        .error(R.mipmap.img_me)
                        .placeholder(R.mipmap.img_me)
                        .resize(ConstantValue.ICON_RESIZE,ConstantValue.ICON_RESIZE)
                        .into(iv_icon);
//                String name=item.getStartCity()+"-"+item.getEndCity();
                tv_name.setText(item.getGroupTitle());
                tv_manNum.setText("人数".concat(String.valueOf(item.getGroupNum())));
                tv_info.setText(item.getGroupNotice());
                if(item.isJoined()){
                    iv_add.setVisibility(View.GONE);
                }else {
                    iv_add.setVisibility(View.VISIBLE);
                }

                if(item.getDistance()>0){
                    tv_distance.setVisibility(View.VISIBLE);
                    tv_distance.setText("路程".concat(String.valueOf(item.getDistance()).concat("公里")));
                }else {
                    tv_distance.setVisibility(View.GONE);
                }
                if(item.getGroupAdvicePrice()>0){
                    tv_price.setVisibility(View.VISIBLE);
                    tv_price.setText("推荐价".concat(String.valueOf(item.getGroupAdvicePrice())));
                }else {
                    tv_price.setVisibility(View.GONE);
                }

                switch (item.getGroupType()){
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
                        listener.onItemClick(item.getGroupId(),item.getGroupType());
                    }
                    break;
                case R.id.item_hot_way_add:
                    if (listener!=null){
                        listener.onAddClick(item.getGroupId(),iv_add);
                    }
                    break;
            }
        }
    }
}
