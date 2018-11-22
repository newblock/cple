package com.qcx.mini.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.UiUtils;

import java.util.List;


/**
 * Created by Administrator on 2018/7/23.
 *
 */
@ViewPager.DecorView
public class PagerTitle extends View {
    private ViewPager mPager;
    private PageListener mPageListener;

    private Paint mTextPaint;
    private Paint mCurrentTextPaint;
    private int mNormalTextSize = UiUtils.getSize(20);
    private int mNormalTextColor = Color.BLACK;
    private int mCurrentTextColor = Color.WHITE;
    private int mCurrentTextSize = UiUtils.getSize(20);

    private Paint mLinePaint;
    private int mLineSize = UiUtils.getSize(2);
    private int mLineColor = mCurrentTextColor;
    private int mLineWidth = -1;//设置此值后 不自动适配line的长度
    private Drawable mLineDrawable;

    private boolean isFull = false;//true item充满屏幕且平分宽度,默认fase TODO :文字显示待优化,当文字长度大于item宽度时会重叠
    private boolean itemEven = false;// true 每个item 宽度相同，取最长的文字item的宽度
    private int itemEvenWidth;//itemEven==true 时，每个item的宽度
    private int mWidth;// 控件宽度

    private List<String> titles;
    private int paddingLR = UiUtils.getSize(10);//title左右间距
    private int moveX;//移动的距离

    private int mCurrentPosition = 0;//当前显示的title位置
    private int mOffSetPosition = 0;//当前显示的title位置
    private float positionOffSet = 0;//偏移量

    private ValueAnimator moveAnim;

    private int lastX;
    private float downX;
    private int minMoveX = UiUtils.getSize(5);//最小有效滑动距离
    private boolean isMove = false;//当次事件是否是滑动事件

