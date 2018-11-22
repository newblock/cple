package com.qcx.mini.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.MeMessageListAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.entity.MeMessagesEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 */

public class MeMessageFragment extends BaseFragment {
    private String mToken;
    private RecyclerView mRecyclerView;
    private MeMessageListAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private MeFragment meFragment;
    private long phone;
    private boolean isOrther;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_me_message;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mToken == null || !mToken.equals(User.getInstance().getToken())) {
                getData(1);
                mToken = User.getInstance().getToken();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(isVisible()){
        LogUtil.i("MeMessageFragment isVisivle");
        if (mToken == null || !mToken.equals(User.getInstance().getToken())) {
            getData(1);
            mToken = User.getInstance().getToken();
//            }
        }
    }

    public void getData(final int page) {
        Map<String, Object> params = new HashMap<>();
        String url;
        if (isOrther) {
            url = URLString.listOtherNews;
            params.put("otherPhone", phone);
        } else {
            url = URLString.listPersonNews;
            params.put("token", User.getInstance().getToken());
        }
        params.put("pageNo", page);
        pageNum = page;
        Request.post(url, params, new EntityCallback(MeMessagesEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoad(page);
                MeMessagesEntity datas = (MeMessagesEntity) t;
                if (page == 1) {
                    adapter.setDatas(datas.getNews());
                } else {
                    adapter.addDatas(datas.getNews());
                }
                if (datas.getNews() != null && datas.getNews().size() > 0) {
                    pageNum++;
                }
            }

            @Override
            public void onError(String errorInfo) {
                finishLoad(page);
            }
        });
    }

    public void changeIcon(String icon, String name) {
        if (adapter == null) return;
        List<MeMessagesEntity.MeMessageEntity> news = adapter.getData();
        if (news == null || news.size() < 1) return;
        if (news.size() > 0) {
            for (int i = 0; i < news.size(); i++) {
                news.get(i).setPicture(icon);
                news.get(i).setNickName(name);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void finishLoad(int page) {
        if (page == 1) {

        } else {
            if (refreshLayout != null) refreshLayout.finishLoadmore();
        }
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        try {
            meFragment = (MeFragment) getParentFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        refreshLayout = view.findViewById(R.id.fragment_me_message_refresh);


        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(false);

        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(pageNum);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getData(pageNum);
            }
        });

        mRecyclerView = view.findViewById(R.id.fragment_me_message_list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new MeMessageListAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
        getData(1);
    }

    public void setPhone(long phone, boolean isOrther) {
        this.phone = phone;
        this.isOrther = isOrther;
    }


}
