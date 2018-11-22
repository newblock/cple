package com.qcx.mini.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.qcx.mini.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/7/11.
 */

public class RadarView_2 extends View {
    private Paint mPaint;
    private Paint pointPaint;
    private int circleCount=5;
    private int centerCircleR;
    private int centerCircleColor;
    private int centerX;
    private int centerY;
    private List<Point> points;

    public RadarView_2(Context context) {
        super(context);
        init();
    }

    public RadarView_2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView_2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint=new Paint();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        pointPaint=new Paint();
        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(0x33ff3333);

        points=new ArrayList<>();
        for(int i=0;i<50;i++){
            points.add(new Point());
        }
    }
    ValueAnimator animator;
    SweepGradient shader;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        centerCircleR=Math.min(width,height)/circleCount;
        centerX=width/2;
        centerY=height/2;
        if(shader==null){
            shader = new SweepGradient(width /2.0f, height/2.0f, 0x2200FF00, 0x8800FF00);
        }
        mPaint.setShader(shader);
        if(animator==null){
            animator=ValueAnimator.ofFloat(0,360);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    degrees=(float)animation.getAnimatedValue();
                        if(Math.random()>0.97){
                            for(Point point:points){
                                if(point.r==0){
                                    point.init(centerX-centerCircleR,centerX+centerCircleR,centerY-centerCircleR,centerY+centerCircleR,10);
                                    break;
                                }
                            }
                    }
                    invalidate();
                }
            });
            animator.setDuration(2000);
            animator.setRepeatCount(Integer.MAX_VALUE-1);
            animator.setInterpolator(new LinearInterpolator());
        }
        animator.start();
    }

    float degrees=0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(degrees,centerX,centerY);
        canvas.drawCircle(centerX,centerY,centerCircleR,mPaint);
        canvas.restore();
        for(Point point:points){
            if(point.r<10){
                canvas.drawCircle(point.x,point.y,point.r,pointPaint);
            }else {
                point.setR(0);
            }
        }
    }

    public class  Point implements ValueAnimator.AnimatorUpdateListener{
        private int x;
        private int y;
        private float r;

        public void init(int minX,int maxX,int minY,int maxY,int maxR){
            Random random=new Random();
            x=random.nextInt(maxX-minX)+minX;
            y=random.nextInt(maxY-minY)+minY;
            ValueAnimator pointAnim=ValueAnimator.ofFloat(0,maxR);
            pointAnim.addUpdateListener(this);
            pointAnim.setDuration(2000);
            pointAnim.start();
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public float getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            r=(float)animation.getAnimatedValue();
        }
    }
}
