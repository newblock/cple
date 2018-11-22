package com.qcx.mini.listener;

import com.qcx.mini.entity.HeadEntity;

/**
 * Created by Administrator on 2018/5/30.
 */

public interface TravelDetailPassengerClickListener  {
    void onLate(HeadEntity entity);
    void onCancelTravel();
    void onDeletePassenger(HeadEntity entity);
    void onShare();
    void onDeleteSeat();
    void onAttenetionChanged();

}
