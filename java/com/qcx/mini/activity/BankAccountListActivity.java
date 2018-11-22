package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemBankAccountViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.BankAccountListEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BankAccountListActivity extends BaseActivity {
    private SimpleRecyclerViewAdapter<BankAccountListEntity.BankAccount> adapter;
    private BankAccountListEntity.BankAccount checkAccount;

    @BindView(R.id.bank_account_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.bank_account_list_title_right)
    TextView tv_titleRight;
    @BindView(R.id.bank_account_submit)
    TextView tv_submit;

    @Override
    public int getLayoutID() {
        return R.layout.activity_bank_account_list;
    }

    @OnClick(R.id.bank_account_submit)
    void submit(){
        if(adapter.params.getViewType()==ItemBankAccountViewHolder.VIEW_TYPE_DELETE){
            ToastUtil.showToast("删除");
            deleteBankAccount();
        }else {
            startActivity(new Intent(this,AddBankAccountActivity.class));
        }
    }

    @OnClick(R.id.bank_account_list_title_right)
    void topRightClick(){
        if(adapter.params.getViewType()==ItemBankAccountViewHolder.VIEW_TYPE_CHECK){
            adapter.params.setViewType(ItemBankAccountViewHolder.VIEW_TYPE_DELETE);
            tv_titleRight.setText("返回");
            tv_submit.setText("确认删除");
            tv_submit.setTextColor(Color.WHITE);
            tv_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
        }else {
            adapter.params.setViewType(ItemBankAccountViewHolder.VIEW_TYPE_CHECK);
            tv_titleRight.setText("删除");
            tv_submit.setText("添加新账号");
            tv_submit.setTextColor(0xFF484848);
            tv_submit.setBackgroundResource(R.drawable.bg_circular_white_border_gray);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        checkAccount = getIntent().getParcelableExtra("account");
        StatusBarUtil.setColor(this,Color.WHITE,0);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        QuItemDecoration decoration=new QuItemDecoration(0,0, UiUtils.getSize(15),UiUtils.getSize(15),UiUtils.getSize(1),UiUtils.getSize(0));
        QuItemDecoration decoration1=new QuItemDecoration(0,0, UiUtils.getSize(0),UiUtils.getSize(0),UiUtils.getSize(0),UiUtils.getSize(70));
        decoration.setColor(getResources().getColor(R.color.gray_line));
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.addItemDecoration(decoration1);
        adapter=new SimpleRecyclerViewAdapter<>(this,ItemBankAccountViewHolder.class,R.layout.item_bank_account);
        adapter.params.setViewType(ItemBankAccountViewHolder.VIEW_TYPE_CHECK);
        adapter.params.setData(checkAccount);
        adapter.setListener(new ItemBankAccountViewHolder.OnBankAccountClickListener() {
            @Override
            public void click(BankAccountListEntity.BankAccount bankAccount) {
                if(adapter.params.getViewType()==ItemBankAccountViewHolder.VIEW_TYPE_CHECK){
                    adapter.params.setData(bankAccount);
                    usingBankAccount(bankAccount);
                }else {
                    bankAccount.setDeleted(!bankAccount.isDeleted());
                }
                adapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        if(isLoading){
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.bankCardList, params, new QuCallback<BankAccountListEntity>() {
            @Override
            public void onSuccess(BankAccountListEntity o) {
                isLoading=false;
                hideLoadingDialog();
                checkAccount=o.getBankInfo();
                adapter.params.setData(checkAccount);
                adapter.setDatas(o.getAllBankCard());
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
                isLoading=false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(adapter.params.getViewType()==ItemBankAccountViewHolder.VIEW_TYPE_DELETE){
                topRightClick();
                return true;
            }else {
                returnDataAndFinish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean isLoading=false;
    private void deleteBankAccount(){
        if(isLoading){
            return;
        }
        Map<String,Object> params=new HashMap<>();
        List<Long> accounts=new ArrayList<>();
        for(int i=0;i<adapter.getItemCount();i++){
            BankAccountListEntity.BankAccount account=adapter.getItem(i);
            if(account.isDeleted()){
                accounts.add(Long.parseLong(account.getBankCard()));
            }
        }
        if(accounts.size()<1){
            ToastUtil.showToast("请选择您要删除的银行卡");
            return;
        }
        params.put("token",User.getInstance().getToken());
        params.put("bankCards",accounts);
        showLoadingDialog();
        isLoading=true;
        Request.post(URLString.deleteBankCard, params, new QuCallback<IntEntity>() {
            @Override
            public void onSuccess(IntEntity o) {
                isLoading=false;
                if(o.getStatus()==200){
                    getData();
                    topRightClick();
                }

            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                isLoading=false;
                hideLoadingDialog();
            }
        });
    }

    private void returnDataAndFinish(){
        Intent intent=new Intent();
        intent.putExtra("account",checkAccount);
        setResult(RESULT_OK,intent);
        super.finish();
    }

    @Override
    public void finish() {
        returnDataAndFinish();
    }


    private void usingBankAccount(final BankAccountListEntity.BankAccount bankAccount){
        if(isLoading){
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("bankCard",bankAccount.getBankCard());

        showLoadingDialog();
        isLoading=true;
        Request.post(URLString.usingBackAccount, params, new QuCallback<IntEntity>() {
            @Override
            public void onSuccess(IntEntity o) {
                hideLoadingDialog();
                isLoading=false;
                if(o.getStatus()==200){
                    checkAccount=bankAccount;
                    returnDataAndFinish();
                }else {
                    onError("");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
                isLoading=false;
                adapter.params.setData(checkAccount);
            }
        });
    }
}
