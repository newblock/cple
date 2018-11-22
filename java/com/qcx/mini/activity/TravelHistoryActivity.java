package com.qcx.mini.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.MeTravelsListAdapter;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.entity.TravelHistoryEntity;
import com.qcx.mini.entity.TravelsListEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.itemDecoration.ItemGrayDecoration;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelHistoryActivity extends BaseActivity {
    private int page = 1;
    private MeTravelsListAdapter meTravelsListAdapter;
    private int travelType=ConstantValue.TravelType.PASSENGER;
    @BindView(R.id.travel_history_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.travel_history_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.travel_history_noData)
    View v_noData;

    @BindView(R.id.view_travel_type_passenger)
    TextView tv_passenger;
    @BindView(R.id.view_travel_type_driver)
    TextView tv_driver;

    @OnClick(R.id.view_travel_type_passenger)
    void onPassenger(){
        if(travelType!= ConstantValue.TravelType.PASSENGER){
            travelType=ConstantValue.TravelType.PASSENGER;
            meTravelsListAdapter.setDatas(null);
            getHistoryData(1);
        }
        setTitleBack(travelType);
    }

    @OnClick(R.id.view_travel_type_driver)
    void onDriver(){
        if(travelType!=ConstantValue.TravelType.DRIVER){
            travelType=ConstantValue.TravelType.DRIVER;
            meTravelsListAdapter.setDatas(null);
            getHistoryData(1);
        }
        setTitleBack(travelType);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_history;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("历史行程", true, false);
        travelType=getIntent().getIntExtra("travelType",ConstantValue.TravelType.PASSENGER);
        setTitleBack(travelType);
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(true);

        refreshLayout.setRefreshHeader(new ClassicsHeader(this).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getHistoryData(page);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                getHistoryData(page);
            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        meTravelsListAdapter = new MeTravelsListAdapter(this);
        meTravelsListAdapter.setType(MeTravelsListAdapter.TYPE_HISTORY);
        mRecyclerView.setAdapter(meTravelsListAdapter);
//        ItemGrayDecoration decoration=new ItemGrayDecoration(8,getResources().getColor(R.color.gray_line));
        QuItemDecoration decoration=new QuItemDecoration();
        decoration.setTop(UiUtils.getSize(15));
        decoration.setLeft(UiUtils.getSize(8));
        decoration.setRight(UiUtils.getSize(8));
        decoration.setLastBottom(UiUtils.getSize(20));
        mRecyclerView.addItemDecoration(decoration);
        meTravelsListAdapter.setListener(new MeTravelsListAdapter.OnMeTravelsClicklistener() {
            @Override
            public void onLikesClick(TravelsListEntity.TravelEntity data, ImageView likeViw, TextView likeNum) {

            }

            @Override
            public void onMessageClick(TravelsListEntity.TravelEntity data, TextView messageNum) {

            }

            @Override
            public void onShareClick(TravelsListEntity.TravelEntity data) {

            }

            @Override
            public void onTravelOperClick(TravelsListEntity.TravelEntity data) {

            }

            @Override
            public void onTravelClick(TravelsListEntity.TravelEntity data) {
                if (data.getType() == 0) {
                    Intent intent = new Intent(TravelHistoryActivity.this, TravelHistoryDetailActivity.class);
                    intent.putExtra("travelId", data.getTravelId());
                    intent.putExtra("travelType", ConstantValue.TravelType.DRIVER);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(TravelHistoryActivity.this, TravelHistoryDetailActivity.class);
                    intent.putExtra("ordersId", data.getOrdersId());
                    intent.putExtra("travelType", ConstantValue.TravelType.PASSENGER);
                    startActivity(intent);
                }
            }

            @Override
            public void onAttentionClick(TravelsListEntity.TravelEntity data, ImageView iv_attention) {

            }
        });

        showLoadingDialog();
        getHistoryData(1);

    }

    void getHistoryData(final int pageNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo", pageNo);
        params.put("travelType", travelType);
        page=pageNo;
        Request.post(URLString.travelHistory, params, new EntityCallback(TravelHistoryEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                finishLoad(pageNo);
                TravelHistoryEntity entity = (TravelHistoryEntity) t;
                if (entity.getHistory() != null && entity.getHistory().size() > 0) {
                    if (pageNo == 1) {
                        meTravelsListAdapter.setDatas(entity.getHistory());
                    } else {
                        meTravelsListAdapter.addDatas(entity.getHistory());
                    }
                    page++;
                }else {
                    if(pageNo==1){
                        meTravelsListAdapter.setDatas(null);
                    }
                }
                showData();
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
                finishLoad(page);
                hideLoadingDialog();
                showData();
            }
        });
    }

    private void finishLoad(int page) {
        if (page == 1) {
            if (refreshLayout != null) refreshLayout.finishRefresh();
        } else {
            if (refreshLayout != null) refreshLayout.finishLoadmore();
        }
    }

    private void showData() {
        if (meTravelsListAdapter == null || meTravelsListAdapter.getItemCount() == 0) {
            v_noData.setVisibility(View.VISIBLE);
        } else {
            v_noData.setVisibility(View.GONE);
        }
    }

    private void setTitleBack(int travelType){
        if(travelType==ConstantValue.TravelType.DRIVER){
            tv_driver.setTextColor(Color.WHITE);
            tv_driver.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray_6));
            tv_passenger.setTextColor(0xFF939499);
            tv_passenger.setBackgroundColor(Color.TRANSPARENT);
        }else {
            tv_passenger.setTextColor(Color.WHITE);
            tv_passenger.setBackground(getResources().getDrawable(R.drawable.bg_circular_gray_6));
            tv_driver.setTextColor(0xFF939499);
            tv_driver.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
