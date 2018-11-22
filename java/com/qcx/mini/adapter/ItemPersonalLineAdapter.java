package com.qcx.mini.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.AuthenticationActivity;
import com.qcx.mini.base.BaseRecycleViewHolder;
import com.qcx.mini.base.BaseRecyclerViewAdapter;
import com.qcx.mini.dialog.ChooseWeekDialog;
import com.qcx.mini.dialog.QAlertDialog;
import com.qcx.mini.dialog.QAlertDialog2;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PersonalLineEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.widget.SwitchView;
import com.qcx.mini.widget.WayPointEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/3.
 *
 */

public class ItemPersonalLineAdapter extends BaseRecyclerViewAdapter<PersonalLineEntity, ItemPersonalLineAdapter.ItemPersonalLineViewHolder> {
    private boolean isDeleteAble = false;
    private OnLineClickListener listener;
    private FragmentActivity activity;
    private BaseRecycleViewHolder.Params params;

    public ItemPersonalLineAdapter(Context context) {
        super(context);
        activity = (FragmentActivity) context;
    }

    public void setParams(BaseRecycleViewHolder.Params params) {
        this.params = params;
        notifyDataSetChanged();
    }

    @Override
    public ItemPersonalLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemPersonalLineViewHolder(inflater.inflate(R.layout.item_personal_line, parent, false));
    }

    public boolean isDeleteAble() {
        return isDeleteAble;
    }

    public void setDeleteAble(boolean deleteAble) {
        isDeleteAble = deleteAble;
        if (isDeleteAble) {
            initChecked();
        }
        notifyDataSetChanged();
    }

    private void initChecked() {
        for (int i = 0; i < getItemCount(); i++) {
            getItem(i).setChecked(false);
        }
    }

    public void setListener(OnLineClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ItemPersonalLineViewHolder holder, int position) {
        if(params==null){
            params=new BaseRecycleViewHolder.Params();
        }
        params.setPosition(position);
        holder.bindData(getItem(position),params);
    }

    public class ItemPersonalLineViewHolder extends BaseRecycleViewHolder {
        private PersonalLineEntity entity;
        private boolean isSpread = false;

        private TextView tv_lineName;
        private TextView tv_lineInfo1;
        private TextView tv_lineInfo2;
        private TextView tv_lineInfo3;
        private TextView tv_startAddress;
        private TextView tv_endAddress;
        private TextView tv_auto;
        private ImageView sv_auto;
        private TextView tv_week;
        private ImageView iv_deleatCheck;
        private View v_detail;
        private LinearLayout v_wayPoint;
        private TextView tv_showAllWayPoint;
        private View v_point1;
        private View v_point2;
        private View v_point3;
        private TextView tv_point1;
        private TextView tv_point2;
        private TextView tv_point3;
        private boolean isClick=false;


        public ItemPersonalLineViewHolder(View itemView) {
            super(itemView);
            tv_lineName = itemView.findViewById(R.id.item_personal_line_name);
            tv_lineInfo1 = itemView.findViewById(R.id.item_personal_line_info1);
            tv_lineInfo2 = itemView.findViewById(R.id.item_personal_line_info2);
            tv_lineInfo3 = itemView.findViewById(R.id.item_personal_line_info3);
            tv_startAddress = itemView.findViewById(R.id.item_personal_line_startAddress);
            tv_endAddress = itemView.findViewById(R.id.item_personal_line_endAddress);
            sv_auto = itemView.findViewById(R.id.item_personal_line_auto);
            tv_week = itemView.findViewById(R.id.item_personal_line_week);
            iv_deleatCheck = itemView.findViewById(R.id.item_personal_line_delete_img);
            tv_auto = itemView.findViewById(R.id.item_personal_line_auto_text);
            v_detail = itemView.findViewById(R.id.item_personal_line_to_detail);
            tv_showAllWayPoint = itemView.findViewById(R.id.item_personal_line_show_all_way_points);
            v_wayPoint = itemView.findViewById(R.id.item_personal_line_way_points_view);
            v_point1 = itemView.findViewById(R.id.item_personal_line_way_points_view_1);
            v_point2 = itemView.findViewById(R.id.item_personal_line_way_points_view_2);
            v_point3 = itemView.findViewById(R.id.item_personal_line_way_points_view_3);
            tv_point1 = itemView.findViewById(R.id.item_personal_line_way_points_view_1_text);
            tv_point2 = itemView.findViewById(R.id.item_personal_line_way_points_view_2_text);
            tv_point3 = itemView.findViewById(R.id.item_personal_line_way_points_view_3_text);

            tv_showAllWayPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity == null || entity.getWaypointsAddress() == null) {
                        return;
                    }
                    isSpread = !isSpread;
                    spreadWayPoint(isSpread);
                }
            });
            v_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&entity!=null){
                        listener.onDetail(entity);
                    }
                }
            });

            iv_deleatCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&entity!=null){
                        listener.onDelete(entity);
                    }
                }
            });
        }

        @Override
        public void bindData(Object data,Params params) {
            try {
                entity = (PersonalLineEntity) data;
                if (TextUtils.isEmpty(entity.getLineDesc())) {
                    tv_lineName.setText("常用");
                } else {
                    tv_lineName.setText(entity.getLineDesc());
                }

                tv_lineInfo1.setText(entity.getStartTime());
                if (entity.getType() == ConstantValue.TravelType.DRIVER) {
                    tv_lineInfo2.setText(String.format(Locale.CHINA, "%d座", entity.getSeats()));
                    tv_lineInfo3.setText(String.format(Locale.CHINA, "%s元/位", CommonUtil.formatPrice(entity.getPrice(),0)));
                } else {
                    tv_lineInfo2.setText(String.format(Locale.CHINA, "%d人", entity.getSeats()));
                    tv_lineInfo3.setText(String.format(Locale.CHINA, "%s元", CommonUtil.formatPrice(entity.getPrice(),0)));
                }

                tv_startAddress.setText(entity.getStartAddress());
                tv_endAddress.setText(entity.getEndAddress());
                tv_week.setText(getWeekStr(entity.getAutoDays()));
                if(entity.isAutoCreate()){
                    sv_auto.setImageResource(R.mipmap.controls_switch_on);
                }else {
                    sv_auto.setImageResource(R.mipmap.controls_switch_off);
                }
                changeAutoTextColor(entity.isAutoCreate());
                isClick=false;
                sv_auto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!entity.isAutoCreate()){
                            new ChooseWeekDialog()
                                    .setWeek(entity.getAutoDays())
                                    .setListener(new ChooseWeekDialog.OnChooseWeekListener() {
                                        private boolean isSure = false;

                                        @Override
                                        public void weeks(boolean[] status, ChooseWeekDialog dialog) {
                                            entity.setAutoDays(getAutoDays(status));
                                            tv_week.setText(getWeekStr(entity.getAutoDays()));
                                            changeAutoTextColor(true);
                                            if (entity.getAutoDays() == null || entity.getAutoDays().size() == 0) {
                                                entity.setAutoCreate(false);
                                            }else {
                                                entity.setAutoCreate(true);
                                            }
                                            submit();
                                            isSure = true;
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onDismiss(ChooseWeekDialog dialog,boolean isOnWeeks) {
                                            if ((entity.getAutoDays() == null || entity.getAutoDays().size() == 0) || !isSure) {
                                               entity.setAutoCreate(false);
                                                changeAutoTextColor(false);
                                            }
                                            if(dialog!=null){
                                                dialog.dismiss();
                                            }
                                        }
                                    }).show(activity.getSupportFragmentManager(), "");
                        }else {
                            entity.setAutoCreate(false);
                            changeAutoTextColor(false);
                            submit();
                        }
                    }
                });
                isSpread = false;
                spreadWayPoint(isSpread);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private List<Integer> getAutoDays(boolean[] weekStatus) {
            List<Integer> autoDays = new ArrayList<>();
            if (weekStatus == null) weekStatus = new boolean[]{false};
            for (int i = 0; i < weekStatus.length; i++) {
                if (weekStatus[i]) {
                    if (i == 6) {
                        autoDays.add(0);
                    } else {
                        autoDays.add(i + 1);
                    }
                }
            }
            return autoDays;
        }

        private String getWeekStr(List<Integer> weeks) {
            String str = "未开启自动发布，该路线在发布页可快速发布";
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
                str = "每周日，发布下一周(" + builder + ")的行程";
            }
            return str;
        }

        private void changeAutoTextColor(boolean isAuto) {
            if (isAuto) {
                tv_week.setTextColor(0xFF737373);
                tv_auto.setTextColor(0xFF4A4A4A);
                sv_auto.setImageResource(R.mipmap.controls_switch_on);
            } else {
                tv_week.setTextColor(0xFFC4C4C4);
                tv_auto.setTextColor(0xFFC4C4C4);
                sv_auto.setImageResource(R.mipmap.controls_switch_off);
            }
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

        public void submit() {
            Map<String, Object> params = new HashMap<>();
            params.put("token", User.getInstance().getToken());
            params.put("lineId", entity.getLineId());
            params.put("start", entity.getStart());
            params.put("end", entity.getEnd());
            params.put("startAddress", entity.getStartAddress());
            params.put("endAddress", entity.getEndAddress());
            if (entity.getType() == ConstantValue.TravelType.DRIVER) {
                params.put("strategy", entity.getStrategy());
                params.put("waypoints", entity.getWaypoints());
                params.put("waypointsAddress", entity.getWaypointsAddress());
            }
            params.put("seats", entity.getSeats());
            params.put("startTime", entity.getStartTime());
            params.put("price", entity.getPrice());
            params.put("lineDesc", entity.getLineDesc());
            params.put("type", entity.getType());
            params.put("autoCreate", entity.isAutoCreate());
            params.put("autoDays", entity.getAutoDays());
            Request.post(URLString.personalLineUpdate, params, new EntityCallback(IntEntity.class) {
                @Override
                public void onSuccess(Object t) {
                    IntEntity intEntity = (IntEntity) t;
                    if (intEntity.getStatus() == -5) {
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
                                        Intent intent = new Intent(activity, AuthenticationActivity.class);
                                        activity.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                                .show(activity.getSupportFragmentManager(), "");
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
                                .show(activity.getSupportFragmentManager(), "");
                    }else if(intEntity.getStatus()==200&&entity.isAutoCreate()){
                        try {
                            new QAlertDialog2()
                                    .setListener(new QAlertDialog2.OnDialogClick() {
                                        @Override
                                        public void onClick(QAlertDialog2 dialog) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onDismiss() {
                                        }
                                    }).show(activity.getSupportFragmentManager(),"");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(String errorInfo) {
                    entity.setAutoCreate(!entity.isAutoCreate());
                    changeAutoTextColor(entity.isAutoCreate());
                }
            });
        }

        private void spreadWayPoint(boolean isSpread) {
            if (isSpread) {
                int size = entity.getWaypointsAddress().size();
                switch (size) {
                    case 1:
                        v_point1.setVisibility(View.VISIBLE);
                        v_point2.setVisibility(View.GONE);
                        v_point3.setVisibility(View.GONE);
                        tv_point1.setText(entity.getWaypointsAddress().get(0));
                        break;
                    case 2:
                        v_point1.setVisibility(View.VISIBLE);
                        v_point2.setVisibility(View.VISIBLE);
                        v_point3.setVisibility(View.GONE);
                        tv_point1.setText(entity.getWaypointsAddress().get(0));
                        tv_point2.setText(entity.getWaypointsAddress().get(1));
                        tv_showAllWayPoint.setText("收起");
                        break;
                    case 3:
                        v_point1.setVisibility(View.VISIBLE);
                        v_point2.setVisibility(View.VISIBLE);
                        v_point3.setVisibility(View.VISIBLE);
                        tv_point1.setText(entity.getWaypointsAddress().get(0));
                        tv_point2.setText(entity.getWaypointsAddress().get(1));
                        tv_point3.setText(entity.getWaypointsAddress().get(2));
                        tv_showAllWayPoint.setText("收起");
                        break;
                }
            } else {
                List<String> wayPointNames = entity.getWaypointsAddress();
                tv_showAllWayPoint.setVisibility(View.GONE);
                if (wayPointNames != null && wayPointNames.size() > 0) {
                    v_point1.setVisibility(View.VISIBLE);
                    StringBuilder stringBuilder = new StringBuilder();
                    if (wayPointNames.size() == 1) {
                        stringBuilder.append(wayPointNames.get(0));
                        stringBuilder.append("、");
                    } else {
                        tv_showAllWayPoint.setVisibility(View.VISIBLE);
                        tv_showAllWayPoint.setText("展开");
                        stringBuilder.append("经");
                        stringBuilder.append(wayPointNames.size());
                        stringBuilder.append("地：");
                        for (int i = 0; i < wayPointNames.size(); i++) {
                            stringBuilder.append(wayPointNames.get(i));
                            stringBuilder.append("、");
                        }
                    }
                    stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
                    tv_point1.setText(stringBuilder);
                } else {
                    v_point1.setVisibility(View.GONE);
                }
                v_point2.setVisibility(View.GONE);
                v_point3.setVisibility(View.GONE);
            }
        }


    }

    public interface OnLineClickListener {
        void onDelete(PersonalLineEntity line);

        void onDetail(PersonalLineEntity line);
    }
}
