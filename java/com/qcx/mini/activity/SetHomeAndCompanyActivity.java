package com.qcx.mini.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.HomeAndCommpanyEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SetHomeAndCompanyActivity extends BaseActivity {

    boolean isNext;
    private Tip home;
    private Tip company;

    private String token;

    @BindView(R.id.set_home_address_text1)
    TextView tv_home1;
    @BindView(R.id.set_home_address_text2)
    TextView tv_home2;
    @BindView(R.id.set_company_address_text1)
    TextView tv_company1;
    @BindView(R.id.set_company_address_text2)
    TextView tv_company2;
    @BindView(R.id.set_address_next)
    ImageView iv_next;


    @OnClick(R.id.set_address_back)
    void back() {
        finish();
    }

    @OnClick(R.id.set_home_address)
    void setHomeAddress() {
        Intent intent = new Intent(this, SetAddressActivity.class);
        intent.putExtra("inputHint","请输入家庭地址");
        intent.putExtra("home_company_view", false);
        startActivityForResult(intent, 11);
    }

    @OnClick(R.id.set_company_address)
    void setCompanyAddress() {
        Intent intent = new Intent(this, SetAddressActivity.class);
        intent.putExtra("inputHint","请输入公司地址");
        intent.putExtra("home_company_view", false);
        startActivityForResult(intent, 12);
    }


    @OnClick(R.id.set_address_next)
    void next() {
        if (User.getInstance().isLogin()) {
            token = User.getInstance().getToken();
        }

        if (TextUtils.isEmpty(token)) {
            ToastUtil.showToast("获取登录信息失败，请重新登录");
            return;
        }
        if (isNext) {
            if (home == null) {
                ToastUtil.showToast("请重新选择家庭地址");
                return;
            }
            if (company == null) {
                ToastUtil.showToast("请重新选择公司地址");
                return;
            }

            final Map<String, Object> params = new HashMap<>();
            params.put("token", token);
            params.put("addr_home", home.getName());
            params.put("addr_company", company.getName());
            params.put("location_home", new double[]{home.getPoint().getLongitude(), home.getPoint().getLatitude()});
            params.put("location_company", new double[]{company.getPoint().getLongitude(), company.getPoint().getLatitude()});
            Request.post(URLString.addressChange, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity entity = (IntEntity) t;
                    if (entity.getStatus() == 200) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtil.showToast("上传信息失败,请重新上传");
                    }
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                }
            });


        } else {
            ToastUtil.showToast("请重新选择地址信息");
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_set_home_and_company;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (User.getInstance().isLogin()) {
            getAddress();
            showLoadingDialog();
        }
        initView();
    }

    private void initView() {
        if (home != null) {
            tv_home1.setVisibility(View.VISIBLE);
            tv_home2.setText(home.getName());
        } else {
            tv_home1.setVisibility(View.GONE);
            tv_home2.setText("家庭地址");
        }
        if (company != null) {
            tv_company1.setVisibility(View.VISIBLE);
            tv_company2.setText(company.getName());
        } else {
            tv_company1.setVisibility(View.GONE);
            tv_company2.setText("公司地址");
        }

        if (home != null && company != null) {
            isNext = true;
            iv_next.setImageResource(R.mipmap.btn_blue_confirm);
        } else {
            isNext = false;
            iv_next.setImageResource(R.mipmap.btn_blue_confirm_off);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    home = data.getParcelableExtra("tip");
                    if (home != null) {
                        tv_home1.setVisibility(View.VISIBLE);
                        tv_home2.setText(home.getName());
                    }
                    break;
                case 12:
                    company = data.getParcelableExtra("tip");
                    if (company != null) {
                        tv_company1.setVisibility(View.VISIBLE);
                        tv_company2.setText(company.getName());
                    }
                    break;
            }
            initView();
        }
    }

    private void getAddress() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.homeAndCompanyAddress, params, new EntityCallback(HomeAndCommpanyEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                HomeAndCommpanyEntity homeAndCommpanyEntity = (HomeAndCommpanyEntity) t;
                HomeAndCommpanyEntity.Address address = homeAndCommpanyEntity.getDriver();
                home = new Tip();
                home.setPostion(new LatLonPoint(address.getLocation_home()[1], address.getLocation_home()[0]));
                home.setName(address.getAddr_home());
                company = new Tip();
                company.setPostion(new LatLonPoint(address.getLocation_company()[1], address.getLocation_company()[0]));
                company.setName(address.getAddr_company());
                initView();
            }

            @Override
            public void onError(String errorInfo) {
                hideLoadingDialog();
                super.onError(errorInfo);
            }
        });
    }
}
