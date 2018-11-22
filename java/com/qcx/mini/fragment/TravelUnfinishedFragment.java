package com.qcx.mini.fragment;

import android.content.Intent;
import android.graphics.Color;
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
import com.qcx.mini.activity.TravelDetail_DriverActivity;
import com.qcx.mini.activity.TravelDetail_NoPayActivity;
import com.qcx.mini.activity.TravelDetail_PassengerActivity;
import com.qcx.mini.activity.TravelNoneActivity;
import com.qcx.mini.adapter.MeTravelsListAdapter;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemMyTravelViewHolder;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.CommentDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.entity.TravelsListEntity;
import com.qcx.mini.entity.UnFinishedTravelsEntity;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.QuCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareTravelEntity;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.NotDriverView;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/9.
 */

public class TravelUnfinishedFragment extends BaseFragment {
    private String mToken = User.getInstance().getToken();
    private int travelType;
    private int pageNum;
    private UnFinishedTravelsEntity travels;
    private SimpleRecyclerViewAdapter<DriverAndTravelEntity> adapter;

    private Handler mHandler = new Handler();

    @BindView(R.id.fragment_travel_unfinished_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_travel_unfinished_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fragment_travel_unfinished_not_driver)
    NotDriverView v_notDriver;

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            travelType = savedInstanceState.getInt("travelType");
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        adapter = new SimpleRecyclerViewAdapter<>(getContext(), ItemMyTravelViewHolder.class, R.layout.item_my_travel);
        adapter.setListener(listener);

        mRecyclerView.setAdapter(adapter);
        QuItemDecoration decoration = new QuItemDecoration(0, UiUtils.getSize(8), UiUtils.getSize(8), UiUtils.getSize(8), 0, UiUtils.getSize(8));
        mRecyclerView.addItemDecoration(decoration);

        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableAutoLoadmore(false);

        refreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()).setSpinnerStyle(SpinnerStyle.Translate));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getTravelsList(pageNum);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getTravelsList(1);
            }
        });

        if (travelType == ConstantValue.TravelType.DRIVER && User.getInstance().getDriverStatus() != ConstantValue.AuthStatus.PASS) {
            v_notDriver.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            v_notDriver.setListener(new OnDriverAuthClickListener(getActivity()));
        } else {
            v_notDriver.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
        showLoadingDialog();
        getTravelsList(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(v_notDriver!=null){
            v_notDriver.setListener(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("travelType", travelType);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_travel_unfinished;
    }


    public void getTravelsList(final int page) {
        Map<String, Object> params = new HashMap<>();
        String url;
        params.put("token", User.getInstance().getToken());
        url = URLString.feedUnFinalListByRole;
        params.put("pageNo", page);
        params.put("role", travelType);

        pageNum = page;
        Request.post(url, params, new QuCallback<UnFinishedTravelsEntity>() {
            @Override
            public void onSuccess(UnFinishedTravelsEntity o) {
                finishLoad(page);

                travels = o;
                if (travels.getUnFinishedTravel() != null && travels.getUnFinishedTravel().size() > 0) {
                    pageNum++;
                    if (page == 1) {
                        adapter.setDatas(travels.getUnFinishedTravel());
                        mHandler.removeCallbacks(runnable);
                        mHandler.postDelayed(runnable, 1000);
                    } else {
                        adapter.addDatas(travels.getUnFinishedTravel());
                    }
                } else {
                    if (page == 1) {
                        adapter.setDatas(null);
                        mHandler.removeCallbacks(runnable);
                    }
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoad(page);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isCu = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isCu = false;
    }

    ItemMyTravelViewHolder.OnMeTravelsClickListener listener = new ItemMyTravelViewHolder.OnMeTravelsClickListener() {
        @Override
        public void onLikesClick(final DriverAndTravelEntity data, final ImageView likeViw, final TextView likeNum) {

            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("travelId", data.getTravelVo().getTravelId());
            String url = "";
            if (data.getTravelVo().isLiked()) {
                url = URLString.travelUnlike;
            } else {
                url = URLString.travelLike;
            }

            Request.post(url, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity entity = (IntEntity) t;
                    if (entity.getStatus() == 200) {
                        if (data.getTravelVo().isLiked()) {
                            likeViw.setImageResource(R.mipmap.btn_good_mini);
                            data.getTravelVo().setLikesNum(data.getTravelVo().getLikesNum() - 1);
                            likeNum.setText(String.valueOf(data.getTravelVo().getLikesNum()));
                            data.getTravelVo().setLiked(false);
                        } else {
                            likeViw.setImageResource(R.mipmap.btn_goodyes_mini);
                            data.getTravelVo().setLikesNum(data.getTravelVo().getLikesNum() + 1);
                            likeNum.setText(String.valueOf(data.getTravelVo().getLikesNum()));
                            data.getTravelVo().setLiked(true);
                            ToastUtil.showToast("点赞成功");
                        }
                    }
                }

                @Override
                public void onError(String errorInfo) {

                }
            });
        }

        @Override
        public void onMessageClick(final DriverAndTravelEntity data, final TextView messageNum) {

            try {
                new CommentDialog().initData(data.getTravelVo().getCommentsNum(), data.getTravelVo().getTravelPhone(), data.getTravelVo().getTravelId())
                        .setListener(new CommentDialog.OnCommentSuccessListener() {
                            @Override
                            public void onSuccess(int commentNum) {
                                data.getTravelVo().setCommentsNum(data.getTravelVo().getCommentsNum() + 1);
                                messageNum.setText(String.valueOf(data.getTravelVo().getCommentsNum()));
                            }
                        }).show(getFragmentManager(), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onShareClick(DriverAndTravelEntity data) {
            ShareTravelEntity travel = new ShareTravelEntity();
            TravelEntity tra = data.getTravelVo();
            travel.setStart(tra.getStart());
            travel.setEnd(tra.getEnd());
            travel.setStartAddress(tra.getStartAddress());
            travel.setEndAddress(tra.getEndAddress());
            travel.setIcon(data.getMyPicture());
            travel.setName(data.getMyNickname());
            travel.setPrice(tra.getTravelPrice());
            travel.setSeatsNum(String.valueOf(tra.getSeats()));
            travel.setTravelId(tra.getTravelId());
            travel.setTravelType(tra.getType());
            travel.setStartTime(tra.getStartTimeTxt());
            travel.setSurplusSeats(tra.getSurplusSeats());
            travel.setWaypoints(tra.getWaypoints());
            ShareUtil.shareTravel(getFragmentManager(), User.getInstance().getPhoneNumber(), travel);
        }

        @Override
        public void onTravelOperClick(DriverAndTravelEntity data) {

            if (data == null) {
                return;
            }
            Intent intent = new Intent(getContext(), TravelDetail_NoPayActivity.class);
            intent.putExtra("travelId", data.getTravelVo().getTravelId());
            intent.putExtra("travelType", data.getTravelVo().getType());
            getActivity().startActivity(intent);
        }

        @Override
        public void onTravelClick(DriverAndTravelEntity data) {
            Intent intent;
            TravelEntity travel = data.getTravelVo();
            switch (travel.getStatus()) {
                case 0://正在寻找乘客
                    intent = new Intent(getContext(), TravelNoneActivity.class);
                    intent.putExtra("travelId", travel.getTravelId());
                    intent.putExtra("travelType", ConstantValue.TravelType.DRIVER);
                    startActivity(intent);
                    break;
                case 1://有乘客下单
                case 2://已发车，行程中
                case 9://车主抢单，乘客未支付
                case 11://车主抢单，乘客已支付
                case 12://车主抢单，已发车，行车中
                    intent = new Intent(getContext(), TravelDetail_DriverActivity.class);
                    intent.putExtra("travelID", travel.getTravelId());
                    startActivity(intent);
                    break;

                case 3://乘客发布的行程，等待接单
                    intent = new Intent(getContext(), TravelNoneActivity.class);
                    intent.putExtra("travelId", travel.getTravelId());
                    intent.putExtra("travelType", ConstantValue.TravelType.PASSENGER);
                    startActivity(intent);
                    break;
                case 6://乘客订的车主的行程，已支付
                case 7://乘客订的车主的行程，已发车
                case 8://乘客订的车主的行程，行程中
                    intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                    intent.putExtra("ordersID", travel.getOrdersTravelId());
                    startActivity(intent);
                    break;
                case 4://乘客发布的行程，车主已抢单，等待支付
                    intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                    intent.putExtra("travelID", travel.getTravelId());
                    startActivity(intent);
                    break;
                case 5://乘客订的车主的行程，未支付
//                    startActivity(new Intent(getContext(), PayActivity.class));
                    intent = new Intent(getContext(), TravelDetail_PassengerActivity.class);
                    intent.putExtra("ordersID", travel.getOrdersTravelId());
                    intent.putExtra("travelID", travel.getTravelId());
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onAttentionClick(DriverAndTravelEntity data, ImageView iv_attention) {

            if (data.isAttention()) {
                cancelAttention(data, iv_attention);
            } else {
                attention(data, iv_attention);
            }
        }
    };


    private void attention(final DriverAndTravelEntity data, final ImageView iv_attention) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());//attentione
        params.put("attentione", data.getPhone());
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

    private void cancelAttention(final DriverAndTravelEntity data, final ImageView iv_attention) {
        DialogUtil.unfollowDialog(getActivity(), data.getPhone(), new EntityCallback(IntEntity.class) {
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


    boolean isCu = true;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isCu) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    mHandler.postDelayed(this, 1000);
                }
            }
        }
    };

    private void finishLoad(int page) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
        hideLoadingDialog();
    }


    public void setTravelType(int travelType) {
        this.travelType = travelType;
    }
}
