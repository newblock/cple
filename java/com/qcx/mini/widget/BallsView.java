package com.qcx.mini.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/8/22.
 *
 */

public class BallsView extends View {
    private Drawable mBall;
    private List<Ball> balls;
    private List<Ball> removeBalls;
    private Paint textPaint;
    private int maxMoveY = UiUtils.getSize(15);
    private int textSize = UiUtils.getSize(14);
    private int maxShowBallsNum = 6;

    private int deleteY = UiUtils.getSize(87);
    private int deleteX = UiUtils.getSize(33);
    private int deleteR=UiUtils.getSize(6);
    private int ballRadius=UiUtils.getSize(22);


    public BallsView(Context context) {
        super(context);
        init(context);
    }

    public BallsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BallsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBall = context.getResources().getDrawable(R.mipmap.icon_qd_new);
        balls = new ArrayList<>();
        removeBalls = new ArrayList<>();

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(UiUtils.getSize(12));
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.MONOSPACE);

        addTestBall();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        postDelayed(runnable, 40);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        float velocity = 0.25f;
        float deleteVelocity = 10f;

        @Override
        public void run() {
            for (int i = 0; i < balls.size() && i < maxShowBallsNum; i++) {
                Ball ball = balls.get(i);
                if (ball.moveTop) {
                    ball.centerRY += velocity;
                    if (ball.centerRY - ball.centerY > maxMoveY) {
                        ball.centerRY = maxMoveY + ball.centerY;
                        ball.moveTop = false;
                    }
                } else {
                    ball.centerRY -= velocity;
                    if (ball.centerRY - ball.centerY < 0) {
                        ball.centerRY = ball.centerY;
                        ball.moveTop = true;
                    }
                }
            }

            for (int i = 0; i < removeBalls.size(); i++) {
                Ball ball = removeBalls.get(i);
                float cX = ball.getCenterRX() - deleteX;
                float cY = ball.getCenterRY() - deleteY;
                float r=(ballRadius-deleteR)*ball.getCenterRX()/ball.getCenterX()+deleteR;
                ball.setRadius(r);
                float cZ = (float) Math.sqrt((double) cX * cX + cY * cY);
                if (cZ <= 1) {
                    removeBalls.remove(ball);
                } else {
                    float d=cZ-deleteVelocity;
                    if(d<0){
                        d=0;
                    }
                    ball.centerRX = deleteX + (d) * cX / cZ;
                    ball.centerRY = deleteY + (d) * cY / cZ;
                }
            }
            invalidate();
            postDelayed(this, 10);
        }
    };


    private void addBall(Ball ball) {
        balls.add(ball);
    }


    private void deleteBall(Ball ball) {
        balls.remove(ball);
        ball.setText("");
        removeBalls.add(ball);
        if (balls.size() == 0) {
            addTestBall();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int ballsLength = balls.size();
        Ball drawBall;
        for (int i = 0; i < ballsLength; i++) {
            drawBall = balls.get(i);
            drawBall(drawBall, canvas);
        }
        int rBallsLength = removeBalls.size();
        for (int i = 0; i < rBallsLength; i++) {
            drawBall = removeBalls.get(i);
            drawBall(drawBall, canvas);
        }
    }

    private void drawBall(Ball drawBall, Canvas canvas) {
        int centerY = (int) (drawBall.getCenterRY());
        int centerX = (int) (drawBall.getCenterRX());
        int radius = (int) (drawBall.getRadius());
        mBall.setBounds(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        mBall.draw(canvas);
        if(!TextUtils.isEmpty(drawBall.text)){
            int textWidth = (int) textPaint.measureText(drawBall.text);
            canvas.drawText(drawBall.text, centerX - textWidth / 2, centerY + radius + textPaint.getTextSize(), textPaint);
        }
    }


    private float lastX;
    private float lastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX=event.getX();
                lastY=event.getY();
                if(isClickBall(lastX,lastY)){
                    getParent().requestDisallowInterceptTouchEvent(true);
                    performClick();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                 x = event.getX();
                 y = event.getY();
                 if((Math.abs(x-lastX)>10||Math.abs(y-lastY)>10)&&isClickBall(x,y)){
                     return true;
                 }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isClickBall(float x,float y){
        for (int i = 0; i < balls.size() && i < maxShowBallsNum; i++) {
            Ball ball = balls.get(i);
            float centerX = ball.getCenterX();
            float centerY = ball.getCenterY();
            float radius = ball.getRadius();
            if (centerX - radius < x && x < centerX + radius && centerY - radius < y && y < centerY + radius + textSize) {
                deleteBall(ball);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private class Ball {
        private float radius;
        private float centerX;
        private float centerY;
        private float centerRX;
        private float centerRY;
        private String text;
        private boolean moveTop = true;

        public Ball(float radius, float centerX, float centerY) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
            this.centerRX = centerX;
            this.centerRY = centerY;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public float getCenterY() {
            return centerY;
        }

        public void setCenterY(float centerY) {
            this.centerY = centerY;
        }

        public float getCenterRX() {
            return centerRX;
        }

        public void setCenterRX(float centerRX) {
            this.centerRX = centerRX;
        }

        public float getCenterRY() {
            return centerRY;
        }

        public void setCenterRY(float centerRY) {
            this.centerRY = centerRY;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isMoveTop() {
            return moveTop;
        }

        public void setMoveTop(boolean moveTop) {
            this.moveTop = moveTop;
        }
    }

    int ballNum = 1;

    private void addTestBall() {
        float height = UiUtils.getSize(40);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            if (i % 3 == 0) {
                height += UiUtils.getSize(100);
            }
            float r = UiUtils.getSize(22);
            float x = UiUtils.getSize(100 + 100 * (i % 3));
            float y;
            if (i % 3 == 1) {
                y = height + r;
            } else {
                y = height;
            }
            Ball ball = new Ball(r, x, y);
            ball.centerRY = random.nextInt(maxMoveY) + ball.centerY;
            ball.text = "第" + ballNum + "个球";
            ballNum++;
            addBall(ball);
        }
    }
}
