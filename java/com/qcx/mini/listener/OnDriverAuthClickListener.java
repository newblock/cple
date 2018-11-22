package com.qcx.mini.listener;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.qcx.mini.User;
import com.qcx.mini.activity.AuthenticationActivity;
import com.qcx.mini.activity.AuthenticationStep3Activity;
import com.qcx.mini.activity.DriverInfoActivity;
import com.qcx.mini.activity.RealNameActivity;
import com.qcx.mini.dialog.QuDialog;

import static com.qcx.mini.ConstantValue.AuthStatus.CHECKING;
import static com.qcx.mini.ConstantValue.AuthStatus.NOTPASS;
import static com.qcx.mini.ConstantValue.AuthStatus.PASS;
import static com.qcx.mini.ConstantValue.AuthStatus.UNCOMMITTED;

/**
 * Created by Administrator on 2018/8/21.
 * 点击车主认证
 */

public class OnDriverAuthClickListener {
    private FragmentActivity activity;
    private QuDialog dialog;

    public OnDriverAuthClickListener(FragmentActivity activity) {
        this.activity = activity;
    }

    public void onClick() {
        switch (User.getInstance().getDriverStatus()) {
            case PASS:
                activity.startActivity(new Intent(activity, DriverInfoActivity.class));
                break;
            case CHECKING:
                if (dialog == null) {
                    dialog = new QuDialog().setTitle("您已提交资料，我们将尽快为您审核...")
                            .setRightBtn("知道了", new QuDialog.OnClickListener() {
                                @Override
                                public void onClick(QuDialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                }
                dialog.show(activity.getSupportFragmentManager(), "");
                break;
            case UNCOMMITTED:
            case NOTPASS:
                if (User.getInstance().isAuthenStatus()) {
                    activity.startActivity(new Intent(activity, AuthenticationActivity.class));
                } else {
                    Intent intent = new Intent(activity, RealNameActivity.class);
                    intent.putExtra("type", RealNameActivity.TYPE_DRIVER);
                    activity.startActivity(intent);
                }
                break;
        }
    }

}
