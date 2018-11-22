package com.qcx.mini.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.R;
import com.qcx.mini.net.URLString;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by zsp on 2017/7/5.
 */

public class CommonUtil {

    private static NumberFormat priceFormat;

    public static String formatPrice(double money, int digit) {
        if (priceFormat == null) {
            priceFormat = NumberFormat.getNumberInstance();
        }
        priceFormat.setMaximumFractionDigits(digit);
        String price = priceFormat.format(money);
        String[] prices = price.split("\\.");
        if (digit > 0) {
            if (prices.length == 1) {
                return prices[0].concat(".").concat(addZero(null, digit));
            } else if (prices.length == 2) {
                return prices[0].concat(".").concat(addZero(prices[1], digit));
            }
        } else {
            return prices[0];
        }
        return price;
    }

    public static String forMatPriceNoZero(double money, int digit) {
        if (priceFormat == null) {
            priceFormat = NumberFormat.getNumberInstance();
        }
        priceFormat.setMaximumFractionDigits(digit);
        String price = priceFormat.format(money);
        String[] prices = price.split("\\.");
        if (digit > 0) {
            if (prices.length == 1) {
                return prices[0];
            } else if (prices.length == 2) {
                if (prices[1].length() > digit) {
                    prices[1] = prices[1].substring(0, digit);
                }

                return prices[0].concat(".").concat(prices[1]);
            }
        } else {
            return prices[0];
        }
        return price;
    }

