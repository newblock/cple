package com.qcx.mini.activity;

import android.os.Bundle;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.SwitchView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 群设置
 */
public class GroupSetActivity extends BaseActivity {
    private long groupId;

    @BindView(R.id.group_set_notification_switch)
    SwitchView switchView;

    @OnClick(R.id.group_set_notification_switch)
    void notificationSwitch(){
        ToastUtil.showToast("开关");
    }

    @OnClick(R.id.group_set_out)
    void out(){
        if(groupId!=0){
            Map<String,Object> params=new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("groupId",groupId);
            Request.post(URLString.leaveGroup, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity= (IntEntity) t;
                    if(intEntity.getStatus()==200){
                        ToastUtil.showToast("成功退出");
                        finish();
                    }else {
                        ToastUtil.showToast("操作失败");
                    }
                }
            });
        }else {
            ToastUtil.showToast("操作失败，请退出后重试");
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_group_set;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("更多",true,false);
        groupId=getIntent().getLongExtra("groupId",0);
    }
}
