package com.qcx.mini.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.adapter.recommendAdapter.SpaceItemDecoration;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.GroupInfoEntity;
import com.qcx.mini.utils.UiUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */

public class ItemGroupTravelsGroupAdapter extends BaseRecyclerViewAdapter<List<GroupInfoEntity.GroupTravelEntity>,ItemGroupTravelsGroupAdapter.ItemGroupTravelsGroupViewHolder> {
    private BaseRecycleViewHolder.Params params;

    public ItemGroupTravelsGroupAdapter(Context context) {
        super(context);
    }

    public ItemGroupTravelsGroupAdapter(Context context, List<List<GroupInfoEntity.GroupTravelEntity>> datas) {
        super(context, datas);
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    @Override
    public ItemGroupTravelsGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemGroupTravelsGroupViewHolder(inflater.inflate(R.layout.item_group_travels_group,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemGroupTravelsGroupViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemGroupTravelsGroupViewHolder extends BaseRecycleViewHolder{
        private TextView tv_time;
        private RecyclerView rv_list;
        private ItemGroupTravelsAdapter adapter;

        public ItemGroupTravelsGroupViewHolder(View itemView) {
            super(itemView);
            tv_time=itemView.findViewById(R.id.item_group_travels_group_time);
            rv_list=itemView.findViewById(R.id.item_group_travels_group_list);

            LinearLayoutManager layoutManager=new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_list.setLayoutManager(layoutManager);
            adapter=new ItemGroupTravelsAdapter(context);
            rv_list.setAdapter(adapter);
            rv_list.setNestedScrollingEnabled(false);
            rv_list.addItemDecoration(new SpaceItemDecoration((int)(8* UiUtils.SCREENRATIO)));
        }

        @Override
        public void bindData(Object data, @NonNull Params params) {
            List<GroupInfoEntity.GroupTravelEntity> datas= (List<GroupInfoEntity.GroupTravelEntity>) data;
            tv_time.setText(datas.get(0).getTitle());
            adapter.setDatas(datas);
        }
    }
}
