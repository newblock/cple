package com.qcx.mini.net;

import com.qcx.mini.ConstantString;

import java.util.Map;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AppServer{

    public static void login(Map params, EntityCallback callback) {
        String url= URLString.login+"/login";
        Request.post(url,params,callback);
    }
}
