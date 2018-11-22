package com.qcx.mini.widget.numSoftKeyboart;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.qcx.mini.R;
import com.qcx.mini.adapter.SimpleRecyclerViewAdapter;
import com.qcx.mini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/31.
 * 数字输入
 */

public class NumSoftKeyboard extends RecyclerView{
    SimpleRecyclerViewAdapter<String> adapter;

    public NumSoftKeyboard(Context context) {
        super(context);
        init(context);
    }

    public NumSoftKeyboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumSoftKeyboard(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        setOverScrollMode(OVER_SCROLL_NEVER);
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        setLayoutManager(manager);
        addItemDecoration(new SoftKeyboardItemDecoration());
        adapter = new SimpleRecyclerViewAdapter<>(getContext(), ItemNumSoftKeyboartViewHolder.class, R.layout.item_soft_keyboard_num);
        setAdapter(adapter);
        adapter.setDatas(getNumText());
    }

    private List<String> getNumText() {
        List<String> data = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            data.add(i + "");
        }
        data.add("");
        data.add("0");
        data.add("-1");
        return data;
    }

    private class SoftKeyboardItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            int size = state.getItemCount();
            if (position < 3) {
                outRect.top = UiUtils.getSize(3);
            }
            if (position % 3 == 0) {
                outRect.left = UiUtils.getSize(3);
            }
            if (position % 3 == 2) {
                outRect.right = UiUtils.getSize(3);
            }
            if (position + 3 > size) {
                outRect.bottom = UiUtils.getSize(3);
            }
//            outRect.bottom=UiUtils.getSize(7);
//            outRect.right=UiUtils.getSize(7);
        }
    }

    public void setListener(OnNumInputListener listener) {
        adapter.setListener(listener);
    }
}
