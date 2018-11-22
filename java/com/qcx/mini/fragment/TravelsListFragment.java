package com.qcx.mini.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.PayActivity;
import com.qcx.mini.activity.TravelDetail_DriverActivity;
import com.qcx.mini.activity.TravelDetail_NoPayActivity;
import com.qcx.mini.activity.TravelDetail_PassengerActivity;
import com.qcx.mini.activity.TravelHistoryActivity;
import com.qcx.mini.activity.TravelNoneActivity;
import com.qcx.mini.adapter.MeTravelsListAdapter;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.CommentDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.PayDialog;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.TravelsListEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareTravelEntity;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
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

/**
 * Created by Administrator on 2017/12/18.
 */

public class TravelsListFragment extends BaseFragment {
    private String mToken = User.getInstance().getToken();
    //    private CommentView mCommentView;
    private Handler mHandler = new Handler();
    private RecyclerView mRecyclerView;
    private MeTravelsListAdapter meTravelsListAdapter;
    private SmartRefreshLayout refreshLayout;
    private int pageNum = 1;
    private TravelsListEntity travels;
    private MeFragment meFragment;
    private long phone;
    private boolean isOrther;
    private int travelType=ConstantValue.TravelType.PASSENGER;
    private MyItemDecoration decoration = new MyItemDecoration();

    @BindView(R.id.view_travel_type_passenger)
    TextView tv_passenger;
    @BindView(R.id.view_travel_type_driver)
    TextView tv_driver;

    @OnClick(R.id.view_travel_type_passenger)
    void onPassenger(){
        if(travelType!=ConstantValue.TravelType.PASSENGER){
            travelType=ConstantValue.TravelType.PASSENGER;
            getTravelsList(1);
        }
        setTitleBack(travelType);
    }
    @OnClick(R.id.view_travel_type_driver)
    void onDriver(){
        if(travelType!=ConstantValue.TravelType.DRIVER){
            travelType=ConstantValue.TravelType.DRIVER;
            getTravelsList(1);
        }
        setTitleBack(travelType);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_travels_list;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mToken != null && !mToken.equals(User.getInstance().getToken())) {
                getTravelsList(1);
                mToken = User.getInstance().getToken();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mToken = "";
        isCu = true;
        if (isVisible()) {
            LogUtil.i("TravelsListFragment isVisivle");
            if (mToken != null && !mToken.equals(User.getInstance().getToken())) {
                getTravelsList(1);
                mToken = User.getInstance().getToken();
            }
        }
    }

