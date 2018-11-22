package com.qcx.mini.verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/12.
 */

public class VerifyPhoneUtil {
    public static boolean isBasePhone(String mobile) {
        Pattern p1 = Pattern
                .compile("^1(3|4|5|6|7|8|9)[0-9]\\d{8}$");
        Matcher m1 = p1.matcher(mobile);
        return m1.matches();
    }

    public static boolean isPhoneH(String mobile) {
        String p = mobile.substring(0, 3);
        Pattern p1 = Pattern
                .compile("^1(3|4|5|7|8|9)[0-9]");
        Matcher m1 = p1.matcher(p);
        return m1.matches();
    }

    /**
     * 验证是否是有效手机号
     * 条件：
     * 以+86开头或者是11位有效手机号
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）、177
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNum(String mobiles) {
        Pattern p2 = Pattern
                .compile("^(\\+?86)\\d{11}$");
        Matcher m = p2.matcher(mobiles);

        if (mobiles.length() == 11) {
            return isBasePhone(mobiles);

        } else if (mobiles.length() > 11 && m.matches()) {
            String mobile = mobiles.substring(3);
            return isBasePhone(mobile);
        }
        return false;

    }

    /**
     * 验证是否是以“+86”开头的手机号码
     *
     * @return
     */
    public static boolean isPhonePre(String phoneNum) {
        Pattern p2 = Pattern
                .compile("^(\\+?86)\\d{11}$");
        Matcher m = p2.matcher(phoneNum);

        if (m.matches()) {
            String mobile = phoneNum.substring(3);
            return isBasePhone(mobile);
        }
        return false;
    }

}
