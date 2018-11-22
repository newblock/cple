package com.qcx.mini.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/26.
 * 折线走势图
 */

public class LineGraphView extends View {
    private List<Coordinate> mCoordinates;//坐标集合
    private Coordinate mMinCoordinateX;//coordinates里最小X
    private Coordinate mMinCoordinateY;//coordinates里最小Y
    private Coordinate mMaxCoordinateX;//coordinates里最大X
    private Coordinate mMaxCoordinateY;//coordinates里最大Y

    private int mWidth;//控件的宽
    private int mHeight;//控件的高

    private Paint mLinePaint;//折线paint
    private int mLineColor;//折线颜色
    private int mLineSize;//折线宽度

    private Paint mLineBackPaint;//折线paint
    private int mBottomColor;//折线下面部分的颜色

    private Paint mBackLinePaint;//背景线paint
    private int mBackLineColor;//背景线颜色
    private int mBackLineSize;//背景线宽度

    private Paint mBackTextPaint;//背景文字paint
    private int mBackTextColor;//背景文字颜色
    private int mBackTextSize;//背景文字大小

    private Paint mSelectedCirclePaint;//选中时的圆
    private int mSelectedCircleR;
    private int mSelectedCircleColor;


    private int mBackLineNum;//背景线数量
    private List<Coordinate> mYCoordinates;
    private List<Coordinate> mXCoordinates;

    private int mYNum;//Y轴标刻度数量
    private int mXNum;//X轴标刻度数量
//    private String[] mYTexts;//Y轴刻度文字
//    private String[] mXTexts;//X轴刻度文字

    public LineGraphView(Context context) {
        super(context);
        initParams();
        init();
    }

