package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.BankAccountListEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.squareup.picasso.Picasso;
import java.util.Locale;

/**
 * Created by Administrator on 2018/8/14.
 */

public class ItemBankAccountViewHolder extends BaseRecycleViewHolder<BankAccountListEntity.BankAccount> {
    public final static int VIEW_TYPE_DELETE=1;
    public final static int VIEW_TYPE_CHECK=0;
    private BankAccountListEntity.BankAccount bankAccount;
    private OnBankAccountClickListener listener;
    private ImageView iv_icon;
    private TextView tv_name;
    private TextView tv_info;
    private ImageView iv_check;

    public ItemBankAccountViewHolder(View itemView) {
        super(itemView);
        iv_icon=itemView.findViewById(R.id.item_bank_account_icon);
        tv_name=itemView.findViewById(R.id.item_bank_account_name);
        tv_info=itemView.findViewById(R.id.item_bank_account_info);
        iv_check=itemView.findViewById(R.id.item_bank_account_check);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&bankAccount!=null){
                    listener.click(bankAccount);
                }
            }
        });
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener= (OnBankAccountClickListener) holederListener;
    }

    @Override
    public void bindData(BankAccountListEntity.BankAccount data, @NonNull Params params) {
        this.bankAccount=data;
        if(data!=null){
            String logoUrl=String.format("%s%s.png",URLString.bankLogo,data.getBank());
            Picasso.with(holderContext)
                    .load(logoUrl)
                    .into(iv_icon);
            tv_name.setText(CommonUtil.getBankName(data.getBank()));
            String info=String.format(Locale.CHINA,"尾号%s    储蓄卡",data.getBankCardSub());
            tv_info.setText(info);
            if(params.getViewType()==VIEW_TYPE_DELETE){
                if(data.isDeleted()){
                    iv_check.setImageResource(R.mipmap.icon_check);
                }else {
                    iv_check.setImageResource(R.mipmap.icon_uncheck);
                }
            }else {
                BankAccountListEntity.BankAccount account= (BankAccountListEntity.BankAccount) params.getData();
                if(account!=null&&!TextUtils.isEmpty(account.getBankCard())&&account.getBankCard().equals(data.getBankCard())){
                    iv_check.setImageResource(R.mipmap.btn_login_confirm_mini);
                }else {
                    iv_check.setImageBitmap(null);
                }
            }
        }
    }

    public interface OnBankAccountClickListener extends ItemClickListener{
        void click(BankAccountListEntity.BankAccount bankAccount);
    }
}
