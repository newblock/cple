package com.qcx.mini.net;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.MainClass;
import com.qcx.mini.User;
import com.qcx.mini.activity.LoginActivity;
import com.qcx.mini.entity.Entity;
import com.qcx.mini.utils.GenericsUtils;
import com.qcx.mini.utils.GsonUtil;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.ToastUtil;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2018/8/13.
 */

public abstract class QuCallback<T> extends StringCallback {
    private Class<T> aClass;

    public QuCallback(){
        try {
            aClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Response<String> response) {
        if(aClass!=null){
            try{
                JSONObject object=new JSONObject(response.body());
                if(object.has("status")&&object.getInt("status")==-1){
                    ToastUtil.showToast("请重新登录");
                    Intent intent=new Intent(MainClass.getInstance(),LoginActivity.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    MainClass.getInstance().startActivity(intent);
                    User.getInstance().signOut();
                }
                Gson gson= GsonUtil.create();
                T o=gson.fromJson(response.body(),aClass);
                onSuccess(o);
            }catch (Exception e){
                e.printStackTrace();
                onError("数据解析失败，请退出后重试");
            }
        }else {
            onError("");
            if(aClass!=null){
                LogUtil.e(aClass.getName()+" is not a Entity");
            }else {
                LogUtil.e(" aClass is null");
            }
        }
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        onError("服务器连接失败");
    }

    public abstract void onSuccess(T t);

    public void onError(String errorInfo){
        if(!TextUtils.isEmpty(errorInfo)){
            ToastUtil.showToast(errorInfo);
        }
    }
}
