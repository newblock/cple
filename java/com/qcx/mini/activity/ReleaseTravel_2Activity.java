package com.qcx.mini.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.amap.api.services.route.RouteSearch;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.dialog.ChooseWeekDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.dialog.DriverReleaseTimeDialog;
import com.qcx.mini.dialog.PassengerReleaseTimeDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.QAlertDialog2;
import com.qcx.mini.dialog.ReleaseSuccessDialog;
import com.qcx.mini.dialog.ReminderSingleWheelDialog;
import com.qcx.mini.dialog.SingleWheelDialog;
import com.qcx.mini.entity.EventStatus;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PriceEntity;
import com.qcx.mini.entity.PriceListEntity;
import com.qcx.mini.entity.ReleasePageEntity;
import com.qcx.mini.entity.ReleaseResultEntity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.utils.ToastUtil;
import com.qcx.mini.widget.SwitchView;
import com.qcx.mini.widget.calendar.DateEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qcx.mini.widget.calendar.CalendarHelper.getValue;

/**
 * 发布行程、添加常用路线
 */
public class ReleaseTravel_2Activity extends BaseActivity {
    public static final int TYPE_LINE = 0;
    public static final int TYPE_RELEASE = 1;

    private final int REQUEST_END_ADDRESS_CODE = 10;
    private final int REQUEST_START_ADDRESS_CODE = 11;
    private final int REQUEST_STRATEGY_CODE = 23;

    private int type;//表示该页面是发布行程还是添加常用路线
    private int travelType;
    private Tip start;
    private Tip end;
    private int seats;
    private double price;//行程单价
    private long startTime;
    private List<DateEntity> dates;
    private int strategy = -1;
    private double redPacketPrice;
    private List<PriceEntity> priceList;
    private ArrayList<Tip> wayPoints;

    private List<Integer> autoDays;
    private ChooseWeekDialog chooseWeekDialog;
    private long lineId;

    @BindView(R.id.release_travel_add_way_point)
    View v_DAddWayPoint;
    @BindView(R.id.release_travel_route_strategy)
    View v_DRouteStrategy;
    @BindView(R.id.release_travel_price_type)
    TextView tv_priceType;
    @BindView(R.id.release_travel_choose_price_img)
    ImageView iv_choosePrice;
    @BindView(R.id.release_travel_describe_view)
    View v_describe;

    @BindView(R.id.release_travel_startAddress)
    TextView tv_startAddress;
    @BindView(R.id.release_travel_endAddress_text)
    TextView tv_endAddress;
    @BindView(R.id.release_travel_route_strategy_text)
    TextView tv_routeStrategy;
    @BindView(R.id.release_travel_start_time_text)
    TextView tv_startTime;
    @BindView(R.id.release_travel_seats_text)
    TextView tv_seats;
    @BindView(R.id.release_travel_price_text)
    TextView tv_priceText;
    @BindView(R.id.release_travel_way_point_view)
    View v_wayPoint;
    @BindView(R.id.release_travel_way_point_line)
    View v_cuttingLine;
    @BindView(R.id.release_travel_way_point_text)
    TextView tv_wayPoint;
    @BindView(R.id.release_travel_submit)
    View v_submit;

    @BindView(R.id.release_travel_switch_auto_release_day)
    SwitchView sv_autoDay;
    @BindView(R.id.release_travel_lint_auto_day)
    TextView tv_autoDay;
    @BindView(R.id.release_travel_line_name)
    EditText et_lineName;
    @BindView(R.id.release_travel_line_name_view)
    View v_lineName;
    @BindView(R.id.release_travel_line_auto_day_view)
    View v_lineAuto;

    @OnClick(R.id.release_travel_line_auto_day_view)
    void chooseAutoDay() {
        if (chooseWeekDialog == null) {
            chooseWeekDialog = new ChooseWeekDialog()
                    .setWeek(autoDays)
                    .setListener(weekListener);
        }
        showDialog(chooseWeekDialog,"");
    }

    @OnClick(R.id.release_travel_startAddress_view)
    void changeStartAddress() {
        if (travelType == ConstantValue.TravelType.PASSENGER) {
            Intent intent = new Intent(this, SetAddressActivity.class);
            intent.putExtra("inputHint", "请输入起点地址");
            startActivityForResult(intent, REQUEST_START_ADDRESS_CODE);
        } else {
            changeWayPoint();
        }
    }

