package com.qcx.mini.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

/**
 * 圆角imageView
 * Created by Administrator on 2017/12/13.
 */

public class RoundImageView extends AppCompatImageView{
    private float width,height;
    private int rou= UiUtils.getSize(20);
    private Paint mPaint;
    private Paint rPaint;

    public RoundImageView(Context context) {
        this(context, null);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        init(context);
    }

    private void init(Context context){
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(context.getResources().getColor(R.color.masking));

        rPaint=new Paint();
        rPaint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        rPaint.setColor(Color.WHITE);
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);

        rPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawPath(getPath(width,height), rPaint);
        canvas.restoreToCount(saveCount);
        rPaint.setXfermode(null);
        canvas.drawPath(getPath(width,height),mPaint);
    }

    private Path getPath(float width,float height){
        rou=(int)Math.min(width,height)/8;
        Path path = new Path();
        path.moveTo(rou, 0);
        path.lineTo(width - rou, 0);
        path.quadTo(width, 0, width, rou);
        path.lineTo(width, height - rou);
        path.quadTo(width, height, width - rou, height);
        path.lineTo(rou, height);
        path.quadTo(0, height, 0, height - rou);
        path.lineTo(0, rou);
        path.quadTo(0, 0, rou, 0);
        return path;
    }

    private Path getPath(){
        int r=270;
        rou=(int)Math.min(width,height)/10;
        RectF rectF1=new RectF(0,0,rou,rou);
        RectF rectF2=new RectF(width-rou,0,width,rou);
        RectF rectF3=new RectF(0,height-rou,rou,height);
        RectF rectF4=new RectF(width-rou,height-rou,width,height);

        Path path=new Path();
        path.moveTo(rou,0);
        path.lineTo(width-rou,0);
        path.arcTo(rectF2,r,90);
        path.lineTo(width,height-rou);
        path.arcTo(rectF4,r+90,90);
        path.lineTo(rou,height);
        path.arcTo(rectF3,r+180,90);
        path.lineTo(0,rou);
        path.arcTo(rectF1,r+270,90);
        return path;
    }


}
