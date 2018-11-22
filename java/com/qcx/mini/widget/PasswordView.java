package com.qcx.mini.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2018/8/1.
 * 密码显示
 */

public class PasswordView  extends View{
    private CharSequence text="";
    private int length=6;
    private int textSize= UiUtils.getSize(20);
    private int textColor= 0xFF484848;
    private int borderColor=0xFFEDEDF0;
    private int borderSize=UiUtils.getSize(1);
    private int radiu=UiUtils.getSize(2);
    private int paddingSize=UiUtils.getSize(1);//边框背景的padding,不设置边框有部分显示不出来

    private Paint mTextPaint;
    private Paint mborderPaint;

    private int width;
    private int height;
    private float itemWidth;


    public PasswordView(Context context) {
        super(context);
        init();
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);

        mborderPaint=new Paint();
        mborderPaint.setAntiAlias(true);
        mborderPaint.setStrokeWidth(borderSize);
        mborderPaint.setColor(borderColor);
        mborderPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=MeasureSpec.getSize(widthMeasureSpec);
        height=MeasureSpec.getSize(heightMeasureSpec);

        if(length>0){
            itemWidth=(width-paddingSize*2)/length;
        }else {
            itemWidth=0;
        }
    }

    private Path getBorderPath(){
        Path path=new Path();
        path.addRoundRect(new RectF(paddingSize,paddingSize,width-paddingSize,height-paddingSize),radiu,radiu, Path.Direction.CW);
        for(int i=1;i<length;i++){
            path.moveTo(i*itemWidth,paddingSize);
            path.lineTo(i*itemWidth,height-paddingSize);
        }
        return path;
    }

    private float getTextHeight(Paint paint){
        Paint.FontMetrics metrics=paint.getFontMetrics();
        return metrics.descent -metrics.ascent;
    }

    private float getMetricsY(Paint paint){
        Paint.FontMetrics metrics=paint.getFontMetrics();
        return (height-metrics.descent-metrics.ascent)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(itemWidth<=0){
            return;
        }
        canvas.drawPath(getBorderPath(),mborderPaint);
        if(!TextUtils.isEmpty(text)){
            for(int i=0;i<length&&i<text.length();i++){
                String itemText="*";
                float baseLine=getMetricsY(mTextPaint);
                if("*".equals(itemText)){
                    baseLine=baseLine+getTextHeight(mTextPaint)/7;
                }
                canvas.drawText(itemText,itemWidth*i+(itemWidth-mTextPaint.measureText(itemText))/2,baseLine,mTextPaint);
            }
        }
    }

    public void setText(CharSequence text) {
        this.text = text;
        invalidate();
    }

    public int getLength() {
        return length;
    }
}