    public LineGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams();
        init();
    }

    public LineGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        if(mLineBackPaint==null){
            mLineBackPaint=new Paint();
            mLineBackPaint.setShader(new LinearGradient(0, 0, 0, mHeight, new int[]{0x44478DFF, 0x08478DFF}, null, Shader.TileMode.CLAMP));
            mLineBackPaint.setAntiAlias(true);
            mLineBackPaint.setStyle(Paint.Style.FILL);
        }

    }

    private void init(){
       mLinePaint=new Paint();
       mLinePaint.setAntiAlias(true);
       mLinePaint.setColor(mLineColor);
       mLinePaint.setStrokeWidth(mLineSize);
       mLinePaint.setStyle(Paint.Style.STROKE);



       mBackLinePaint=new Paint();
       mBackLinePaint.setAntiAlias(true);
       mBackLinePaint.setColor(mBackLineColor);
       mBackLinePaint.setStrokeWidth(mBackLineSize);

       mBackTextPaint=new Paint();
       mBackTextPaint.setAntiAlias(true);
       mBackTextPaint.setTextSize(mBackTextSize);
       mBackTextPaint.setColor(mBackTextColor);
       mBackTextPaint.setTypeface(Typeface.MONOSPACE);

       mSelectedCirclePaint=new Paint();
       mSelectedCirclePaint.setAntiAlias(true);
       mSelectedCirclePaint.setColor(mSelectedCircleColor);

        mYNum=mYCoordinates.size();
        mXNum=mXCoordinates.size();
    }
    private void initParams(){
        mLineColor=0xFF4A90E2;
        mBottomColor=0x884A90E2;
        mLineSize= UiUtils.getSize(2);

        mBackLineColor=0xFFF2F2F5;
        mBackLineSize=UiUtils.getSize(1);

        mBackTextColor= 0xFF939499;
        mBackTextSize=UiUtils.getSize(11);

        mSelectedCircleColor=Color.WHITE;
        mSelectedCircleR=UiUtils.getSize(2);


        //测试数据
        mCoordinates=new ArrayList<>();
        mCoordinates.add(new Coordinate(0,1.4f,"07-01","1.4元"));
        mCoordinates.add(new Coordinate(1,1.8f,"07-02","1.8元"));
        mCoordinates.add(new Coordinate(2,1.5f,"07-03","1.5元"));
        mCoordinates.add(new Coordinate(3,2.4f,"07-04","2.4元"));
        mCoordinates.add(new Coordinate(4,2.3f,"07-05","2.3元"));
        mCoordinates.add(new Coordinate(5,1.6f,"07-06","1.6元"));
        mCoordinates.add(new Coordinate(6,1.7f,"07-07","1.7元"));
        mCoordinates.add(new Coordinate(7,1.9f,"07-08","1.9元"));
        mCoordinates.add(new Coordinate(8,1.9f,"07-09","1.9元"));
        mCoordinates.add(new Coordinate(9,1.9f,"07-10","1.9元"));
        mCoordinates.add(new Coordinate(10,2.1f,"07-11","2.1元"));
        mCoordinates.add(new Coordinate(11,2.0f,"07-12","2.0元"));
        mCoordinates.add(new Coordinate(12,2.8f,"07-13","2.8元"));
        mCoordinates.add(new Coordinate(13,1.2f,"07-14","1.2元"));
        mCoordinates.add(new Coordinate(14,1.4f,"07-15","1.4元"));
        mCoordinates.add(new Coordinate(15,1.9f,"07-16","1.9元"));
        mCoordinates.add(new Coordinate(16,2.0f,"07-17","2.0元"));
        mCoordinates.add(new Coordinate(17,2.3f,"07-18","2.3元"));
        mCoordinates.add(new Coordinate(18,2.1f,"07-19","2.1元"));
        mCoordinates.add(new Coordinate(19,2.0f,"07-20","2.0元"));

        dataChanged();
        mYCoordinates=new ArrayList<>();
        mYCoordinates.add(new Coordinate(0,0,"","0"));
        for(int i=1;i<6;i++){
            float y=0.8f+i*0.4f;
            mYCoordinates.add(new Coordinate(0,y,"", CommonUtil.formatPrice(y,2)));
        }
        mXCoordinates=new ArrayList<>();
        mXCoordinates.add(new Coordinate(0,0,"07-01","0"));
        mXCoordinates.add(new Coordinate(6,0,"07-07","0"));
        mXCoordinates.add(new Coordinate(12,0,"07-13","0"));
        mXCoordinates.add(new Coordinate(18,0,"07-19","0"));
    }

    private void dataChanged(){
        if(mCoordinates!=null&&mCoordinates.size()>0){
            for(Coordinate coordinate:mCoordinates){
                if(mMinCoordinateX==null){
                    mMinCoordinateX=coordinate;
                }
                if(mMinCoordinateY==null){
                    mMinCoordinateY=coordinate;
                }
                if(mMaxCoordinateX==null){
                    mMaxCoordinateX=coordinate;
                }
                if(mMaxCoordinateY==null){
                    mMaxCoordinateY=coordinate;
                }

                if(mMinCoordinateX.getX()>coordinate.getX()){
                    mMinCoordinateX=coordinate;
                }
                if(mMinCoordinateY.getY()>coordinate.getY()){
                    mMinCoordinateY=coordinate;
                }
                if(mMaxCoordinateX.getX()<coordinate.getX()){
                    mMaxCoordinateX=coordinate;
                }
                if(mMaxCoordinateY.getY()<coordinate.getY()){
                    mMaxCoordinateY=coordinate;
                }
            }
            if(mYCoordinates==null){
                mYCoordinates=new ArrayList<>();
            }else {
                mYCoordinates.clear();
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mXCoordinates.size()<2||mYCoordinates.size()<2){
            return;
        }
        itemLineHeight=(mHeight-getFontHeight(mBackTextPaint)-UiUtils.getSize(20)-mPaddintTop)/(mYNum-1);
        itemWidth=(mWidth-mPaddintLeft-getFontWidth(mBackTextPaint,mXCoordinates.get(mXNum-1).getxName())/2)/(mXNum-1);
        drawBack(canvas);
        Path[] paths=getLinePath();
        canvas.drawPath(paths[0],mLinePaint);
        canvas.drawPath(paths[1],mLineBackPaint);
//        mLinePaint.setColor(Color.RED);
//        canvas.drawPath(getQPath(),mLinePaint);
        if(mSCoordinate!=null){
            float[] xy=getXY(mSCoordinate);
            canvas.drawLine(mPaddintLeft,xy[1],mWidth,xy[1],mLinePaint);
            canvas.drawLine(xy[0],0,xy[0],itemLineHeight*(mYNum-1)+mPaddintTop,mLinePaint);
            canvas.drawCircle(xy[0],xy[1],mSelectedCircleR+UiUtils.getSize(1),mLinePaint);
            canvas.drawCircle(xy[0],xy[1],mSelectedCircleR,mSelectedCirclePaint);
            float tWidth=getFontWidth(mBackTextPaint,mSCoordinate.getxName());
            float sX=xy[0];
            if(xy[0]+tWidth>mWidth){
                sX=sX-tWidth;
            }
            canvas.drawText(mSCoordinate.getxName(),sX,getFontHeight(mBackTextPaint),mBackTextPaint);
            canvas.drawText(mSCoordinate.getyName(),mWidth-getFontWidth(mBackTextPaint,mSCoordinate.getxName()),xy[1]+getFontHeight(mBackTextPaint),mBackTextPaint);
        }
    }

    private float itemLineHeight;
    private float itemWidth;
    private float mPaddintLeft=UiUtils.getSize(40);
    private float mPaddintTop=UiUtils.getSize(10);
    private void drawBack(Canvas canvas){
        for(int i=0;i<mYNum;i++){
            float itemY=itemLineHeight*i+mPaddintTop;
            canvas.drawLine(mPaddintLeft,itemY,mWidth,itemY,mBackLinePaint);
            String text=mYCoordinates.get(mYNum-i-1).getyName();
            float textStartX=mPaddintLeft-UiUtils.getSize(10)-getFontWidth(mBackTextPaint,text);
            canvas.drawText(text,textStartX,itemY+getFontHeight(mBackTextPaint)/3,mBackTextPaint);
        }

        for(int i=0;i<mXNum;i++){
            String text=mXCoordinates.get(i).getxName();
            canvas.drawText(text,i*itemWidth+mPaddintLeft-getFontWidth(mBackTextPaint,text)/2,mHeight-UiUtils.getSize(10),mBackTextPaint);
        }
    }

    private Path[] getLinePath(){
        Path path=new Path();
        Path path1=new Path();
        float[] lastXY=null;
        float lastDY=0;
        for(int i=0;i<mCoordinates.size();i++){
            float[] xy=getXY(mCoordinates.get(i));
            if(i==0){
                path.moveTo(xy[0],xy[1]);
                path1.moveTo(xy[0],xy[1]);
            }else {
                float dy=Math.abs(xy[1]-lastXY[1]);
                float x=(lastXY[0]+xy[0])/2;
                float y;
                if(dy-lastDY*2/3>0){
                    y=Math.min(lastXY[1],xy[1]);
                }else {
                    y=Math.max(lastXY[1],xy[1]);
                }
                path.quadTo(x,y,xy[0],xy[1]);
                path1.quadTo(x,y,xy[0],xy[1]);
//                path.lineTo(xy[0],xy[1]);
//                path1.lineTo(xy[0],xy[1]);
                lastDY=dy;
            }
            lastXY=xy;
        }
        path1.lineTo(getXY(mCoordinates.get(mCoordinates.size()-1))[0],itemLineHeight*(mYNum-1)+mPaddintTop);
        path1.lineTo(mPaddintLeft,itemLineHeight*(mYNum-1)+mPaddintTop);
        return new Path[]{path,path1};
    }
    private Path getQPath(){
        Path path=new Path();
        float[] lastXY=null;
        for(int i=0;i<mCoordinates.size();i++){
            float[] xy=getXY(mCoordinates.get(i));
            if(i==0){
                path.moveTo(xy[0],xy[1]);
            }else {
                float x=(lastXY[0]+xy[0])/2;
//                float y=(xy[1]+lastXY[1])/2+(xy[1]-lastXY[1])/4;
                float y=Math.min(lastXY[1],xy[1]);
                path.quadTo(x,y,xy[0],xy[1]);
            }
            lastXY=xy;
        }
        return path;
    }

    private float[] getXY(Coordinate coordinate){
        float[] xy=new float[2];
        float x0=mPaddintLeft;
        float y0=itemLineHeight*(mYNum-2)+mPaddintTop; //x0,y0 mYCoordinates第二个数据的实际坐标

        float s=mYCoordinates.get(mYCoordinates.size()-1).getY()-mYCoordinates.get(mYCoordinates.size()-2).getY();//y轴单位距离表示的值
        float b=mXCoordinates.get(mXCoordinates.size()-1).getX()-mXCoordinates.get(mXCoordinates.size()-2).getX();//X轴单位就是表示的值

        float dy=itemLineHeight;//Y轴单位长度
        float dx=itemWidth;//X轴单位长度
        xy[0]=(coordinate.getX()-mXCoordinates.get(0).getX())/b*dx+x0;
        xy[1]=y0-(coordinate.getY()-mYCoordinates.get(1).getY())/s*dy;
        return xy;
    }

    /**
     * @return 返回文字宽度
     */
    public float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    /**
     * @return 返回文字高度
     */
    private float getFontHeight(Paint paint){
        Paint.FontMetrics metrics=paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return metrics.descent -metrics.ascent;
    }


    private Coordinate mSCoordinate;
    private float minMoveX=UiUtils.getSize(5);//最小有效滑动距离，超过此距离将拦截event
    private float downX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mSCoordinate=null;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=event.getX();
            case MotionEvent.ACTION_MOVE:
                float x=event.getX();
                if(Math.abs(downX-event.getX())>minMoveX){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(x>mPaddintLeft){
                    float dx=0;
                    for(int i=0;i<mCoordinates.size();i++){
                        float[] xy=getXY(mCoordinates.get(i));
                        if(xy[0]==x){
                            mSCoordinate=mCoordinates.get(i);
                        }else if(xy[0]<x){
                            dx=x-xy[0];
                        }else {
                            if(dx<xy[0]-x){
                                mSCoordinate=mCoordinates.get(i-1);
                            }else {
                                mSCoordinate=mCoordinates.get(i);
                            }
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mSCoordinate=null;
                break;
        }
        invalidate();
        performClick();
        return true;
    }

    public void setmCoordinates(List<Coordinate> mCoordinates) {
        this.mCoordinates = mCoordinates;
        invalidate();
    }

    public void setmYCoordinates(@NonNull List<Coordinate> mYCoordinates) {
        this.mYCoordinates = mYCoordinates;
        this.mYNum=mYCoordinates.size();
        invalidate();
    }

    public void setmXCoordinates(@NonNull List<Coordinate> mXCoordinates) {
        this.mXCoordinates = mXCoordinates;
        this.mXNum=mXCoordinates.size();
        invalidate();
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public static class Coordinate{
        private float x;
        private float y;
        private String xName;
        private String yName;

        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate(float x, float y, String xName, String yName) {
            this.x = x;
            this.y = y;
            this.xName = xName;
            this.yName = yName;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public String getxName() {
            return xName;
        }

        public void setxName(String xName) {
            this.xName = xName;
        }

        public String getyName() {
            return yName;
        }

        public void setyName(String yName) {
            this.yName = yName;
        }
    }
}
