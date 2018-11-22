package com.qcx.mini.adapter.viewHolder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.adapter.ItemCityAdapter;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.listener.ItemClickListener;
import com.qcx.mini.listener.ItemDriverAndTravelClickListener;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.ConstantValue;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.OnItemClick;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ItemTravelNoneShareViewHolder extends BaseRecycleViewHolder implements View.OnClickListener {
    DriverAndTravelEntity data;
    OnShareTravelClickListener clickListener;


    public ItemTravelNoneShareViewHolder(View itemView) {
        super(itemView);
        itemView.findViewById(R.id.item_travel_none_head_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener!=null&&data!=null){
                    clickListener.share(data);
                }
            }
        });
    }

    @Override
    public void setHolederListener(ItemClickListener holederListener) {
        if(holederListener!=null&&holederListener instanceof OnShareTravelClickListener){
            clickListener= (OnShareTravelClickListener) holederListener;
        }
    }

    @Override
    public void bindData(Object data, @NonNull Params params) {
        if(data!=null&&data instanceof DriverAndTravelEntity){
            this.data= (DriverAndTravelEntity) data;
        }
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnShareTravelClickListener extends ItemClickListener{
        void share(DriverAndTravelEntity data);
    }
}
