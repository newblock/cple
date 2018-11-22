package com.qcx.mini.widget.numSoftKeyboart;

import com.qcx.mini.listener.ItemClickListener;

/**
 * Created by Administrator on 2018/7/31.
 */

public interface OnNumInputListener extends ItemClickListener {

    void onInputNum(int num);

    void onDeleteNum();
}
