package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RealNameInfoActivity extends BaseActivity {
    public final static int TYPE_PASSWORD=2;
    private int type;
    @BindView(R.id.real_name_info_name)
    TextView tv_name;
    @BindView(R.id.real_name_info_id_card)
    TextView tv_IDCard;

    @BindView(R.id.real_name_info_next)
    TextView tv_next;

    @OnClick(R.id.real_name_info_next)
    void next(){
        if(type==TYPE_PASSWORD){
            startActivity(new Intent(this,VerifyPhoneNumActivity.class));
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_real_name_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("认证信息",true,false);
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.selectAuthen, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    JSONObject object=new JSONObject(response.body());
                    if(200==object.getInt("status")){
                        JSONObject data=object.getJSONObject("authenInfo");
                        tv_name.setText(data.getString("name"));
                        tv_IDCard.setText(data.getString("IDcard"));
                    }else {
                        ToastUtil.showToast("获取数据失败");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    ToastUtil.showToast("获取数据失败");
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtil.showToast("获取数据失败,请检查您的网络");
            }
        });
        type=getIntent().getIntExtra("type",0);
        if(type==2){
            tv_next.setVisibility(View.VISIBLE);
        }else {
            tv_next.setVisibility(View.GONE);
        }
    }
}
