package com.qcx.mini.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.qcx.mini.utils.UiUtils;

/**
 * Created by Administrator on 2018/8/31.
 *
 */

public class CloseAbleTextView extends View {
    private boolean isClosed=true;
    private int height;
    private int width;
    private String closeText="关闭";
    private String unfoldText="展开";
    private String contentText;

    private Paint paint;
    private int textSize;
    private int contentTextColor;
    private int otherTextColor;

    public CloseAbleTextView(Context context) {
        super(context);
        init(context);
    }

    public CloseAbleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CloseAbleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    TextView textView;
    private void init(Context context){
        textSize=11;
        contentTextColor=0xFF939499;
        otherTextColor=0xFF4A90E2;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        textView=new TextView(context);
        contentText="趣出行坚持“让价值贡献者获得价值”的理念,用户在趣出行所贡献的一切价值都将转化为原力,持有原力的用户可以参与每日的趣钻（CPLE）产出,获得属于自己的趣钻(CPLE)。用户完成不同任务将获得不同数量的原力,同一时段内,原力越高,获得的趣钻(CPLE)越多。";

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=MeasureSpec.getSize(widthMeasureSpec);
        if(isClosed){
            height=getLineHeight();
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isClosed){
            canvas.drawText(contentText,0,0,paint);
        }else {
        }
    }

    private int getLineHeight(){
        Paint.FontMetrics metrics=paint.getFontMetrics();
        return (int)(metrics.descent -metrics.ascent);
    }

    private int getSingleLineLength(String text){
        if (TextUtils.isEmpty(text)){
            return 0;
        }
        int w=(int)paint.measureText(text,0,1);
        int ow=width-((int)paint.measureText(unfoldText)+ UiUtils.getSize(10));//加上padding

        int size=ow/w;
        int width=(int)paint.measureText(text,0,size);
        if(size<text.length()&&width<ow){
            while (width<ow&&size<text.length()){
                size++;
                width=(int)paint.measureText(text,0,size);
            }
        }else {
            while (width>ow&&size>0){
                size--;
                width=(int)paint.measureText(text,0,size);
            }
        }
        return size;
    }
}
