package com.qcx.mini.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.RouteSearch;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.AuthenticationActivity;
import com.qcx.mini.activity.PersonalLinesActivity;
import com.qcx.mini.activity.SelectRouteStrategyActivity;
import com.qcx.mini.activity.SetAddressActivity;
import com.qcx.mini.base.BaseFragment;
import com.qcx.mini.dialog.ChooseWeekDialog;
import com.qcx.mini.dialog.PassengerReleaseTimeDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.QAlertDialog2;
import com.qcx.mini.dialog.ReminderSingleWheelDialog;
import com.qcx.mini.dialog.SingleWheelDialog;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.entity.PersonalTravelLine;
import com.qcx.mini.entity.PriceEntity;
import com.qcx.mini.entity.PriceListEntity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.listener.OnDataChangedListener;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.SwitchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/5/2.
 */

public class SetLinePageFragment extends BaseFragment {
    private final int REQUEST_END_ADDRESS_CODE = 10;
    private final int REQUEST_START_ADDRESS_CODE = 11;
    private final int REQUEST_STRATEGY_CODE = 23;
    private long lineId;
    private boolean isToLinesActivity=true;

    private int type;//行程类型
    private OnDataChangedListener listener;

    private Tip startAddress;
    private Tip endAddress;
    private long startTime = 0;
    private int seats = 0;
    private int strategy = -1;
    private double price;
    private String lineDesc;
    private boolean autoCreate;
    private List<Integer> autoDays;
    private ArrayList<Tip> waypoints;

    private boolean isSetStrategy = false;
    private List<PriceEntity> priceList;

    @BindView(R.id.fragment_set_line_page_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.fragment_set_line_page_endAddress)
    TextView tv_endAddress;
    @BindView(R.id.fragment_set_line_page_start_time_text)
    TextView tv_startTime;

    @BindView(R.id.fragment_set_line_page_passenger_num_view)
    View v_passengerNum;
    @BindView(R.id.fragment_set_line_page_passenger_num_text)
    TextView tv_passengerNum;

    @BindView(R.id.fragment_set_line_page_passenger_rout_strategy_view)
    View v_strategy;
    @BindView(R.id.fragment_set_line_page_passenger_rout_strategy_text)
    TextView tv_strategy;

    @BindView(R.id.fragment_set_line_page_driver_view_line)
    View v_driverLine;
    @BindView(R.id.fragment_set_line_page_driver_view)
    View v_driver;

    @BindView(R.id.fragment_set_line_page_driver_seats_text)
    TextView tv_driverSeats;
    @BindView(R.id.fragment_set_line_page_driver_price)
    TextView tv_driverPrice;

    @BindView(R.id.fragment_set_line_page_passenger_price)
    TextView tv_passengerPrice;
    @BindView(R.id.fragment_set_line_page_passenger_price_view)
    View v_passengerPrice;
    @BindView(R.id.fragment_set_line_page_switchView)
    SwitchView mSwitchView;
    @BindView(R.id.fragment_set_line_page_weeks_text)
    TextView tv_weeks;
    @BindView(R.id.fragment_set_line_page_describe)
    EditText et_discribe;
    @BindView(R.id.fragment_set_line_page_way_point_text)
    TextView tv_wayPoint;
    @BindView(R.id.fragment_set_line_page_way_point_view)
    View v_point;
    @BindView(R.id.fragment_set_line_page_line)
    View v_line;

    @OnClick(R.id.fragment_set_line_page_startAddress)
    void setStartAddress() {
        if (type == ConstantValue.TravelType.DRIVER) {
            line();
        } else {
            Intent intent = new Intent(getContext(), SetAddressActivity.class);
            intent.putExtra("inputHint", "请输入起点");
            startActivityForResult(intent, REQUEST_START_ADDRESS_CODE);
        }
    }

    @OnClick(R.id.fragment_set_line_page_endAddress)
    void setEndAddress() {
        if (type == ConstantValue.TravelType.DRIVER) {
            line();
        } else {
            Intent intent = new Intent(getContext(), SetAddressActivity.class);
            intent.putExtra("inputHint", "请输入目的地");
            startActivityForResult(intent, REQUEST_END_ADDRESS_CODE);
        }
    }