    @OnClick(R.id.release_travel_endAddress_view)
    void changeEndAddress() {
        if (travelType == ConstantValue.TravelType.PASSENGER) {
            Intent intent = new Intent(this, SetAddressActivity.class);
            intent.putExtra("inputHint", "请输入终点地址");
            startActivityForResult(intent, REQUEST_END_ADDRESS_CODE);
        } else {
            changeWayPoint();
        }
    }

    @OnClick({R.id.release_travel_way_point_view, R.id.release_travel_add_way_point})
    void changeWayPoint() {
        Intent intent = new Intent(this, SelectRouteStrategyActivity.class);
        intent.putExtra("startTip", start);
        intent.putExtra("endTip", end);
        intent.putExtra("waypoints", wayPoints);
        startActivityForResult(intent, REQUEST_STRATEGY_CODE);
    }

    @OnClick(R.id.release_travel_exchange_address)
    void exchangeAddress() {
        Tip tip = start;
        start = end;
        end = tip;
        notifyDataSetChanged();
    }

    private SingleWheelDialog<WheelIntEntity> seatsDialog;
    @OnClick(R.id.release_travel_seats_view)
    void changeSeats() {
        if(seatsDialog==null){
            seatsDialog=new SingleWheelDialog<WheelIntEntity>()

                    .setListener(new SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity>() {
                        @Override
                        public void onRightClick(WheelIntEntity wheelIntEntity, SingleWheelDialog dialog, int position) {
                            if (seats != wheelIntEntity.getData()) {
                                seats = wheelIntEntity.getData();
                                if (travelType == ConstantValue.TravelType.PASSENGER) {
                                    price = 0;
                                    clearPriceList();
                                    getPrice();
                                }
                                notifyDataSetChanged();
                            }
                            nextOper();
                            dialog.dismiss();
                        }
                    });
        }
        seatsDialog .setData(getSeatsData());
        showDialog(seatsDialog,"");
    }


    private PassengerReleaseTimeDialog pStartTimeDialog;
    private DriverReleaseTimeDialog dStartTimeDialog;

    @OnClick(R.id.release_travel_start_time_view)
    void changeStartTime() {
        if (travelType == ConstantValue.TravelType.PASSENGER || type == TYPE_LINE) {
            if (pStartTimeDialog == null) {
                pStartTimeDialog = new PassengerReleaseTimeDialog()
                        .setShowDay(type == TYPE_RELEASE)
                        .setLisenter(new PassengerReleaseTimeDialog.OnTimeSelectLisenter() {
                            @Override
                            public void date(long date, PassengerReleaseTimeDialog dialog, String text) {
                                startTime = date;
                                notifyDataSetChanged();
                                dialog.dismiss();
                                nextOper();
                            }
                        });
            }
            pStartTimeDialog.setSelectTime(startTime < 10 ? System.currentTimeMillis() : startTime);
            showDialog(pStartTimeDialog,"");

        } else if (travelType == ConstantValue.TravelType.DRIVER) {
            if (dStartTimeDialog == null) {
                dStartTimeDialog=new DriverReleaseTimeDialog().setListener(new DriverReleaseTimeDialog.OnDatesSelectedListener() {
                    @Override
                    public void onSelected(List<DateEntity> dateEntities,DriverReleaseTimeDialog dialog) {
                        dates = dateEntities;
                        notifyDataSetChanged();
                        dialog.dismiss();
                        nextOper();
                    }
                });
            }
            dStartTimeDialog.setSelectedDays(dates);
            showDialog(dStartTimeDialog,"");
        }
    }

    private void showDialog(DialogFragment dialog,String tag){
        if(dialog!=null&&!dialog.isAdded()&&!dialog.isVisible()&&!dialog.isRemoving()){
            dialog.show(getSupportFragmentManager(),tag);
        }
    }

    private ReminderSingleWheelDialog<PriceEntity> priceDialog;

