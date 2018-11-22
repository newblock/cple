package com.qcx.mini.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qcx.mini.R;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemForceInfoDetailViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemForceInfoHeadViewHolder;
import com.qcx.mini.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ForceInfoActivity extends BaseActivity {
    private SimpleRecyclerViewAdapter<String> adapter;

    @BindView(R.id.force_info_list)
    RecyclerView rv_list;

    @Override
    public int getLayoutID() {
        return R.layout.activity_force_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("原力明细",true,false);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        adapter=new SimpleRecyclerViewAdapter<>(this, ItemForceInfoDetailViewHolder.class,R.layout.item_force_info_detail);
        adapter.setHeadView(ItemForceInfoHeadViewHolder.class,R.layout.item_force_info_head,null);
        rv_list.setAdapter(adapter);
        adapter.setDatas(getTestData());
    }

    private List<String> getTestData(){
        List<String> datas=new ArrayList<>();
        for(int i=0;i<40;i++){
            datas.add(""+i);
        }
        return datas;
    }
}
