package com.qcx.mini.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.utils.QRCodeUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteActivity extends BaseActivity {
    String code;
    @BindView(R.id.invite_qr_code)
    ImageView iv_qrCode;
    @BindView(R.id.invite_code_text)
    TextView tv_code;

    @OnClick(R.id.invite_type_url)
    void typeUrl(){
        ToastUtil.showToast("连接邀请");
    }

    @OnClick(R.id.invite_type_text)
    void typeText(){
        ToastUtil.showToast("文字邀请");
    }

    @Override
    public void onTitleRightClick(View v) {
        ToastUtil.showToast("分享");
    }

    @OnClick(R.id.invite_copy)
    void copyCode(){
        code=User.getInstance().getInviteCode();
        if(TextUtils.isEmpty(code)){
            ToastUtil.showToast("无效的邀请码");
        }else {
            ClipboardManager manager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData data=ClipData.newPlainText("123",code);
            if(manager!=null){
                manager.setPrimaryClip(data);
                ToastUtil.showToast("已复制，粘贴给好友吧");
            }
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_invite;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("邀请好友");
        iv_qrCode.setImageBitmap(QRCodeUtil.createQRCodeBitmap("这是一个邀请二维码1234567890_1234567890_1234567890_1234567890_1234567890_1234567890_1234567890_1234567890", UiUtils.getSize(186),UiUtils.getSize(186)));
        tv_code.setText(User.getInstance().getInviteCode());

    }
}
