package com.qcx.mini.net;

/**
 * Created by Administrator on 2017/11/28.
 *
 */

public class URLString {

    public static class H5PageUrl{
        public final static String RED_PACKAGE_INTRODUCE="https://www.quchuxing.com/userAgreement/redEnvelopeRules.html";
        public final static String CLAUSE="https://www.quchuxing.com/userAgreement/userAgreement.html";
    }
//
//    private static final String URL_DRIVER_TEST = "http://10.10.2.40:8080";
//    private static final String URL_CLIENT_TEST = "http://172.18.66.1:8081";
    private static final String URL_DRIVER_TEST = "http://47.92.84.65:20002";
    private static final String URL_CLIENT_TEST = "http://47.92.84.65:20001";
    private static final String URL_DRIVER_ONLINE = "https://v1.driver.quchuxing.com.cn";
    private static final String URL_CLIENT_ONLINE = "https://v1.passenger.quchuxing.com.cn";

    private static String url_driver = URL_DRIVER_TEST;
    private static String url_client = URL_CLIENT_TEST;
//    private static String url_driver = URL_DRIVER_ONLINE;
//    private static String url_client = URL_CLIENT_ONLINE;


    public static String travelCreate = url_driver + "/travel/createlv3";
    public static String travelPassengerCreate = url_driver + "/travel/passenger/createlv1";
    public static String mainRecommendTravels = url_driver + "/feed/feedList";
    public static String recommendTravel = url_driver + "/feed/recommendTravel";
    public static String listComments = url_driver + "/feed/listComments";//获取评论列表
    public static String addComment = url_driver + "/feed/addComment";//添加评论
    public static String travelLike = url_driver + "/feed/travelLike";//点赞
    public static String travelUnlike = url_driver + "/feed/travelUnlike";//点赞
    public static String eachFeedList = url_driver + "/feed/eachFeedList";//单用户行程列表
    public static String publishPrice = url_driver + "/travel/publishPrice";//获取行程价格
    public static String feedUnFinalList = url_driver + "/feed/feedUnFinalList";//获取未完成行程
    public static String feedUnFinalListByRole = url_driver + "/feed/feedUnFinalListByRole";//获取车主或乘客未完成行程
    public static String personalInfo = url_driver + "/driver/personalInfoNew";//个人主页
    public static String myTravel = url_driver + "/travel/myTravel2";//个人主页行程列表
    public static String otherTravel = url_driver + "/travel/othersTravel2";//其他人主页行程列表
    public static String listPersonNews = url_driver + "/feed/ListPersonNews";//个人主页动态列表
    public static String listOtherNews = url_driver + "/feed/listOtherNews";//其他人主页动态列表
    public static String addPicture = url_driver + "/driver/detail/picture";//上传头像
    public static String changeInfo = url_driver + "/driver/detail/change_weapp";//修改司机信息
    public static String updateLoginInfo = url_driver + "/driver/detail/changelv";//修改司机信息
    public static String addressChange = url_driver + "/driver/address/change";//修改家公司地址
    public static String money = url_driver + "/driver/moneylv1";//钱包信息
    public static String balance = url_driver + "/driver/balance";//钱包信息
    public static String changAttention = url_driver + "/attention/changAttention";//关注
    public static String pullAttention = url_driver + "/attention/pullAttention";//取消关注

    public static String login = url_driver + "/loginNew";//登录
    public static String sendCaptcha = url_driver + "/sendCaptcha";//发送验证码
    public static String checkCode = url_driver + "/driver/checkCode";//校验短信验证码
    public static String authen = url_driver+ "/driver/authen";//实名认证
    public static String setPassword = url_driver+ "/driver/setPassword";//实名认证
    public static String selectAuthen = url_driver+ "/driver/selectAuthen";//实名认证信息展示
    public static String authenCheck = url_driver+ "/driver/checkAuthen";//实名认证查询
    public static String uploadAuth = url_driver+ "/driver/upload/audit";//上传车主认证信息
    public static String driverStatus = url_driver+ "/driver/status";//查询车主认证状态
    public static String driverAuthenInfo = url_driver+ "/driver/auth/showFeedback";//查询车主认证信息
    public static String zmxy = url_driver+ "/driver/zmxy";//芝麻认证
    public static String changeDriverDetail = url_driver+ "/driver/detail/change_weapp";//修改司机信息
    public static String withDraw = url_driver+ "/driver/enchshmentNew";//提现
    public static String addBankCard = url_driver+ "/driver/insert/bankCard";//添加银行卡
    public static String deleteBankCard = url_driver+ "/driver/delete/bankCard";//删除银行卡
    public static String usingBackAccount = url_driver+ "/driver/using/bankCard";//选择银行卡
    public static String bankCardList = url_driver+ "/driver/select/bankCard";//提现账号列表
    public static String bankLogo = "https://t1.driver.quchuxing.com.cn/resources/bankImg/";//银行logo
    public static String homeAndCompanyAddress = url_driver+ "/driver/address";//查询家公司地址
    public static String saveAlipay = url_driver+ "/driver/saveAlipay";//保存提现账号

