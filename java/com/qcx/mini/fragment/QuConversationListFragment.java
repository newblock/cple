package com.qcx.mini.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qcx.mini.R;

import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;

/**
 * Created by Administrator on 2018/8/29.
 *
 */

public class QuConversationListFragment extends ConversationListFragment {
    private View headView;
    @Override
    protected List<View> onAddHeaderView() {
        List<View> headViews=super.onAddHeaderView();
        headView=LayoutInflater.from(getContext()).inflate(R.layout.item_new_head_veiw,null,false);
        headViews.add(headView);
        return headViews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=super.onCreateView(inflater, container, savedInstanceState);
        if(view!=null){
            ListView list=view.findViewById(io.rong.imkit.R.id.rc_list);
            list.setEmptyView(null);

            View emptyView = this.findViewById(view, io.rong.imkit.R.id.rc_conversation_list_empty_layout);
            emptyView.setVisibility(View.GONE);
        }
        return view;
    }
}
