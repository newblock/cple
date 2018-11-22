package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.qcx.mini.ConstantString;
import com.qcx.mini.MainClass;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.LoginEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.NetUtil;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.EditTextUtil;
import com.qcx.mini.utils.H5PageUtil;
import com.qcx.mini.utils.ImageCompress;
import com.qcx.mini.utils.KeybordS;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.PictureUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.verify.VerifyPhoneUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private static final String ICON_FILE_NAME = ConstantString.FilePath.getImageFilePath() + File.separator + "icon.jpg";
    private static final String CAMERA_NAME = ConstantString.FilePath.getImageFilePath() + File.separator + "cameraName.jpg";
    RxPermissions rxPermissions;
    boolean isImage;
    int step;
    boolean isNext = false;
    private EditText et_input;
    private TextView tv_tsw, tv_tsh, tv_step;
    ImageView iv_next, iv_back;
    private TextView tv_title;

    View v_step1, v_step2;
    ImageView iv_icon;
    TextView tv_sex1, tv_sex2;
    EditText et_name;

    String phone;
    String verifyNum;
    int verifyTime = 60;
    Handler verifyHandler = new Handler();

    int sex;//
    String name;
    String iconFileName;

    private String token;
    private String rongToken;

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (User.getInstance().isLogin()) {
            JPushInterface.setAlias(MainClass.getInstance(),1,String.valueOf(User.getInstance().getPhoneNumber()));
            logined();
        } else {
            init();
        }
    }

    @Override
    public boolean setStatusBar() {
        UiUtils.setStatusBarTransparent(this);
        return true;
    }

    private void init() {
        step = 0;
        v_step1 = findViewById(R.id.login_step1_viw);
        v_step2 = findViewById(R.id.login_step2_viw);

        tv_title = findViewById(R.id.login_title);
        et_input = findViewById(R.id.login_input);
        tv_tsw = findViewById(R.id.login_tsw);
        tv_tsh = findViewById(R.id.login_tsh);
        tv_step = findViewById(R.id.login_step);

        iv_next = findViewById(R.id.login_next);
        iv_back = findViewById(R.id.login_back);

        tv_sex1 = findViewById(R.id.login_sex1);
        tv_sex2 = findViewById(R.id.login_sex2);
        et_name = findViewById(R.id.login_input_name);
        iv_icon = findViewById(R.id.login_icon);


        iv_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        et_input.addTextChangedListener(this);

        tv_sex1.setOnClickListener(this);
        tv_sex2.setOnClickListener(this);
        et_name.addTextChangedListener(this);
        iv_icon.setOnClickListener(this);

        EditTextUtil.setEditTextInputSpace(et_input);
        EditTextUtil.setEditTextInputSpace(et_name);

        initUI(0);
    }

    private void initUI(int step) {
        LogUtil.i("step=" + step + " phone=" + phone + " ver=" + verifyNum + " name=" + name);
        canSendCaptcha=false;
        tv_tsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSendCaptcha){
                    verifyTime=60;
                    sendCaptcha();
                }
            }
        });
        if (step == 0) {
            verifyTime = 60;
            verifyHandler.removeCallbacks(verifyTimeRunnable);
            v_step1.setVisibility(View.VISIBLE);
            v_step2.setVisibility(View.GONE);
            iv_back.setVisibility(View.INVISIBLE);
            tv_title.setText("欢迎回来");
            tv_tsw.setText("继续表示您同意");
            tv_tsh.setText("趣出行拼车用户协议");
            tv_tsh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    H5PageUtil.toClausePage(LoginActivity.this);
                }
            });
            et_input.setText(phone);
            et_input.requestFocus();
            et_input.setHint("请输入手机号");
            tv_step.setText("1 of 3");
            if (!TextUtils.isEmpty(phone)) {
                et_input.setSelection(phone.length());
            }

            if (VerifyPhoneUtil.isPhoneNum(phone)) {
                iv_next.setImageResource(R.mipmap.btn_login_next);
                isNext = true;
            } else {
                iv_next.setImageResource(R.mipmap.btn_login_next_off);
                isNext = false;
            }
        }
        if (step == 1) {
            v_step1.setVisibility(View.VISIBLE);
            v_step2.setVisibility(View.GONE);
            iv_back.setVisibility(View.VISIBLE);
            tv_title.setText("请输入".concat(phone).concat("收到的验证码"));
//            tv_tsw.setText("倒计时");
            tv_tsh.setText("");
            et_input.setText("");
            et_input.setHint("请输入验证码");
            et_input.requestFocus();
            iv_next.setImageResource(R.mipmap.btn_login_confirm_off);
            isNext = false;
            tv_step.setText("1 of 3");
        }
        if (step == 2) {
            v_step1.setVisibility(View.GONE);
            v_step2.setVisibility(View.VISIBLE);
            sex = 2;
            name = "";
            iconFileName = "";
            isUpLoadImage = false;

            et_name.setText(name);
            et_name.requestFocus();
            iv_next.setImageResource(R.mipmap.btn_login_confirm_off);
            tv_sex1.setBackground(getResources().getDrawable(R.drawable.bg_circular_white1));
            tv_sex2.setBackgroundColor(Color.TRANSPARENT);
            tv_sex1.setTextColor(0xFF699FF1);
            tv_sex2.setTextColor(0xFFFFFFFF);
            isNext = false;
            tv_step.setText("2 of 3");
        }
    }

    private boolean isSending=false;
    void sendCaptcha() {
        if(isSending){
            ToastUtil.showToast("您手速太快啦！");
            return;
        }
        isSending=true;
        NetUtil.sendCaptcha(Long.parseLong(phone), new QuCallback<IntEntity>() {
            @Override
            public void onSuccess(IntEntity intEntity) {

                isSending=false;
                IntEntity d = intEntity;
                if (d.getStatus() == 200) {
                    step = 1;
                    initUI(step);
                    isNext = false;
                    verifyHandler.post(verifyTimeRunnable);
                } else {
                    onError("验证码发送失败");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                isSending=false;
            }
        });
    }


    void next() {
        if (step == 0) {
            if (isNext) {
                sendCaptcha();
            } else {
                ToastUtil.showToast("请输入手机号");
            }
        } else if (step == 1) {
            if (isNext) {
                login();
            } else {
                ToastUtil.showToast("请输入验证码");
            }
        } else if (step == 2) {
            if (isNext) {
//                if (isUpLoadImage) {
                changeInfo();
//                } else {
//                    ToastUtil.showToast("您还没有上传图片");
//                }
            } else {
                ToastUtil.showToast("请输入昵称");
            }
        }
    }

    private void login() {
        showLoadingDialog();
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("captcha", verifyNum);
        Request.post(URLString.login, params, new EntityCallback(LoginEntity.class) {
            @Override
            public void onSuccess(Object t) {
                LoginEntity login = (LoginEntity) t;
                if (login.getStatus() == 200) {
//                    if (login.isLogin()) {//
                        logined();
                        login.setPhone(Long.parseLong(phone));
                        User.getInstance().logIn(login);
//                    } else {
//                        step = 2;
//                        initUI(step);
//                        token = login.getToken();
//                        rongToken = login.getRongToken();
//                    }
                    hideLoadingDialog();
                } else {
                    onError("验证码有误");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
            }
        });
    }

    private void logined() {
        if (et_input != null) KeybordS.closeKeybord(et_input, LoginActivity.this);
        if (!haveMainAcitvity()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    boolean canSendCaptcha=false;
    Runnable verifyTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (verifyTime > 0) {
                tv_tsw.setText(verifyTime + "s");
                tv_tsh.setText("");
                verifyTime--;
                verifyHandler.postDelayed(verifyTimeRunnable, 1000);
                canSendCaptcha=false;
            } else {
                tv_tsw.setText("");
                tv_tsh.setText("重新获取验证码");
                canSendCaptcha=true;
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
//        if(et_input!=null){
//            et_input.requestFocus();
//            KeybordS.openKeybord(et_input, this);
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(et_input!=null){
            KeybordS.closeKeybord(et_input, this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (step != 0) {
                step--;
                initUI(step);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_next:
                next();
                break;
            case R.id.login_back:
                step--;
                initUI(step);
                break;
            case R.id.login_sex1:
                tv_sex1.setBackground(getResources().getDrawable(R.drawable.bg_circular_white1));
                tv_sex2.setBackgroundColor(Color.TRANSPARENT);
                tv_sex1.setTextColor(0xFF699FF1);
                tv_sex2.setTextColor(0xFFFFFFFF);
                sex = 2;
                break;
            case R.id.login_sex2:
                tv_sex1.setBackgroundColor(Color.TRANSPARENT);
                tv_sex2.setBackground(getResources().getDrawable(R.drawable.bg_circular_white1));
                tv_sex1.setTextColor(0xFFFFFFFF);
                tv_sex2.setTextColor(0xFF699FF1);
                sex = 1;
                break;
            case R.id.login_icon:

                File file = new File(ICON_FILE_NAME);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                DialogUtil.pictureDialog(this, CAMERA_NAME);
//                secletImage();
                KeybordS.closeKeybord(et_name, this);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (step == 0) {
            phone = s.toString();
            if (VerifyPhoneUtil.isPhoneNum(s.toString())) {//验证手机号
                isNext = true;
                iv_next.setImageResource(R.mipmap.btn_login_next);
            } else {
                isNext = false;
                iv_next.setImageResource(R.mipmap.btn_login_next_off);
            }
        }
        if (step == 1) {
            verifyNum = s.toString();
            if (s.length() == 4 || s.length() == 6 || s.length() == 8) {
                isNext = true;
                iv_next.setImageResource(R.mipmap.btn_login_confirm);
            } else {
                isNext = false;
                iv_next.setImageResource(R.mipmap.btn_login_confirm_off);
            }

        }
        if (step == 2) {
            name = s.toString();
            if (!TextUtils.isEmpty(name)) {
                isNext = true;
                iv_next.setImageResource(R.mipmap.btn_login_confirm);
            } else {
                isNext = false;
                iv_next.setImageResource(R.mipmap.btn_login_confirm_off);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (verifyHandler != null) verifyHandler.removeCallbacks(verifyTimeRunnable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.i("requestCode=" + requestCode + "  resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureUtil.REQUEST_CODE_PHOTO:
                    if (data != null) {
                        Uri uri = data.getData();
                        ImageCompress.cropImg(this, 31, uri, ICON_FILE_NAME);
                    }
                    break;
                case PictureUtil.REQUEST_CODE_CAMERA:
                    ImageCompress.cropImg(this, 31, PictureUtil.getImageContentUri(new File(CAMERA_NAME), this), ICON_FILE_NAME);
                    break;
                case 31:
                    if (data != null) {
                        File img = new File(ICON_FILE_NAME);
                        Picasso.with(this)
                                .load(img)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(iv_icon);
                        isImage = true;
                    }
                    break;
                case 30:
                    logined();
                    break;
            }
        }
    }

    boolean isUpLoadImage = false;

    private void changeInfo() {
        if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("请重新登录");
            return;
        }
        if (!isImage) {
            ToastUtil.showToast("请选择头像");
            return;
        }
        File file = new File(ICON_FILE_NAME);
        if (!file.exists()) {
            ToastUtil.showToast("请重新选择头像");
            return;
        }
        showLoadingDialog();
        OkGo.<String>post(URLString.updateLoginInfo)
                .isMultipart(true)
                .params("token", token)
                .params("nickName", name)
                .params("sex", sex)
                .params("picture", file)
                .execute(new EntityCallback(IntEntity.class) {
                    @Override
                    public void onSuccess(Object t) {
                        hideLoadingDialog();
                        IntEntity intEntity = (IntEntity) t;
                        if (intEntity.getStatus() == 200) {
                            Intent intent = new Intent(LoginActivity.this, SetHomeAndCompanyActivity.class);
                            intent.putExtra("token", token);
                            intent.putExtra("rongToken", rongToken);
                            intent.putExtra("phone", Long.parseLong(phone));
                            startActivityForResult(intent, 30);
                            KeybordS.closeKeybord(et_name, LoginActivity.this);
                        } else {
                            onError("资料上传失败");
                        }
                    }

                    @Override
                    public void onError(String errorInfo) {
                        hideLoadingDialog();
                        if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
                    }
                });
    }

}
