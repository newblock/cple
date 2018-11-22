package com.qcx.mini.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.utils.UiUtils;


/**
 * Created by zsp on 2017/8/9.
 */

public class LetterListView extends View {
    private Paint paint;
    private int pressBackColor;
    private int textSize;
    private int textColor;
    private int checkedTextColor;
    private OnLetterChangedListener listener;
    private String[] items;
    private int padding = (int) (5 * UiUtils.SCREENRATIO);

    private int checkedItem = -1;
    private boolean isPress = false;

    public LetterListView(Context context) {
        super(context);
        init();
    }

    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterListView);
        if (typedArray != null) {
            pressBackColor = typedArray.getColor(R.styleable.LetterListView_pressBackColor, 0x00000000);
            textColor = typedArray.getColor(R.styleable.LetterListView_textColor, 0xff6095D3);
            checkedTextColor = typedArray.getColor(R.styleable.LetterListView_checkedTextColor, 0x00000000);
            textSize = typedArray.getDimensionPixelSize(R.styleable.LetterListView_textSize, 0);
            typedArray.recycle();
        }
    }

    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        pressBackColor = 0x00ff0000;
        textSize = 0;
        textColor = 0xFF007AFF;
        checkedTextColor = 0xFFFF0000;
        items = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setPressBackColor(int pressBackColor) {
        this.pressBackColor = pressBackColor;
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setCheckedTextColor(int checkedTextColor) {
        this.checkedTextColor = checkedTextColor;
        invalidate();
    }

    public void setOnTouchingLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.listener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChanged(String s);

        void onTouch(boolean isTouch);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPress) {
            canvas.drawColor(pressBackColor);
        }

        int height = getHeight();
        int width = getWidth();
        int itemHeight = height / items.length;
        if (textSize == 0) {
            textSize = width < itemHeight - 5 ? width : itemHeight - 5;
        }
        for (int i = 0; i < items.length; i++) {
            paint.setTextSize(textSize);
            paint.setColor(textColor);
            if (i == checkedItem) {
                paint.setColor(checkedTextColor);
            }
            float xStart = width / 2 - paint.measureText(items[i]) / 2 + padding / 4.0f;
            float yStart = itemHeight * i + textSize;
            canvas.drawText(items[i], xStart, yStart, paint);
        }

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int oldCheckedItem = checkedItem;
        int checkItem = (int) (event.getY() / getHeight() * items.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isPress = true;
                if (listener != null) {
                    listener.onTouch(true);
                }
                if (oldCheckedItem != checkItem) {
                    if (checkItem > -1 && checkItem < items.length) {
                        if (listener != null) {
                            listener.onLetterChanged(items[checkItem]);
                        }
                        checkedItem = checkItem;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldCheckedItem != checkItem && listener != null) {
                    if (checkItem > -1 && checkItem < items.length) {
                        listener.onLetterChanged(items[checkItem]);
                        checkedItem = checkItem;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onTouch(false);
                }
                isPress = false;
                checkedItem = -1;
                invalidate();
                break;
        }
        return true;
    }
}