    public static String eachFeedTravelDetail = url_driver + "/feed/eachFeedTravelDetail";//未下单行程详情
    public static String getTravelDetail = url_driver + "/travel/getTravelDetail";//未下单行程详情
    public static String driverSeckill = url_driver + "/travel/driverSeckill";//未下单行程详情
    public static String createOrder = url_client + "/orders/createlv4";//下单
    public static String nopay = url_client + "/orders/nopaylv1";//未支付订单信息
    public static String driverTravelDetail = url_driver + "/travel/detaillv3";//司机行程详情
    public static String updateTravel = url_driver + "/travel/updateTravel";//更新行程信息
    public static String seckillTravelDetail = url_client+ "/travel/seckillTravelDetail";//乘客被秒杀后 乘客行程详情
    public static String ordersDetail = url_client+ "/orders/detailv1";//乘客下单后 乘客行程详情
    public static String ordersCompletedDetail = url_client+ "/orders/detailv2";//乘客下单后 乘客行程详情
    public static String travelOrdersDetail = url_client+ "/orders/ordersTravelDetail";//乘客行程详情
    public static String departure = url_driver+ "/travel/departurelv1";//发车
    public static String finishTravel = url_driver+ "/travel/finalylv1";//结束行程
    public static String ordersUp = url_client+ "/orders/up";//乘客结束行程
    public static String ordersDown = url_client+ "/orders/down";//乘客结束行程
    public static String ordersLate = url_client+ "/orders/late";//乘客标记车主迟到
    public static String refund = url_client+ "/orders/refundlv2";//乘客下单后，取消行程
    public static String ordersCancel = url_client+ "/orders/cancel";//取消订单
    public static String travelCancel = url_client+ "/travel/cancel";//乘客下单前，取消行程接口
    public static String driverDetail = url_driver+ "/driver/detail";//司机行程详情
    public static String travel_cancel_drevier = url_driver+ "/travel/cancelv1";//取消行程
    public static String passengerLate = url_driver+ "/travel/passenger/late";//标记乘客迟到
    public static String cancelPassengerOrder = url_driver+ "/travel/cancelPassengerOrder";//司机删除某个乘客
    public static String travelHistory = url_driver+ "/travel/travelHistory2";//历史行程
    public static String travelShare = url_driver+ "/feed/travelShare";//历史行程
    public static String travelInfo_Driver = url_driver+ "/travel/travelInfo_Driver";//历史行程详情

    public static String version = url_driver+ "/version/update";//版本号
    public static String fansList = url_driver+ "/attention/getOtherFans";//粉丝列表
    public static String attentionList = url_driver+ "/attention/getOtherAttentions";//关注列表
    public static String attentionTravels = url_driver+ "/travel/attentionTravels";//关注的人的行程列表
    public static String matchTravelPassengers = url_driver+ "/travel/matchTravelPassengers";//匹配乘客
    public static String matchTravelDrivers = url_driver+ "/travel/matchTravelDrivers";//匹配车主
    public static String matchTravel = url_driver+ "/travel/publish";//行程匹配
    public static String searchTravel = url_driver+ "/travel/searchTravel";//搜索行程
    public static String searchTravel_2 = url_driver+ "/travel/search";//搜索行程
    public static String commentsTravelList = url_driver+ "/feed/commentsTravelList";//评论列表
    public static String comments = url_driver+ "/feed/listComments";//评论详情

    public static String getPushHistory = url_driver+ "/push/getPushHistory";//获取系统消息列表
    public static String updatePushRead = url_driver+ "/push/updatePushRead";//阅读系统消息列表
    public static String changCommentStatus = url_driver+ "/feed/changCommentStatus";//阅读评论

    public static String myJoinedGroups = url_driver+ "/group/myJoinedGroups";//我的线路群&推荐线路群
    public static String joinGroup = url_driver+ "/group/joinGroup";//加群
    public static String joinGroupByIds = url_driver+ "/group/joinGroupByIds";//加群
    public static String hotGroups = url_driver+ "/group/hotGroups";//热门线路群
    public static String groupDetail = url_driver+ "/group/groupDetail";//上下班&跨城群详情
    public static String touristGroupDetail = url_driver+ "/group/touristGroupDetail";//景点群详情
    public static String crossCityGroupDetail = url_driver+ "/group/crossCityGroupDetail";//跨城群群详情
    public static String groupMembers = url_driver+ "/group/groupMembers";//群成员列表
    public static String groupCreate = url_driver+ "/group/create";//创建群
    public static String uploadGroupBanner = url_driver+ "/group/uploadGroupBanner";//创建群
    public static String recommendGroupMembers = url_driver+ "/group/recommendGroupMembers";//推荐群成员
    public static String leaveGroup = url_driver+ "/group/leaveGroup";//退出群
    public static String isHaveCreateGroupAuth = url_driver+ "/group/isHaveCreateGroupAuth";//创建群权限

    public static String  personalLineCreate= url_driver+ "/personalTL/create";//创建常用线路
    public static String  personalLineList= url_driver+ "/personalTL/list";//常用线路列表
    public static String  personalLineListByRole= url_driver+ "/personalTL/listByRole";//常用线路列表
    public static String  personalLineUpdate= url_driver+ "/personalTL/update";//常用线路列表
    public static String  personalLineDelete= url_driver+ "/personalTL/delete";//删除线路
    public static String  personalLineDetail= url_driver+ "/personalTL/detail";//线路详情
    public static String  personalLinesRelease= url_driver+ "/personalTL/listManual";//发布行程页的线路
    public static String  personalTLListByRole= url_driver+ "/personalTL/listByRole";
    public static String  travelLineDetail= url_driver+ "/personalTL/travelLineDetail";//线路匹配的行程

    public static String  updateLocation= url_driver+ "/driver/update/location";//上传位置信息

    //QC
    public static String  myQC= url_driver+ "/QC/myQC";//QC信息

    //主页信息
    public static String listOfHome=url_driver+"/personalTL/listOfHome";


}