    private VelocityTracker mVelocityTracker;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPager != null) {
            mPager.addOnPageChangeListener(mPageListener);
            mPager.addOnAdapterChangeListener(mPageListener);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPager != null) {
            mPager.removeOnAdapterChangeListener(mPageListener);
            mPager.removeOnPageChangeListener(mPageListener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    public PagerTitle(Context context) {
        super(context);
        init();
    }

    public PagerTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PagerTitle);
        if (array != null) {
            mNormalTextColor = array.getColor(R.styleable.PagerTitle_mPTNormalTextColor, Color.GRAY);
            mCurrentTextColor = array.getColor(R.styleable.PagerTitle_mPTCurrentTextColor, Color.BLACK);
            mLineColor = array.getColor(R.styleable.PagerTitle_mPTLineColor, mCurrentTextColor);
            isFull = array.getBoolean(R.styleable.PagerTitle_mPTIsFull, false);

            mLineSize = UiUtils.getSize(array.getDimensionPixelSize(R.styleable.PagerTitle_mPTLineSize, 2));
            mNormalTextSize = UiUtils.getSize(array.getDimensionPixelSize(R.styleable.PagerTitle_mPTTextSize, 15));
            mCurrentTextSize =UiUtils.getSize(array.getDimensionPixelSize(R.styleable.PagerTitle_mPTCurrentTextSize, 15));
            paddingLR = UiUtils.getSize(array.getDimensionPixelSize(R.styleable.PagerTitle_mPTPaddingLR, 15));
            mLineWidth = array.getDimensionPixelSize(R.styleable.PagerTitle_mPTLineWidth, -1);
            if(mLineWidth!=-1){
                mLineWidth=UiUtils.getSize(mLineWidth);
            }

            mLineDrawable = array.getDrawable(R.styleable.PagerTitle_mPTLineDrawable);
            array.recycle();
        }
        init();
    }

    public PagerTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setTextSize(mNormalTextSize);
        mTextPaint.setColor(mNormalTextColor);

        mCurrentTextPaint = new Paint();
        mCurrentTextPaint.setAntiAlias(true);
        mCurrentTextPaint.setTypeface(Typeface.MONOSPACE);
        mCurrentTextPaint.setTextSize(mCurrentTextSize);
        mCurrentTextPaint.setColor(mCurrentTextColor);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mLineSize);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);

        mPageListener = new PageListener();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        SaveStatus saveStatus = new SaveStatus(parcelable);
        saveStatus.setcPosition(mCurrentPosition);
        saveStatus.setOffset(positionOffSet);
        saveStatus.setoPosition(mOffSetPosition);
        saveStatus.setMoveX(moveX);
        return saveStatus;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SaveStatus) {
            SaveStatus saveStatus = (SaveStatus) state;
            super.onRestoreInstanceState(saveStatus.getSuperState());
            mCurrentPosition = saveStatus.getcPosition();
            mOffSetPosition = saveStatus.getoPosition();
            positionOffSet = saveStatus.getOffset();
            moveX = saveStatus.getMoveX();
        }

    }


    /**
     * 获取position 的宽度
     *
     * @param position 位置
     * @return 宽
     */
    private int getItemWidth(int position) {
        if (isFull) {
            return mWidth / titles.size();
        }
        if (itemEven) {
            return itemEvenWidth;
        }
        if (titles != null && position >= 0 && position < titles.size()) {
            return (int) mTextPaint.measureText(titles.get(position)) + paddingLR * 2;
        }
        return 0;
    }

    /**
     * 获取position 的开始位置
     *
     * @param position 位置
     * @return 开始的横坐标
     */
    private int getStartX(int position) {
        int x = 0;
        for (int i = 0; i < position; i++) {
            x += getItemWidth(i);
        }
        return x;
    }

    /**
     * 初始化均分item宽度时每个item的宽度
     */
    private void initItemWidth() {
        if (titles == null || titles.size() == 0) {
            itemEvenWidth = 0;
            return;
        }
        for (int i = 0; i < titles.size(); i++) {
            if (!TextUtils.isEmpty(titles.get(i))) {
                itemEvenWidth = Math.max(itemEvenWidth, (int) mTextPaint.measureText(titles.get(i)) + paddingLR * 2);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(-moveX, 0);
        float lineWidth = 0;
        float nextLineWidth = 0;
        if (titles != null && titles.size() > 0) {
            float startX;
            float startY = (getHeight() + mNormalTextSize) / 2;
            for (int i = 0; i < titles.size(); i++) {
                float textWidth = mCurrentPosition == i ? mCurrentTextPaint.measureText(titles.get(i)) : mTextPaint.measureText(titles.get(i));
                startX = getStartX(i) + (getItemWidth(i) - textWidth) / 2;
                if (mOffSetPosition == i) {//获取line的长度和目标line的长度
                    lineWidth = textWidth;//在这里设置lineWidth那么nextLineWidth一定是i+1的文字的长度
                    if (i < titles.size() - 1) {
                        if (!TextUtils.isEmpty(titles.get(i + 1))) {
                            nextLineWidth = mCurrentTextPaint.measureText(titles.get(i + 1));
                        }
                    }
                    if (nextLineWidth == 0) {
                        nextLineWidth = lineWidth;
                    }
                }
                if (mCurrentPosition == i) {
                    canvas.drawText(titles.get(i), startX, startY, mCurrentTextPaint);
                } else {
                    canvas.drawText(titles.get(i), startX, startY, mTextPaint);
                }
            }
            if (mLineWidth == -1) {//自动适配Line 的长度
                lineWidth = lineWidth + (nextLineWidth - lineWidth) * positionOffSet;
            } else {
                lineWidth = mLineWidth;
            }
            float lineStartX = getStartX(mOffSetPosition) + (getItemWidth(mOffSetPosition) - lineWidth) / 2;
            lineStartX = lineStartX + (getItemWidth(mOffSetPosition) + getItemWidth(mOffSetPosition + 1)) / 2 * positionOffSet;
            float lineY = getHeight() - mLineSize;
            float lineStopX = lineStartX + lineWidth;

            if (mLineDrawable != null) {
                mLineDrawable.setBounds((int) lineStartX, (int) lineY, (int) lineStopX, (int) lineY + mLineSize);
//                mLineDrawable.setBounds(0,0,getWidth(),getHeight());
                mLineDrawable.draw(canvas);
            } else {
                canvas.drawLine(lineStartX, lineY, lineStopX, lineY, mLinePaint);
            }
        }
    }


    /**
     * 移动画布
     *
     * @param x 移动的距离
     */
    private void move(int x) {
        int maxMoveX = getStartX(titles.size() - 1) + getItemWidth(titles.size() - 1) - getWidth();
        maxMoveX = maxMoveX > 0 ? maxMoveX : 0;
        moveX = moveX + x;
        if (moveX < 0) {
            moveX = 0;
        } else if (moveX > maxMoveX) {
            moveX = maxMoveX;
        }
    }

    private void moveByAnim(int x) {
        if (moveAnim != null) {
            moveAnim.cancel();
        }
        moveAnim = ValueAnimator.ofInt(0, x);
        moveAnim.setDuration(300);
        moveAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int lastX = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                move((int) animation.getAnimatedValue() - lastX);
                invalidate();
                lastX = (int) animation.getAnimatedValue();
            }
        });
        moveAnim.start();
    }

