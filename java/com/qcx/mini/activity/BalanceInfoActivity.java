package com.qcx.mini.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemBalanceInfoDetailViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemBalanceInfoHeadViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.QuDialog;
import com.qcx.mini.entity.BankAccountListEntity;
import com.qcx.mini.entity.WalletEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class BalanceInfoActivity extends BaseActivity {
    private final static int ACCOUNT_CHANGE_CODE = 3;
    private final static int WITH_DRAW_SUCCESS_CODE = 4;
    private SimpleRecyclerViewAdapter<WalletEntity.MoneyEntity> adapter;
    private WalletEntity wallet;
    private int pageNum;

    @BindView(R.id.balance_info_list)
    RecyclerView mRecyclerView;

    @Override
    public int getLayoutID() {
        return R.layout.activity_balance_info;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("我的余额",true,false);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter=new SimpleRecyclerViewAdapter<>(this, ItemBalanceInfoDetailViewHolder.class,R.layout.item_balance_info_detail);
        adapter.setHeadView(ItemBalanceInfoHeadViewHolder.class, R.layout.item_balance_info_head, new ItemBalanceInfoHeadViewHolder.OnClickListener() {
            @Override
            public void onAccountClick(BankAccountListEntity.BankAccount account) {
                if(!canNext()){
                    return;
                }
                Intent intent;
                if(TextUtils.isEmpty(account.getBankCard())||account.getBankCard().length()<10){
                    intent = new Intent(BalanceInfoActivity.this, AddBankAccountActivity.class);
                }else {
                    intent = new Intent(BalanceInfoActivity.this, BankAccountListActivity.class);
                }
                intent.putExtra("account", account);
                startActivityForResult(intent, ACCOUNT_CHANGE_CODE);
            }

            @Override
            public void onWithdrawClick(BankAccountListEntity.BankAccount account, double money) {
                if(!canNext()){
                    return;
                }
                Intent intent = new Intent(BalanceInfoActivity.this, WithdrawActivity.class);
                intent.putExtra("money", money);
                intent.putExtra("account", account);
                startActivityForResult(intent, WITH_DRAW_SUCCESS_CODE);
            }
        });

        mRecyclerView.setAdapter(adapter);
        QuItemDecoration decoration=new QuItemDecoration(1, UiUtils.getSize(0),UiUtils.getSize(16),UiUtils.getSize(16),UiUtils.getSize(1),0);
        decoration.setColor(getResources().getColor(R.color.gray_line));
        mRecyclerView.addItemDecoration(decoration);
    }

    private boolean canNext(){
        if(!User.getInstance().isAuthenStatus()){
            toRealNameAuth();
            return false;
        }

        if(!User.getInstance().isPasswordStatus()){
            toSetPassword();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case ACCOUNT_CHANGE_CODE:
                    BankAccountListEntity.BankAccount account=data.getParcelableExtra("account");
                    if(account!=null){
                        wallet.setBank(account.getBank());
                        wallet.setBankcard(account.getBankCard());
                        adapter.setHeadData(wallet);
                    }
                    break;
            }
        }
    }

    public void getData(final int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo", page);
        pageNum=page;
        showLoadingDialog();
        Request.post(URLString.balance, params, new EntityCallback(WalletEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoad();
                WalletEntity wallet = (WalletEntity) t;
                if(page==1){
                    BalanceInfoActivity.this.wallet=wallet;
                    adapter.setHeadData(wallet);
                    adapter.setDatas(wallet.getWaterBill());
                }else {
                    adapter.addDatas(wallet.getWaterBill());
                }

                if(wallet.getWaterBill()!=null&&wallet.getWaterBill().size()>0){
                    pageNum=page+1;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoad();
            }
        });
    }

    private void finishLoad(){

        hideLoadingDialog();
    }

    private QuDialog toRealNameAutDialog;
    private void toRealNameAuth(){
        if(toRealNameAutDialog==null){
            toRealNameAutDialog=new QuDialog()
                    .setTitle("还未实名认证")
                    .setMessage("你还未进行实名认证,认证后才可添加银 \n行卡进行余额提现,并且每次出行将获赠 \n30万意外险")
                    .setLeftBtn("暂不认证", new QuDialog.OnClickListener() {
                        @Override
                        public void onClick(QuDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setRightBtn("去认证", new QuDialog.OnClickListener() {
                        @Override
                        public void onClick(QuDialog dialog) {
                            Intent intent=new Intent(BalanceInfoActivity.this,RealNameActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
        }
        toRealNameAutDialog.show(getSupportFragmentManager(),"");
    }

    private QuDialog toSetPasswordDialog;
    private void toSetPassword(){
        if(toSetPasswordDialog==null){
            toSetPasswordDialog=new QuDialog()
                    .setTitle("设置交易密码")
                    .setMessage("你还未设置交易密码,设置后可用于余额提现、支付车费等")
                    .setLeftBtn("暂不设置", new QuDialog.OnClickListener() {
                        @Override
                        public void onClick(QuDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setRightBtn("去设置", new QuDialog.OnClickListener() {
                        @Override
                        public void onClick(QuDialog dialog) {
                            Intent intent=new Intent(BalanceInfoActivity.this,VerifyPhoneNumActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
        }
        toSetPasswordDialog.show(getSupportFragmentManager(),"");
    }
}
