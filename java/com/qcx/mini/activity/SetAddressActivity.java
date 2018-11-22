package com.qcx.mini.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.SearchAddressListAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.HomeAndCommpanyEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.KeybordS;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 单行地址输入
 */
public class SetAddressActivity extends BaseActivity implements TextWatcher, Inputtips.InputtipsListener{
    private SearchAddressListAdapter adapter;
    private String inputHint;

    @BindView(R.id.set_address_company_address_text)
    TextView tv_company;
    @BindView(R.id.set_address_home_address_text)
    TextView tv_home;
    @BindView(R.id.set_address_city)
    TextView tv_city;
    @BindView(R.id.set_address_input)
    EditText et_input;
    @BindView(R.id.set_address_list)
    RecyclerView lv_tips;
    @BindView(R.id.set_address_choose_home_and_company_view)
    View v_view;
    @BindView(R.id.set_address_choose_home_and_company_view_line)
    View v_viewLine;
    private Tip home,company;

    @OnClick(R.id.set_address_choose_company_address)
    void company(){
        if(company!=null&&!(TextUtils.isEmpty(company.getName()))){
            setResult(company);
        }else {
            ToastUtil.showToast("未获取到公司地址");
        }
    }

    @OnClick(R.id.set_address_clear)
    void clear(){
        et_input.setText("");
        adapter.setDatas(null);

    }

    @OnClick(R.id.set_address_choose_home_address)
    void home(){
        if(home!=null&&!(TextUtils.isEmpty(home.getName()))){
            setResult(home);
        }else {
            ToastUtil.showToast("未获取到家庭地址");
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_set_address;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("",true,false);
        inputHint=getIntent().getStringExtra("inputHint");
        if(getIntent().getBooleanExtra("home_company_view",true)){
            v_view.setVisibility(View.VISIBLE);
            v_viewLine.setVisibility(View.VISIBLE);
        }else {
            v_view.setVisibility(View.GONE);
            v_viewLine.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(inputHint)){
            et_input.setHint(inputHint);
        }
        et_input.addTextChangedListener(this);
        adapter=new SearchAddressListAdapter(this);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_tips.setLayoutManager(manager);
        lv_tips.setAdapter(adapter);
        adapter.setListener(new SearchAddressListAdapter.OnItemClickListener() {
            @Override
            public void onAddressClick(Tip tip) {
                setResult(tip);
            }

            @Override
            public void onClearClick() {

            }
        });
        getAddress();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String newText = s.toString().trim();
        LogUtil.i("newText="+newText);
        if (!TextUtils.isEmpty(newText)) {
            LogUtil.i("newText="+newText);
            InputtipsQuery inputquery = new InputtipsQuery(newText, tv_city.getText().toString());
            Inputtips inputTips = new Inputtips(SetAddressActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            for(int i=0;i<list.size();i++){
                if(list.get(i).getPoint()==null){
                    list.remove(i);
                    i--;
                }
                adapter.setDatas(list);
            }

        }
    }

    private void setResult(Tip tip){
        KeybordS.closeKeybord(et_input,this);
        Intent intent=new Intent();
        intent.putExtra("tip",tip);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void getAddress() {
        if(User.getInstance().isLogin()){
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
                    tv_company.setText(company.getName());
                    tv_home.setText(home.getName());
                }

                @Override
                public void onError(String errorInfo) {
                    hideLoadingDialog();
                    super.onError(errorInfo);
                }
            });
        }

    }
}
