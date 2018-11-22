package com.qcx.mini.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qcx.mini.R;
import com.qcx.mini.utils.LogUtil;

/**
 * Created by Administrator on 2018/6/26.
 */

public class TestDialog extends BaseDialog {

    @Override
    public int getLayoutId() {
        LogUtil.i("TestDialog  getLayoutId");
        return R.layout.dialog_pay;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.i("TestDialog  onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i("TestDialog  onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView(View view) {
        LogUtil.i("TestDialog  initView");
    }

    @Override
    public void onStart() {
        LogUtil.i("TestDialog  onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.i("TestDialog  onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtil.i("TestDialog  onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.i("TestDialog  onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LogUtil.i("TestDialog  onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        LogUtil.i("TestDialog  onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        LogUtil.i("TestDialog  onDestroyView");
        super.onDismiss(dialog);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtil.i("TestDialog  onDestroyView hidden="+hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtil.i("TestDialog  onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.i("TestDialog  onCreateDialog");
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LogUtil.i("TestDialog  onGetLayoutInflater");
        return super.onGetLayoutInflater(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        LogUtil.i("TestDialog  onAttach");
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        LogUtil.i("TestDialog  onAttachFragment");
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        LogUtil.i("TestDialog  onCancel");
        super.onCancel(dialog);
    }

    @Override
    public void onDetach() {
        LogUtil.i("TestDialog  onDetach");
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtil.i("TestDialog  onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        LogUtil.i("TestDialog  onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }
}
