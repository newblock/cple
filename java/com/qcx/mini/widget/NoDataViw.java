package com.qcx.mini.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.dexfun.layout.DexFrameLayout;
import com.qcx.mini.R;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/27.
 *
 */

public class NoDataViw extends DexFrameLayout {
    private List<View> contentViews;

    private boolean isAuto = true;
    private RecyclerView rv_content;

    private ImageView iv_img;
    private TextView tv_describe;
    private int imgResourceID;
    private String describe;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child != iv_img && child != tv_describe && child.getLayoutParams() instanceof LayoutParams) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                if (params.hideAbel) {
                    contentViews.add(child);
                    if (child instanceof RecyclerView) {
                        rv_content = (RecyclerView) child;
                        rv_content.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                            @Override
                            public void onChanged() {
                                if (rv_content.getAdapter().getItemCount() > 0) {
                                    show(true);
                                } else {
                                    show(false);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    public NoDataViw(@NonNull Context context) {
        super(context);
        init(context);
    }

    public NoDataViw(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoDataViw);
        if (typedArray != null) {
            imgResourceID = typedArray.getResourceId(R.styleable.NoDataViw_NDDescribe, R.mipmap.img_witchoutplace);
            describe = typedArray.getString(R.styleable.NoDataViw_NDDescribe);
            typedArray.recycle();
        }
        init(context);
    }

    public NoDataViw(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        iv_img = new ImageView(context);
        FrameLayout.LayoutParams params = new LayoutParams(UiUtils.getSize(240), UiUtils.getSize(213));
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = UiUtils.getSize(80);
        iv_img.setLayoutParams(params);
        iv_img.setImageResource(imgResourceID);
        iv_img.setVisibility(GONE);
        addView(iv_img);

        tv_describe = new TextView(context);
        tv_describe.setTextColor(0xFF838393);
        tv_describe.setTextSize(14);
        FrameLayout.LayoutParams params2 = new LayoutParams(-2, -2);
        params2.gravity = Gravity.CENTER_HORIZONTAL;
        params2.topMargin = UiUtils.getSize(290);
        tv_describe.setLayoutParams(params2);
        tv_describe.setText(describe);
        tv_describe.setVisibility(GONE);
        addView(tv_describe);
        contentViews = new ArrayList<>();
    }

    public void show(boolean isShowContentView) {
        if (isShowContentView) {
            setContentViewsVisibility(VISIBLE);
            tv_describe.setVisibility(GONE);
            iv_img.setVisibility(GONE);
        } else {
            setContentViewsVisibility(GONE);
            tv_describe.setVisibility(VISIBLE);
            iv_img.setVisibility(VISIBLE);
        }
    }

    public void setContentViewsVisibility(int visibility) {
        for (int i = 0; i < contentViews.size(); i++) {
            contentViews.get(i).setVisibility(visibility);

        }
    }

    public NoDataViw setImgResourceID(int imgResourceID) {
        this.imgResourceID = imgResourceID;
        return this;
    }

    public NoDataViw setDescribe(String describe) {
        this.describe = describe;
        return this;
    }

    public static class LayoutParams extends DexFrameLayout.LayoutParams {
        private boolean hideAbel = true;

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.NoDataViw);
            if (typedArray != null) {
                hideAbel = typedArray.getBoolean(R.styleable.NoDataViw_NDHideAble, true);
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(@NonNull ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(@NonNull MarginLayoutParams source) {
            super(source);
        }
    }
}
