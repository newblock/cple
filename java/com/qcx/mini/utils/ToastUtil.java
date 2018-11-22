package com.qcx.mini.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qcx.mini.MainClass;
import com.qcx.mini.R;

/**
 * Created by zsp on 2017/8/30.
 */

public class ToastUtil {
    private static Toast toast;
    public static void showToast(CharSequence text){
        if(toast!=null){
            toast.cancel();
        }
        LogUtil.i("tosatText="+text);
        toast=Toast.makeText(MainClass.getInstance(),text,Toast.LENGTH_SHORT);
        toast.show();
    }
    public static void showToast( @StringRes int resId){
        showToast(MainClass.getInstance().getResources().getText(resId));
    }

    public static void showToastCenter(CharSequence text){
        Toast toast=new Toast(MainClass.getInstance());
        LayoutInflater inflater=LayoutInflater.from(MainClass.getInstance());
        View view=inflater.inflate(R.layout.toast_text,null,false);
        ((TextView)view.findViewById(R.id.toast_text)).setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
