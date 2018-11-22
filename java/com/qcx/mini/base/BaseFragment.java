package com.qcx.mini.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.dialog.LoadingDialog;
import com.qcx.mini.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/25.
 *
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder bind;
    private LoadingDialog mLoadingDialog;
    private Integer statusBarColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        setStatusBarColor();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            setStatusBarColor();
//        }
    }


    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected abstract int getLayoutID();


    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.dismiss();
            } catch (Exception e) {//java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                e.printStackTrace();
            }
        }
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        if (!mLoadingDialog.isVisible()) mLoadingDialog.show(getFragmentManager(), "");
    }

    protected void setStatusBarColor(@ColorInt int color) {
        this.statusBarColor = color;
    }

    private void setStatusBarColor() {
        if(statusBarColor!=null){
            StatusBarUtil.setColor(getActivity(), statusBarColor, 0);
        }
    }

}
