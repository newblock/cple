package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;

/**
 * Created by Administrator on 2018/3/21.
 */

public class MainActionBarView extends FrameLayout implements View.OnClickListener {
    private int currentPosition;//当前显示页
    private int selcetedTextColor = 0xFF4186E7;
    private int normalTextColer = 0xFFABB1BA;

    private View view_0, view_1, view_2, view_3;
    private ImageView iv_0, iv_1, iv_2, iv_3;
    private ImageView iv_start;
    private ImageView iv_point;
    private TextView tv_0, tv_1, tv_2, tv_3;

    private OnActionBarClickListener listener;

    public MainActionBarView(Context context) {
        super(context);
        init(context);
    }

    public MainActionBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainActionBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context == null) return;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.actionbar_main, this, false);
        addView(view);
        initViews(view);
    }

    private void initViews(View view) {
        view_0 = view.findViewById(R.id.action_bar_main_view_0);
        iv_0 = view.findViewById(R.id.action_bar_main_img_0);
        tv_0 = view.findViewById(R.id.action_bar_main_text_0);
        view_1 = view.findViewById(R.id.action_bar_main_view_1);
        iv_1 = view.findViewById(R.id.action_bar_main_img_1);
        tv_1 = view.findViewById(R.id.action_bar_main_text_1);
        view_2 = view.findViewById(R.id.action_bar_main_view_2);
        iv_2 = view.findViewById(R.id.action_bar_main_img_2);
        tv_2 = view.findViewById(R.id.action_bar_main_text_2);
        view_3 = view.findViewById(R.id.action_bar_main_view_3);
        iv_3 = view.findViewById(R.id.action_bar_main_img_3);
        tv_3 = view.findViewById(R.id.action_bar_main_text_3);
        iv_start = view.findViewById(R.id.action_bar_main_start);
        iv_point = view.findViewById(R.id.action_bar_main_img_1_point);

        view_0.setOnClickListener(this);
        view_1.setOnClickListener(this);
        view_2.setOnClickListener(this);
        view_3.setOnClickListener(this);
        iv_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_main_view_0:
                setCurrentPosition(0);
                break;
            case R.id.action_bar_main_view_1:
                setCurrentPosition(1);
                break;
            case R.id.action_bar_main_view_2:
                setCurrentPosition(2);
                break;
            case R.id.action_bar_main_view_3:
                setCurrentPosition(3);
                break;
            case R.id.action_bar_main_start:
                setCurrentPosition(-1);
                if (listener != null) {
                    listener.onStartClick();
                }
            default:
                return;
        }
        if (listener != null) {
            listener.onClick(currentPosition);
        }
    }

    public void setPointVisible(boolean isVisibity) {
        if (isVisibity) {
            iv_point.setVisibility(VISIBLE);
        } else {
            iv_point.setVisibility(GONE);
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        setBackGround();
    }

    public void setListener(OnActionBarClickListener listener) {
        this.listener = listener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    private void setBackGround() {
        initBackGrount();
        switch (currentPosition) {
            case 0:
                iv_0.setImageResource(R.mipmap.btn_home_on);
                tv_0.setTextColor(selcetedTextColor);
                break;
            case 1:
                iv_1.setImageResource(R.mipmap.btn_find_on);
                tv_1.setTextColor(selcetedTextColor);
                break;
            case 2:
                iv_2.setImageResource(R.mipmap.btn_news_on);
                tv_2.setTextColor(selcetedTextColor);
                break;
            case 3:
                iv_3.setImageResource(R.mipmap.btn_me_on);
                tv_3.setTextColor(selcetedTextColor);
                break;
            default:
                break;
        }
    }

    private void initBackGrount() {
        iv_0.setImageResource(R.mipmap.btn_home);
        tv_0.setTextColor(normalTextColer);
        iv_1.setImageResource(R.mipmap.btn_find);
        tv_1.setTextColor(normalTextColer);
        iv_2.setImageResource(R.mipmap.btn_news);
        tv_2.setTextColor(normalTextColer);
        iv_3.setImageResource(R.mipmap.btn_me);
        tv_3.setTextColor(normalTextColer);
    }

    public interface OnActionBarClickListener {
        void onClick(int position);

        void onStartClick();
    }
}
