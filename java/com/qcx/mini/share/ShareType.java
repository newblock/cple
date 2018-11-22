package com.qcx.mini.share;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

/**
 * Created by Administrator on 2018/1/27.
 */

public enum ShareType {
    WX_CHAT("微信聊天",WXSceneSession),
    WX_PYQ("微信朋友圈",WXSceneTimeline);

    private String description;
    private int status;

    ShareType(String description, int status) {
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }
}
