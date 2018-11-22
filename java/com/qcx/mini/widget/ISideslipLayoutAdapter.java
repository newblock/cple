package com.qcx.mini.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by Administrator on 2018/1/5.
 */

public interface ISideslipLayoutAdapter<T> {
    int getItemCount();
    T getItem(int position);
    View  onCreateView(ViewGroup parent, View contentView, int position);
}
