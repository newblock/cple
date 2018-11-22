package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemRecommendGroupMemberAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.RecommendGroupMembersEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.QuRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加群成员
 */
public class AddGroupMembersActivity extends BaseActivity {

    private ItemRecommendGroupMemberAdapter adapter;
    private long groupId;
    private int groupType;
    private int pageNum = 1;

    @BindView(R.id.add_group_noData)
    View v_noData;
    @BindView(R.id.add_group_members_list)
    RecyclerView rv_list;
    @BindView(R.id.add_group_members_refreshLayout)
    SmartRefreshLayout refreshLayout;

    @OnClick(R.id.add_group_members_submit)
    void submit() {
        joinGroup();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_add_group_members;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        initTitleBar("群成员", true, false);
        groupId = getIntent().getLongExtra("groupId", 0);
        groupType = getIntent().getIntExtra("groupType", 0);
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setRefreshHeader(new QuRefreshHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMembers(1);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getMembers(pageNum);
            }
        });
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        adapter = new ItemRecommendGroupMemberAdapter(this);
        adapter.setListener(new ItemRecommendGroupMemberAdapter.OnItemRecommendGroupMemberClickListener() {
            @Override
            public void onItemClick(RecommendGroupMembersEntity.Member member, ImageView iv_selected) {
                member.setSelected(!member.isSelected());
                if (member.isSelected()) {
                    iv_selected.setImageResource(R.mipmap.icon_check);
                } else {
                    iv_selected.setImageResource(R.mipmap.icon_uncheck);
                }
            }
        });
        rv_list.setAdapter(adapter);
        getMembers(1);
    }


    private void getMembers(final int page) {
        if (page == 1) {
            pageNum = 1;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId", groupId);
        params.put("pageNo", page);
        showLoadingDialog();
        Request.post(URLString.recommendGroupMembers, params, new EntityCallback(RecommendGroupMembersEntity.class) {
            @Override
            public void onSuccess(Object t) {
                RecommendGroupMembersEntity groupMembersEntity = (RecommendGroupMembersEntity) t;
                List<RecommendGroupMembersEntity.Member> members = groupMembersEntity.getMatchUsers();
                for (int i = 0; i < members.size(); i++) {
                    members.get(i).setSelected(true);
                }
                if (page == 1) {
                    adapter.setDatas(members);
                } else {
                    adapter.addDatas(members);
                }
                if (members.size() > 0) {
                    pageNum++;
                }
                finishLoading();
                showData();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
                showData();
            }
        });
    }

    private void finishLoading() {
        hideLoadingDialog();
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }

    public void joinGroup() {
        List<Long> phones = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isSelected()) {
                phones.add(adapter.getItem(i).getPhone());
            }
        }
        if (phones.size() < 1) {
            ToastUtil.showToast("至少选择一个人");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId", new long[]{groupId});
        params.put("phones", phones);
        showLoadingDialog();
        Request.post(URLString.joinGroup, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == 200) {
                    ToastUtil.showToast("添加成功");
                    showGroupeInfo();
                    finish();
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
            }
        });
    }

    private void showGroupeInfo() {
        Intent intent = new Intent(this, WayInfoActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("groupType", groupType);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            showGroupeInfo();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showData(){
        if(adapter==null||adapter.getItemCount()==0){
            v_noData.setVisibility(View.VISIBLE);
        }else {
            v_noData.setVisibility(View.GONE);
        }
    }
}
