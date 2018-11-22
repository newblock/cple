package com.qcx.mini.verify;

import android.text.TextUtils;

import com.amap.api.services.help.Tip;
import com.qcx.mini.User;
import com.qcx.mini.widget.calendar.DateEntity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/4.
 */

public class VerifyUtil {

    /**
     * 验证登录状态
     *
     * @throws VerifyException
     */
    public static void verifyLogin() throws VerifyException {
        if (!User.getInstance().isLogin()) {
            throw new VerifyException("您还未登录");
        }
    }

    public static void verifyStartAddress(Tip startTip) throws VerifyException {
        if (startTip == null) {
            throw new VerifyException("请输入起点");
        } else if (startTip.getPoint() == null) {
            throw new VerifyException("起点坐标无效");
        }
    }

    public static void verifyEndAddress(Tip startTip) throws VerifyException {
        if (startTip == null) {
            throw new VerifyException("请输入终点");
        } else if (startTip.getPoint() == null) {
            throw new VerifyException("终点坐标无效");
        }
    }

    public static void verifyTime(long time, long minTime) throws VerifyException {
        if (time == 0) {
            throw new VerifyException("请输入时间");
        } else if (time < minTime) {
            throw new VerifyException("时间不在范围内，请重新选择");
        }
    }

    public static void verifyDriverReleaseTime(List<DateEntity> dates, long minTime) throws VerifyException {
        if (dates == null || dates.size() < 1) {
            throw new VerifyException("请选择时间");
        } else {
            for (int i = 0; i < dates.size(); i++) {
                verifyTime(dates.get(i).getMillion(), minTime);
            }
        }
    }


    public static void verifySeats(int seats) throws VerifyException {
        if (seats < 1) {
            throw new VerifyException("请选择座位数");
        }
    }

    public static void verifyPeoples(int num) throws VerifyException {
        if (num < 1) {
            throw new VerifyException("请选择乘车人数");
        }
    }

    public static void verifyPrice(int price) throws VerifyException {
        if (price < 1) {
            throw new VerifyException("请选择票价");
        }
    }

    public static void verifyStrategy(int strategy) throws VerifyException {
        if (strategy < 0) {
            throw new VerifyException("请选择线路");
        }
    }



    // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
    // 清除掉所有特殊字符
    public static void stringFilter(String str) throws VerifyException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.matches() || hasEmoji(str)) {
            throw new VerifyException("请不要使用特殊字符");
        }
    }

    public static boolean hasEmoji(String content) {

        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }



    public static void verifyAliCashAccount(String cashAccount) throws VerifyException {
        cashAccount.trim();
        if (TextUtils.isEmpty(cashAccount)) {
            throw new VerifyException("提现账号不能为空");
        }
        if(!VerifyPhoneUtil.isPhoneNum(cashAccount)&&!cashAccount.matches("^[A-Za-z0-9.\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            throw new VerifyException("请输入正确的支付宝账号");
        }
    }

}
