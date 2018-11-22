package com.qcx.mini;

/**
 * Created by Administrator on 2018/3/27.
 */

public class ConstantValue {
    public static int ICON_RESIZE=120;
    public static int ICON_RESIZE_V2=300;

//    public final static int TYPE_DRIVER=0;
//    public final static int TYPE_PASSENGER=1;

    public static class GroupType{
        public final static int WORK=0;
        public final static int SCENIC=1;
        public final static int ACROSS_CITY=2;
    }

    /**
     * 跨城群往返类型
     */
    public static class WayType{
        public final static int GO_BACK=0;//往返
        public final static int GO=1;//往
        public final static int BACK=2;//返
    }

    /**
     * 评论状态
     */
    public static class CommentStatus{
        public final static int UNREAD=0;//未读
        public final static int IS_READED=1;//已读
    }

    /**
     * 行程类型
     */
    public static class TravelType{
        public final static int DRIVER=0;//车主行程
        public final static int PASSENGER=1;//乘客行程
        public final static int ALL=2;//全部行程 部分接口请求时使用
    }
    /**
     * 行常用线路类型
     */
    public static class LinelType{
        public final static int DRIVER=0;//车主
        public final static int PASSENGER=1;//乘客
    }

    /**
     * 性别
     */
    public static class SexType{
        public final static int SECRECY=0;//保密secrecy
        public final static int MAN=1;//男
        public final static int WOMAN=2;//女
    }

    /**
     * 预定行程类型
     */
    public static class ReservedTravelType{
        public final static int RESERVED_TRAVEL_STATION = 0;//站点乘车
        public final static int RESERVED_TRAVEL_PICKUP = 1;//车接车送
    }

    /**
     * 消息类型
     */
    public static class PushType{
        public final static String TRAVEL_DEPARTURE="departure";//车主发车
        public final static String DRIVER_CANCEL="driver_cancel";//车主取消行程
        public final static String NEW_DRIVER_TRAVEL="newDriverTravelCreate";//车主发布了行程
        public final static String NEW_PASSENGER_TRAVEL="newPassengerTravelCreate";//乘客发布了行程
        public final static String TRAVEL_WATER_BILL="travelWaterBill";//行程收入流水
        public final static String SHARE_WATER_BILL="shareWaterBill";//分享收入流水
        public final static String ATTENTION="attention";//被人关注
        public final static String REFUND="refund";//乘客退款
    }

    /**
     * 支付类型
     */
    public static class PayType{
        public final static int ZHIFUBAO = 0;//支付宝
        public final static int WEIXING = 1;//微信
    }

    /**
     * 导航类型
     */
    public static class NavigationType{
        public final static int GAODE=0;//高德
        public final static int BAIDU=1;//百度
    }

    /**
     * 导航模式
     */
    public static class NavigationMode{
        //t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）
        public final static int DRIVE=0;
        public final static int BUS=1;
        public final static int WALK=2;
        public final static int RIDING=3;
    }

    /**
     * 车主认证状态
     */
    public static class AuthStatus{
        //0:审核中 1：未提交 2：未通过 3：已拉黑 4：审核通过
        public final static int CHECKING=0;
        public final static int UNCOMMITTED=1;
        public final static int NOTPASS=2;
        public final static int BLACKLIST=3;
        public final static int PASS=4;
    }

    /**
     * 订单状态
     */
    public static class OrdersStatus{
        //NO_PAY(1,"未支付"),PAY(2,"已支付"),UNDER_WAY(3,"行程中"),DEPARTURE(4,"已发车"),
        // PASSENGER_DOWN(8,"乘客已下车")，FINAL(0,"完成"),DRIVER_LATE(-2,"司机迟到"),PASSENGER_LATE(-3,"乘客迟到")
        public final static int NO_PAY=1;
        public final static int PAY=2;
        public final static int UNDER_WAY=3;
        public final static int DEPARTURE=4;
        public final static int PASSENGER_DOWN=8;
        public final static int FINAL=0;
        public final static int DRIVER_LATE=-2;
        public final static int PASSENGER_LATE=-3;
        public final static int REFUND=-6;//已退款
        public final static int PAY_TIMEOUT=-1;//支付超时
        public final static int CANCEL=-1;//取消订单

    }

}
