package com.qcx.mini.net;

import android.view.View;
import android.widget.ImageView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qcx.mini.R;
import com.qcx.mini.User;
import com.qcx.mini.dialog.CenterImgDialog;
import com.qcx.mini.dialog.DialogUtil;
import com.qcx.mini.entity.DriverAndTravelEntity;
import com.qcx.mini.entity.DriverAuthInfo;
import com.qcx.mini.entity.IntEntity;
import com.qcx.mini.entity.PersonalInfoEntity;
import com.qcx.mini.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/11.
 */

public class NetUtil {

    /**
     * 加群
     * @param groupId 需要加入的群ID
     * @param view 成功后隐藏的View
     */
    public static void joinGroup(long groupId,final View view){
        Map<String,Object> params=new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("groupId",new long[]{groupId});
        params.put("phones",new long[]{User.getInstance().getPhoneNumber()});
        Request.post(URLString.joinGroup, params, new EntityCallback(IntEntity.class) {
            @Override
            public void onSuccess(Object t) {
                IntEntity intEntity= (IntEntity) t;
                if(intEntity.getStatus()==200){
                    view.setVisibility(View.GONE);
                    ToastUtil.showToast("添加成功");
                }
            }
        });
    }

    /**
     * 关注
     * @param phone 关注的人的phone
     * @param callback 成功后回调
     */
    public static void attention(long phone,EntityCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("attentione", phone);
        Request.post(URLString.changAttention, params,callback);
    }

    /**
     * 取消关注
     * @param phone 取消关注的人的phone
     * @param callback 成功后回调
     */
    public static void cancelAttention(long phone,EntityCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("attentionPhone", phone);
        Request.post(URLString.pullAttention, params,callback);
    }

    /**
     * 查询并更新车主认证状态
     * 更新后存入User.driverStatus
     */
    public static void updateDriverStatus(){
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        Request.post(URLString.driverStatus, params, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                try {
                    JSONObject object=new JSONObject(response.body());
                    int status=object.getInt("driverStatus");
                    User.getInstance().setDriverStatus(status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取车主认证后的信息
     * @param callback 成功后回调
     */
    public static void authenInfo(QuCallback<DriverAuthInfo> callback){
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("status", User.getInstance().getDriverStatus());
        Request.post(URLString.driverAuthenInfo, params,callback);
    }

    /**
     * 获取自己或他人个人中心信息
     * @param phone 电话
     * @param callBack 成功后回调
     */
    public static void getPersonalInfo(long phone,QuCallback<PersonalInfoEntity> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", User.getInstance().getToken());
        params.put("otherPhone",phone);
        Request.post(URLString.personalInfo, params,callBack);
    }


    /**
     * 发送验证码
     * @param phone 电话
     * @param callback 成功后回调
     */
    public static void sendCaptcha(long phone,QuCallback<IntEntity> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        Request.post(URLString.sendCaptcha, params, callback);
    }

    /**
     * 校验短信验证码
     * @param captcha 验证码
     * @param callback 成功后回调
     */
    public static void checkCaptcha(String captcha,QuCallback<IntEntity> callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("token",User.getInstance().getToken());
        params.put("captcha", captcha);
        Request.post(URLString.checkCode, params, callback);
    }
}
