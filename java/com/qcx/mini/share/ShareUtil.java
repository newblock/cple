package com.qcx.mini.share;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;

import java.util.Locale;

/**
 * Created by Administrator on 2018/1/30.
 */

public class ShareUtil {
    public static String SHARE_TRAVEL_PATH="src/travelInfo/travelInfo";

    public static void shareTravel(FragmentManager fragmentManager, long phone, ShareTravelEntity travel){
        LogUtil.i(travel.toString());
        String path=String.format(Locale.CHINA,SHARE_TRAVEL_PATH+"?travelId=%d&phone=%s&travelType=%d",travel.getTravelId(),phone,travel.getTravelType());
        String title;
        LogUtil.i(path);
       if(travel.getTravelType()==0){
           title="车找人，乘客快来订座吧";
       }else {
           title="人找车，车主快来抢单吧";
       }
        String description="趣出行";
        int bitmap= R.mipmap.artboard;
        ShareParmas parmas=ShareParmas.buildWXMiniProgramParams("www.quchuxing.com.cn", ConstantString.MINI_PROGRAM_ID,path,title,description,bitmap);
        ShareDialog shareDialog=new ShareDialog();
        shareDialog.setShareParmas(parmas);
        shareDialog.setTravel(travel);
        shareDialog.setType(ShareDialog.Type.TRAVEL);
        shareDialog.show(fragmentManager,"");
    }

    public static void shareTravelImg(AppCompatActivity activity,String imgPath){
        ShareParmas shareParmas=ShareParmas.buildImgParams(imgPath);
        shareParmas.setShareType(ShareType.WX_PYQ);
        ShareHelper shareHelper=new ShareHelper(activity);
        shareHelper.shareImg(shareParmas);
    }

    public static void shareGroup(FragmentManager fragmentManager,long groupId,int groupType,String title,String describe){
        String path=String.format(Locale.CHINA,"/src/lineInfo/lineInfo?groupId=%d&groupType=%d",groupId,groupType);
        LogUtil.i("dddd"+path);
        int bitmap= R.mipmap.img_share_weixinhaoyou;
        ShareParmas parmas=ShareParmas.buildWXMiniProgramParams("www.quchuxing.com.cn", ConstantString.MINI_PROGRAM_ID,path,title,describe,bitmap);
        ShareDialog shareDialog=new ShareDialog();
        shareDialog.setShareParmas(parmas);
        shareDialog.setType(ShareDialog.Type.GROUP);
        shareDialog.setGroupParams(groupId,groupType);
        shareDialog.show(fragmentManager,"");
    }
}
