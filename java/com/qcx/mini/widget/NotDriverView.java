package com.qcx.mini.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.activity.AuthenticationActivity;
import com.qcx.mini.activity.RealNameActivity;
import com.qcx.mini.dialog.QuDialog;
import com.qcx.mini.listener.OnDriverAuthClickListener;

import static com.qcx.mini.ConstantValue.AuthStatus.CHECKING;
import static com.qcx.mini.ConstantValue.AuthStatus.NOTPASS;
import static com.qcx.mini.ConstantValue.AuthStatus.PASS;
import static com.qcx.mini.ConstantValue.AuthStatus.UNCOMMITTED;

/**
 * Created by Administrator on 2018/8/21.
 */

public class NotDriverView extends FrameLayout {
    private View view;
    private OnDriverAuthClickListener listener;

    public void setListener(OnDriverAuthClickListener listener) {
        this.listener = listener;
    }

    public NotDriverView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NotDriverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotDriverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(R.layout.view_not_driver,this,false);
        addView(view);
        TextView driver=view.findViewById(R.id.view_not_driver_text);
        if(driver!=null) {
            driver.setText("注册成为车主");

            driver.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onClick();
                    }
                }
            });
        }
    }
}