    public void getTravelsList(final int page) {
        Map<String, Object> params = new HashMap<>();
        String url;
        params.put("token", User.getInstance().getToken());
        if (isOrther) {
            url = URLString.otherTravel;
            params.put("otherPhone", phone);
            params.put("travelType", travelType);
        } else {
            url = URLString.myTravel;
            params.put("pageNo", page);
            params.put("travelType", travelType);
        }
        setTitleBack(travelType);
        pageNum = page;
        Request.post(url, params, new EntityCallback(TravelsListEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoad(page);
                travels = (TravelsListEntity) t;
                if (travels.getResult() != null && travels.getResult().size() > 0) {
                    pageNum++;
                    if (page == 1) {
                        meTravelsListAdapter.setDatas(travels.getResult());
                        mHandler.removeCallbacks(runnable);
                        mHandler.postDelayed(runnable, 1000);
                    } else {
                        meTravelsListAdapter.addDatas(travels.getResult());
                    }
                } else {
                    if (page == 1) {
                        meTravelsListAdapter.setDatas(null);
                        mHandler.removeCallbacks(runnable);
                    }
                }
                meTravelsListAdapter.setBottomeView(travels.getHistoryTravel());
            }

            @Override
            public void onError(String errorInfo) {
                if (!TextUtils.isEmpty(errorInfo)) ToastUtil.showToast(errorInfo);
                finishLoad(page);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        isCu = false;
    }

    boolean isCu = true;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCu) {
                if (meTravelsListAdapter != null)
                    meTravelsListAdapter.dateChanged();
                mHandler.postDelayed(this, 1000);
            }
        }
    };
    private void finishLoad(int page) {
        if (refreshLayout != null) refreshLayout.finishLoadmore();
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        try {
            meFragment = (MeFragment) getParentFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        refreshLayout = view.findViewById(R.id.fragment_travels_refresh);

        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableAutoLoadmore(false);

        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                if (isOrther) {//TODO:为什么要写这段代码呢。。。
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast("没有更多了");
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                refreshlayout.finishLoadmore();
                            }
                        }
                    }).start();
                } else {
                    getTravelsList(pageNum);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getTravelsList(pageNum);
            }
        });


        mRecyclerView = view.findViewById(R.id.fragment_travels_list);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        meTravelsListAdapter = new MeTravelsListAdapter(getContext());
        meTravelsListAdapter.setOrther(isOrther);
        meTravelsListAdapter.setListener(listener);
        mRecyclerView.setAdapter(meTravelsListAdapter);
        mRecyclerView.addItemDecoration(decoration);
        getTravelsList(1);
    }

    MeTravelsListAdapter.OnMeTravelsClicklistener listener = new MeTravelsListAdapter.OnMeTravelsClicklistener() {
        @Override
        public void onLikesClick(final TravelsListEntity.TravelEntity data, final ImageView likeViw, final TextView likeNum) {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("travelId", data.getTravelId());
            String url = "";
            if (data.isLiked()) {
                url = URLString.travelUnlike;
            } else {
                url = URLString.travelLike;
            }

            Request.post(url, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    try {
                        IntEntity entity = (IntEntity) t;
                        if (entity.getStatus() == 200) {
                            if (data.isLiked()) {
                                likeViw.setImageResource(R.mipmap.btn_good_mini);
                                data.setLikesNum(data.getLikesNum() - 1);
                                likeNum.setText(String.valueOf(data.getLikesNum()));
                                data.setLiked(false);
                            } else {
                                likeViw.setImageResource(R.mipmap.btn_goodyes_mini);
                                data.setLikesNum(data.getLikesNum() + 1);
                                likeNum.setText(String.valueOf(data.getLikesNum()));
                                data.setLiked(true);
                                ToastUtil.showToast("点赞成功");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError("");
                    }
                }

                @Override
                public void onError(String errorInfo) {

                }
            });
        }

        @Override
        public void onMessageClick(final TravelsListEntity.TravelEntity entity, final TextView messageView) {
            try {
                new CommentDialog().initData(entity.getCommentsNum(), entity.getTravelPhone(), entity.getTravelId())
                        .setListener(new CommentDialog.OnCommentSuccessListener() {
                            @Override
                            public void onSuccess(int commentNum) {
                                entity.setCommentsNum(entity.getCommentsNum() + 1);
                                messageView.setText(String.valueOf(entity.getCommentsNum()));
                            }
                        }).show(getFragmentManager(), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onShareClick(TravelsListEntity.TravelEntity data) {
            ShareTravelEntity travel = new ShareTravelEntity();
            travel.setStart(data.getStart());
            travel.setEnd(data.getEnd());
            travel.setStartAddress(data.getStartAddress());
            travel.setEndAddress(data.getEndAddress());
            travel.setIcon(travels.getPicture());
            travel.setName(travels.getNickName());
            travel.setPrice(data.getTravelPrice());
            travel.setSeatsNum(String.valueOf(data.getSeats()));
            travel.setTravelId(data.getTravelId());
            travel.setTravelType(data.getType());
            travel.setStartTime(data.getStartTimeTxt());
            travel.setSurplusSeats(data.getSurplusSeats());
            travel.setWaypoints(data.getWaypoints());
            ShareUtil.shareTravel(getFragmentManager(), User.getInstance().getPhoneNumber(), travel);
        }

        @Override
        public void onTravelOperClick(TravelsListEntity.TravelEntity data) {
            if(data==null) {
                return;
            }
            Intent intent=new Intent(getContext(), TravelDetail_NoPayActivity.class);
            intent.putExtra("travelId",data.getTravelId());
            intent.putExtra("travelType",data.getType());
            getActivity().startActivity(intent);
        }

        @Override
        public void onTravelClick(TravelsListEntity.TravelEntity data) {
            if(isOrther){
                onTravelOperClick(data);
                return;
            }
            Intent intent;
            switch (data.getStatus()) {
                case 0://正在寻找乘客
                    intent = new Intent(getContext(), TravelNoneActivity.class);
                    intent.putExtra("travelId", data.getTravelId());
                    intent.putExtra("travelType", ConstantValue.TravelType.DRIVER);
                    startActivity(intent);
                    break;
                case 1://有乘客下单
                case 2://已发车，行程中
                case 9://车主抢单，乘客未支付
                case 11://车主抢单，乘客已支付
                case 12://车主抢单，已发车，行车中
                    intent = new Intent(getContext(), TravelDetail_DriverActivity.class);
                    intent.putExtra("travelID", data.getTravelId());
                    startActivity(intent);
                    break;

                case 3://乘客发布的行程，等待接单
                    intent = new Intent(getContext(), TravelNoneActivity.class);
                    intent.putExtra("travelId", data.getTravelId());
                    intent.putExtra("travelType", ConstantValue.TravelType.PASSENGER);
                    startActivity(intent);
                    break;
                case 6://乘客订的车主的行程，已支付
                case 7://乘客订的车主的行程，已发车
                case 8://乘客订的车主的行程，行程中
                    intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                    intent.putExtra("ordersID", data.getOrdersTravelId());
                    startActivity(intent);
                    break;
                case 4://乘客发布的行程，车主已抢单，等待支付
                    intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                    intent.putExtra("travelID", data.getTravelId());
                    startActivity(intent);
                    break;
                case 5://乘客订的车主的行程，未支付
//                    startActivity(new Intent(getContext(), PayActivity.class));
                    intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                    intent.putExtra("ordersID", data.getOrdersTravelId());
                    intent.putExtra("travelID", data.getTravelId());
                    startActivity(intent);
//                new PayDialog()
//                        .setListener(new PayDialog.PayDialogListener() {
//                            @Override
//                            public void paySuccess(String ordersId) {
//                                getTravelsList(1);
//                            }
//
//                            @Override
//                            public void cancelledOrder() {
//                                getTravelsList(1);
//                            }
//
//                            @Override
//                            public void payOvertime(PayDialog dialog) {
//                                dialog.dismiss();
//                                getTravelsList(1);
//                            }
//                        })
//                        .show(getFragmentManager(),"");
                    break;
            }
        }

        @Override
        public void onAttentionClick(TravelsListEntity.TravelEntity data, ImageView iv_attention) {
            if (data.isAttention()) {
                cancelAttention(data, iv_attention);
            } else {
                attention(data, iv_attention);
            }
        }
    };


    private void attention(final TravelsListEntity.TravelEntity data, final ImageView iv_attention) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());//attentione
        params.put("attentione", data.getDriverInfo().getPhone());
        Request.post(URLString.changAttention, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(true);
                    iv_attention.setImageResource(R.mipmap.btn_followed_mini);
                    new CenterImgDialog().show(getFragmentManager(), "");
                } else {
                    onError("操作失败");
                }
            }

        });
    }

    private void cancelAttention(final TravelsListEntity.TravelEntity data, final ImageView iv_attention) {
        DialogUtil.unfollowDialog(getActivity(), data.getDriverInfo().getPhone(), new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity entity = (IntEntity) t;
                if (entity.getStatus() == 200) {
                    data.setAttention(false);
                    iv_attention.setImageResource(R.mipmap.btn_follow_mini);
                } else {
                    onError("操作失败");
                }
            }
        });
    }

    public void setPhone(long phone, boolean isOrther) {
        this.phone = phone;
        this.isOrther = isOrther;
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration{
        private int startPosition;
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            if(position>=startPosition){

                outRect.top = UiUtils.getSize(15);
                outRect.left=UiUtils.getSize(8);
                outRect.right=UiUtils.getSize(8);
                if (position == state.getItemCount() - 1) {
                    outRect.bottom = UiUtils.getSize(20);
                }
            }
        }

        public void setStartPosition(int position){
            this.startPosition=position;
        }
    }

    private void setTitleBack(int travelType){
        if (meTravelsListAdapter!=null){
            meTravelsListAdapter.setTravelType(travelType);
        }
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
