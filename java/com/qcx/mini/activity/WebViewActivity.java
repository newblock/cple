package com.qcx.mini.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.qcx.mini.R;
import com.qcx.mini.base.BaseActivity;
import com.qcx.mini.utils.LogUtil;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {
    public static final int SHAREDE_LINE_INTRODUCE = 10;
    AgentWeb.PreAgentWeb mWeb;
    AgentWeb mAgentWeb;

    @BindView(R.id.web_content_layout)
    FrameLayout frameLayout;

    @BindView(R.id.title_bar_title)
    TextView include_title;
    String url="";

    boolean fx = false;

    @Override
    public void onTitleLeftClick(View v) {
        if (fx) finish();
        if (!mAgentWeb.back()) {
            finish();
        }
    }

    @Override
    public int getLayoutID() {
        return  R.layout.activity_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(Bundle savedInstanceState) {
        initTitleBar("加载中...",true,false);
        String s=getIntent().getStringExtra("url");
         if(!TextUtils.isEmpty(s)){
             url = new String(Base64.decode(s.getBytes(), Base64.DEFAULT));
         }
        LogUtil.i(url);

        mWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(frameLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                .useDefaultIndicator()// 使用默认进度条
                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(new ChromeClientCallbackManager.ReceivedTitleCallback() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        include_title.setText(title);
                    }
                }) //设置 Web 页面的 title 回调
                .createAgentWeb()//
                .ready();

        mAgentWeb = mWeb.go(url);

//        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
//        webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 6.6.6; zh-cn; Nexus One Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
//        //设置自适应屏幕，两者合用
////        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
////        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        webSettings.setSupportZoom(false);  //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。
////若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//        //webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100
//
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        webSettings.setAppCacheEnabled(false);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
////        webSettings.setAllowFileAccess(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
        mAgentWeb.destroyAndKill();
//        System.exit(0);
    }

}