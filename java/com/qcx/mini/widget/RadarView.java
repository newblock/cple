package com.qcx.mini.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.qcx.mini.utils.LogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 雷达效果
 * Created by Administrator on 2018/1/16.
 */

public class RadarView extends View {
    private Paint mPaint0;
    private Paint mPaint1;//中心圆
    private Paint mPaint2;
    private Paint mPaint3;
    private Paint mPaint3_5;
    private Paint mPaint4;
    private float cr0,cr1,cr2,cr3,cr3_5,cr4;
    private float  r1,r2,r3,r3_5,r4;//圆半径界限
    private int rX,rY;

    private Timer mTimer;
    private TimerTask mTimerTask;

    int with;
    int height;

    private int centerColor=0x66499EFF;

    public void setCenterColor(int centerColor) {
        this.centerColor = centerColor;
        col=centerColor;
    }

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint0=new Paint();
        mPaint1=new Paint();
        mPaint2=new Paint();
        mPaint3=new Paint();
        mPaint3_5=new Paint();
        mPaint4=new Paint();

        mPaint0.setColor(centerColor);
        mPaint0.setAntiAlias(true);

        mPaint1.setColor(centerColor);
        mPaint1.setAntiAlias(true);
        mPaint2.setColor(centerColor-0x10000000);
        mPaint2.setAntiAlias(true);
        mPaint3.setColor(centerColor-0x20000000);
        mPaint3.setAntiAlias(true);
        mPaint3_5.setColor(centerColor-0x30000000);
        mPaint3_5.setAntiAlias(true);
        mPaint4.setColor(centerColor-0x40000000);
        mPaint4.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(height==0||with==0){
            with=MeasureSpec.getSize(widthMeasureSpec);
            height=MeasureSpec.getSize(heightMeasureSpec);
            r1=with/4;
            r2=r1+r1*3/4;
            r3=r1+r1+r1*2/3;
            r4=Math.max(with,height);
            r3_5=r3+(r4-r3)/2;

            cr1=r1;
            cr2=r2;
            cr3=r3;
            cr3_5=r3_5;
            cr4=r4;

            rX=with/2;
            rY=with*3/5;


        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(rX,rY,cr0,mPaint1);
        canvas.drawCircle(rX,rY,cr1,mPaint1);
        canvas.drawCircle(rX,rY,cr2,mPaint2);
        canvas.drawCircle(rX,rY,cr3,mPaint3);
        canvas.drawCircle(rX,rY,cr3_5,mPaint3);
        canvas.drawCircle(rX,rY,cr4,mPaint4);
    }

    // 每秒50次，2秒 100次;
    int time=96;
    int col=centerColor;
    int jc=0;//计数
    private void start(){
        if(mTimerTask==null){
            mTimer=new Timer();
            mTimerTask=new TimerTask() {
                @Override
                public void run() {
                    if(jc<time){
                        jc++;
                        if(jc%6==0){
                            col-=0x01000000;
                        }

                        cr0+=(r1)/time;
                        cr1+=(r2-r1)/time;
                        cr2+=(r3-r2)/time;
                        cr3+=(r3_5-r3)/time;
                        cr3_5+=(r4-r3_5)/time;

                        mPaint1.setColor(col);
                        mPaint2.setColor(col-0x10000000);
                        mPaint3.setColor(col-0x20000000);
                        mPaint3_5.setColor(col-0x30000000);
                        mPaint4.setColor(col-0x40000000);
                    }else {
                        jc=0;
                        cr0=0;
                        cr1=r1;
                        cr2=r2;
                        cr3=r3;
                        cr3_5=r3_5;

                        mPaint1.setColor(centerColor);
                        mPaint2.setColor(centerColor-0x10000000);
                        mPaint3.setColor(centerColor-0x20000000);
                        mPaint3_5.setColor(centerColor-0x30000000);
                        mPaint4.setColor(centerColor-0x40000000);
                        col=centerColor;
                    }

                    postInvalidate();
                }
            };
        }
        mTimer.schedule(mTimerTask,0,10);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }
}
