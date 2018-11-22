package com.qcx.mini.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.AuthenticationActivity;
import com.qcx.mini.activity.PersonalLinesActivity;
import com.qcx.mini.activity.ReleaseTravelActivity;
import com.qcx.mini.activity.ReleaseTravel_2Activity;
import com.qcx.mini.activity.SetLineActivity;
import com.qcx.mini.activity.TravelNoneActivity;
import com.qcx.mini.adapter.ItemReleaseLineAdapter;
import com.qcx.mini.adapter.viewHolder.ItemReleaseLineViewHolder;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.ReleaseLineInfoEntity;
import com.qcx.mini.entity.ReleaseLinesEntity;
import com.qcx.mini.entity.ReleaseResultEntity;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.CasheUtil;
import com.qcx.mini.utils.DateUtil;
import com.qcx.mini.utils.GsonUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ReleaseTypeDialog extends DialogFragment {
    private final int LINE_CHANGE = 22;
    private ItemReleaseLineAdapter adapter;
    private RecyclerView mRecyclerView;
    private View v_line1;
    private View v_line2;
    private View v_add;
    private ItemReleaseLineViewHolder holder1;
    private ItemReleaseLineViewHolder holder2;
    private ItemReleaseLineAdapter.ItemReleaseLineBottomViewHolder holder3;
    private boolean isShowSuccessDialog = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_release_type, null);

        final Dialog dialog = new Dialog(getActivity(), R.style.style_bottom_dialog);
        dialog.setContentView(view);
        dialog.show();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); //可设置dialog的位置
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        init(view, dialog);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLines();
    }

    private void init(View view, final Dialog dialog) {
        view.findViewById(R.id.find_passenger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int autoStatus = User.getInstance().getDriverStatus();
                switch (autoStatus) {
                    case ConstantValue.AuthStatus.CHECKING:
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
                        break;
                    case ConstantValue.AuthStatus.PASS:
                        Intent intent = new Intent(getActivity(), ReleaseTravelActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                        dialog.cancel();
                        break;
                    case -1:
                        ToastUtil.showToast("无法获取实名认证状态，请稍后再试");
                        break;
                    default:
                        startActivity(new Intent(getContext(), AuthenticationActivity.class));
                        break;
                }
            }
        });
        view.findViewById(R.id.find_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReleaseTravelActivity.class);
                intent.putExtra("type", 0);
