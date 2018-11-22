package com.qcx.mini.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.Entity;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.test.TestActivity;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RealNameActivity extends BaseActivity {
    public final static int TYPE_NORMAL = 0;//正常进入实名认证
    public final static int TYPE_DRIVER = 1;//车主认证时进入
    public final static int TYPE_PASSWORD = 2;//设置密码时进入
    private int type = TYPE_NORMAL;
    private String name;
    private String idCard;
    private String bizNo;
    @BindView(R.id.real_name_step)
    View v_step;
    @BindView(R.id.real_name_name_edit)
    EditText et_name;
    @BindView(R.id.real_name_id_edit)
    EditText et_id;
    @BindView(R.id.real_name_submit)
    TextView tv_submit;

    @OnClick(R.id.real_name_submit)
    void onSubmit() {
        if (canSubmit) {
            ZM();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!TextUtils.isEmpty(bizNo)){
            checkAuthen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i("onDestroy");
    }

    private void showNotPassToast(){
        ToastUtil.showToast("认证失败，请重试");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_real_name;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", TYPE_NORMAL);
        initUI(type);
        et_id.addTextChangedListener(watcher);
        et_name.addTextChangedListener(watcher);
        if(savedInstanceState!=null){
            idCard=savedInstanceState.getString("IDCard");
            name=savedInstanceState.getString("name");
            bizNo=savedInstanceState.getString("bizNo");
            et_id.setText(idCard);
            et_name.setText(name);
        }
    }

    private void initUI(int type) {
        switch (type) {
            case TYPE_DRIVER:
                v_step.setVisibility(View.VISIBLE);
                tv_submit.setText("下一步");
                initTitleBar("车主认证", true, false);
                break;
            default:
                v_step.setVisibility(View.GONE);
                tv_submit.setText("提交");
                initTitleBar("实名认证", true, false);
                break;
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            textChanged();
        }
    };

    boolean canSubmit = false;

    private void textChanged() {
        if (TextUtils.isEmpty(et_id.getText()) || et_id.getText().length() != 18) {
            canSubmit = false;
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
            return;
        }
        if (TextUtils.isEmpty(et_name.getText())) {
            canSubmit = false;
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
            return;
        }
        canSubmit = true;
        tv_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
    }

    private void checkAuthen(){
        String regex = "^[1-9]\\d{5}(18|19|([2]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        if(TextUtils.isEmpty(bizNo)||TextUtils.isEmpty(idCard)||TextUtils.isEmpty(name)){
            LogUtil.i(""+TextUtils.isEmpty(bizNo)+TextUtils.isEmpty(idCard)+TextUtils.isEmpty(name));
            showNotPassToast();
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("bizNo",bizNo);
        params.put("IDcard",idCard);
        params.put("name",name);
        Request.post(URLString.authenCheck, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    JSONObject object=new JSONObject(response.body()).getJSONObject("checkResult");
                    if(object.getBoolean("pass")){
                        ToastUtil.showToast("认证成功");
                        if(type==TYPE_DRIVER){
                            startActivity(new Intent(RealNameActivity.this, AuthenticationActivity.class));
                        }else if(type==TYPE_PASSWORD){
                            Intent intent=new Intent(RealNameActivity.this, RealNameInfoActivity.class);
                            intent.putExtra("type",TYPE_PASSWORD);
                            startActivity(intent);
                        }
                        User.getInstance().setAuthenStatus(true);
                        finish();
                    }else {
                        showNotPassToast();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showNotPassToast();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("IDCard",idCard);
        outState.putString("name",name);
        outState.putString("bizNo",bizNo);
    }

    private void ZM() {
        Editable name = et_name.getText();
        Editable id = et_id.getText();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("IDcard", id.toString());
        params.put("name", name.toString());
        idCard=id.toString();
        this.name=name.toString();
//        params.put("phone",User.getInstance().getPhoneNumber());

        Request.post(URLString.authen, params, new QuCallback<ZM>() {
            @Override
            public void onSuccess(ZM o) {
                switch (o.getStatus()){
                    case 200:
                        doVerify(o.getRealCheck().getUrl());
                        bizNo=o.getRealCheck().getBizNo();
                        break;
                    case -107:
                        ToastUtil.showToast("已经实名认证过了");
                        break;
                    case -108:
                        ToastUtil.showToast("该身份证号已经被使用");
                        break;
                    case -380:
                        ToastUtil.showToast("身份证号错误");
                        break;
                }
            }
        });
    }

    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            // 这里使用固定appid 20000067
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            // 处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

    private String getParams() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                LogUtil.i("dddddddd ", URLDecoder.decode(uri.getQueryParameter("biz_content"),"UTF-8"));
                LogUtil.i("dddddddd ", URLDecoder.decode(uri.getQueryParameter("sign"),"UTF-8"));

                String json=URLDecoder.decode(uri.getQueryParameter("biz_content"),"UTF-8");
                JSONObject object=new JSONObject(json);
                return object.getString("biz_no");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class ZM extends Entity {
        private int status;
        private ZiMa realCheck;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public ZiMa getRealCheck() {
            return realCheck;
        }

        public void setRealCheck(ZiMa realCheck) {
            this.realCheck = realCheck;
        }

        public class ZiMa {
            private String bizNo;
            private String url;

            public String getBizNo() {
                return bizNo;
            }

            public void setBizNo(String bizNo) {
                this.bizNo = bizNo;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
