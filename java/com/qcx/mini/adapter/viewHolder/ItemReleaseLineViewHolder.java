package com.qcx.mini.adapter.viewHolder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.adapter.ItemReleaseLineAdapter;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.ReleaseLineInfoEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;

import java.util.Locale;

/**
 * Created by Administrator on 2018/5/4.
 */
public class ItemReleaseLineViewHolder extends BaseRecycleViewHolder {
    private ReleaseLineInfoEntity lineInfo;
    private TextView tv_lineName;
    private TextView tv_time;
    private TextView tv_seat;
    private TextView tv_price;
    private TextView tv_release;
    private TextView tv_startAddress;
    private TextView tv_endAddress;
    private View v_line;
    private ImageView iv_auto;
    private ItemReleaseLineAdapter.OnReleaseLineItemClickListener listener;

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener= (ItemReleaseLineAdapter.OnReleaseLineItemClickListener) holederListener;
    }

    public ItemReleaseLineViewHolder(View itemView) {
        super(itemView);
        tv_lineName=itemView.findViewById(R.id.item_release_line_name);
        tv_time=itemView.findViewById(R.id.item_release_line_time);
        tv_seat=itemView.findViewById(R.id.item_release_line_seat);
        tv_price=itemView.findViewById(R.id.item_release_line_price);
        tv_release=itemView.findViewById(R.id.item_release_line_release);
        tv_startAddress=itemView.findViewById(R.id.item_release_line_startAddress);
        tv_endAddress=itemView.findViewById(R.id.item_release_line_endAddress);
        v_line=itemView.findViewById(R.id.item_release_line_info);
        iv_auto=itemView.findViewById(R.id.item_release_line_auto);

        v_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&lineInfo!=null){
                    listener.onInfoClick(lineInfo);
                }
            }
        });
        tv_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&lineInfo!=null){
                    listener.onReleaseClick(lineInfo,tv_release);
                }
            }
        });
    }

    @Override
    public void bindData(Object data, @NonNull Params params) {
        try {
            lineInfo= (ReleaseLineInfoEntity) data;
            String time= DateUtil.formatDay("MM-dd",lineInfo.getStartTime())+DateUtil.getTimeStr(lineInfo.getStartTime(),"HH:mm");
            tv_time.setText(time);
            if(TextUtils.isEmpty(lineInfo.getLineDesc())){
                tv_lineName.setText("常用");
            }else {
                tv_lineName.setText(lineInfo.getLineDesc());
            }
            if(lineInfo.getType()== ConstantValue.TravelType.DRIVER){
                tv_seat.setText(String.format(Locale.CHINA,"%d座",lineInfo.getSeats()));
                tv_price.setText(String.format(Locale.CHINA,"%s元/座", CommonUtil.formatPrice(lineInfo.getPrice(),0)));
            }else {
                tv_seat.setText(String.format(Locale.CHINA,"%d人",lineInfo.getSeats()));
                tv_price.setText(String.format(Locale.CHINA,"%s元", CommonUtil.formatPrice(lineInfo.getPrice(),0)));
            }
            tv_startAddress.setText(lineInfo.getStartAddress());
            tv_endAddress.setText(lineInfo.getEndAddress());
            if(lineInfo.isHaveCreated()){
                tv_release.setText("已发布");
                tv_release.setTextColor(Color.WHITE);
            }else {
                tv_release.setText("发布");
                tv_release.setTextColor(0xFF499EFF);
            }
            if(lineInfo.isAutoCreate()){
                iv_auto.setVisibility(View.VISIBLE);
            }else {
                iv_auto.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}