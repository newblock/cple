package com.qcx.mini.utils;

import com.qcx.mini.message.TravelMessage;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2018/8/30.
 *
 */

public class ConversationUtil {

    public static void setUserInfo(){
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                return null;
            }
        },true);
    }

    public static void sendTravel(TravelMessage travel,String targetId){
        Message message=Message.obtain(targetId, Conversation.ConversationType.PRIVATE,travel);
        RongIM.getInstance().sendMessage(message, "pushContent", "pushData", new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });

    }
}