    @OnClick(R.id.search_exchange)
    void exchange() {
        Tip tip;
        tip = startAddress;
        startAddress = endAddress;
        endAddress = tip;
        if (startAddress != null) {
            tv_startAddress.setText(startAddress.getName());
        } else {
            tv_startAddress.setText("");
        }
        if (endAddress != null) {
            tv_endAddress.setText(endAddress.getName());
        } else {
            tv_endAddress.setText("");
        }
    }

    @OnClick({R.id.fragment_set_line_page_start_time_view})
    void startTime() {
        new PassengerReleaseTimeDialog()
                .setShowDay(false)
                .setSelectTime(startTime < 10 ? System.currentTimeMillis() : startTime)
                .setLisenter(new PassengerReleaseTimeDialog.OnTimeSelectLisenter() {
                    @Override
                    public void date(long date, PassengerReleaseTimeDialog dialog, String text) {
                        startTime = date;
                        tv_startTime.setText(DateUtil.getTimeStr(startTime, "HH:mm"));
                        dataChanged();
                        dialog.dismiss();
                    }
                }).show(getFragmentManager(), " ");
    }

    @OnClick(R.id.fragment_set_line_page_driver_seats_view)
    void seatNum() {
        new SingleWheelDialog<WheelIntEntity>()
                .setData(getSeatsData())
                .setListener(new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                        seats = wheelIntEntity.getData();
                        tv_driverSeats.setText(wheelIntEntity.getPickerViewText());
                        dataChanged();
                        dialog.dismiss();
                    }
                })
                .show(getFragmentManager(), "SingleWheelDialog seat");
    }

    @OnClick(R.id.fragment_set_line_page_passenger_num_view)
    void manNum() {
        new SingleWheelDialog<WheelIntEntity>()
                .setData(getPeopleData())
                .setListener(new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                    @Override
                    public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                        seats = wheelIntEntity.getData();
                        tv_passengerNum.setText(wheelIntEntity.getPickerViewText());
                        initPrice();
                        getPrice();
                        dataChanged();
                        dialog.dismiss();
                    }
                })
                .show(getFragmentManager(), "SingleWheelDialog seat");
    }

    @OnClick(R.id.fragment_set_line_page_passenger_rout_strategy_view)
    void line() {
        Intent intent = new Intent(getContext(), SelectRouteStrategyActivity.class);
        intent.putExtra("startTip", startAddress);
        intent.putExtra("endTip", endAddress);
        intent.putExtra("waypoints", waypoints);
        startActivityForResult(intent, REQUEST_STRATEGY_CODE);
    }

    @OnClick({R.id.fragment_set_line_page_way_point_add, R.id.fragment_set_line_page_way_point_text})
    void addPoint() {
        line();
    }

    @OnClick(R.id.fragment_set_line_page_driver_price_view)
    void price() {
        if (startAddress == null) {
            ToastUtil.showToast("请输入起点位置");
            return;
        }
        if (endAddress == null) {
            ToastUtil.showToast("请输入终点位置");
            return;
        }

        if (priceList == null || priceList.size() < 1) {
            ToastUtil.showToast("正在获取价格信息");
            return;
        }
        PriceEntity.itemTextInfo = "元/位";
        new ReminderSingleWheelDialog<PriceEntity>()
                .setData(priceList)
                .setShowReminderText(false)
                .setShowTopView(false)
                .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<PriceEntity>() {
                    @Override
                    public void onRightClick(PriceEntity priceEntity, ReminderSingleWheelDialog dialog, int position) {
                        tv_driverPrice.setText(priceEntity.getPickerViewText());
                        price = (int) priceEntity.getPrice();
                        dataChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onTopViewClick() {

                    }
                })
                .setPosition(price + "元/位")
                .show(getFragmentManager(), "ReminderSingleWheelDialog");
    }

    @OnClick(R.id.fragment_set_line_page_passenger_price_view)
    void cprice() {
        if (startAddress == null) {
            ToastUtil.showToast("请输入起点位置");
            return;
        }
        if (endAddress == null) {
            ToastUtil.showToast("请输入终点位置");
            return;
        }

        if (seats < 1) {
            ToastUtil.showToast("请选择乘车人数");
            return;
        }

        if (priceList == null || priceList.size() < 1) {
            ToastUtil.showToast("正在获取价格信息");
            return;
        }
        PriceEntity.itemTextInfo = "";
        new ReminderSingleWheelDialog<PriceEntity>()
                .setData(priceList)
                .setShowTopView(false)
                .setShowReminderText(false)
                .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<PriceEntity>() {
                    @Override
                    public void onRightClick(PriceEntity priceEntity, ReminderSingleWheelDialog dialog, int position) {
                        tv_passengerPrice.setText(priceEntity.getPickerViewText());
                        price = (int) priceEntity.getPrice();
                        dataChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onTopViewClick() {
                        ToastUtil.showToast("什么是黄牛红包");
                    }
                })
                .show(getFragmentManager(), "ReminderSingleWheelDialog");
    }

    @OnClick(R.id.fragment_set_line_page_weeks_view)
    void weeks() {
        new ChooseWeekDialog()
                .setWeek(autoDays)
                .setListener(weekListener)
                .show(getFragmentManager(), "");
    }

    private ChooseWeekDialog.OnChooseWeekListener weekListener = new ChooseWeekDialog.OnChooseWeekListener() {
        private boolean isSure=false;
        @Override
        public void weeks(boolean[] status, ChooseWeekDialog dialog) {
            initAutoDays(status);
            isSure=true;
            dialog.dismiss();
        }

        @Override
        public void onDismiss(ChooseWeekDialog dialog,boolean isOnWeeks) {
            if ((autoDays == null || autoDays.size() == 0)||!isSure) {
                mSwitchView.setState(false);
            }
            if(dialog!=null){
                dialog.dismiss();
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_START_ADDRESS_CODE:
                    startAddress = data.getParcelableExtra("tip");
                    if (startAddress != null) {
                        tv_startAddress.setText(startAddress.getName());
                        initPrice();
                        getPrice();
                    }
                    break;
                case REQUEST_END_ADDRESS_CODE:
                    endAddress = data.getParcelableExtra("tip");
                    if (endAddress != null) {
                        tv_endAddress.setText(endAddress.getName());
                        initPrice();
                        getPrice();
                    }
                    break;
                case REQUEST_STRATEGY_CODE:
                    strategy = data.getIntExtra("strategy", -1);
                    waypoints = data.getParcelableArrayListExtra("waypoints");
                    startAddress = data.getParcelableExtra("start");
                    if (startAddress != null) {
                        tv_startAddress.setText(startAddress.getName());
                    }
                    endAddress = data.getParcelableExtra("end");
                    if (endAddress != null) {
                        tv_endAddress.setText(endAddress.getName());
                    }
                    showWayPoint();
                    initPrice();
                    getPrice();
                    if (strategy == RouteSearch.DRIVING_SINGLE_DEFAULT) {
                        tv_strategy.setText("速度最快");
                        isSetStrategy = true;
                    }
                    if (strategy == RouteSearch.DRIVING_SINGLE_SHORTEST) {
                        tv_strategy.setText("距离最短");
                        isSetStrategy = true;
                    }
                    if (strategy == RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION) {
                        tv_strategy.setText("少收费避拥堵");
                        isSetStrategy = true;
                    }

                    break;
            }
            dataChanged();
        }
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        if (type == ConstantValue.TravelType.PASSENGER) {
            v_driverLine.setVisibility(View.GONE);
            v_driver.setVisibility(View.GONE);
            v_strategy.setVisibility(View.GONE);
            v_passengerNum.setVisibility(View.VISIBLE);
            v_passengerPrice.setVisibility(View.VISIBLE);
        } else {
            v_driverLine.setVisibility(View.VISIBLE);
            v_driver.setVisibility(View.VISIBLE);
            v_strategy.setVisibility(View.VISIBLE);
            v_passengerNum.setVisibility(View.GONE);
            v_passengerPrice.setVisibility(View.GONE);
        }
        autoDays = new ArrayList<>();
        mSwitchView.setOnChangedListener(new SwitchView.OnChangedListener() {
            @Override
            public void state(int state) {
                if (state == 4) {
                    new ChooseWeekDialog()
                            .setListener(weekListener)
                            .setWeek(autoDays)
                            .show(getFragmentManager(), "");
                }
            }
        });

        if (lineId > 0) {
            getLineData(lineId);
        }
        showWayPoint();
    }

    ChooseWeekDialog chooseWeekDialog;

    public void setToLinesActivity(boolean toLinesActivity) {
        isToLinesActivity = toLinesActivity;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_set_line_page;
    }

    private boolean isLoading=false;
    public void submit() {
        if(isLoading){
            return;
        }
        if (canSubmit()) {
            String url;
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            if (lineId > 0) {
                url = URLString.personalLineUpdate;
                params.put("lineId", lineId);
            } else {
                url = URLString.personalLineCreate;
            }
            params.put("start", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
            params.put("end", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});
            params.put("startAddress", startAddress.getName());
            params.put("endAddress", endAddress.getName());
            if (type == ConstantValue.TravelType.DRIVER) {
                params.put("strategy", strategy);
                List<double[]> points = new ArrayList<>();
                List<String> pointsName = new ArrayList<>();
                if (waypoints != null) {
                    for (int i = 0; i < waypoints.size(); i++) {
                        Tip tip = waypoints.get(i);
                        if (tip != null) {
                            points.add(new double[]{tip.getPoint().getLongitude(), tip.getPoint().getLatitude()});
                            pointsName.add(tip.getName());
                        }
                    }
                }
                params.put("waypoints", points);
                params.put("waypointsAddress", pointsName);
            }
            params.put("seats", seats);
            params.put("startTime", DateUtil.getTimeStr(startTime, "HH:mm"));
            params.put("price", price);
            params.put("lineDesc", et_discribe.getText().toString());
            params.put("type", type);
            params.put("autoCreate", mSwitchView.getState() == 4);
            params.put("autoDays", autoDays);
            isLoading=true;
            showLoadingDialog();
            Request.post(url, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    isLoading=false;
                    hideLoadingDialog();
                    if (intEntity.getStatus() == 200) {
                        if(mSwitchView.getState()==4){
                            try {
                                new QAlertDialog2()
                                        .setListener(new QAlertDialog2.OnDialogClick() {
                                            @Override
                                            public void onClick(QAlertDialog2 dialog) {
                                                close();
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onDismiss() {
                                                close();
                                            }
                                        }).show(getFragmentManager(),"");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            close();
                        }
                    } else if (intEntity.getStatus() == -5) {
                        new QAlertDialog().setImg(QAlertDialog.IMG_ALERT)
                                .setBTN(QAlertDialog.BTN_TWOBUTTON)
                                .setTitleText("您还未进行车主认证")
                                .setRightText("去认证")
                                .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                                    @Override
                                    public void onClick(QAlertDialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .setSureClickListener(new QAlertDialog.OnDialogClick() {
                                    @Override
                                    public void onClick(QAlertDialog dialog) {
                                        new OnDriverAuthClickListener(getActivity()).onClick();
                                        dialog.dismiss();
                                    }
                                })
                                .show(getFragmentManager(), "");
                    } else if (intEntity.getStatus() == -361) {
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
                    isLoading=false;
                    hideLoadingDialog();
                }
            });
        }
    }

    private void close(){
        if(isToLinesActivity){
            startActivity(new Intent(getContext(), PersonalLinesActivity.class));
        }
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    public boolean canSubmit() {
        LogUtil.i("startAddress=" + (startAddress == null || startAddress.getPoint() == null)
                + " \n endAddress=" + (endAddress == null || endAddress.getPoint() == null)
                + "\n startTime" + startTime
                + " \n isSetStrategy=" + (!isSetStrategy && type == ConstantValue.TravelType.DRIVER)
                + "\n seats=" + seats
                + "\n price=" + price);
        if (startAddress == null || startAddress.getPoint() == null
                || endAddress == null || endAddress.getPoint() == null
                || startTime <= 0
                || (!isSetStrategy && type == ConstantValue.TravelType.DRIVER)
                || seats <= 0
                || price <= 0) {
            return false;
        }
        return true;
    }

    private void initPrice() {
        priceList = null;
        price = 0;
        tv_driverPrice.setText("票价(元/位）");
        tv_passengerPrice.setText("00.00");
    }

    private void getPrice() {
        if (startAddress != null && endAddress != null) {
            if (type == ConstantValue.TravelType.PASSENGER && seats < 1) {
                return;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("start", new double[]{startAddress.getPoint().getLongitude(), startAddress.getPoint().getLatitude()});
            params.put("end", new double[]{endAddress.getPoint().getLongitude(), endAddress.getPoint().getLatitude()});
            if (type == ConstantValue.TravelType.PASSENGER) {
                params.put("seats", seats);
                params.put("role", 0);
            } else {
                params.put("role", 1);
            }
            Request.post(URLString.publishPrice, params, new EntityCallback(PriceListEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    PriceListEntity p = (PriceListEntity) t;
                    priceList = p.getPriceList();
                    if (priceList != null && priceList.size() > 0) {
                        for (int i = 0; i < priceList.size(); i++) {
                            priceList.get(i).setHintRemiderText(true);
                        }
                    }
                    if (type == ConstantValue.TravelType.PASSENGER) {
                        if (p.getPriceList() != null && p.getPriceList().size() > 0) {
                            price = p.getPriceList().get(0).getPrice();
                            try {
                                tv_passengerPrice.setText(CommonUtil.formatPrice(p.getPriceList().get(0).getPrice(),2));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dataChanged();
                        } else {
                            ToastUtil.showToast("获取价格信息失败");
                        }
                    }
                }
            });
        }
    }

    private void dataChanged() {
        if (listener != null) {
            listener.onChanged();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public OnDataChangedListener getListener() {
        return listener;
    }

    public void setListener(OnDataChangedListener listener) {
        this.listener = listener;
    }

    private void initAutoDays(boolean[] weekStatus) {
        autoDays.clear();
        if (weekStatus == null) weekStatus = new boolean[]{false};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < weekStatus.length; i++) {
            if (weekStatus[i]) {
                if (i == 6) {
                    autoDays.add(0);
                } else {
                    autoDays.add(i + 1);
                }
                builder.append(getWeekStr(i));
                builder.append("、");
            }
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
            String s = "每周日，发布下一周(" + builder + ")的行程";
            tv_weeks.setText(s);
        } else {
            tv_weeks.setText("设置发布时间，开启自动发布行程，遇见更多惊喜！");
            mSwitchView.setState(false);
        }
    }

    private String getWeekStr(int day) {
        switch (day) {
            case 0:
                return "周一";
            case 1:
                return "周二";
            case 2:
                return "周三";
            case 3:
                return "周四";
            case 4:
                return "周五";
            case 5:
                return "周六";
            case 6:
                return "周日";
            default:
                return "";
        }
    }

    private List<WheelIntEntity> getSeatsData() {
        List<WheelIntEntity> datas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            datas.add(new WheelIntEntity(i + "个座位", i));
        }
        return datas;
    }

    private List<WheelIntEntity> getPeopleData() {
        List<WheelIntEntity> datas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            datas.add(new WheelIntEntity(i + "人", i));
        }
        return datas;
    }

    private void getLineData(long lineId) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("lineId", lineId);
        Request.post(URLString.personalLineDetail, params, new EntityCallback(PersonalTravelLine.class) {
            @Override
            public void onSuccess(Object t) {
                PersonalTravelLine travelLine = (PersonalTravelLine) t;
                PersonalLineEntity lineEntity = travelLine.getPersonalTravelLine();
                setLineInfo(lineEntity);
                LogUtil.i(lineEntity.getStartAddress() + "  " + lineEntity.getEndAddress());
            }
        });
    }

    private void setLineInfo(PersonalLineEntity line) {
        startAddress = new Tip();
        startAddress.setName(line.getStartAddress());
        startAddress.setPostion(new LatLonPoint(line.getStart()[1], line.getStart()[0]));
        endAddress = new Tip();
        endAddress.setName(line.getEndAddress());
        endAddress.setPostion(new LatLonPoint(line.getEnd()[1], line.getEnd()[0]));

        startTime = getStartTime(line.getStartTime());
        if (line.getType() == type) {
            seats = line.getSeats();
            price = line.getPrice();
            lineDesc = line.getLineDesc();
            autoCreate = line.isAutoCreate();
            autoDays = line.getAutoDays();
            if (line.getType() == ConstantValue.TravelType.DRIVER) {
                strategy = line.getStrategy();
            }
        }
        if (waypoints == null) {
            waypoints = new ArrayList<>();
        } else {
            waypoints.clear();
        }
        if (line.getWaypoints() != null
                && line.getWaypoints().size() > 0
                && line.getWaypointsAddress() != null
                && line.getWaypoints().size() == line.getWaypointsAddress().size()) {
            for (int i = 0; i < line.getWaypoints().size(); i++) {
                Tip tip = new Tip();
                tip.setPostion(new LatLonPoint(line.getWaypoints().get(i)[1], line.getWaypoints().get(i)[0]));
                tip.setName(line.getWaypointsAddress().get(i));
                waypoints.add(tip);
            }
        }

        showWayPoint();
        getPrice();
        showLineInfo();
    }

    private long getStartTime(String str) {
        String day = DateUtil.getTimeStr(System.currentTimeMillis(), "yyyy-MM-dd");
        return DateUtil.getTimeFromString(day + str, "yyyy-MM-ddHH:mm");
    }

    private void showLineInfo() {
        tv_startAddress.setText(startAddress.getName());
        tv_endAddress.setText(endAddress.getName());
        tv_startTime.setText(DateUtil.getTimeStr(startTime, "HH:mm"));

        if (seats <= 0) {
            tv_driverSeats.setText("座位数量");
            tv_passengerNum.setText("乘车人数");
        } else {
            tv_driverSeats.setText(String.format(Locale.CHINA, "%d座", seats));
            tv_passengerNum.setText(String.format(Locale.CHINA, "%d人", seats));
        }
        if (price <= 0) {
            tv_passengerPrice.setText("00.00");
            tv_driverPrice.setText("票价(元/位）");
        } else {
            tv_passengerPrice.setText(CommonUtil.formatPrice(price,2));
            String dPrice = price + "元/座";
            tv_driverPrice.setText(dPrice);
        }

        switch (strategy) {
            case RouteSearch.DRIVING_SINGLE_DEFAULT:
                tv_strategy.setText("速度最快");
                isSetStrategy = true;
                break;
            case RouteSearch.DRIVING_SINGLE_SHORTEST:
                tv_strategy.setText("距离最短");
                isSetStrategy = true;
                break;
            case RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION:
                tv_strategy.setText("少收费避拥堵");
                isSetStrategy = true;
                break;
            default:
                tv_strategy.setText("选择线路");
                isSetStrategy = false;
                break;
        }
        if (!TextUtils.isEmpty(lineDesc)) {
            et_discribe.setText(lineDesc);
            et_discribe.setSelection(lineDesc.length());
        }

        mSwitchView.setState(autoCreate);
        tv_weeks.setText(getWeekStr(autoDays));
        dataChanged();
    }

    private String getWeekStr(List<Integer> weeks) {
        String str = "设置发布时间，开启自动发布行程，遇见更多惊喜！";
        StringBuilder builder = new StringBuilder();
        if (weeks == null) {
            weeks = new ArrayList<>();
        }
        for (int i = 0; i < weeks.size(); i++) {
            builder.append(getWeek(weeks.get(i)));
            builder.append("、");
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
            str =  "每周日，发布下一周(" + builder + ")的行程";
        }
        return str;
    }

    private String getWeek(int day) {
        switch (day) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 0:
                return "周日";
            default:
                return "";
        }
    }

    private void showWayPoint() {
        if (type == ConstantValue.TravelType.DRIVER
                && waypoints != null
                && waypoints.size() > 0) {
            v_point.setVisibility(View.VISIBLE);
            v_line.setVisibility(View.GONE);
            StringBuilder stringBuilder = new StringBuilder();
            if (waypoints.size() > 1) {
                stringBuilder.append("经");
                stringBuilder.append(waypoints.size());
                stringBuilder.append("地：");
                for (int i = 0; i < waypoints.size(); i++) {
                    if (i < waypoints.size()) {
                        stringBuilder.append(waypoints.get(i).getName());
                        stringBuilder.append("、");
                    }
                }
            } else if (waypoints.size() == 1) {
                stringBuilder.append(waypoints.get(0).getName());
                stringBuilder.append("、");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                tv_wayPoint.setText(stringBuilder);
            }
        } else {
            v_point.setVisibility(View.GONE);
            v_line.setVisibility(View.VISIBLE);
        }
    }

    private void showPointMarker() {

    }

}
