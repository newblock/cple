package com.qcx.mini.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.AuthenticationActivity;
import com.qcx.mini.activity.LineTravelActivity;
import com.qcx.mini.activity.ReleaseTravel_2Activity;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.adapter.viewHolder.ItemDriverAndTravelViewHolder;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.PassengerReleaseTimeDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.ReleaseSuccessDialog;
import com.qcx.mini.dialog.SingleWheelDialog;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.entity.ReleasePageEntity;
import com.qcx.mini.entity.ReleaseResultEntity;
import com.qcx.mini.entity.TravelLineDetailEntity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.utils.UiUtils;
import com.qcx.mini.widget.itemDecoration.QuItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/7/24.
 * 线路匹配行程页面
 */

public class LineTravelFragment extends BaseFragment {
    private TravelLineDetailEntity.TravelLineDetail travelLineDetail;
    private PersonalLineEntity line; //第一次加载出来的线路信息
    private int day;
    private long dayTime;
    private long lineId ;
    private int lineType;
    private SimpleRecyclerViewAdapter<DriverAndTravelEntity> adapter;
    private int pageNum = 1;

    @BindView(R.id.fragment_line_travel_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.fragment_line_travel_info)
    TextView tv_lineInfo;
    @BindView(R.id.fragment_line_travel_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.fragment_line_travel_endAddress)
    TextView tv_endAddress;
    @BindView(R.id.fragment_line_travel_release)
    TextView tv_submit;

    private boolean isLoading = false;

