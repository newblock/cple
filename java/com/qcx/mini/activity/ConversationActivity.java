package com.qcx.mini.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.message.TravelMessage;
import com.qcx.mini.utils.ConversationUtil;

import butterknife.OnClick;

public class ConversationActivity extends BaseActivity {
    String targetId="";

    @OnClick(R.id.send_travel)
    void sendTravel(){
        TravelMessage message=TravelMessage.obtain("测试行程起点","测试行程终点",System.currentTimeMillis(),"2222222",String.valueOf(User.getInstance().getPhoneNumber()),1);
        ConversationUtil.sendTravel(message,targetId);
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_conversation;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Uri uri=getIntent().getData();
        if(uri!=null){
            targetId=uri.getQueryParameter("targetId");
        }
        initTitleBar(targetId);
    }
}
