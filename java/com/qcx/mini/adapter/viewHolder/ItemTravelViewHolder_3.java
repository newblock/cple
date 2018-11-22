package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemTravelClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;

import java.util.Locale;

/**
 * Created by Administrator on 2018/8/13.
 *
 */

public class ItemTravelViewHolder_3 extends BaseRecycleViewHolder<DriverAndTravelEntity> {
    private TravelEntity travel;
    private ItemTravelClickListener listener;

    private ImageView iv_status;
    private TextView tv_status;
    private TextView tv_startAddress;
    private TextView tv_endAddress;

    public ItemTravelViewHolder_3(View itemView) {
        super(itemView);
        iv_status=itemView.findViewById(R.id.item_travel_3_img_status);
        tv_status=itemView.findViewById(R.id.item_travel_3_text_status);
        tv_startAddress=itemView.findViewById(R.id.item_travel_3_start_address);
        tv_endAddress=itemView.findViewById(R.id.item_travel_3_end_address);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null&&travel!=null){
                    listener.onTravelClick(travel);
                }
            }
        });

    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        this.listener= (ItemTravelClickListener) holederListener;
    }

    @Override
    public void bindData(DriverAndTravelEntity data, @NonNull Params params) {
        if(data==null||data.getTravelVo()==null){
            return;
        }
        travel=data.getTravelVo();
        tv_startAddress.setText(travel.getStartAddress());
        tv_endAddress.setText(travel.getEndAddress());
        tv_status.setText(CommonUtil.getTravelStatusText(travel.getStatus(),travel.getStartTime()));

        //车主发车前，乘客上车前显示沙漏
        switch (travel.getStatus()) {
            case 0://正在寻找乘客
            case 1://车主有乘客未发车
            case 3://乘客发布的行程，等待接单
            case 4://乘客发布的行程，车主已抢单，等待支付
            case 5://乘客订的车主的行程，未支付
            case 6://乘客订的车主的行程，已支付
            case 7://乘客订的车主的行程，已发车
            case 9://车主抢单，乘客未支付
            case 11://车主抢单，乘客已支付
               iv_status.setImageResource(R.mipmap.home_icon_completed);
                break;
            case 2://车主发布的行程已发车
            case 12://车主抢单，已发车，行车中
            case 8://乘客订的车主的行程，行程中
                iv_status.setImageResource(R.mipmap.home_icon_doing);
                break;

        }
    }
}
