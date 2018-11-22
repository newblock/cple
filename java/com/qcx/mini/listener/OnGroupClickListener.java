package com.qcx.mini.listener;

import android.view.View;

import com.qcx.mini.entity.WayListEntity;

/**
 * Created by Administrator on 2018/4/4.
 */

public interface OnGroupClickListener {
    void onItemClick(long groupId,int groupType);
    void onAddClick(long groupId, View view);
}
