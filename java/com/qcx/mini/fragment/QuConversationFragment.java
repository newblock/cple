package com.qcx.mini.fragment;

import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.utils.LogUtil;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by Administrator on 2018/9/7.
 *
 */

public class QuConversationFragment extends ConversationFragment {

    @Override
    public void onPluginToggleClick(View v, ViewGroup extensionBoard) {
        LogUtil.i("++++++++++++++++++++++");
        super.onPluginToggleClick(v,extensionBoard);
    }
}
