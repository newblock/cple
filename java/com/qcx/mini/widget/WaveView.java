package com.qcx.mini.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.qcx.mini.R;

/**
 * Created by Administrator on 2018/7/13.
 */

public class WaveView extends View {
    private Paint mPaint;
    private ValueAnimator animator;
    private float width;
    private float height;
    private int moveX = 0;

    Path path;
    private float waveShowCount = 0.5f;//屏幕显示的波的个数
    private int waveCount = (int) (waveShowCount + 0.5) + 3;
    private int waveHeight = 80;//整个波高度的一半
    private int waveWidth;//一个完整的波的长度
    private int rectHeight = 200;//波底部矩形的高度

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        waveWidth = (int) (width / waveShowCount);
        rectHeight = (int) (height - 2 * waveHeight);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0x660022ff);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setShader(new LinearGradient(0, 0, 0, 500, new int[]{0x330044ff, 0xaa0044ff}, null, Shader.TileMode.CLAMP));

        waveShowCount = 0.5f;
        waveCount = (int) (waveShowCount + 0.5 ) + 3;
        waveHeight = 80;
        rectHeight = 200;
        initAnim();

    }

    private void initAnim() {
        animator = ValueAnimator.ofInt(0, 100);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                moveX = -value * waveWidth / 100;
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1500);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.cancel();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(moveX, 0);
        canvas.drawPath(getWavePath_2(), mPaint);
        canvas.restore();
        canvas.translate(moveX + waveWidth / 4, 0);
        canvas.drawPath(getWavePath_2(), mPaint);
    }

    private Path getWavePath_2() {
        if (path == null) {
            path = new Path();
            float startX = -waveWidth * 3 / 2;
            float startY = height - rectHeight - waveHeight;
            int rX = waveWidth / 4;
            path.moveTo(startX, startY);
            for (int i = 1; i <= waveCount; i++) {
                path.quadTo(startX + rX * (4 * i - 3), startY - waveHeight, startX + rX * (4 * i - 2), startY);
                path.quadTo(startX + rX * (4 * i - 1), startY + waveHeight, startX + rX * (4 * i), startY);
            }
            path.lineTo(startX + waveWidth * waveCount, startY + rectHeight + waveHeight);
            path.lineTo(startX, startY + rectHeight + waveHeight);
            path.lineTo(startX, startY);
        }

        return path;
    }

}
