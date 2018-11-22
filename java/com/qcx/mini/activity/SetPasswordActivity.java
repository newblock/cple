package com.qcx.mini.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.widget.PasswordView;
import com.qcx.mini.widget.numSoftKeyboart.NumSoftKeyboard;
import com.qcx.mini.widget.numSoftKeyboart.OnNumInputListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPasswordActivity extends BaseActivity {
    private StringBuilder password;
    private int passwordLength=6;

    @BindView(R.id.set_password_num_soft_keyboard)
    NumSoftKeyboard mNumSoftKeyboard;
    @BindView(R.id.set_password_password_view)
    PasswordView mPasswordView;
    @BindView(R.id.set_password_submit)
    TextView tv_submit;

    @OnClick(R.id.set_password_submit)
    void setPassword(){
        if(canSubmit){
            Map<String,Object> params=new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("password",password.toString());
            Request.post(URLString.setPassword, params, new QuCallback<IntEntity>() {
                @Override
                public void onSuccess(IntEntity o) {
                    if(o.getStatus()==200){
                        User.getInstance().setPasswordStatus(true);
                        finish();
                    }else if(o.getStatus()==-4){
                        onError("密码格式错误");
                    }else {
                        onError("设置密码失败，请重试");
                    }
                }
            });
        }
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_set_password;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("设置交易密码",true,false);
        mNumSoftKeyboard.setListener(new OnNumInputListener() {
            @Override
            public void onInputNum(int num) {
                if(password==null){
                    password=new StringBuilder();
                }
                if(password.length()<passwordLength){
                    password.append(num);
                    passwordChanged();
                }
            }

            @Override
            public void onDeleteNum() {
                if(password!=null&&password.length()>0){
                    password.deleteCharAt(password.length()-1);
                    passwordChanged();
                }
            }
        });
    }

    private boolean canSubmit=false;
    private void passwordChanged(){
        canSubmit=false;
        if(password!=null){
            mPasswordView.setText(password.toString());
        }

        if(password!=null&&password.length()==passwordLength){
            canSubmit=true;
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
        }else {
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
        }
    }
}
