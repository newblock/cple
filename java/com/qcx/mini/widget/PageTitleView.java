package com.qcx.mini.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Administrator on 2018/5/24.
 */

public class PageTitleView extends View {
    private float[] radiusArray;
    private List<String> mTitles;
    private Paint mCurrentTitlePaint;
    private Paint mNormalTextPaint;

    private Paint mBorderPaint;
    private int mBorderColor;
    private int mBorderSize;

    private Paint mCurrentBackPaint;
    private int mCurrentBackColor;

    private Paint mNormalBackPaint;
    private int mNormalBackColor;
    private int mCurrentTextColor;
    private int mNormalTextColor;
    private int textSize;

    private float itemWidth;
    private float itemHeight;
    private int radius=0;

    private boolean haveUnderline=false;

    private int currentPosition = 0;
    float positionOffset;


    public PageTitleView(Context context) {
        super(context);
        init(context);
    }

    public PageTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageTitleView);
        if (typedArray != null) {
            mBorderColor = typedArray.getColor(R.styleable.PageTitleView_mBorderColor, 0xFF765432);
            mCurrentBackColor = typedArray.getColor(R.styleable.PageTitleView_mCurrentBackColor, 0xFF765432);
            mNormalBackColor = typedArray.getColor(R.styleable.PageTitleView_mNormalBackColor, Color.GREEN);
            mCurrentTextColor = typedArray.getColor(R.styleable.PageTitleView_mCurrentTextColor, Color.WHITE);
            mNormalTextColor = typedArray.getColor(R.styleable.PageTitleView_mNormalTextColor, Color.RED);
            textSize = typedArray.getDimensionPixelSize(R.styleable.PageTitleView_mTextSize, UiUtils.getSize(15));
            backPadding = typedArray.getDimensionPixelSize(R.styleable.PageTitleView_backPadding, 0);
            radius = typedArray.getDimensionPixelSize(R.styleable.PageTitleView_mRadius, 0);
            typedArray.recycle();
        }
        init(context);
    }

    public PageTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int backPadding = 4;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw1(canvas);
    }

    private void draw1(Canvas canvas) {
        if (mTitles != null && mTitles.size() > 0) {
            itemWidth = getWidth() / mTitles.size();
            itemHeight = getHeight();
            float textStartX = 0;
            float textStartY = (itemHeight + textSize) / 2;
            canvas.drawPath(getRadiusRectPath(backPadding, backPadding, getWidth() - backPadding, getHeight() - backPadding), mNormalBackPaint);
            for (int i = 0; i < mTitles.size(); i++) {
                float textWidth = mNormalTextPaint.measureText(mTitles.get(i));
                textStartX = itemWidth * i + (itemWidth - textWidth) / 2;
                canvas.drawText(mTitles.get(i), textStartX, textStartY, mNormalTextPaint);
            }
            canvas.drawPath(getRadiusRectPath(backPadding, backPadding, getWidth() - backPadding, getHeight() - backPadding), mBorderPaint);
            canvas.clipPath(getRadiusRectPath(currentPosition * itemWidth + backPadding + itemWidth * positionOffset, backPadding, (currentPosition + 1) * itemWidth - backPadding + itemWidth * positionOffset, itemHeight - backPadding));
            canvas.drawColor(mCurrentBackColor);
            for (int i = 0; i < mTitles.size(); i++) {
                float textWidth = mCurrentTitlePaint.measureText(mTitles.get(i));
                textStartX = itemWidth * i + (itemWidth - textWidth) / 2;
                canvas.drawText(mTitles.get(i), textStartX, textStartY, mCurrentTitlePaint);
            }
        }
    }

    private Path getRadiusRectPath(float left, float top, float right, float bottom) {
        Path path = new Path();
        path.addRoundRect(new RectF(left, top, right, bottom), radiusArray, Path.Direction.CW);
        return path;
    }

    int downPosition;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                downPosition = (int) (event.getX() / itemWidth);
                break;
            case ACTION_MOVE:
                break;
            case ACTION_UP:
                int po = (int) (event.getX() / itemWidth);
                if (downPosition == po) {
                    if (currentPosition != po) {
                        if(clickListener!=null){
                            clickListener.onClick(po);
                        }else {
                            currentPosition = po;
                            invalidate();
                        }
                    }
                }
                break;
        }
        return true;
    }

    public void setRadius(int leftTop, int rightTop, int rightBottom, int leftBottom) {
        radiusArray[0] = leftTop;
        radiusArray[1] = leftTop;
        radiusArray[2] = rightTop;
        radiusArray[3] = rightTop;
        radiusArray[4] = rightBottom;
        radiusArray[5] = rightBottom;
        radiusArray[6] = leftBottom;
        radiusArray[7] = leftBottom;
        invalidate();
    }

    private void init(Context context) {
        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderSize);
        mBorderPaint.setAntiAlias(true);

        mCurrentTitlePaint = new Paint();
        mCurrentTitlePaint.setAntiAlias(true);
        mCurrentTitlePaint.setColor(mCurrentTextColor);
        mCurrentTitlePaint.setTypeface(Typeface.MONOSPACE);
        mCurrentTitlePaint.setTextSize(textSize);

        mNormalTextPaint = new Paint();
        mNormalTextPaint.setAntiAlias(true);
        mNormalTextPaint.setColor(mNormalTextColor);
        mNormalTextPaint.setTypeface(Typeface.MONOSPACE);
        mNormalTextPaint.setTextSize(textSize);

        mCurrentBackPaint = new Paint();
        mCurrentBackPaint.setColor(mCurrentBackColor);

        mNormalBackPaint = new Paint();
        mNormalBackPaint.setColor(mNormalBackColor);
        radiusArray=new float[] {radius, radius, radius, radius, radius, radius, radius, radius};
//        mTitles = new String[]{"TITLE", "TITLE", "TITLE", "TITLE", "TITLE", "TITLE"};

    }

    public void setTitles(List<String> mTitles) {
        this.mTitles = mTitles;
        invalidate();
    }

    public void setPositionOffset(float positionOffset) {
        if (positionOffset > 1) {
            this.positionOffset = 1;
        } else if (positionOffset < -1) {
            this.positionOffset = -1;
        } else {
            this.positionOffset = positionOffset;
        }
        invalidate();
    }

    public void setCurrentPosition(int currentPosition) {
        if (mTitles == null || currentPosition <= 0) {
            this.currentPosition = 0;
        } else if (currentPosition > mTitles.size()) {
            this.currentPosition = mTitles.size();
        } else {
            this.currentPosition = currentPosition;
        }
        positionOffset = 0;
        invalidate();
    }

    public void set(int currentPosition, float positionOffset) {
        this.currentPosition = currentPosition;
        this.positionOffset = positionOffset;
        invalidate();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setChangedListener(OnTitleClickListener changedListener) {
        this.clickListener = changedListener;
    }

    private OnTitleClickListener clickListener;

    public interface OnTitleClickListener {
        void onClick(int position);
    }
}