//    /**
//     * 将下一个title移动到屏幕里
//     *
//     * @param isRight 滑动方向是否向右
//     */
//    private void moveToNextTitle(boolean isRight) {
//        if (isRight) {
//            int cPosition = (moveX + getWidth()) / getItemWidth() + 1;
//            moveByAnim((getStartX(cPosition) - getWidth() - moveX));
//        } else {
//            int cPosition = (moveX) / getItemWidth() - 1;
//            moveByAnim((cPosition * getItemWidth() - moveX));
//        }
//    }

    /**
     * 将当前的选中的title移动到中间位置
     */
    private void moveTitleToCenter() {
        int cPositionX = getStartX(mCurrentPosition) + getItemWidth(mCurrentPosition) / 2;
        int cMoveX = cPositionX - getWidth() / 2;
        moveByAnim(cMoveX - moveX);
    }

    private void changeCurrentPostion(int position) {
        if (position != mCurrentPosition) {
            mCurrentPosition = position;
        }
        moveTitleToCenter();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTracker();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                mVelocityTracker.clear();
                downX = event.getRawX();
                mVelocityTracker.addMovement(event);
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                if (Math.abs(downX - event.getRawX()) > minMoveX) {
                    isMove = true;
                    int m = (int) event.getRawX() - lastX;
                    move(-m);
                    lastX = (int) event.getRawX();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float x = mVelocityTracker.getXVelocity();
                    post(new MoveRunnable(-x));
                } else {
                    int eX = (int) event.getX() + moveX;
                    int position = 0;
                    int x = 0;
                    for (int i = 0; i < titles.size(); i++) {
                        x += getItemWidth(i);
                        if (eX < x) {
                            position = i;
                            break;
                        }
                    }
                    if (mPager != null) {
                        mPager.setCurrentItem(position);
                    }
                }
                performClick();
                break;
        }
        invalidate();
        return true;
    }

    private void initVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
        initItemWidth();
    }

    public void setPager(ViewPager pager) {
        this.mPager = pager;
    }

    private class MoveRunnable implements Runnable {
        private float velocity;

        MoveRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(velocity) < 20) {
                return;
            }
            move((int) velocity / 40);
            velocity /= 1.088888;
            postInvalidate();
            postDelayed(this, 10);
        }
    }

    private class PageListener extends DataSetObserver implements ViewPager.OnPageChangeListener,
            ViewPager.OnAdapterChangeListener {

        PageListener() {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mOffSetPosition = position;
            PagerTitle.this.positionOffSet = positionOffset;
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {
            changeCurrentPostion(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onAdapterChanged(@NonNull ViewPager viewPager, PagerAdapter oldAdapter,
                                     PagerAdapter newAdapter) {
        }

        @Override
        public void onChanged() {
        }
    }


    private static class SaveStatus extends BaseSavedState {
        private int cPosition;
        private int oPosition;
        private float offset;
        private int moveX;

        private int getcPosition() {
            return cPosition;
        }

        private void setcPosition(int cPosition) {
            this.cPosition = cPosition;
        }

        private int getoPosition() {
            return oPosition;
        }

        private void setoPosition(int oPosition) {
            this.oPosition = oPosition;
        }

        private float getOffset() {
            return offset;
        }

        private void setOffset(float offset) {
            this.offset = offset;
        }

        private int getMoveX() {
            return moveX;
        }

        private void setMoveX(int moveX) {
            this.moveX = moveX;
        }

        private SaveStatus(Parcel source) {
            super(source);
            cPosition = source.readInt();
            oPosition = source.readInt();
            offset = source.readFloat();
            moveX = source.readInt();
        }

        private SaveStatus(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(cPosition);
            out.writeInt(oPosition);
            out.writeFloat(offset);
            out.writeInt(moveX);
        }

        public static final Creator<SaveStatus> CREATOR = new Creator<SaveStatus>() {

            @Override
            public SaveStatus createFromParcel(Parcel source) {
                return new SaveStatus(source);
            }

            @Override
            public SaveStatus[] newArray(int size) {
                return new SaveStatus[size];
            }
        };
    }
}
