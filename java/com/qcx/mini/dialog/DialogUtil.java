package com.qcx.mini.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.qcx.mini.ConstantString;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.MainClass;
import com.qcx.mini.User;
import com.qcx.mini.activity.ReleaseTravel_2Activity;
import com.qcx.mini.entity.WheelIntEntity;
import com.qcx.mini.listener.OnDriverAuthClickListener;
import com.qcx.mini.net.EntityCallback;
import com.qcx.mini.net.Request;
import com.qcx.mini.net.URLString;
import com.qcx.mini.utils.Navigation;
import com.qcx.mini.utils.PictureUtil;
import com.qcx.mini.utils.SystemUtil;
import com.qcx.mini.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/19.
 * 
 */

public class DialogUtil {

    public static void showSetPermissionDialog(final Context context, final FragmentManager fragmentManager, String permissionName) {
        if (fragmentManager != null) {
            new QAlertDialog().setTitleText("提示")
                    .setContentText("该功能需要您手动开启" + permissionName)
                    .setCancelClickListener(new QAlertDialog.OnDialogClick() {
                        @Override
                        public void onClick(QAlertDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .setSureClickListener(new QAlertDialog.OnDialogClick() {
                        @Override
                        public void onClick(QAlertDialog dialog) {
                            SystemUtil.toPermissionSetPage(context);
                            dialog.dismiss();
                        }
                    }).show(fragmentManager, "");
        }
    }

    /**
     *
     * @param context context
     * @param phoneNums null 拨打客服电话
     */
    public static void call(final Context context, String[] phoneNums) {
        if (phoneNums == null) phoneNums = new String[]{ConstantString.HELP_PHONE};
        final String[] items = phoneNums;
        final ItemsDialog dialog = new ItemsDialog(context, items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_DIAL);//Intent.ACTION_DIAL
                Uri data = Uri.parse("tel:".concat(items[position]));
                intent.setData(data);
                MainClass.getInstance().startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    public static void navigation(final double[] address, final String addressText, final int navMode, final Context context) {
        if (address == null || address.length != 2) {
            ToastUtil.showToast("没有位置信息");
            return;
        }
        String[] items = {"高德地图", "百度地图"};
        final ItemsDialog dialog = new ItemsDialog(context, items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Navigation.togGaode(address[0], address[1], addressText, navMode, context);
                        break;
                    case 1:
                        Navigation.toBaidu(address[0], address[1], addressText, navMode, context);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    public static void pictureDialog(final AppCompatActivity activity, final String cameraName) {
        String[] items = {"相册", "拍照"};
        final ItemsDialog dialog = new ItemsDialog(activity, items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        PictureUtil.secletImage(activity);
                        break;
                    case 1:
                        //39.91441,116.40405
                        PictureUtil.takeCameraOnly(activity, cameraName);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    public static void unfollowDialog(Activity activity, final long attentionPhone, final EntityCallback callback) {
        String[] items = {"取消关注"};
        final ItemsDialog dialog = new ItemsDialog(activity, items, null);
        dialog.itemTextColor(Color.BLACK)
                .cancelText(Color.BLACK)
                .cancelText("取消")
                .isTitleShow(false)
                .layoutAnimation(null).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Map<String, Object> params = new HashMap<>();
                        params.put("token", User.getInstance().getToken());
                        params.put("attentionPhone", attentionPhone);
                        Request.post(URLString.pullAttention, params, callback);
                        break;
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 选择座位 dialog
     * @param travelType 行程类型
     * @param listener 回调
     * @return dialog
     */
    public static SingleWheelDialog getChooseSeatsDialog(int travelType, SingleWheelDialog.OnSingleWheelDialogListener<WheelIntEntity> listener) {
        List<WheelIntEntity> datas = new ArrayList<>();
        String unit = travelType == ConstantValue.TravelType.DRIVER ? "个座位" : "人";
        for (int i = 1; i < 5; i++) {
            datas.add(new WheelIntEntity(i + unit, i));
        }
        return new SingleWheelDialog<WheelIntEntity>()
                .setData(datas)
                .setListener(listener);
    }

    /**
     *  需要实名认证弹窗
     * @param activity FragmentActivity
     * @return QAlertDialog
     */
    public static QAlertDialog getAuthDialog(final FragmentActivity activity){
        return new QAlertDialog().setImg(QAlertDialog.IMG_ALERT)
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
                        new OnDriverAuthClickListener(activity).onClick();
                        dialog.dismiss();
                    }
                });
    }
}
