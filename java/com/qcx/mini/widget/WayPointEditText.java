package com.qcx.mini.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qcx.mini.R;

/**
 * Created by Administrator on 2018/5/8.
 */

public class WayPointEditText extends FrameLayout {
    private EditText et_input;
    private ImageView iv_img;

    public WayPointEditText(Context context) {
        super(context);
        init(context);
    }

    public WayPointEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WayPointEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_way_point, this, false);
        et_input = view.findViewById(R.id.view_way_point_input);
        iv_img = view.findViewById(R.id.view_way_point_img);
        addView(view);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        et_input.addTextChangedListener(watcher);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener focusChangeListener) {
        et_input.setOnFocusChangeListener(focusChangeListener);
    }

    public void setImgClickListener(OnClickListener listener) {
        iv_img.setOnClickListener(listener);
    }

    public boolean isFocused() {
        return et_input.isFocused();
    }

    public EditText getEditText() {
        return et_input;
    }

    public void setImageResource(int imageResourceId){
        iv_img.setImageResource(imageResourceId);
    }
}
