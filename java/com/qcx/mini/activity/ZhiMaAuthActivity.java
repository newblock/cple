package com.qcx.mini.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.ZiMaEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.H5PageUtil;
import com.qcx.mini.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ZhiMaAuthActivity extends BaseActivity {
    @BindView(R.id.zhima_name)
    EditText tv_name;
    @BindView(R.id.zhima_id_card)
    EditText tv_idCard;
    @BindView(R.id.zhima_submit)
    Button btn_submit;

    @OnClick(R.id.zhima_submit)
    void submit(){
        String name = tv_name.getText().toString();
        String ids = tv_idCard.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入您的真实姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ids)) {
            Toast.makeText(this, "请输入您的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ids.length() == 15 || ids.length() == 18) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("name", name);
            params.put("IDcard", ids);
            Request.post(URLString.zmxy, params, new EntityCallback(ZiMaEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    ZiMaEntity intEntity= (ZiMaEntity) t;
                    if(intEntity.getStatus()==200){
                        EventBus.getDefault().post(EventStatus.ZMXY_AUTH_SUCCESS);
                        H5PageUtil.toZiMAPage(intEntity.getZmxy_url(),ZhiMaAuthActivity.this);
                        finish();
                    }else if(intEntity.getStatus()==-2){
                        ToastUtil.showToast("姓名与身份证号不符，请检查是否输入正确，或该身份是否在支付宝实名认证");
                    }else if(intEntity.getStatus()==-130){
                        ToastUtil.showToast("已绑定其他手机号");
                    }else {
                        ToastUtil.showToast("认证失败"+intEntity.getStatus());
                    }
                }
            });

        } else {
            Toast.makeText(this, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_zhi_ma_auth;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("芝麻信用",true,false);
    }
}
