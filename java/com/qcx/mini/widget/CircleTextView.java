package com.qcx.mini.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qcx.mini.R;

/**
 * 圆形TextView
 * Created by Administrator on 2018/1/18.
 */

public class CircleTextView extends AppCompatTextView {
    private Paint mPaint;
    private int circleColor;
    float r;
    float x,y;

    public CircleTextView(Context context) {
        super(context);
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(attrs!=null){
            TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
            circleColor = a.getColor(R.styleable.CircleTextView_CircleTextView_circle_color, Color.TRANSPARENT);
        }
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(circleColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(r==0){
            int w=MeasureSpec.getSize(widthMeasureSpec);
            int h=MeasureSpec.getSize(heightMeasureSpec);
            r=Math.min(w,h)/2;
            x=w/2;
            y=h/2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(x,y,r,mPaint);
        super.onDraw(canvas);
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        mPaint.setColor(circleColor);
        invalidate();
    }
}
