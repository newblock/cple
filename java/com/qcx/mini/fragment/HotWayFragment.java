package com.qcx.mini.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.MainActivity;
import com.qcx.mini.activity.WayInfoActivity;
import com.qcx.mini.adapter.ItemHotWayAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.entity.HotGroupsEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.listener.OnGroupClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class HotWayFragment extends BaseFragment {
    private String mToken = User.getInstance().getToken();
    private ItemHotWayAdapter adapter;
    private int pageNum=1;

    @BindView(R.id.fragment_list_refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.fragment_list_recyclerView)
    RecyclerView mRecyclerView;

    @Override
    public void onResume() {
        super.onResume();
        mToken="";
        if (mToken != null && !mToken.equals(User.getInstance().getToken())) {
            mToken = User.getInstance().getToken();
            getData(1);
        }
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableAutoLoadmore(false);
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(pageNum);
            }
        });
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter=new ItemHotWayAdapter(getContext());
        adapter.setListener(new OnGroupClickListener() {
            @Override
            public void onItemClick(long groupId,int groupType) {
                Intent intent=new Intent(getContext(),WayInfoActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupType",groupType);
                startActivity(intent);
            }

            @Override
            public void onAddClick(long groupId,View view1) {
                joinGroup(groupId,view1);
            }
        });
        mRecyclerView.setAdapter(adapter);
        getData(1);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_list;
    }

    public void getData(final int pageNo){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("currentCity", SharedPreferencesUtil.getAppSharedPreferences().getString(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY));
        params.put("pageNo",pageNo);
        Request.post(URLString.hotGroups, params, new EntityCallback(HotGroupsEntity.class) {
            @Override
            public void onSuccess(Object t) {
                HotGroupsEntity hotGroups= (HotGroupsEntity) t;
                finishLoading();
                if(pageNo==1){
                    pageNum=1;
                    if(hotGroups!=null){
                        adapter.setDatas(hotGroups.getHotGroups());
                    }else {
                        adapter.setDatas(null);
                    }
                }else if(hotGroups!=null){
                    adapter.addDatas(hotGroups.getHotGroups());
                }
                if(hotGroups!=null&&hotGroups.getHotGroups()!=null&&hotGroups.getHotGroups().size()>0){
                    pageNum++;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
            }
        });
    }

    private void finishLoading(){
        if(mRefreshLayout!=null){
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i("HotWayFragment onHiddenChanged "+hidden);
    }

    private void joinGroup(long groupId, final View view){
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("groupId",new long[]{groupId});
        params.put("phones",new long[]{User.getInstance().getPhoneNumber()});
        Request.post(URLString.joinGroup, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity= (IntEntity) t;
                if(intEntity.getStatus()==200){
                    ((WayFragment)getParentFragment()).getWayData();
                    view.setVisibility(View.GONE);
                    ToastUtil.showToast("添加成功");
                }
            }
        });
    }
}
