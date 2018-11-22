package com.qcx.mini.listener;

import com.qcx.mini.entity.TravelEntity;

/**
 * Created by Administrator on 2018/4/2.
 */

public interface ItemTravelClickListener extends ItemClickListener {
    void onTravelClick(TravelEntity data);
}
