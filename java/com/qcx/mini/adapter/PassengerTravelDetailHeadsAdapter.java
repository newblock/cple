package com.qcx.mini.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2018/1/22.
 */

public class PassengerTravelDetailHeadsAdapter extends BaseRecyclerViewAdapter<TravelDetail_PassengerEntity.Passenger,PassengerTravelDetailHeadsAdapter.PassengerTravelDetailHeadsViewHolder> {
    private DriverAndPassengersPageAdapter.OnHeadIconClickListener listener;


    public PassengerTravelDetailHeadsAdapter(Context context) {
        super(context);
    }

    public PassengerTravelDetailHeadsAdapter(Context context, List<TravelDetail_PassengerEntity.Passenger> datas) {
        super(context, datas);
    }

    public void setListener(DriverAndPassengersPageAdapter.OnHeadIconClickListener listener) {
        this.listener = listener;
    }

    @Override
    public PassengerTravelDetailHeadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PassengerTravelDetailHeadsViewHolder(inflater.inflate(R.layout.item_passengers_heads,parent,false));
    }

    @Override
    public void onBindViewHolder(PassengerTravelDetailHeadsViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    class PassengerTravelDetailHeadsViewHolder extends RecyclerView.ViewHolder {
        private TravelDetail_PassengerEntity.Passenger passenger;
        private ImageView iv_icon;
        public PassengerTravelDetailHeadsViewHolder(View itemView) {
            super(itemView);
             iv_icon=itemView.findViewById(R.id.item_passengers_heads);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick(passenger.getPassengerPhone());
                    }
                }
            });
        }

        void bindData(TravelDetail_PassengerEntity.Passenger passenger){
            this.passenger=passenger;
            Picasso.with(context)
                    .load(passenger.getPassengerPicture())
                    .error(R.mipmap.img_me)
                    .into(iv_icon);
        }
    }

}
