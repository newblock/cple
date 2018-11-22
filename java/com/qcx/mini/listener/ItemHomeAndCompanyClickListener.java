package com.qcx.mini.listener;

import com.amap.api.services.help.Tip;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface ItemHomeAndCompanyClickListener extends ItemClickListener {
    void onHomeClick(Tip homeTip);
    void onCompanyClick(Tip companyTip);
}
