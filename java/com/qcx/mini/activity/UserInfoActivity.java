package com.qcx.mini.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.utils.UiUtils;

import java.util.Locale;

import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class UserInfoActivity extends BaseActivity {
    private long phone;

    @OnClick(R.id.user_info_send_message)
    void sendMessage() {
        if (phone == 0) {
            return;
        }

//        RongIM.getInstance().setCurrentUserInfo(new UserInfo("14300000000","zzz", Uri.parse("https://t1.driver.quchuxing.com.cn/resources/pictures/1523584253545.jpg")));
        RongIM.getInstance().setMessageAttachedUserInfo(true);

        RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE,String.valueOf(phone),"标题");
    }

    @OnClick(R.id.user_info_attention)
    void attention() {
        if (phone == 0) {
            return;
        }
//        Map<String, Boolean> supportConversation = new HashMap<>();
//        supportConversation.put(Conversation.ConversationType.PRIVATE.getName(), true);
//        supportConversation.put(Conversation.ConversationType.SYSTEM.getName(), true);
//        RongIM.getInstance().startConversationList(this, supportConversation);
        RongIM.getInstance().startSubConversationList(this, Conversation.ConversationType.PRIVATE);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    public boolean setStatusBar() {
        initTitleBar("TA的主页",true,false);
        UiUtils.setStatusBarTransparent(this);
        UiUtils.setStatusBarLightMode(this, Color.TRANSPARENT);
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        phone = getIntent().getLongExtra("phone", 0);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

        }
        return super.onKeyDown(keyCode, event);
    }
}
