package com.qcx.mini.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.qcx.mini.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Created by zsp on 2017/9/6.
 */

public class QuRefreshHeader implements RefreshHeader {
    private View mView;
    private ImageView mImageView;
    private Context context;
    private ObjectAnimator mAnimator;
    private long lastRefreshTime;

    public QuRefreshHeader(Context context){
        this.context=context;
        init(context);
    }

    private void init(Context context){
        if(context==null) return;
        LayoutInflater inflater=LayoutInflater.from(context);
        mView=inflater.inflate(R.layout.refresh_header,null);
        mImageView=mView.findViewById(R.id.refresh_loading_img);
        mAnimator=ObjectAnimator.ofFloat(mImageView,"rotation",360);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(1500);
        mAnimator.setRepeatCount(-1);
    }

    public void setBackground(int color){
        mView.setBackgroundColor(color);
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @NonNull
    @Override
    public View getView() {
        return mView;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        mAnimator.start();
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
//        mAnimator.cancel();
        return 500;
    }

//    private String getTimeText(long time){
//       return  "最后刷新时间：".concat(DateUtil.getDay(time).concat(DateUtil.getTimeStr(time," HH:mm")));
//    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
//                if(lastRefreshTime==0) lastRefreshTime=DateUtil.getCurrentTimeMillis();
//                mTextView.setText(getTimeText(lastRefreshTime));
//                mHeaderText.setText("下拉开始刷新");
                break;
            case Refreshing:
//                mHeaderText.setText("正在刷新");
                break;
            case ReleaseToRefresh:
//                mHeaderText.setText("释放立即刷新");
                break;
        }
    }
}
