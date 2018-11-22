package com.qcx.mini;

import android.os.Environment;

import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;

import java.io.File;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ConstantString {
//    public static final String WXAPP_ID = MainClass.isOldApp() ? "wxb4188a08e56b21a0" : "wxf1098f657c706952";
    public static final String WXAPP_ID = MainClass.isOldApp() ? "wxb4188a08e56b21a0" : "wxf6fd3fb0de8fa3b5";//微信ID
    public static final String MINI_PROGRAM_ID = "gh_a82a854af101";//小程序ID
    public static final String HELP_PHONE="010-89940360";

    //SharedPreferencesKey
    public class SharedPreferencesKey {
        public static final String SP_APP = "sp_app";//存放APP相关配置的SP文件名（与用户无关）；
        public static final String SP_TOKEN = "sp_token";//token key
        public static final String SP_RONG_TOKEN = "sp_rongToken";//融云 token key
        public static final String SP_PHONE_NUMBER = "sp_phoneNumber_long";//电话号码
        public static final String SP_ZM_STATUS = "sp_zm_status";//芝麻认证状态
        public static final String SP_DRIVER_STATUS = "sp_auto_status";//用户车主认证状态
        public static final String SP_AUTHEN_STATUS = "sp_authen_status";//实名认证状态
        public static final String SP_PASSWORD_STATUS = "sp_password_status";//支付密码状态
        public static final String SP_INVITE_CODE = "sp_invite_code";//邀请码


        public static final String SP_WELCOME_PAGER_IS_SHOW = "sp_welcome_pager_is_show";//第一次启动欢迎页
        public static final String SP_LOCATION_CITY = "sp_location_city";//用户当前城市
        public static final String SP_MAP_STYLE_CODE = "sp_map_style_code";//地图style版本
        public static final String SP_NAVIGATION_TYPE = "sp_navigation_type";//用户选择的导航模式
    }

    public final static class FilePath {
        private static final String imageFilePath = Environment.getExternalStorageDirectory() + File.separator + "QCX";//趣出行根目录
        private static final String mapStylePath = imageFilePath + File.separator + "mapStyle";//地图style 目录

        public static String getImageFilePath() {
            File file = new File(imageFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return imageFilePath;
        }

        public static String getMapStyleFilePath() {
            File file = new File(mapStylePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return mapStylePath + File.separator + "style.data";
        }

    }
}
