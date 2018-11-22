package com.qcx.mini.net;


import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.qcx.mini.User;
import com.qcx.mini.entity.Entity;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/11/27.
 */

public class Request {
    public static void post(String url, Map params, Callback callback) {
        JSONObject jsonObject = new JSONObject(params);
        OkGo.<String>post(url)
                .upJson(jsonObject)
                .execute(callback);
    }


    public static void uploadingPicture(String url, @NonNull File file, EntityCallback callback) {
        OkGo.<String>post(url)
                .isMultipart(true)
                .params("token", User.getInstance().getToken())
                .params("picture", file)
                .execute(callback);
    }
}
