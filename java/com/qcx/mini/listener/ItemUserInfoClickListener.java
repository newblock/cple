package com.qcx.mini.listener;

import android.widget.ImageView;

/**
 * Created by Administrator on 2018/7/3.
 */

public interface ItemUserInfoClickListener extends ItemClickListener {
    void onIconClick(long phone);
    void onAttentionClick(boolean isAttention, long phone, ImageView attentionView);
}
