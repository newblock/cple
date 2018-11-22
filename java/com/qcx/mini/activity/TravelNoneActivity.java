package com.qcx.mini.activity;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.adapter.ItemDriverAndTravelAdapter;
import com.qcx.mini.adapter.viewHolder.ItemTravelNoneShareViewHolder;
import com.qcx.mini.adapter.viewHolder.ItemTravelViewHolder;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.ItemsDialog;
import com.qcx.mini.dialog.PassengerReleaseTimeDialog;
import com.qcx.mini.dialog.SingleWheelDialog;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.Entity;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.MatchTravelEntity;
import com.qcx.mini.entity.TravelEntity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.listener.ItemDriverAndTravelClickListenerImp;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.share.ShareTravelEntity;
import com.qcx.mini.share.ShareUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.QuRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TravelNoneActivity extends BaseActivity {
    private ItemDriverAndTravelAdapter adapter;
    private MatchTravelEntity matchData;
    private ItemTravelViewHolder myViewHolder;
    private long travelId;
    private int travelType;
    private int page=1;

    @BindView(R.id.travel_none_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.travel_none_list)
    RecyclerView rl_list;
    @BindView(R.id.travel_none_no_data)
    TextView tv_noData;
    @BindView(R.id.travel_none_my_travel_info)
    View v_travel;
    @BindView(R.id.travel_none_show_travel_info_img)
    ImageView iv_showTravel;
    @BindView(R.id.travel_none_travel_info_title_text)
    TextView tv_travelInfo;
    @BindView(R.id.travel_none_match_num)
    TextView tv_matchNum;

    boolean isShowTravel=true;
    @OnClick({R.id.travel_none_travel_info_title,R.id.travel_none_my_close_travel_info,R.id.travel_none_my_travel_info})
    void onTravelTitleClick(){
        if(isShowTravel){
            v_travel.animate().translationY(-v_travel.getHeight()).setDuration(200).setListener(travelAnim);
            isShowTravel=false;
            iv_showTravel.setVisibility(View.INVISIBLE);
        }else {
            v_travel.animate().translationY(0).setDuration(200).setListener(travelAnim);
            isShowTravel=true;
        }
    }

    Animator.AnimatorListener travelAnim=new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if(isShowTravel){
                iv_showTravel.setVisibility(View.INVISIBLE);
            }else {
                iv_showTravel.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.activity_travel_none;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        travelId = getIntent().getLongExtra("travelId", -1);
        travelType = getIntent().getIntExtra("travelType", 0);
        initTitle(travelType);
        QuRefreshHeader refreshHeader = new QuRefreshHeader(this);
        refreshLayout.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        refreshLayout.setRefreshHeader(refreshHeader);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(travelId,page);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(travelId,1);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rl_list.setLayoutManager(manager);
        adapter = new ItemDriverAndTravelAdapter(this);
        rl_list.setAdapter(adapter);
        adapter.setHeadViewHolder(ItemTravelNoneShareViewHolder.class, R.layout.item_travel_none_head, 1);
        adapter.setListener(new ItemDriverAndTravelClickListenerImp(this));
        adapter.setHeadListener(new ItemTravelNoneShareViewHolder.OnShareTravelClickListener() {
            @Override
            public void share(DriverAndTravelEntity data) {

                if (data == null || data.getTravelVo() == null) {
                    return;
                }
                TravelEntity travelEntity = data.getTravelVo();
                ShareTravelEntity travel = new ShareTravelEntity();
                travel.setStart(travelEntity.getStart());
                travel.setEnd(travelEntity.getEnd());
                travel.setStartAddress(travelEntity.getStartAddress());
                travel.setEndAddress(travelEntity.getEndAddress());
                travel.setIcon(data.getPicture());
                travel.setName(data.getNickName());
                travel.setPrice(travelEntity.getTravelPrice());
                travel.setSeatsNum(String.valueOf(travelEntity.getSeats()));
                travel.setTravelId(travelEntity.getTravelId());
                travel.setTravelType(travelEntity.getType());
                travel.setStartTime(travelEntity.getStartTimeTxt());
                travel.setCar(data.getCar());
                travel.setSurplusSeats(data.getTravelVo().getSurplusSeats());
                travel.setAge(data.getAge());
                travel.setSex(data.getSex());
                travel.setWaypoints(travelEntity.getWaypoints());
                ShareUtil.shareTravel(getSupportFragmentManager(), User.getInstance().getPhoneNumber(), travel);
            }
        });
        rl_list.addItemDecoration(decoration);
        myViewHolder=new ItemTravelViewHolder(v_travel);
        v_travel.post(new Runnable() {
            @Override
            public void run() {
                v_travel.animate().translationY(-v_travel.getHeight()).setDuration(0);
                isShowTravel=false;
            }
        });

        rl_list.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(isShowTravel){
                    onTravelTitleClick();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    public void onTitleRightClick(View v) {
        String[] items;
        if(travelType==ConstantValue.TravelType.DRIVER){
            items = new String[]{"修改时间","修改座数","取消行程","客服"};
        }else {
            items = new String[]{"修改时间","修改人数","取消行程","客服"};
        }

        final ItemsDialog dialog = new ItemsDialog(this, items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new ItemsDialog.OnItemClick() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        long start=0;
                        if(matchData!=null&&matchData.getSelfTravel()!=null&&matchData.getSelfTravel().getTravelVo()!=null){
                            start=matchData.getSelfTravel().getTravelVo().getStartTime();
                        }
                        startCTime(start);
                        break;
                    case 1:
                        seatNum();
                        break;
                    case 2:
                        cancelTravel();
                        break;
                    case 3:
                        DialogUtil.call(TravelNoneActivity.this,null);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(travelId, 1);
    }

    private void getData(long travelId, final int pageNum) {
        page=pageNum;
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        params.put("pageNo", pageNum);

        String url = "";
        if (travelType == ConstantValue.TravelType.DRIVER) {
            url = URLString.matchTravelPassengers;
            params.put("role", 1);
        } else {
            url = URLString.matchTravelDrivers;
            params.put("role", 0);//{"role":0,"travelId":81516125057390,"pageNo":1,"token":"e92fb26c4cd61bbf0a7811a04294258e"}
        }
        Request.post(url, params, new EntityCallback(MatchTravelEntity.class) {
            @Override
            public void onSuccess(Object t) {
                finishLoading();
                matchData = (MatchTravelEntity) t;
                if (matchData != null) {
                    if(pageNum==1){
                        if (travelType == ConstantValue.TravelType.DRIVER) {
                            adapter.setDatas(matchData.getMatchTravelPassengers().getMatchTravelPassengers());
                            tv_matchNum.setText(String.format(Locale.CHINA,"%d位顺路乘客",matchData.getMatchTravelPassengers().getMatchTravelNum()));
                        } else {
                            tv_matchNum.setText(String.format(Locale.CHINA,"%d位顺路车主",matchData.getMatchTravelDrivers().getMatchTravelNum()));
                            adapter.setDatas(matchData.getMatchTravelDrivers().getTravelResults());
                        }
                        List<Entity> objects = new ArrayList<>();
                        myViewHolder.bindData(matchData.getSelfTravel().getTravelVo());
                        objects.add(matchData.getSelfTravel());
                        adapter.setHeadData(objects);
                        setTitleTravelInfo(matchData.getSelfTravel());
                    }else {
                        if (travelType == ConstantValue.TravelType.DRIVER) {
                            adapter.addDatas(matchData.getMatchTravelPassengers().getMatchTravelPassengers());
                        } else {
                            adapter.addDatas(matchData.getMatchTravelDrivers().getTravelResults());
                        }
                    }
                    if((travelType == ConstantValue.TravelType.PASSENGER&&matchData.getMatchTravelDrivers()!=null&&matchData.getMatchTravelDrivers().getTravelResults()!=null&&matchData.getMatchTravelDrivers().getTravelResults().size()>0)
                            ||(travelType == ConstantValue.TravelType.DRIVER&&matchData.getMatchTravelPassengers()!=null&&matchData.getMatchTravelPassengers().getMatchTravelPassengers()!=null&&matchData.getMatchTravelPassengers().getMatchTravelPassengers().size()>0)){
                        page++;
                    }

                }
                showNoData();
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                finishLoading();
                showNoData();
            }
        });
    }

    private void finishLoading(){
        if(refreshLayout!=null){
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
        }
        hideLoadingDialog();
    }

    private void initTitle(int type) {
        String title = " ";
        if (type == ConstantValue.TravelType.PASSENGER) {
            title = "等待车主接单";
        } else if (type == ConstantValue.TravelType.DRIVER) {
            title = "等待乘客订座";
        }
        initTitleBar(title, true, true);
    }

    private void setTitleTravelInfo(DriverAndTravelEntity data){
        if(data!=null&&data.getTravelVo()!=null){
            StringBuilder builder=new StringBuilder();
            builder.append(DateUtil.formatDay("MM-dd ",data.getTravelVo().getStartTime()));
            builder.append(DateUtil.getTimeStr(data.getTravelVo().getStartTime(),"HH:mm"));
            builder.append(" · ");
            builder.append(data.getTravelVo().getSeats());
            if(data.getTravelVo().getType()==ConstantValue.TravelType.DRIVER){
                builder.append("座");
                builder.append(" · ");
                builder.append(data.getTravelVo().getTravelPrice());
                builder.append("元/座");
            }else {
                builder.append("人");
                builder.append(" · ");
                builder.append(data.getTravelVo().getTravelPrice());
                builder.append("元");
            }
            tv_travelInfo.setText(builder);
        }else {
            tv_travelInfo.setText("");
        }
    }

    private void cancelPassengerTravel() {
        Map<String, Object> params = new HashMap<>();
        String urlStr = "";
        params.put("token", User.getInstance().getToken());
        params.put("passengerTravelId", travelId);
        urlStr = URLString.travelCancel;
        Request.post(urlStr, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == -110) {
                    ToastUtil.showToast("今日取消行程次数已达上限");
                } else if (intEntity.getStatus() == 110) {
                    ToastUtil.showToast("取消行程成功");
                    finish();
                } else if (intEntity.getStatus() == -123) {
                    ToastUtil.showToast("已发车的行程不能取消");
                }
            }
        });
    }

    private void cancelDriverTravel() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("travelId", travelId);
        Request.post(URLString.travel_cancel_drevier, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity = (IntEntity) t;
                if (intEntity.getStatus() == 200) {
                    ToastUtil.showToast("取消成功");
                    finish();
                } else {
                    onError("取消失败");
                }
            }
        });
    }

    private void cancelTravel() {
        if (travelType == ConstantValue.TravelType.PASSENGER) {
            cancelPassengerTravel();
        } else {
            cancelDriverTravel();
        }
    }

    private void showNoData(){
        if(adapter==null||adapter.getItemCount()<=1){
            tv_noData.setVisibility(View.VISIBLE);
        }else {
            tv_noData.setVisibility(View.GONE);
        }
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

    private MyItemDecoration decoration = new MyItemDecoration();

    boolean isUpdatingTravel=false;
    void updateTravel(String startTime,int seats){
        if(matchData==null){
            return;
        }
        if(isUpdatingTravel){
            ToastUtil.showToast("您操作太快啦!");
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("travelId",travelId);
        params.put("type",travelType);
        if(!TextUtils.isEmpty(startTime)){
            params.put("startTime",startTime);
        }
        if(seats>0){
            params.put("seats",seats);
        }
        isUpdatingTravel=true;
        showLoadingDialog();
        Request.post(URLString.updateTravel, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                isUpdatingTravel=false;
                IntEntity intEntity= (IntEntity) t;
                if(intEntity.getStatus()==200){
                    getData(travelId,1);
                }else {
                    onError("操作失败");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                isUpdatingTravel=false;
                hideLoadingDialog();
            }
        });
    }


    void startCTime(final long startTime) {
        new PassengerReleaseTimeDialog()
                .setSelectTime(startTime < 10 ? System.currentTimeMillis() : startTime)
                .setLisenter(new PassengerReleaseTimeDialog.OnTimeSelectLisenter() {
                    @Override
                    public void date(long date, PassengerReleaseTimeDialog dialog, String text) {
                        LogUtil.i("data="+date+" stime="+System.currentTimeMillis());
                        if(date<System.currentTimeMillis()){
                            ToastUtil.showToast("修改时间应大于当前时间");
                        }else {
                            updateTravel(DateUtil.getTimeStr(date,"yyyy-MM-dd HH:mm:ss"),0);
                            dialog.dismiss();
                        }
                    }
                }).show(getSupportFragmentManager(), "setTimeDialog");
    }
    void seatNum() {
        new SingleWheelDialog<WheelIntEntity>()
                .setData(getSeatsData())
                .setListener(new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                        updateTravel(null,wheelIntEntity.getData());
                        dialog.dismiss();
                    }
                })
                .show(getSupportFragmentManager(), "SingleWheelDialog seat");
    }

    private List<WheelIntEntity> getSeatsData() {
        List<WheelIntEntity> datas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            if(travelType==ConstantValue.TravelType.DRIVER){
                datas.add(new WheelIntEntity(i + "个座位", i));
            }else {
                datas.add(new WheelIntEntity(i + "人", i));
            }
        }
        return datas;
    }
}
