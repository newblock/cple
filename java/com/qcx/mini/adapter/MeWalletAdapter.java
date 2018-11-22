package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.Entity;
import com.qcx.mini.entity.WalletEntity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/1/12.
 */

public class MeWalletAdapter extends BaseRecyclerViewAdapter<WalletEntity.MoneyEntity, MeWalletAdapter.MeWalletViewHolder> {
    private final int TYPE_HEAD = 0;
    private final int TYPE_NORMAL = 1;
    private int headViewId = 0;
    private WalletEntity walletEntity;
    private OnWalletItemClickListener listener;

    public MeWalletAdapter(Context context) {
        super(context);
    }

    public MeWalletAdapter(Context context, List<WalletEntity.MoneyEntity> datas) {
        super(context, datas);
    }

    public void setHeadView() {
        this.headViewId = 1;
        notifyDataSetChanged();
    }

    public void setListener(OnWalletItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(WalletEntity wallet) {
        datas = wallet.getWaterBill();
        walletEntity = wallet;
        notifyDataSetChanged();
    }

    public WalletEntity getWalletEntity() {
        return walletEntity;
    }

    @Override
    public int getItemCount() {
        return headViewId == 0 ? datas.size() : datas.size() + 1;
    }

    @Override
    public WalletEntity.MoneyEntity getItem(int position) {
        if (datas == null || datas.size() < 1) return null;
        return headViewId == 0 ? datas.get(position) : datas.get(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (headViewId == 0) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEAD;
        return TYPE_NORMAL;
    }


    @Override
    public MeWalletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new MeWalletHeadViewHolder(inflater.inflate(R.layout.item_me_wallet_head, parent, false));
        }
        return new MeWalletItemViewHolder(inflater.inflate(R.layout.item_me_wallete, parent, false));
    }

    @Override
    public void onBindViewHolder(MeWalletViewHolder holder, int position) {
        if (headViewId != 0 && position == 0) {
            holder.bindData(walletEntity);
        } else {
            holder.bindData(getItem(position));
        }
    }

    class MeWalletHeadViewHolder extends MeWalletViewHolder {
        private WalletEntity entity;
        private TextView tv_money, tv_income, tv_tixian, tv_redPackageNum, tv_account;
        private View v_redPackage, v_account;

        public MeWalletHeadViewHolder(View itemView) {
            super(itemView);
            tv_money = itemView.findViewById(R.id.item_me_wallet_head_money);
            tv_income = itemView.findViewById(R.id.item_me_wallet_head_income);
            tv_tixian = itemView.findViewById(R.id.item_me_wallet_head_tixian);
            tv_redPackageNum = itemView.findViewById(R.id.item_me_wallet_head_red_package_num);
            v_redPackage = itemView.findViewById(R.id.item_me_wallet_head_red_package);
            v_account = itemView.findViewById(R.id.item_me_wallet_head_account);
            tv_account = itemView.findViewById(R.id.item_me_wallet_head_account_text);

            v_redPackage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast("红包");
                }
            });
            v_account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onAccountClick(entity.getAliPay());
                    }
                }
            });

            tv_tixian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onWithdrawClick(entity.getAliPay(), entity.getMoneyCard());
                    }
                }
            });
        }

        @Override
        public void bindData(Entity entity) {
            this.entity = (WalletEntity) entity;
            setData();
        }

        void setData() {
            if (entity == null) return;
            tv_money.setText(String.valueOf(entity.getMoneyCard()));
            tv_income.setText(String.valueOf(entity.getMoneyIncome()));
            tv_redPackageNum.setText(String.valueOf(entity.getMoneyIncome()));
            if (!TextUtils.isEmpty(entity.getAliPay())) {
                tv_account.setText("支付宝:".concat(entity.getAliPay()));
            } else {
                tv_account.setText("");
            }
        }
    }

    class MeWalletItemViewHolder extends MeWalletViewHolder {
        private TextView tv_income, tv_time, tv_type;

        public MeWalletItemViewHolder(View itemView) {
            super(itemView);
            tv_income = itemView.findViewById(R.id.item_me_wallet_item_income);
            tv_time = itemView.findViewById(R.id.item_me_wallet_item_time);
            tv_type = itemView.findViewById(R.id.item_me_wallet_item_type);
        }

        @Override
        public void bindData(Entity entity) {
            if (entity == null) return;
            WalletEntity.MoneyEntity money = (WalletEntity.MoneyEntity) entity;
            tv_time.setText(money.getBillTime());
            String text = "";
            switch (money.getMark()) {
                case 0://提现中流水
//                    iv_img.setImageResource(R.mipmap.icon_pay);
                    text = "提现中";
                    break;
                case 1://提现失败流水
//                    iv_img.setImageResource(R.mipmap.icon_pay);
                    text = "提现失败";
                    break;
                case 2://收入
                    text = "车费收入";
                    break;
                case 3://提现
//                    iv_img.setImageResource(R.mipmap.icon_pay);
                    text = "提现";
                    break;
                case 4://打赏
                    text = "打赏收入";
                    break;
                case 6://首次解救乘客
                    text = "首次解救乘客收入";
                    break;
                case 9://红包收入
                    text = "点赞红包收入";
                    break;
                case 10://红包收入
                    text = "分享红包收入";
                    break;
                default:
                    text = "未知";
                    break;
            }
            tv_type.setText(text);
            String moneyStr = CommonUtil.formatPrice(money.getBillMoney(),2) + "元";
            tv_income.setText(moneyStr);
        }
    }

    abstract class MeWalletViewHolder extends RecyclerView.ViewHolder {
        public MeWalletViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(Entity entity);
    }

    public interface OnWalletItemClickListener {
        void onAccountClick(String account);

        void onWithdrawClick(String account, double money);
    }
}
