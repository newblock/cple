package com.qcx.mini.net;

/**
 * Created by Administrator on 2018/5/29.
 */

public enum ResultCode {
    SUCCESS(200, "请求成功"),

    UNKNOWN(-1001, "未知状态"),

    ORDER_IS_EXIST(-100, "您有未支付的订单"),

    HAVE_NO_PAY_ORDER(-101, "您有未支付的订单"),

    FAILED_TO_CREATE_ORDER(-102, "订座失败 请重新尝试"),

    OVERDUE_TRAVEL(-103, "行程已过期"),

    OWN_TRAVEL(-105, "不可预订自己行程"),

    NOT_AUTO_ZMXY(-106, "未进行芝麻认证"),

    TRAVEL_HAS_STARTED(-107, "车主已发车，不可预定"),

    NO_SEAT(-108, "座位已被抢完"),

    TRAVEL_CANCELLED(-109, "车主已取消行程");


    private int code;
    private String codeDescribe;

    ResultCode(int code, String codeDescribe) {
        this.code = code;
        this.codeDescribe = codeDescribe;
    }

    public int getCode() {
        return code;
    }

    public String getCodeDescribe() {
        return codeDescribe;
    }

    public void setCodeDescribe(String codeDescribe) {
        this.codeDescribe = codeDescribe;
    }

    public static ResultCode getResultCode(int code) {
        ResultCode[] resultCodes = ResultCode.values();
        for (int i = 0; i < resultCodes.length; i++) {
            if (code == resultCodes[i].getCode()) {
                return resultCodes[i];
            }
        }
        return UNKNOWN;
    }
}
