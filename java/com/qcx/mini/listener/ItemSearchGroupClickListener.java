package com.qcx.mini.listener;

import android.view.View;

/**
 * Created by Administrator on 2018/4/9.
 */

public interface ItemSearchGroupClickListener extends ItemClickListener {
    void onGroupClick(long groupId,int groupType);
    void onJoinClick(long groupId,View view);
    void onReleaseTravelClick();

}
