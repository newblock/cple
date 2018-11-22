package com.qcx.mini.listener;

import com.amap.api.services.help.Tip;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface ItemSearchAddressTipClickListener extends ItemClickListener {

    void onAddressClick(Tip tip);
    void onClearClick();
}
