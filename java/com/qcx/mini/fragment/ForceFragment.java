package com.qcx.mini.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.qcx.mini.R;
import com.qcx.mini.activity.ForceInfoActivity;
import com.qcx.mini.activity.InviteActivity;
import com.qcx.mini.activity.QCInfoActivity;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemForceTask;
import com.qcx.mini.adapter.viewHolder.ItemForceTaskBase;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/8/22.
 * 探索页
 */

public class ForceFragment extends BaseFragment{
    @BindView(R.id.fragment_force_base_task_list)
    RecyclerView rv_baseTask;
    @BindView(R.id.fragment_force_task_list)
    RecyclerView rv_task;

    SimpleRecyclerViewAdapter<String> taskAdapterBase;
    SimpleRecyclerViewAdapter<String> taskAdapter;

    @OnClick(R.id.fragment_force_force_view)
    void force(){
        startActivity(new Intent(getContext(), ForceInfoActivity.class));
    }
    @OnClick(R.id.fragment_force_ranking_list)
    void rank(){
        ToastUtil.showToast("排行榜");
    }
    @OnClick(R.id.fragment_force_invite)
    void invite(){
        startActivity(new Intent(getContext(), InviteActivity.class));
    }
    @OnClick(R.id.fragment_force_cple_view)
    void cple(){
        startActivity(new Intent(getContext(), QCInfoActivity.class));

    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

        LinearLayoutManager hManger=new LinearLayoutManager(getContext());
        hManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_baseTask.setLayoutManager(hManger);
        taskAdapterBase=new SimpleRecyclerViewAdapter<>(getContext(), ItemForceTaskBase.class,R.layout.item_force_task_base);
        taskAdapterBase.setDatas(getTestData(10));
        rv_baseTask.setAdapter(taskAdapterBase);
        rv_baseTask.addItemDecoration(new BaseTaskItemDecoration());

        LinearLayoutManager vManger=new LinearLayoutManager(getContext());
        vManger.setOrientation(LinearLayoutManager.VERTICAL);
        rv_task.setLayoutManager(vManger);
        taskAdapter=new SimpleRecyclerViewAdapter<>(getContext(), ItemForceTask.class,R.layout.item_force_task);
        taskAdapter.setDatas(getTestData(20));
        rv_task.setAdapter(taskAdapter);
        rv_baseTask.setFocusable(false);
        rv_task.setFocusable(false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_force;
    }

    private List<String> getTestData(int size){
        List<String> data=new ArrayList<>();
        for(int i=0;i<size;i++){
            data.add(i+"");
        }
        return data;
    }

    private class BaseTaskItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position=parent.getChildAdapterPosition(view);
            if(position==0){
                outRect.left= UiUtils.getSize(20);
            }
            outRect.right=UiUtils.getSize(12);
        }
    }
}