    @OnClick(R.id.release_travel_price_view)
    void changePrice() {
        if (travelType == ConstantValue.TravelType.DRIVER) {
            if (start == null) {
                ToastUtil.showToast("请输入起点位置");
                return;
            }
            if (end == null) {
                ToastUtil.showToast("请输入终点位置");
                return;
            }

            if (priceList == null || priceList.size() < 1) {
                ToastUtil.showToast("未获取到价格信息，请稍后");
                return;
            }
            PriceEntity.itemTextInfo = "元/位";
            if (priceDialog == null) {

                priceDialog = new ReminderSingleWheelDialog<PriceEntity>()
                        .setListener(new ReminderSingleWheelDialog.OnSelectPriceDialogListener<PriceEntity>() {
                            @Override
                            public void onRightClick(PriceEntity priceEntity, ReminderSingleWheelDialog dialog, int position) {
                                price = (int) priceEntity.getPrice();
                                redPacketPrice = priceEntity.getRedPacketPrice();
                                nextOper();
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }

                            @Override
                            public void onTopViewClick() {

                            }
                        })
                        .setPosition(price + "元/位");
            }
            priceDialog.setData(priceList);
            showDialog(priceDialog,"");
        }
    }

    private boolean isLoading = false;

    @OnClick(R.id.release_travel_submit)
    void submit() {
        if (type == TYPE_RELEASE) {
            releaseTravel();
        } else {
            submitLine();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_release_travel_2;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_START_ADDRESS_CODE:
                    start = data.getParcelableExtra("tip");
                    break;
                case REQUEST_END_ADDRESS_CODE:
                    end = data.getParcelableExtra("tip");
                    break;
                case REQUEST_STRATEGY_CODE:
                    strategy = data.getIntExtra("strategy", -1);
                    wayPoints = data.getParcelableArrayListExtra("waypoints");
                    start = data.getParcelableExtra("start");
                    end = data.getParcelableExtra("end");
                    break;
            }
            price = 0;
            clearPriceList();
            getPrice();
            notifyDataSetChanged();
            nextOper();
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, Color.WHITE, 0);
        initIntendData();
        initUi(type, travelType);
        notifyDataSetChanged();
        nextOper();
        initLineView();
    }

    private void initLineView() {
        sv_autoDay.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                chooseAutoDay();
            }

            @Override
            public void toggleToOff() {
                sv_autoDay.toggleSwitch(false);
            }
        });
    }

    private void initUi(int type, int travelType) {
        if (type == TYPE_LINE) {
            v_lineName.setVisibility(View.VISIBLE);
            v_lineAuto.setVisibility(View.VISIBLE);
        } else {
            v_lineName.setVisibility(View.GONE);
            v_lineAuto.setVisibility(View.GONE);
        }
        switch (travelType) {
            case ConstantValue.TravelType.DRIVER:
                if (type == TYPE_LINE) {
                    if(lineId>0){
                        initTitleBar("修改车主常用路线", true, false);
                    }else {
                        initTitleBar("添加车主常用路线", true, false);
                    }
                } else {
                    initTitleBar("发布车主行程", true, false);
                }
                v_describe.setVisibility(View.VISIBLE);
                v_DAddWayPoint.setVisibility(View.VISIBLE);
                v_DRouteStrategy.setVisibility(View.VISIBLE);
                iv_choosePrice.setVisibility(View.VISIBLE);
                tv_priceType.setText("元/座");
                tv_seats.setHint("座位数量");
                break;
            case ConstantValue.TravelType.PASSENGER:
                if (type == TYPE_LINE) {
                    if(lineId>0){
                        initTitleBar("修改乘客常用路线", true, false);
                    }else {
                        initTitleBar("添加乘客常用路线", true, false);
                    }
                } else {
                    initTitleBar("发布乘客行程", true, false);
                }
                v_describe.setVisibility(View.GONE);
                v_DAddWayPoint.setVisibility(View.GONE);
                v_DRouteStrategy.setVisibility(View.GONE);
                iv_choosePrice.setVisibility(View.GONE);
                tv_priceType.setText("元");
                tv_seats.setHint("乘车人数");
                break;
            default:
                ToastUtil.showToast("无效的行程类型,请重试");
                finish();
                break;
        }
    }

    private void initIntendData() {
        ReleasePageEntity data = getIntent().getParcelableExtra("data");
        if (data == null) {
            data = ReleasePageEntity.Factory.releasePassengerData();
        }

        type = getIntent().getIntExtra("type", data.getType());
        travelType = getIntent().getIntExtra("travelType", data.getTravelType());

        start = getIntent().getParcelableExtra("start");
        if (start == null || start.getPoint() == null) {
            start = data.getStart();
        }

        end = getIntent().getParcelableExtra("end");
        if (end == null || end.getPoint() == null) {
            end = data.getEnd();
        }

        seats = getIntent().getIntExtra("seats", data.getSeats());
        price = getIntent().getDoubleExtra("price", data.getPrice());
        startTime = getIntent().getLongExtra("startTime", data.getStartTime());
        strategy = getIntent().getIntExtra("strategy", data.getStrategy());
        redPacketPrice = getIntent().getDoubleExtra("redPacketPrice", data.getRedPacketPrice());
        lineId = getIntent().getLongExtra("lineId", data.getLineId());

        if (travelType == ConstantValue.TravelType.DRIVER && startTime > 0) {
            dates = new ArrayList<>();
            DateEntity entity = new DateEntity();
            entity.setMillion(startTime);
            entity.setDate(getDate(startTime));
            dates.add(entity);
        }
        if (price == 0 || travelType == ConstantValue.TravelType.DRIVER) {
            getPrice();
        }
    }

    private void notifyDataSetChanged() {
        tv_startAddress.setText(start == null ? "" : start.getName());
        tv_endAddress.setText(end == null ? "" : end.getName());
        showWayPoint();

        if (travelType == ConstantValue.TravelType.DRIVER) {
            v_DRouteStrategy.setVisibility(View.VISIBLE);
            switch (strategy) {
                case RouteSearch.DRIVING_SINGLE_DEFAULT:
                    tv_routeStrategy.setText("速度最快");
                    break;
                case RouteSearch.DRIVING_SINGLE_SHORTEST:
                    tv_routeStrategy.setText("距离最短");
                    break;
                case RouteSearch.DRIVING_SINGLE_AVOID_CONGESTION:
                    tv_routeStrategy.setText("少收费避拥堵");
                    break;
                default:
                    v_DRouteStrategy.setVisibility(View.GONE);
                    break;
            }
        } else {
            v_DRouteStrategy.setVisibility(View.GONE);
        }

        String time = "";
        if (type == TYPE_RELEASE) {

            if (startTime != 0 && travelType == ConstantValue.TravelType.PASSENGER) {
                time = DateUtil.formatDayInWeek(startTime) + DateUtil.getTimeStr(startTime, "HH点mm分");
            } else if (dates != null && dates.size() > 0 && travelType == ConstantValue.TravelType.DRIVER) {
                time = String.format(Locale.CHINA, "%s(%d天)", DateUtil.getTimeStr(dates.get(0).getMillion(), "HH:mm"), dates.size());
            }
        } else {
            if (startTime != 0) {
                time = DateUtil.getTimeStr(startTime, "HH:mm");
            }
        }
        tv_startTime.setText(time);

        if (seats > 0) {
            String seatStr = travelType == ConstantValue.TravelType.PASSENGER ? String.format(Locale.CHINA, "%d人", seats) : String.format(Locale.CHINA, "%d个座位", seats);
            tv_seats.setText(seatStr);
        } else {
            tv_seats.setText("");
        }
        tv_priceText.setText(CommonUtil.formatPrice(price, 1));
        checkParams();
    }

    private void showWayPoint() {
        if (wayPoints != null && wayPoints.size() > 0) {
            v_wayPoint.setVisibility(View.VISIBLE);
            v_cuttingLine.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            if (wayPoints.size() > 1) {
                stringBuilder.append("经");
                stringBuilder.append(wayPoints.size());
                stringBuilder.append("地：");
                for (int i = 0; i < wayPoints.size(); i++) {
                    if (i < wayPoints.size()) {
                        stringBuilder.append(wayPoints.get(i).getName());
                        stringBuilder.append("、");
                    }
                }
            } else if (wayPoints.size() == 1) {
                stringBuilder.append(wayPoints.get(0).getName());
                stringBuilder.append("、");
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
            tv_wayPoint.setText(stringBuilder);
        } else {
            v_wayPoint.setVisibility(View.GONE);
            v_cuttingLine.setVisibility(View.GONE);
        }
    }

    private void nextOper() {
        if (start == null || end == null) {
            LogUtil.i("TTTTTTTTTTTT  1");
            return;
        }

        if(dialogIsShow(pStartTimeDialog)||
                dialogIsShow(dStartTimeDialog)||
                dialogIsShow(seatsDialog)||
                dialogIsShow(priceDialog)) {
            LogUtil.i("TTTTTTTTTTTT  2");
            return;
        }

        if (type == TYPE_RELEASE) {
            if ((ConstantValue.TravelType.PASSENGER == travelType && startTime == 0) ||
                    (ConstantValue.TravelType.DRIVER == travelType && (dates == null || dates.size() < 1))) {
                changeStartTime();
                LogUtil.i("TTTTTTTTTTTT  3");
                return;
            }
        } else {
            if (startTime == 0) {
                changeStartTime();
                LogUtil.i("TTTTTTTTTTTT  4");
                return;
            }
        }

        if (seats < 1) {
            changeSeats();
            LogUtil.i("TTTTTTTTTTTT  5");
            return;
        }
        if (priceList != null && priceList.size() >0 && price <= 0) {
            LogUtil.i("TTTTTTTTTTTT  6");
            changePrice();
            return;
        }
        LogUtil.i("TTTTTTTTTTTT  7");
    }

    private boolean dialogIsShow(DialogFragment dialog){
        boolean d=dialog != null && dialog.isVisible();
        LogUtil.i("tttttttttttttttt "+d);
        return dialog != null && (dialog.isVisible()||dialog.isRemoving());
    }

    private void getPrice() {
        if (start != null && end != null) {
            if (travelType == ConstantValue.TravelType.PASSENGER && seats < 1) {
                return;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("start", new double[]{start.getPoint().getLongitude(), start.getPoint().getLatitude()});
            params.put("end", new double[]{end.getPoint().getLongitude(), end.getPoint().getLatitude()});
            params.put("role", ConstantValue.TravelType.DRIVER);

            if (travelType == ConstantValue.TravelType.PASSENGER) {
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
                    if (travelType == ConstantValue.TravelType.PASSENGER) {
                        price = priceList.get(0).getPrice();
                        notifyDataSetChanged();
                    }
                    nextOper();
                }
            });
        }
    }

    private boolean checkParams() {
        if (start == null || end == null || seats == 0 || price == 0) {
            v_submit.setBackgroundResource(R.drawable.bg_circular_gray1);
            return false;
        }

        v_submit.setBackgroundResource(R.drawable.bg_circular_gradient_blue);
        return true;
    }

    void releaseTravel() {
        if (isLoading) {
            ToastUtil.showToast("您点的太快啦");
            return;
        }
        if (!checkParams()) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        String url;
        if (travelType == ConstantValue.TravelType.PASSENGER) {
            url = URLString.travelPassengerCreate;
            params.put("startTimes", new String[]{DateUtil.getTimeStr(startTime, "yyyy-MM-dd HH:mm:ss")});
            params.put("seatsNum", seats);
        } else {
            url = URLString.travelCreate;
            List<String> startTimes = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                startTimes.add(DateUtil.getTimeStr(dates.get(i).getMillion(), "yyyy-MM-dd HH:mm:ss"));
                params.put("startTimes", startTimes);
            }
            params.put("seats", seats);
            params.put("strategy", strategy);
            params.put("waypoints", null);
            List<double[]> points = new ArrayList<>();
            List<String> pointsName = new ArrayList<>();
            if (wayPoints != null) {
                for (int i = 0; i < wayPoints.size(); i++) {
                    Tip tip = wayPoints.get(i);
                    if (tip != null) {
                        points.add(new double[]{tip.getPoint().getLongitude(), tip.getPoint().getLatitude()});
                        pointsName.add(tip.getName());
                    }
                }
            }
            params.put("waypoints", points);
            params.put("waypointsAddress", pointsName);
        }

        params.put("token", User.getInstance().getToken());
        params.put("start", new double[]{start.getPoint().getLongitude(), start.getPoint().getLatitude()});
        params.put("end", new double[]{end.getPoint().getLongitude(), end.getPoint().getLatitude()});
        params.put("startAddress", start.getName());
        params.put("endAddress", end.getName());
        params.put("travelPrice", price);
        params.put("startCity", start.getDistrict());
        params.put("endCity", end.getDistrict());
        params.put("redPacketPrice", redPacketPrice);
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
                    data.setTravelType(travelType);
                    EventBus.getDefault().post(EventStatus.SHOW_MAIN_RED_POINT);
                    if (data.getJoinedGroups() != null && data.getJoinedGroups().size() > 0) {
                        new ReleaseSuccessDialog()
                                .setResult(data)
                                .setClick(new ReleaseSuccessDialog.OnDialogClick() {
                                    @Override
                                    public void onDismiss() {
                                        finish();
                                    }
                                })
                                .show(getSupportFragmentManager(), "");
                    } else {
                        finish();
                    }

                } else if (data.getStatus() == -122) {
                    ToastUtil.showToast("今日发布行程已达3次！");
                } else if (data.getStatus() == -106) {//未进行芝麻认证
                    ToastUtil.showToast("未进行芝麻认证！");

                } else if (data.getStatus() == -5) {
                    Intent intent = new Intent(ReleaseTravel_2Activity.this, AuthenticationActivity.class);
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
                            .show(getSupportFragmentManager(), "");
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

    public void submitLine() {
        if (isLoading) {
            return;
        }
        if (checkParams()) {
            String url;
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            if (lineId > 0) {
                url = URLString.personalLineUpdate;
                params.put("lineId", lineId);
            } else {
                url = URLString.personalLineCreate;
            }
            params.put("start", new double[]{start.getPoint().getLongitude(), start.getPoint().getLatitude()});
            params.put("end", new double[]{end.getPoint().getLongitude(), end.getPoint().getLatitude()});
            params.put("startAddress", start.getName());
            params.put("endAddress", end.getName());
            if (travelType == ConstantValue.TravelType.DRIVER) {
                params.put("strategy", strategy);
                List<double[]> points = new ArrayList<>();
                List<String> pointsName = new ArrayList<>();
                if (wayPoints != null) {
                    for (int i = 0; i < wayPoints.size(); i++) {
                        Tip tip = wayPoints.get(i);
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
            params.put("lineDesc", et_lineName.getText().toString());
            params.put("type", travelType);
            params.put("autoCreate", sv_autoDay.getState() == 4);
            params.put("autoDays", autoDays);
            isLoading = true;
            showLoadingDialog();
            Request.post(url, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    isLoading = false;
                    hideLoadingDialog();
                    if (intEntity.getStatus() == 200) {
                        if (sv_autoDay.getState() == 4) {
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
                                    }).show(getSupportFragmentManager(), "");
                        } else {
                            close();
                        }
                    } else if (intEntity.getStatus() == -5) {
                        DialogUtil.getAuthDialog(ReleaseTravel_2Activity.this)
                                .show(getSupportFragmentManager(), "");
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
                                .show(getSupportFragmentManager(), "");
                    }
                }

                @Override
                public void onError(String errorInfo) {
                    super.onError(errorInfo);
                    isLoading = false;
                    hideLoadingDialog();
                }
            });
        }
    }

    private void close() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    private ChooseWeekDialog.OnChooseWeekListener weekListener = new ChooseWeekDialog.OnChooseWeekListener() {
        @Override
        public void weeks(boolean[] status, ChooseWeekDialog dialog) {
            initAutoDays(status);
            if (autoDays != null && autoDays.size() > 0) {
                if (sv_autoDay.getState() != 4) {
                    sv_autoDay.toggleSwitch(true);
                }
            } else {
                if (sv_autoDay.getState() == 4) {
                    sv_autoDay.toggleSwitch(false);
                }
            }
            dialog.dismiss();
        }

        @Override
        public void onDismiss(ChooseWeekDialog dialog, boolean isOnWeeks) {
            if (isOnWeeks) {
                return;
            }
            if ((autoDays == null || autoDays.size() == 0)) {
                sv_autoDay.setState(false);
            }
        }
    };

    private void clearPriceList() {
        if (priceList != null) {
            priceList.clear();
        }
    }

    private List<WheelIntEntity> getSeatsData() {
        List<WheelIntEntity> datas = new ArrayList<>();
        String unit = travelType == ConstantValue.TravelType.DRIVER ? "个座位" : "人";
        for (int i = 1; i < 5; i++) {
            datas.add(new WheelIntEntity(i + unit, i));
        }
        return datas;
    }

    private String getDate(long time) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return getValue(cal.get(Calendar.YEAR)) + "-" + getValue(cal.get(Calendar.MONTH) + 1) + "-" + getValue(cal.get(Calendar.DATE));
    }

    private void initAutoDays(boolean[] weekStatus) {
        if (autoDays == null) {
            autoDays = new ArrayList<>();
        } else {
            autoDays.clear();
        }
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
            tv_autoDay.setText(s);
        } else {
            tv_autoDay.setText("设置发布时间，开启自动发布行程，遇见更多惊喜！");
            sv_autoDay.toggleSwitch(false);
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

}
