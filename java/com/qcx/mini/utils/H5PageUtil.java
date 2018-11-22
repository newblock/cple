package com.qcx.mini.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;

import com.qcx.mini.activity.WebViewActivity;
import com.qcx.mini.net.URLString;

/**
 * Created by Administrator on 2018/2/5.
 */

public class H5PageUtil {

    public static void toWebView(String url, Context context){
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
    public static void toRedPackageIntroducePage(Context context){
        Intent intent=new Intent(context, WebViewActivity.class);

        intent.putExtra("url", Base64.encodeToString(URLString.H5PageUrl.RED_PACKAGE_INTRODUCE.getBytes(),Base64.DEFAULT));
        context.startActivity(intent);
    }
    public static void toClausePage(Context context){
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.putExtra("url", Base64.encodeToString(URLString.H5PageUrl.CLAUSE.getBytes(),Base64.DEFAULT));
        context.startActivity(intent);
    }

    public static void toZiMAPage(String url,Context context){
        Intent intent=new Intent(context, WebViewActivity.class);
        intent.putExtra("url", Base64.encodeToString(url.getBytes(),Base64.DEFAULT));
        context.startActivity(intent);
    }
}
