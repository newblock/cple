package com.qcx.mini.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.qcx.mini.R;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.utils.LogUtil;

import butterknife.BindView;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/3/23.
 *
 */

public class NewsListFragment extends BaseFragment {
    @BindView(R.id.fragment_news_content)
    FrameLayout mContentView;

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        initConversationList();
        FragmentManager manager=getChildFragmentManager();
        manager.beginTransaction().add(R.id.fragment_news_content,mConversationListFragment).commit();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_news_list;
    }


    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            LogUtil.i("ffffffffffffffffff new mConversationListFragment");
            ConversationListFragment listFragment = new QuConversationListFragment();
//            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            uri = Uri.parse("rong://" + getContext().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM
            };
//            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

}
