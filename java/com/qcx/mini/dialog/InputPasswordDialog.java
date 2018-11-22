package com.qcx.mini.dialog;

import android.app.Dialog;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.PasswordView;
import com.qcx.mini.widget.numSoftKeyboart.NumSoftKeyboard;
import com.qcx.mini.widget.numSoftKeyboart.OnNumInputListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/31.
 * 输入交易密码
 */

public class InputPasswordDialog extends BaseDialog {
    StringBuilder password;
    private OnPasswordInputListener listener;
    private String title;
    private double price;
    private String describe;

    @BindView(R.id.dialog_input_password_title)
    TextView tv_title;
    @BindView(R.id.dialog_input_password_num)
    TextView tv_price;
    @BindView(R.id.dialog_input_password_describe)
    TextView tv_describe;

    @BindView(R.id.dialog_input_password_input)
    NumSoftKeyboard mNumSoftKeyboart;
    @BindView(R.id.dialog_input_password_view)
    PasswordView mPasswordView;
    @OnClick(R.id.dialog_input_password_close)
    void close(){
        dismiss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_input_password;
    }

    @Override
    public void initView(View view) {
        mNumSoftKeyboart.setListener(new OnNumInputListener() {
            @Override
            public void onInputNum(int num) {
                if (password == null) {
                    password = new StringBuilder();
                }
                if (password.length() < 6) {
                    password.append(num);
                }
                if(password.length()==6&&listener!=null){
                    listener.inputFinish(password.toString(),InputPasswordDialog.this);
                }
                passwordChanged();
            }

            @Override
            public void onDeleteNum() {
                if (password != null && password.length() > 0) {
                    password.deleteCharAt(password.length() - 1);
                    passwordChanged();
                }
            }
        });
        password=new StringBuilder();
        passwordChanged();
        tv_title.setText(title);
        tv_price.setText(String.valueOf(price));
        tv_describe.setText(describe);
    }

    private void passwordChanged() {
        if(password!=null){
            mPasswordView.setText(password.toString());
        }
    }

    public InputPasswordDialog setListener(OnPasswordInputListener listener) {
        this.listener = listener;
        return this;
    }

    public InputPasswordDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public InputPasswordDialog setPrice(double price) {
        this.price = price;
        return this;
    }

    public InputPasswordDialog setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    @Override
    protected void initDialog(Dialog dialog) {
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    public interface OnPasswordInputListener{
        void inputFinish(String password,InputPasswordDialog dialog);
    }

}