//                Intent intent = new Intent(getActivity(), ReleaseTravel_2Activity.class);
//                intent.putExtra("travelType", ConstantValue.TravelType.PASSENGER);
                startActivity(intent);
                dialog.cancel();
            }
        });
        adapter = new ItemReleaseLineAdapter(getContext());
        mRecyclerView = view.findViewById(R.id.release_type_list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        adapter.setListener(itemClickListener);
        v_line1 = view.findViewById(R.id.release_type_min_line_1);
        v_line2 = view.findViewById(R.id.release_type_min_line_2);
        v_add = view.findViewById(R.id.release_type_min_line_add);
        holder1 = new ItemReleaseLineViewHolder(v_line1);
        holder2 = new ItemReleaseLineViewHolder(v_line2);
        holder1.setHolederListener(itemClickListener);
        holder2.setHolederListener(itemClickListener);
        holder3 = new ItemReleaseLineAdapter.ItemReleaseLineBottomViewHolder(v_add);
        holder3.setHolederListener(itemClickListener);

        ReleaseLinesEntity linesEntity = CasheUtil.getReleaseLinesEntity();
        setLines(linesEntity);
//        getLines();
    }

    private void getLines() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.personalLinesRelease, params, new EntityCallback(ReleaseLinesEntity.class) {
            @Override
            public void onSuccess(Object t) {
                ReleaseLinesEntity linesEntity = (ReleaseLinesEntity) t;
                setLines(linesEntity);
                CasheUtil.putReleaseLinesEntity(linesEntity);
            }
        });
    }

    private ItemReleaseLineAdapter.OnReleaseLineItemClickListener itemClickListener = new ItemReleaseLineAdapter.OnReleaseLineItemClickListener() {
        @Override
        public void onInfoClick(ReleaseLineInfoEntity line) {
            Intent intent = new Intent(getContext(), ReleaseTravelActivity.class);
//            intent.putExtra("lineId",line.getLineId());
            if (line.getType() == ConstantValue.TravelType.DRIVER) {
                intent.putExtra("type", 1);
            } else {
                intent.putExtra("type", 0);
            }
            String lineInfo = GsonUtil.create().toJson(line);
            intent.putExtra("lineInfo", lineInfo);
            startActivityForResult(intent, LINE_CHANGE);
            dismiss();
        }

        @Override
        public void onReleaseClick(ReleaseLineInfoEntity line, TextView releaseView) {
            if (!line.isHaveCreated()) {
                release(line, releaseView);
            }
        }

        @Override
        public void onAddClick() {
            Intent intent = new Intent(getContext(), SetLineActivity.class);
            startActivityForResult(intent, LINE_CHANGE);
        }

        @Override
        public void onManger() {
            Intent intent = new Intent(getContext(), PersonalLinesActivity.class);
            startActivityForResult(intent, LINE_CHANGE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == LINE_CHANGE) {
//            getLines();
        }
    }

    boolean isRelease = false;
    private boolean noToDetail = false;
    private LoadingDialog dialog;

    void release(final ReleaseLineInfoEntity line, final TextView releaseView) {
        if (isRelease) {
            ToastUtil.showToast("请稍后");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        String url;
        if (line.getType() == ConstantValue.TravelType.PASSENGER) {
            url = URLString.travelPassengerCreate;
            params.put("seatsNum", line.getSeats());
        } else {
            url = URLString.travelCreate;
            params.put("seats", line.getSeats());
            params.put("strategy", line.getStrategy());
            params.put("waypoints", line.getWaypoints());
        }

        params.put("startTimes", new String[]{DateUtil.getTimeStr(line.getStartTime(), "yyyy-MM-dd HH:mm:ss")});
        params.put("token", User.getInstance().getToken());
        params.put("start", line.getStart());
        params.put("end", line.getEnd());
        params.put("startAddress", line.getStartAddress());
        params.put("endAddress", line.getEndAddress());
        params.put("travelPrice", line.getPrice());
        params.put("redPacketPrice", line.getRedPacketPrice());
        isRelease = true;
        dialog = new LoadingDialog();
        dialog.show(getFragmentManager(), "");
        Request.post(url, params, new EntityCallback(ReleaseResultEntity.class) {
            @Override
            public void onSuccess(Object t) {
                isRelease = false;
                if (dialog != null) {
                    dialog.dismiss();
                }
                final ReleaseResultEntity data = (ReleaseResultEntity) t;
                if (data.getStatus() == 200) {
                    data.setTravelType(line.getType());
                    releaseView.setText("已发布");
                    releaseView.setTextColor(Color.WHITE);
                    if (listener != null) {
                        listener.onSuccess();
                    }
                    if (isShowSuccessDialog && data.getJoinedGroups() != null && data.getJoinedGroups().size() > 0) {
                        new ReleaseSuccessDialog()
                                .setResult(data)
                                .show(getFragmentManager(), "");
                    } else {
                        ToastUtil.showToast("发布成功");
                        if (!noToDetail) {
                            long id = -1;
                            if (line.getType() == ConstantValue.TravelType.DRIVER && data.getTravelIds() != null && data.getTravelIds().size() > 0) {
                                id = data.getTravelIds().get(0);
                            } else if (data.getPassengerTravelIds() != null && data.getPassengerTravelIds().size() > 0) {
                                id = data.getPassengerTravelIds().get(0);
                            }
                            if (id > 0) {
                                Intent intent = new Intent(getContext(), TravelNoneActivity.class);
                                intent.putExtra("travelId", id);
                                intent.putExtra("travelType", data.getTravelType());
                                getContext().startActivity(intent);
                            }
                        }
                    }
                    dismiss();
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
                if (dialog != null) {
                    dialog.dismiss();
                }
                isRelease = false;
            }
        });

    }

    public ReleaseTypeDialog setShowSuccessDialog(boolean showSuccessDialog) {
        isShowSuccessDialog = showSuccessDialog;
        return this;
    }

    private void setLines(ReleaseLinesEntity linesEntity) {
        if (linesEntity == null) {
            return;
        }
        List<ReleaseLineInfoEntity> lines = linesEntity.getListManualPersonalTL();
        if (lines == null || lines.size() <= 0) {
            mRecyclerView.setVisibility(View.GONE);
            v_line1.setVisibility(View.GONE);
            v_line2.setVisibility(View.GONE);
            v_add.setVisibility(View.VISIBLE);
        } else if (lines.size() == 1) {
            mRecyclerView.setVisibility(View.GONE);
            v_line1.setVisibility(View.VISIBLE);
            v_line2.setVisibility(View.GONE);
            v_add.setVisibility(View.VISIBLE);
            holder1.bindData(lines.get(0));
        } else if (lines.size() == 2) {
            mRecyclerView.setVisibility(View.GONE);
            v_line1.setVisibility(View.VISIBLE);
            v_line2.setVisibility(View.VISIBLE);
            v_add.setVisibility(View.VISIBLE);
            holder1.bindData(lines.get(0));
            holder2.bindData(lines.get(1));
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            v_line1.setVisibility(View.GONE);
            v_line2.setVisibility(View.GONE);
            v_add.setVisibility(View.GONE);
            adapter.setDatas(lines);
        }
    }

    OnReleaseSuccessListener listener;

    public ReleaseTypeDialog setListener(OnReleaseSuccessListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnReleaseSuccessListener {
        void onSuccess();
    }

}
