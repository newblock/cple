package com.qcx.mini.listener;

import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.entity.DriverAndTravelEntity;

/**
 * Created by Administrator on 2018/4/2.
 */

public interface ItemDriverAndTravelClickListener extends ItemTravelClickListener{

    void onIconClick(long phoneNum);

    void onAttentionClick(DriverAndTravelEntity data, ImageView iv_attention);

    void onLikesClick(DriverAndTravelEntity data, ImageView likeViw, TextView likeNum);

    void onMessageClick(DriverAndTravelEntity data, TextView messageNum);

    void onShareClick(DriverAndTravelEntity data);
}
