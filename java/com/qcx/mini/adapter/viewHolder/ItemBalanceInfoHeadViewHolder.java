package com.qcx.mini.adapter.viewHolder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.BankAccountListEntity;
import com.qcx.mini.entity.WalletEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/8/2.
 * 余额 头部View
 */

public class ItemBalanceInfoHeadViewHolder extends BaseRecycleViewHolder implements View.OnClickListener {
    private WalletEntity wallet;
    private TextView tv_balance;
    private TextView tv_all;
    private View v_withdraw;
    private TextView tv_account;
    private ImageView iv_account;
    private OnClickListener listener;


    public ItemBalanceInfoHeadViewHolder(View itemView) {
        super(itemView);
        tv_balance = itemView.findViewById(R.id.item_balance_info_head_balance_text);
        tv_all = itemView.findViewById(R.id.item_balance_info_head_all_text);
        v_withdraw = itemView.findViewById(R.id.item_balance_info_head_withdraw);
        tv_account = itemView.findViewById(R.id.item_balance_info_head_account);
        iv_account = itemView.findViewById(R.id.item_balance_info_head_account_img);
        v_withdraw.setOnClickListener(this);
        tv_account.setOnClickListener(this);
        iv_account.setOnClickListener(this);
    }

    @Override
    public void bindData(Object data, Params params) {
        try {
            wallet = (WalletEntity) data;
        } catch (Exception e) {
            e.printStackTrace();
            wallet = null;
        }
        bindData();
    }

    private void bindData() {
        if (wallet != null) {
            tv_balance.setText(CommonUtil.formatPrice(wallet.getMoneyCard(), 2));
            tv_all.setText(CommonUtil.formatPrice(wallet.getMoneyIncome(), 2));
            String account=wallet.getBankcard();
            if(TextUtils.isEmpty(account)||account.length()<12){
                tv_account.setText("添加银行卡");
            }else {
                tv_account.setText(String.format("银行卡(%s)",wallet.getBankcard().substring(account.length()-4,account.length())));
            }
            Picasso.with(holderContext)
                    .load(CommonUtil.getCardImgUrl(wallet.getBank()))
                    .into(iv_account);
        } else {
            tv_balance.setText("----");
            tv_all.setText("----");
            tv_account.setText("----");
        }
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener= (OnClickListener) holederListener;
    }

    @Override
    public void onClick(View v) {
        BankAccountListEntity.BankAccount account=new BankAccountListEntity.BankAccount();
        account.setBank(wallet.getBank());
        account.setBankCard(wallet.getBankcard());
        switch (v.getId()) {
            case R.id.item_balance_info_head_withdraw:
                if(listener!=null){
                    listener.onWithdrawClick(account,wallet.getMoneyCard());
                }
                break;
            case R.id.item_balance_info_head_account:
            case R.id.item_balance_info_head_account_img:
                if(listener!=null){
                    listener.onAccountClick(account);
                }
                break;
        }
    }

    public interface OnClickListener extends ItemClickListener{
        void onWithdrawClick(BankAccountListEntity.BankAccount account, double money);
        void onAccountClick(BankAccountListEntity.BankAccount account);
    }
}
