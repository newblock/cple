package com.qcx.mini.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.AddBankAccountActivity;
import com.qcx.mini.activity.BalanceInfoActivity;
import com.qcx.mini.activity.CashAccountChangeActivity;
import com.qcx.mini.activity.WithdrawActivity;
import com.qcx.mini.adapter.MeWalletAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.entity.WalletEntity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/18.
 *
 */

public class MeWalletFragment extends BaseFragment {
    private String mToken=User.getInstance().getToken();
    private final static int ACCOUNT_ACANGE_CODE = 3;
    private final static int WITH_DRAW_SUCCESS_CODE = 4;
    private RecyclerView mRecyclerView;
    private MeWalletAdapter mAdapter;
    private SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private MeFragment meFragment;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_me_wallet;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i("ttttttttttttttt onHiddenChanged="+hidden);
        mToken="";
        if(!hidden){
            if(mToken!=null&&!mToken.equals(User.getInstance().getToken())){
                getData(1);
                mToken=User.getInstance().getToken();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mToken="";
        if(isVisible()){
            LogUtil.i("MeWalletFragment isVisivle");
            if(mToken!=null&&!mToken.equals(User.getInstance().getToken())){
                getData(1);
                mToken=User.getInstance().getToken();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            WalletEntity entity;
            switch (requestCode) {
                case ACCOUNT_ACANGE_CODE:
                    entity = mAdapter.getWalletEntity();
                    if (entity != null) {
                        if (data != null) {
                            String act = data.getStringExtra("account");
                            entity.setAliPay(act);
                        }
                    }
                    break;
                case WITH_DRAW_SUCCESS_CODE:
                    entity = mAdapter.getWalletEntity();
                    if (entity != null) {
                        if (data != null) {
                            double money = data.getDoubleExtra("money", 0);
                            String act = data.getStringExtra("account");
                            entity.setAliPay(act);
                            if(money!=entity.getMoneyCard()){
                                entity.setMoneyCard(money);
                                getData(1);
                            }
                        }
                    }
                    break;
            }
        }
    }


    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        try{
            meFragment=(MeFragment) getParentFragment();
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        refreshLayout = view.findViewById(R.id.me_wallet_refresh);
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

        mRecyclerView = view.findViewById(R.id.me_wallet_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new MeWalletAdapter(getContext());
        mAdapter.setHeadView();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new MeWalletAdapter.OnWalletItemClickListener() {
            @Override
            public void onAccountClick(String account) {
                Intent intent = new Intent(getContext(), CashAccountChangeActivity.class);
                intent.putExtra("account", account);
                startActivityForResult(intent, ACCOUNT_ACANGE_CODE);
            }

            @Override
            public void onWithdrawClick(String account, double money) {
//                Intent intent = new Intent(getContext(), WithdrawActivity.class);
//                intent.putExtra("money", money);
//                intent.putExtra("account", account);
//                startActivityForResult(intent, WITH_DRAW_SUCCESS_CODE);
                Intent intent=new Intent(getContext(), BalanceInfoActivity.class);
                startActivity(intent);
            }
        });
        getData(1);
    }

    private List<WalletEntity.MoneyEntity> getDatas() {
        List<WalletEntity.MoneyEntity> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            WalletEntity.MoneyEntity entity = new WalletEntity.MoneyEntity();
            datas.add(entity);
        }
        return datas;
    }

    public void getData(final int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("pageNo", page);
        pageNum=page;
        Request.post(URLString.money, params, new EntityCallback(WalletEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                finishLoad(page);
                WalletEntity wallet = (WalletEntity) t;
                if (page == 1) {
                    mAdapter.setData(wallet);
                } else {
                    if (wallet.getWaterBill() != null && wallet.getWaterBill().size() > 0) {
                        mAdapter.addDatas(wallet.getWaterBill());
                    }
                }
                if(wallet.getWaterBill()!=null&&wallet.getWaterBill().size()>0){
                    pageNum++;
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
                finishLoad(page);
            }
        });
    }

    private void finishLoad(int page){
        if(page==1){

        }else {
            if(refreshLayout!=null) refreshLayout.finishLoadmore();
        }
    }
}