    @OnClick(R.id.fragment_line_travel_release)
    void release() {
        if(travelLineDetail==null||travelLineDetail.isPublish()||line==null){
            return;
        }
        if (isLoading) {
            ToastUtil.showToast("您点的太快啦");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        String url;
        if (line.getType() == ConstantValue.TravelType.PASSENGER) {
            url = URLString.travelPassengerCreate;
//            params.put("startTimes", line.getStartTime());
            params.put("seatsNum", line.getSeats());
        } else {
            url = URLString.travelCreate;
            params.put("seats", line.getSeats());
            params.put("strategy", line.getStrategy());
            params.put("waypoints", null);
            params.put("waypoints", line.getWaypoints());
            params.put("waypointsAddress", line.getWaypointsAddress());
        }
        params.put("startTimes", new String[]{String.format(Locale.CHINA, "%s%s%s", DateUtil.getTimeStr(dayTime, "yyyy-MM-dd "), line.getStartTime(), ":00")});

        params.put("token", User.getInstance().getToken());
        params.put("start", line.getStart());
        params.put("end", line.getEnd());
        params.put("startAddress", line.getStartAddress());
        params.put("endAddress", line.getEndAddress());
        params.put("travelPrice", line.getPrice());
        params.put("startCity", null);
        params.put("endCity", null);
        params.put("redPacketPrice", 0);
        showLoadingDialog();
        isLoading = true;
        Request.post(url, params, new EntityCallback(ReleaseResultEntity.class) {
            @Override
            public void onSuccess(Object t) {
                hideLoadingDialog();
                isLoading = false;
                final ReleaseResultEntity data = (ReleaseResultEntity) t;
                if (data.getStatus() == 200) {
                    ToastUtil.showToast("成功发布行程！");
                    data.setTravelType(line.getType());
                    travelLineDetail.setPublish(true);
                    initLineInfo(line);
                    EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
                    if (data.getJoinedGroups() != null && data.getJoinedGroups().size() > 0) {
                        new ReleaseSuccessDialog()
                                .setResult(data)
                                .setNoToDetail(true)
                                .show(getFragmentManager(), "");
                    }

                } else if (data.getStatus() == -122) {
                    ToastUtil.showToast("今日发布行程已达3次！");
                } else if (data.getStatus() == -106) {//未进行芝麻认证
                    ToastUtil.showToast("未进行芝麻认证！");

                } else if (data.getStatus() == -5) {
                    Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                    startActivity(intent);
                } else if (data.getStatus() == -361) {
                    new QAlertDialog().setImg(QAlertDialog.IMG_ALERT)
                            .setBTN(QAlertDialog.BTN_ONEBUTTON)
                            .setTitleText("正在审核中，请稍后")
                            .setSureClickListener(new QAlertDialog.OnDialogClick() {
                                @Override
                                public void onClick(QAlertDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show(getFragmentManager(), "");
                }
            }

            @Override
            public void onError(String errorInfo) {
                super.onError(errorInfo);
                hideLoadingDialog();
                isLoading = false;
            }
        });
    }

    @OnClick(R.id.search_release_view)
    void toReleasePage() {
        LogUtil.i(day + "");
        Intent intent = new Intent(getContext(), ReleaseTravel_2Activity.class);
        ReleasePageEntity data = ReleasePageEntity.Factory.getPageData(line);
        data.setType(ReleaseTravel_2Activity.TYPE_RELEASE);
        data.setStartTime(dayTime + DateUtil.getTimeFromString(line.getStartTime(), "HH:mm") + 8 * DateUtil.HOUR);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            setLineType(savedInstanceState.getInt("lineType"));
            setLineId(savedInstanceState.getLong("lineId"));
            setDay(savedInstanceState.getInt("dayTime"));
        }
        getData(1);
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new SimpleRecyclerViewAdapter<>(getContext(), ItemDriverAndTravelViewHolder.class, R.layout.item_driver_travel_2);
        mRecyclerView.setAdapter(adapter);
        int d = UiUtils.getSize(8);
        mRecyclerView.addItemDecoration(new QuItemDecoration(0, d, d, d, 0, d));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lineType",lineType);
        outState.putLong("lineId",lineId);
        outState.putInt("dayTime", day);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_line_travel;
    }

    private void getData(final int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("type", lineType);
        params.put("lineId", lineId);
        params.put("date", day);
        if (line != null) {
            params.put("startTime", line.getStartTime());
        }
        params.put("pageNo", page);
        pageNum = page;
        Request.post(URLString.travelLineDetail, params, new EntityCallback(TravelLineDetailEntity.class) {
            @Override
            public void onSuccess(Object t) {
                TravelLineDetailEntity travelLineDetailEntity = (TravelLineDetailEntity) t;
                if (travelLineDetailEntity.getTravelLineDetail() != null) {
                    if(travelLineDetail!=null){
                        travelLineDetailEntity.getTravelLineDetail().setPublish(travelLineDetail.isPublish());
                    }
                    travelLineDetail = travelLineDetailEntity.getTravelLineDetail();
                    if (page == 1) {
                        adapter.setDatas(travelLineDetail.getTravelOneDetailVos());
                        if (line == null) {
                            line = travelLineDetail.getPersonalTravelLine();
                            initLineInfo(line);
                        }
                    }
                    if (travelLineDetail.getTravelOneDetailVos() != null && travelLineDetail.getTravelOneDetailVos().size() > 0) {
                        if (page > 1) {
                            adapter.addDatas(travelLineDetail.getTravelOneDetailVos());
                        }
                        pageNum = page + 1;
                    }

                }
            }
        });
    }

    private void initLineInfo(PersonalLineEntity line) {
        String time = DateUtil.formatDay("MM月dd日", dayTime) + line.getStartTime();
        String seats;
        String price;
        if (lineType == ConstantValue.LinelType.DRIVER) {
            seats = line.getSeats() + "座";
            price = CommonUtil.formatPrice(line.getPrice(), 1) + "元/座";
        } else {
            seats = line.getSeats() + "人";
            price = CommonUtil.formatPrice(line.getPrice(), 1) + "元";
        }
        if(tv_lineInfo==null){//activity已经关闭
            return;
        }
        tv_lineInfo.setText(CommonUtil.appendStringInPoint(time, seats, price));
        tv_startAddress.setText(line.getStartAddress());
        tv_endAddress.setText(line.getEndAddress());
        if (travelLineDetail != null) {
            if (travelLineDetail.isPublish()) {
                tv_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
            } else {
                tv_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
            }
        }

        ((LineTravelActivity) getActivity()).setPersonalLine(line);
    }

    public void setDay(int day) {
        this.day = day;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, day);
        dayTime = calendar.getTimeInMillis();

    }

    private PassengerReleaseTimeDialog timeDialog;

    public void changeStartTime() {
        if (travelLineDetail == null || travelLineDetail.getPersonalTravelLine() == null) {
            return;
        }
        long time = DateUtil.getTimeFromString(line.getStartTime(), "HH:mm");
        if (timeDialog == null) {
            timeDialog = new PassengerReleaseTimeDialog()
                    .setShowDay(false)
                    .setSelectTime(time)
                    .setLisenter(new PassengerReleaseTimeDialog.OnTimeSelectLisenter() {
                        @Override
                        public void date(long date, PassengerReleaseTimeDialog dialog, String text) {

                            if(travelLineDetail!=null){
                                travelLineDetail.setPublish(false);
                            }
                            if(line!=null){
                                String time = DateUtil.getTimeStr(date, "HH:mm");
                                line.setStartTime(time);
                                getData(1);
                                initLineInfo(line);
                            }
                            dialog.dismiss();
                        }
                    });
        }
        timeDialog.show(getFragmentManager(), "");
    }

    private SingleWheelDialog seatsDialog;

    public void changeSeats() {
        if (seatsDialog == null) {
            seatsDialog = DialogUtil.getChooseSeatsDialog(lineType, new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                @Override
                public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                    if(line!=null){
                        line.setSeats(wheelIntEntity.getData());
                        initLineInfo(line);
                    }
                    dialog.dismiss();
                }
            });
        }
        seatsDialog.show(getFragmentManager(), "");
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }
}
