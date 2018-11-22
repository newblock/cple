package com.qcx.mini.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.qcx.mini.ConstantString;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemHotWayAdapter;
import com.qcx.mini.adapter.ItemMyJoinGroupAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.HotGroupsEntity;
import com.qcx.mini.entity.WayListEntity;
import com.qcx.mini.listener.LocationListener;
import com.qcx.mini.listener.OnGroupClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.Location;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.QuRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.functions.Action1;

import static com.qcx.mini.net.NetUtil.joinGroup;

/**
 * 已加入的拼车群
 */
public class MyJoinGroupActivity extends BaseActivity {

    private Location location = Location.getInstance();
    private int pageNum = 1;
    private ItemMyJoinGroupAdapter adapter;

    @BindView(R.id.my_join_group_members_list)
    RecyclerView rv_list;
    @BindView(R.id.my_join_group_refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    public int getLayoutID() {
        return R.layout.activity_my_join_group;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("我的拼车群", true, false);
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableAutoLoadmore(false);
        refreshLayout.setRefreshHeader(new QuRefreshHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getWayData(1);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getWayData(pageNum);
            }
        });

        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);

        adapter=new ItemMyJoinGroupAdapter(this);
        rv_list.setAdapter(adapter);
        adapter.setListener(new OnGroupClickListener() {
            @Override
            public void onItemClick(long groupId,int groupType) {
                Intent intent=new Intent(MyJoinGroupActivity.this,WayInfoActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupType",groupType);
                startActivity(intent);
            }

            @Override
            public void onAddClick(long groupId,View view1) {
                joinGroup(groupId,view1);
            }
        });
        getWayData(1);
    }

    public void getWayData(final int page){
        String city= SharedPreferencesUtil.getAppSharedPreferences().getString(ConstantString.SharedPreferencesKey.SP_LOCATION_CITY);
        if(TextUtils.isEmpty(city)){
            mRxPermissions.request("android.permission.ACCESS_COARSE_LOCATION")
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                location.startLocation(new LocationListener() {
                                    @Override
                                    public void onLocationChanged(AMapLocation aMapLocation) {
                                        getWayData(aMapLocation.getCity(),page);
                                    }

                                    @Override
                                    public void onError(int errorCode) {
                                        ToastUtil.showToast("获取位置信息失败");
                                        getWayData("",page);
                                    }

                                    @Override
                                    public void onNoLocationPermission() {
                                        getWayData("",page);
                                    }
                                }, null);
                            } else {
                                getWayData("",page);
                            }
                        }
                    });
        }else {
            getWayData(city,page);
        }
    }

    private void getWayData(String city,final int page){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("currentCity",city);
        params.put("pageNo", page);
        Request.post(URLString.myJoinedGroups, params, new EntityCallback(WayListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                WayListEntity hotGroups= (WayListEntity) t;
                finishLoading();
                if(page==1){
                    pageNum=1;
                    if(hotGroups!=null){
                        adapter.setDatas(hotGroups.getMyJoinedGroups().getGroupsList());
                        LogUtil.i("ddddddddddddd "+adapter.getItemCount());
                    }else {
                        adapter.setDatas(null);
                    }
                }else if(hotGroups!=null){
                    adapter.addDatas(hotGroups.getMyJoinedGroups().getGroupsList());
                }
                if(hotGroups!=null&&hotGroups.getMyJoinedGroups()!=null&&hotGroups.getMyJoinedGroups().getGroupsList().size()>0){
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
        if(refreshLayout!=null){
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
    }
}
