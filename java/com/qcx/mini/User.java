package com.qcx.mini;

import android.text.TextUtils;
import android.util.Log;

import com.qcx.mini.entity.LoginEntity;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.SharedPreferencesUtil;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by Administrator on 2017/11/23.
 */

public class User {

    private String token = "";

    private String ronToken;

    private long phoneNumber;

    private boolean zmxyStatus;
    private boolean authenStatus;
    private boolean passwordStatus;
    private int driverStatus;
    private String inviteCode;

    private boolean isLogin = false;

    private static User user = new User();

    public void signOut() {
        this.isLogin = false;
        setToken(null);
        setRonToken(null);
        setPhoneNumber(-1);
        setPasswordStatus(false);
        setAuthenStatus(false);
        setDriverStatus(-1);

        RongIM.getInstance().logout();
        RongIMClient.getInstance().logout();
        RongIM.getInstance().disconnect();
        RongIMClient.getInstance().disconnect();
    }

    private void logIn(String token, String ronToken, long phoneNumber) {
        setToken(token);
        setRonToken(ronToken);
        setPhoneNumber(phoneNumber);
        isLogin = !TextUtils.isEmpty(token);
        JPushInterface.setAlias(MainClass.getInstance(), 1, String.valueOf(phoneNumber));
        if (!TextUtils.isEmpty(ronToken)) {
            connect(ronToken);
        }
    }

    public void logIn(LoginEntity data) {
        if (data != null && !TextUtils.isEmpty(data.getToken())) {
            logIn(data.getToken(),data.getRongToken(),data.getPhone());

            setAuthenStatus(data.isAuthenStatus());
            setDriverStatus(data.getDriverStatus());
            setPasswordStatus(data.isPassStatus());
            setZmxyStatus(data.isZmxyStatus());
            setInviteCode(data.getInviteCode());
        }
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setZmxyStatus(boolean zmxyStatus) {
        this.zmxyStatus = zmxyStatus;
        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_ZM_STATUS, zmxyStatus);
    }

    public void setAuthenStatus(boolean authenStatus) {
        this.authenStatus = authenStatus;
        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_AUTHEN_STATUS, authenStatus);
    }

    public void setPasswordStatus(boolean passwordStatus) {
        this.passwordStatus = passwordStatus;
        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_PASSWORD_STATUS, passwordStatus);
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_DRIVER_STATUS, driverStatus);
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        SharedPreferencesUtil.getUserSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_INVITE_CODE, inviteCode);
    }

    private void setToken(String token) {
        this.token = token;
        SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_TOKEN, token);
    }

    private void setRonToken(String ronToken) {
        this.ronToken = ronToken;
        SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_RONG_TOKEN, ronToken);
    }

    private void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        SharedPreferencesUtil.getAppSharedPreferences().put(ConstantString.SharedPreferencesKey.SP_PHONE_NUMBER, phoneNumber);
    }

    public String getToken() {
        return token;
    }

    public String getRonToken() {
        return ronToken;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }


    public boolean isZmxyStatus() {
        return zmxyStatus;
    }

    public boolean isAuthenStatus() {
        return authenStatus;
    }


    public boolean isPasswordStatus() {
        return passwordStatus;
    }


    public int getDriverStatus() {
        return driverStatus;
    }

    private User() {
    }

    public static User getInstance() {
        if (!user.isLogin) {
            SharedPreferencesUtil sp=SharedPreferencesUtil.getAppSharedPreferences();

            String token = sp.getString(ConstantString.SharedPreferencesKey.SP_TOKEN, null);
            String ronToken = sp.getString(ConstantString.SharedPreferencesKey.SP_RONG_TOKEN, null);
            long phoneNumber = sp.getLong(ConstantString.SharedPreferencesKey.SP_PHONE_NUMBER);
            user.logIn(token,ronToken,phoneNumber);
            if(user.isLogin){
                SharedPreferencesUtil userSp=SharedPreferencesUtil.getUserSharedPreferences();
                boolean zmxyStatus=userSp.getBoolean(ConstantString.SharedPreferencesKey.SP_ZM_STATUS);
                boolean authenStatus=userSp.getBoolean(ConstantString.SharedPreferencesKey.SP_AUTHEN_STATUS);
                boolean passwordStatus=userSp.getBoolean(ConstantString.SharedPreferencesKey.SP_PASSWORD_STATUS);
                int driverStatus=userSp.getInt(ConstantString.SharedPreferencesKey.SP_DRIVER_STATUS);
                String inviteCode=userSp.getString(ConstantString.SharedPreferencesKey.SP_INVITE_CODE);
                user.setZmxyStatus(zmxyStatus);
                user.setAuthenStatus(authenStatus);
                user.setPasswordStatus(passwordStatus);
                user.setDriverStatus(driverStatus);
                user.setInviteCode(inviteCode);
            }
        }
        return user;
    }


    public void connect(String token) {

        if (MainClass.getInstance().getApplicationInfo().packageName.equals(getCurProcessName(MainClass.getInstance()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtil.e("onTokenIncorrect: ");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
//                    Log.i(TAG, "--onSuccess" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtil.e("onError: ".concat(errorCode.getMessage()));
                }
            });
        }
    }
}
