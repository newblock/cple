package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.FollowerListAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.AttentionEntity;
import com.qcx.mini.entity.FollowerEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 关注、粉丝页
 */
public class FollowerListActivity extends BaseActivity {
    public final static int TYPE_ATTENTION = 1;
    public final static int TYPE_FOLLOWER = 2;

    private long phone;
    private FollowerListAdapter adapter;
    @BindView(R.id.follower_list)
    RecyclerView mRecyclerView;

    @Override
    public int getLayoutID() {
        return R.layout.activity_follower_list;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String title = getIntent().getStringExtra("title");
        int type = getIntent().getIntExtra("type", 0);
        phone = getIntent().getLongExtra("phone",0);
        initTitleBar(title, true, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FollowerListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(new FollowerListAdapter.FollowerOnClickListener() {
            @Override
            public void onAttentionBtnClick(boolean isAttention, long phone, final ImageView iv_status, final FollowerEntity.Follower follower) {
                Map<String, Object> params = new HashMap<>();
                params.put("token", User.getInstance().getToken());
                if (!isAttention) {
                    params.put("attentione", phone);
                    Request.post(URLString.changAttention, params, new EntityCallback(AttentionEntity.class) {
                        @Override
                        public void onSuccess(Object t) {
                            AttentionEntity entity = (AttentionEntity) t;
                            if (entity.getStatus() == 200) {
                                switch (entity.getIsAttention()) {
                                    case 0:
                                        iv_status.setImageResource(R.mipmap.btn_follow);
                                        break;
                                    case 1:
                                        iv_status.setImageResource(R.mipmap.btn_followed);
                                        break;
                                    case 2:
                                        iv_status.setImageResource(R.mipmap.btn_followed_each);
                                        break;
                                    default:
                                        iv_status.setVisibility(View.GONE);
                                        break;
                                }
                                new CenterImgDialog().setImg(CenterImgDialog.CenterImg.ATTENTION)
                                        .show(getSupportFragmentManager(), "");
                                follower.setAttentionStatus(entity.getIsAttention());
                            } else {
                                onError("操作失败");
                            }
                        }
                    });
                } else {
                    DialogUtil.unfollowDialog(FollowerListActivity.this, phone, new EntityCallback(IntEntity.class) {
                        @Override
                        public void onSuccess(Object t) {
                            IntEntity entity = (IntEntity) t;
                            if (entity.getStatus() == 200) {
                                iv_status.setImageResource(R.mipmap.btn_follow);
                                follower.setAttentionStatus(0);
                            } else {
                                onError("操作失败");
                            }
                        }
                    });
                }
            }

            @Override
            public void onIconClick(long phone) {

                Intent intent=new Intent(FollowerListActivity.this, UserInfoActivity.class);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
        getData(type);
    }

    private void getData(int type) {
        if (type == TYPE_ATTENTION) {
            getAttentionData();
        } else if (type == TYPE_FOLLOWER) {
            getFollowerData();
        }
    }

    private void getAttentionData() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("otherPhone", phone);
        Request.post(URLString.attentionList, params, callback);
    }

    private void getFollowerData() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("otherPhone", phone);
        Request.post(URLString.fansList, params, callback);
    }

    EntityCallback callback = new EntityCallback(FollowerEntity.class) {
        @Override
        public void onSuccess(Object t) {
            FollowerEntity entity = (FollowerEntity) t;
            adapter.setDatas(entity.getList());
        }
    };
}
