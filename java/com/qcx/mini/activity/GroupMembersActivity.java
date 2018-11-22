package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemGroupMemberAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.GroupMembersEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.widget.QuRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 群成员
 */
public class GroupMembersActivity extends BaseActivity {
    private ItemGroupMemberAdapter adapter;
    private long groupId;
    private int pageNum=1;

    @BindView(R.id.group_members_list)
    RecyclerView rv_list;
    @BindView(R.id.group_members_refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    public int getLayoutID() {
        return R.layout.activity_group_members;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("群成员",true,false);
        groupId=getIntent().getLongExtra("groupId",0);
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setRefreshHeader(new QuRefreshHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getGroupMembers(1);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getGroupMembers(pageNum);
            }
        });
        final LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        adapter=new ItemGroupMemberAdapter(this);
        rv_list.setAdapter(adapter);
        adapter.setListener(new ItemGroupMemberAdapter.ItemMemberClickListener() {
            @Override
            public void onMemberClick(long phone) {
                Intent intent=new Intent(GroupMembersActivity.this,UserInfoActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }

            @Override
            public void onAttentionClick(GroupMembersEntity.GroupMember member, ImageView attentionImageView) {
                if (member.isAttention()) {
                    cancelAttention(member, attentionImageView);
                } else {
                    attention(member, attentionImageView);
                }
            }

            @Override
            public void onIconClick(long phone) {
                Intent intent=new Intent(GroupMembersActivity.this,UserInfoActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
        getGroupMembers(1);
    }

    private void getGroupMembers(final int page){
        if(page==1){
            pageNum=1;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId", groupId);
        params.put("pageNo", page);
        Request.post(URLString.groupMembers, params, new EntityCallback(GroupMembersEntity.class) {
            @Override
            public void onSuccess(Object t) {
                GroupMembersEntity groupMembersEntity= (GroupMembersEntity) t;
                List<GroupMembersEntity.GroupMember> members=groupMembersEntity.getMembers();
                for(int i=0;i<members.size();i++){
                    members.get(i).setRanking(i+1);
                }
                if(page==1){
                    adapter.setDatas(members);
                }else {
                    adapter.addDatas(members);
                }
                if(members.size()>0){
                    pageNum++;
                }
                finishLoading();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });
    }

    private void finishLoading(){
        if(refreshLayout!=null){
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }

    private void attention(final GroupMembersEntity.GroupMember data, final ImageView iv_attention) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());//attentione
        params.put("attentione", data.getPhone());
        Request.post(URLString.changAttention, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(true);
                    iv_attention.setImageResource(R.mipmap.btn_followed_nomal);
                    new CenterImgDialog().show(getSupportFragmentManager(), "");
                } else {
                    onError("操作失败");
                }
            }

        });
    }

    private void cancelAttention(final  GroupMembersEntity.GroupMember data, final ImageView iv_attention) {
        DialogUtil.unfollowDialog(this, data.getPhone(), new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(false);
                    iv_attention.setImageResource(R.mipmap.btn_follow_nomal);
                } else {
                    onError("操作失败");
                }
            }
        });
    }

}