    private static String addZero(String str, int size) {
        if (!TextUtils.isEmpty(str) && (str.length() >= size)) {
            return str;
        }
        int strSize = 0;
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            strSize = str.length();
            builder.append(str);
        }
        for (int i = strSize; i < size; i++) {
            builder.append("0");
        }
        return builder.toString();
    }

    public static List<LatLonPoint> getLatLonPints(String data) {
        List<LatLonPoint> points = new ArrayList<>();
        try {
            if (!TextUtils.isEmpty(data)) {
                String[] pStr = data.split(";");
                for (int i = 0; i < pStr.length; i++) {
                    String[] p = pStr[i].split(",");
                    if (p.length == 2) {
                        points.add(new LatLonPoint(Double.valueOf(p[1]), Double.valueOf(p[0])));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return points;
    }

    /**
     * 将double[] 坐标转成Tip
     *
     * @param location 坐标[ 经度，纬度 ]
     * @param name     地名
     * @return tip
     */
    public static Tip getTip(double[] location, String name) {
        if (location != null && location.length == 2) {
            Tip tip = new Tip();
            tip.setName(name);
            tip.setPostion(new LatLonPoint(location[1], location[0]));
            return tip;
        }
        return null;
    }

    public static void setSexImg(int status, ImageView iv_sex) {
        switch (status) {
            case ConstantValue.SexType.SECRECY:
                iv_sex.setVisibility(View.GONE);
                break;
            case ConstantValue.SexType.MAN:
                iv_sex.setVisibility(View.VISIBLE);
                iv_sex.setImageResource(R.mipmap.img_men);
                break;
            case ConstantValue.SexType.WOMAN:
                iv_sex.setVisibility(View.VISIBLE);
                iv_sex.setImageResource(R.mipmap.img_women);
                break;
            default:
                iv_sex.setVisibility(View.GONE);
                break;
        }

    }
    public static String getSexText(int sex) {
        switch (sex) {
            case ConstantValue.SexType.MAN:
                return "男";
            case ConstantValue.SexType.WOMAN:
                return "女";
            case ConstantValue.SexType.SECRECY:
            default:
                return "";
        }

    }

    public static String appendStringInPoint(String... strs) {
        StringBuilder builder = new StringBuilder();
        if (strs != null && strs.length > 0) {
            for (int i = 0; i < strs.length; i++) {
                if (!TextUtils.isEmpty(strs[i])) {
                    builder.append(strs[i]);
                    builder.append(" · ");
                }
            }
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - 3, builder.length());
        }
        return builder.toString();
    }

    /**
     * 获取银行名字
     *
     * @param bankCode bank
     * @return name
     */
    public static String getBankName(String bankCode) {
        try {
            JSONObject jsonObject = new JSONObject("{\"SRCB\": \"深圳农村商业银行\", \"BGB\": \"广西北部湾银行\", \"SHRCB\": \"上海农村商业银行\", \"BJBANK\": \"北京银行\", \"WHCCB\": \"威海市商业银行\", \"BOZK\": \"周口银行\", \"KORLABANK\": \"库尔勒市商业银行\", \"SPABANK\": \"平安银行\", \"SDEB\": \"顺德农商银行\", \"HURCB\": \"湖北省农村信用社\", \"WRCB\": \"无锡农村商业银行\", \"BOCY\": \"朝阳银行\", \"CZBANK\": \"浙商银行\", \"HDBANK\": \"邯郸银行\", \"BOC\": \"中国银行\", \"BOD\": \"东莞银行\", \"CCB\": \"中国建设银行\", \"ZYCBANK\": \"遵义市商业银行\", \"SXCB\": \"绍兴银行\", \"GZRCU\": \"贵州省农村信用社\", \"ZJKCCB\": \"张家口市商业银行\", \"BOJZ\": \"锦州银行\", \"BOP\": \"平顶山银行\", \"HKB\": \"汉口银行\", \"SPDB\": \"上海浦东发展银行\", \"NXRCU\": \"宁夏黄河农村商业银行\", \"NYNB\": \"广东南粤银行\", \"GRCB\": \"广州农商银行\", \"BOSZ\": \"苏州银行\", \"HZCB\": \"杭州银行\", \"HSBK\": \"衡水银行\", \"HBC\": \"湖北银行\", \"JXBANK\": \"嘉兴银行\", \"HRXJB\": \"华融湘江银行\", \"BODD\": \"丹东银行\", \"AYCB\": \"安阳银行\", \"EGBANK\": \"恒丰银行\", \"CDB\": \"国家开发银行\", \"TCRCB\": \"江苏太仓农村商业银行\", \"NJCB\": \"南京银行\", \"ZZBANK\": \"郑州银行\", \"DYCB\": \"德阳商业银行\", \"YBCCB\": \"宜宾市商业银行\", \"SCRCU\": \"四川省农村信用\", \"KLB\": \"昆仑银行\", \"LSBANK\": \"莱商银行\", \"YDRCB\": \"尧都农商行\", \"CCQTGB\": \"重庆三峡银行\", \"FDB\": \"富滇银行\", \"JSRCU\": \"江苏省农村信用联合社\", \"JNBANK\": \"济宁银行\", \"CMB\": \"招商银行\", \"JINCHB\": \"晋城银行JCBANK\", \"FXCB\": \"阜新银行\", \"WHRCB\": \"武汉农村商业银行\", \"HBYCBANK\": \"湖北银行宜昌分行\", \"TZCB\": \"台州银行\", \"TACCB\": \"泰安市商业银行\", \"XCYH\": \"许昌银行\", \"CEB\": \"中国光大银行\", \"NXBANK\": \"宁夏银行\", \"HSBANK\": \"徽商银行\", \"JJBANK\": \"九江银行\", \"NHQS\": \"农信银清算中心\", \"MTBANK\": \"浙江民泰商业银行\", \"LANGFB\": \"廊坊银行\", \"ASCB\": \"鞍山银行\", \"KSRB\": \"昆山农村商业银行\", \"YXCCB\": \"玉溪市商业银行\", \"DLB\": \"大连银行\", \"DRCBCL\": \"东莞农村商业银行\", \"GCB\": \"广州银行\", \"NBBANK\": \"宁波银行\", \"BOYK\": \"营口银行\", \"SXRCCU\": \"陕西信合\", \"GLBANK\": \"桂林银行\", \"BOQH\": \"青海银行\", \"CDRCB\": \"成都农商银行\", \"QDCCB\": \"青岛银行\", \"HKBEA\": \"东亚银行\", \"HBHSBANK\": \"湖北银行黄石分行\", \"WZCB\": \"温州银行\", \"TRCB\": \"天津农商银行\", \"QLBANK\": \"齐鲁银行\", \"GDRCC\": \"广东省农村信用社联合社\", \"ZJTLCB\": \"浙江泰隆商业银行\", \"GZB\": \"赣州银行\", \"GYCB\": \"贵阳市商业银行\", \"CQBANK\": \"重庆银行\", \"DAQINGB\": \"龙江银行\", \"CGNB\": \"南充市商业银行\", \"SCCB\": \"三门峡银行\", \"CSRCB\": \"常熟农村商业银行\", \"SHBANK\": \"上海银行\", \"JLBANK\": \"吉林银行\", \"CZRCB\": \"常州农村信用联社\", \"BANKWF\": \"潍坊银行\", \"ZRCBANK\": \"张家港农村商业银行\", \"FJHXBC\": \"福建海峡银行\", \"ZJNX\": \"浙江省农村信用社联合社\", \"LZYH\": \"兰州银行\", \"JSB\": \"晋商银行\", \"BOHAIB\": \"渤海银行\", \"CZCB\": \"浙江稠州商业银行\", \"YQCCB\": \"阳泉银行\", \"SJBANK\": \"盛京银行\", \"XABANK\": \"西安银行\", \"BSB\": \"包商银行\", \"JSBANK\": \"江苏银行\", \"FSCB\": \"抚顺银行\", \"HNRCU\": \"河南省农村信用\", \"COMM\": \"交通银行\", \"XTB\": \"邢台银行\", \"CITIC\": \"中信银行\", \"HXBANK\": \"华夏银行\", \"HNRCC\": \"湖南省农村信用社\", \"DYCCB\": \"东营市商业银行\", \"ORBANK\": \"鄂尔多斯银行\", \"BJRCB\": \"北京农村商业银行\", \"XYBANK\": \"信阳银行\", \"ZGCCB\": \"自贡市商业银行\", \"CDCB\": \"成都银行\", \"HANABANK\": \"韩亚银行\", \"CMBC\": \"中国民生银行\", \"LYBANK\": \"洛阳银行\", \"GDB\": \"广东发展银行\", \"ZBCB\": \"齐商银行\", \"CBKF\": \"开封市商业银行\", \"H3CB\": \"内蒙古银行\", \"CIB\": \"兴业银行\", \"CRCBANK\": \"重庆农村商业银行\", \"SZSBK\": \"石嘴山银行\", \"DZBANK\": \"德州银行\", \"SRBANK\": \"上饶银行\", \"LSCCB\": \"乐山市商业银行\", \"JXRCU\": \"江西省农村信用\", \"ICBC\": \"中国工商银行\", \"JZBANK\": \"晋中市商业银行\", \"HZCCB\": \"湖州市商业银行\", \"NHB\": \"南海农村信用联社\", \"XXBANK\": \"新乡银行\", \"JRCB\": \"江苏江阴农村商业银行\", \"YNRCC\": \"云南省农村信用社\", \"ABC\": \"中国农业银行\", \"GXRCU\": \"广西省农村信用\", \"PSBC\": \"中国邮政储蓄银行\", \"BZMD\": \"驻马店银行\", \"ARCU\": \"安徽省农村信用社\", \"GSRCU\": \"甘肃省农村信用\", \"LYCB\": \"辽阳市商业银行\", \"JLRCU\": \"吉林农信\", \"URMQCCB\": \"乌鲁木齐市商业银行\", \"XLBANK\": \"中山小榄村镇银行\", \"CSCB\": \"长沙银行\", \"JHBANK\": \"金华银行\", \"BHB\": \"河北银行\", \"NBYZ\": \"鄞州银行\", \"LSBC\": \"临商银行\", \"BOCD\": \"承德银行\", \"SDRCU\": \"山东农信\", \"NCB\": \"南昌银行\", \"TCCB\": \"天津银行\", \"WJRCB\": \"吴江农商银行\", \"CBBQS\": \"城市商业银行资金清算中心\", \"HBRCU\": \"河北省农村信用社\"}");
            return jsonObject.getString(bankCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取银行卡类型
     *
     * @param cardType cardType
     * @return cardType
     */
    public static String getCardTypeName(String cardType) {
        switch (cardType) {
            case "DC"://储蓄卡
                return "储蓄卡";
            case "CC"://信用卡
                return "信用卡";
            case "SCC"://准贷记卡
                return "准贷记卡";
            case "PC"://预付费卡
                return "预付费卡";
            default:
                return "";
        }
    }

    public static String getCardImgUrl(String bankType){
        return String.format(Locale.CHINA,"%s%s.png", URLString.bankLogo,bankType);
    }

    /**
     * 车主发车倒计时：startTime前15分钟 倒计时至startTime后15分钟(有乘客未发车)
     * 车主结束行程倒计时: 已发车的行程 倒计时至startTime后5小时
     * 乘客自动上车倒计时：startTime 倒计时至 startTime后30分钟
     *
     * @param travelStatus 行程状态
     * @param startTime    行程开始时间
     * @return 状态字符串
     */
    public static String getTravelStatusText(int travelStatus, long startTime) {
        String info = "";
        long cTime = System.currentTimeMillis();
        long time;
        String timeStr;
        switch (travelStatus) {
            case 0://正在寻找乘客
                info = "等待乘客订座";
                break;
            case 1://车主有乘客未发车
            case 11://车主抢单，乘客已支付
                //发车倒计时
                if (startTime - cTime < 15 * DateUtil.MINUTE) { //距离发车时间小于15分钟
                    long c = startTime + 15 * DateUtil.MINUTE - cTime;
                    if (c > 0) {
                        int m = (int) (c / DateUtil.MINUTE);
                        int s = (int) (c % DateUtil.MINUTE) / (1000);
                        info = String.format(Locale.CHINA, "发车倒计时%02d:%02d", m, s);
                    } else {
                        info = "即将自动发车";
                    }
                } else {
                    if (travelStatus == 1) {
                        info = String.format("%s %s发车哦", DateUtil.formatDay("MM-dd", startTime), DateUtil.getTimeStr(startTime, "HH:mm"));
                    } else {
                        info = String.format("乘客已支付，%s %s发车哦", DateUtil.formatDay("MM-dd", startTime), DateUtil.getTimeStr(startTime, "HH:mm"));
                    }
                }
                break;
            case 2://车主发布的行程已发车
            case 12://车主抢单，已发车，行程中
                //结束行程倒计时
                time = 5 * DateUtil.HOUR - (cTime - startTime);
                if (time > 0) {
                    if (time > DateUtil.DAY) {
                        timeStr = DateUtil.getTime(time, true, true, true, false);
                    } else if (time > DateUtil.HOUR) {
                        timeStr = DateUtil.getTime(time, false, true, true, false);
                    } else {
                        timeStr = DateUtil.getTime(time, false, false, true, true);
                    }
                    info = "已发车，" + timeStr + "后行程将自动完成哦";
                } else {
                    info = "即将自动完成";
                }
                break;
            case 9://车主抢单，乘客未支付
                info = "抢单成功,乘客未支付";
                break;

            case 3://乘客发布的行程，等待接单
                info = "等待车主接单";
                break;
            case 4://乘客发布的行程，车主已抢单，等待支付
                info = "车主已接单，去支付吧";
                break;

            case 5://乘客订的车主的行程，未支付
                info = "座位已锁定，去支付";
                break;
            case 6://乘客订的车主的行程，已支付
            case 7://乘客订的车主的行程，已发车
                if (cTime > startTime) {
                    time = 30 * DateUtil.MINUTE - (cTime - startTime);
                    if (time > 0) {
                        info = DateUtil.getTime(time, false, false, true, true) + "后自动上车";
                    } else {
                        info = "即将自动上车";
                    }
                } else {
                    if (travelStatus == 6) {
                        info = String.format("已支付，%s %s记得准时上车", DateUtil.formatDay("MM-dd", startTime), DateUtil.getTimeStr(startTime, "HH:mm"));
                    }else {
                        info = String.format("车主发车啦，%s %s上车哦",DateUtil.formatDay("MM-dd", startTime), DateUtil.getTimeStr(startTime, "HH:mm"));
                    }
                }
                break;
            case 8://乘客订的车主的行程，行程中
                info = "行程中,请系好安全带";
                break;
        }
        return info;

    }

}
