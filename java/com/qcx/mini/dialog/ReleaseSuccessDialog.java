package com.qcx.mini.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.TravelNoneActivity;
import com.qcx.mini.adapter.ItemReleaseLineAdapter;
import com.qcx.mini.adapter.ItemReleaseSuccessGroupAdapter;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.ReleaseResultEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ReleaseSuccessDialog extends BaseDialog {
    private ReleaseResultEntity result;
    private ItemReleaseSuccessGroupAdapter adapter;
    private boolean noToDetail = false;

    @BindView(R.id.dialog_release_success_title1)
    TextView tv_title1;
    @BindView(R.id.dialog_release_success_title2)
    TextView tv_title2;
    @BindView(R.id.dialog_release_success_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.dialog_release_success_left_btn)
    TextView tv_left;
    @BindView(R.id.dialog_release_success_right_btn)
    TextView tv_right;
    @BindView(R.id.dialog_release_success_line)
    View v_line;

    @OnClick(R.id.dialog_release_success_left_btn)
    void left() {
        dismiss();
    }

    @OnClick(R.id.dialog_release_success_right_btn)
    void right() {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isCheck() && !adapter.getItem(i).isJoined()) {
                ids.add(adapter.getItem(i).getGroupId());
            }
        }
        if (ids.size() > 0) {
            joinGrout(ids);
            dismiss();
        } else {
            dismiss();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_release_success;
    }

    @Override
    public int getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void initView(View view) {
        adapter = new ItemReleaseSuccessGroupAdapter(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        tv_right.setText("知道了");
        tv_left.setVisibility(View.GONE);
        tv_title2.setVisibility(View.GONE);
        v_line.setVisibility(View.GONE);
        if (result == null || result.getJoinedGroups() == null || result.getJoinedGroups().size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            tv_title1.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tv_title1.setVisibility(View.VISIBLE);
            adapter.setIs1(false);
            for (int i = 0; i < result.getJoinedGroups().size(); i++) {
                if (!result.getJoinedGroups().get(i).isJoined()) {
                    tv_right.setText("加入");
                    result.getJoinedGroups().get(i).setCheck(true);
                    adapter.setIs1(true);
                    tv_title2.setVisibility(View.VISIBLE);
                    tv_left.setVisibility(View.VISIBLE);
                    v_line.setVisibility(View.VISIBLE);
                }
            }
            if (adapter.isIs1() && result.getJoinedGroups().size() > 3) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
                params.height = UiUtils.getSize(100);
                mRecyclerView.setLayoutParams(params);
            } else if (result.getJoinedGroups().size() > 5) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
                params.height = UiUtils.getSize(100);
                mRecyclerView.setLayoutParams(params);
            }
            adapter.setDatas(result.getJoinedGroups());
        }

    }

    public ReleaseSuccessDialog setResult(ReleaseResultEntity result) {
        this.result = result;
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!noToDetail) {
            long id = -1;
            if (result.getTravelType() == ConstantValue.TravelType.DRIVER && result.getTravelIds() != null && result.getTravelIds().size() > 0) {
                id = result.getTravelIds().get(0);
            } else if (result.getPassengerTravelIds() != null && result.getPassengerTravelIds().size() > 0) {
                id = result.getPassengerTravelIds().get(0);
            }
            if (id > 0) {
                Intent intent = new Intent(getContext(), TravelNoneActivity.class);
                intent.putExtra("travelId", id);
                intent.putExtra("travelType", result.getTravelType());
                getContext().startActivity(intent);
            }
            if(click!=null){
                click.onDismiss();
            }
        }
    }

    private void joinGrout(List<Long> groupIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupIds", groupIds);
        Request.post(URLString.joinGroupByIds, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == 200) {
                }
            }
        });
    }

    OnDialogClick click;

    public ReleaseSuccessDialog setNoToDetail(boolean noToDetail) {
        this.noToDetail = noToDetail;
        return this;
    }

    public ReleaseSuccessDialog setClick(OnDialogClick click) {
        this.click = click;
        return this;
    }

    public interface OnDialogClick {
        void onDismiss();
    }

}
