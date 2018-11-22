package com.qcx.mini.share;

import com.qcx.mini.utils.LogUtil;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

/**
 * Created by Administrator on 2018/1/29.
 */

public class QWXMiniProgramObject extends WXMiniProgramObject {
    public static int type=0;

    @Override
    public int type() {
        return 36;
    }
}
