package com.qcx.mini.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.loc.r;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;


/**
 * Created by Administrator on 2018/1/26.
 */

public class ShareHelper {
    private Context context;
    private IWXAPI api;

    public ShareHelper(Context context){

        api = WXAPIFactory.createWXAPI(context, ConstantString.WXAPP_ID,false);

        this.context=context;
    }

    public void shareURL(ShareParmas shareParmas){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareParmas.getWebpageUrl();
        SendMessageToWX.Req req=buildWXMediaMessage(webpage,shareParmas);
        api.sendReq(req);
    }

    public void shareWXMiniProgram(ShareParmas parmas){
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl =parmas.getWebpageUrl();
        miniProgram.userName = parmas.getUserName();
        miniProgram.path = parmas.getPath();

        SendMessageToWX.Req req=buildWXMediaMessage(miniProgram,parmas);
        api.sendReq(req);
    }

    public void shareWXMiniProgram(ShareParmas parmas,Bitmap bitmap){
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl =parmas.getWebpageUrl();
        miniProgram.userName = parmas.getUserName();
        miniProgram.path = parmas.getPath();

        WXMediaMessage msg = new WXMediaMessage(miniProgram);
        msg.title =parmas.getTitle();
        msg.description = parmas.getDescription();
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 840, 672, true);
//        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(bitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("qcx_web_");
        req.message = msg;
        req.scene=parmas.getShareType().getStatus();
        api.sendReq(req);
    }


    public void shareWXMiniProgram(){
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = "http://www.qq.com";
        miniProgram.userName = "gh_a82a854af101";
        miniProgram.path = "src/shareTravelDetails/shareTravelDetails";
        WXMediaMessage msg = new WXMediaMessage(miniProgram);
        msg.title = "分享小程序Title";
        msg.description = "分享小程序描述信息";
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_map_star_driver);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);

    }

    public void shareImg(ShareParmas shareParmas){
        String path = shareParmas.getPath();
        File file = new File(path);
        if (!file.exists()) {
            ToastUtil.showToast("分享失败");
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = shareParmas.getShareType().getStatus();
        api.sendReq(req);
    }



    private SendMessageToWX.Req buildWXMediaMessage(WXMediaMessage.IMediaObject object,ShareParmas shareParmas){
        WXMediaMessage msg = new WXMediaMessage(object);
        msg.title =shareParmas.getTitle();
        msg.description = shareParmas.getDescription();
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),shareParmas.getBitmap());
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 840, 672, true);
//        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(bmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("qcx_web_");
        req.message = msg;
        req.scene=shareParmas.getShareType().getStatus();
        return req;
    }





    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();

    }
}
