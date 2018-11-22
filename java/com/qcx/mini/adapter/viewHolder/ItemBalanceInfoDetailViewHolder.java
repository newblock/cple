package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.WalletEntity;
import com.qcx.mini.utils.CommonUtil;

/**
 * Created by Administrator on 2018/8/2.
 *
 */

public class ItemBalanceInfoDetailViewHolder extends BaseRecycleViewHolder {
    private WalletEntity.MoneyEntity money;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_money;

    public ItemBalanceInfoDetailViewHolder(View itemView) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.item_balance_info_detail_name);
        tv_time=itemView.findViewById(R.id.item_balance_info_detail_time);
        tv_money=itemView.findViewById(R.id.item_balance_info_detail_money);
    }

    @Override
    public void bindData(Object data,@NonNull Params params) {
        try {
            money= (WalletEntity.MoneyEntity) data;
        } catch (Exception e) {
            e.printStackTrace();
            money=null;
        }
        bindData();
    }

    private void bindData(){
        tv_time.setText(money.getBillTime());
        String text;
        switch (money.getMark()) {
            case 0://提现中流水
                text = "提现中";
                break;
            case 1://提现失败流水
                text = "提现失败";
                break;
            case 2://收入
                text = "车费收入";
                break;
            case 3://提现
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
            case 11://提现手续费
                text = "提现手续费";
                break;
            default:
                text = "未知";
                break;
        }
        tv_name.setText(text);
        String moneyStr = CommonUtil.formatPrice(money.getBillMoney(),2) + "元";
        tv_money.setText(moneyStr);
    }
}
