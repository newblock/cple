package com.qcx.mini.entity;


import java.util.List;

public class PayEntity extends Entity {
    private long timeStamp;
    private String ordersId;
    private double ticketPrice;
    private String createTime;
    private double price;
    private double discountPrice;
    private int payTimeout;
    private NopaySign nopaySign;
    private int bookSeats;
    private int status;
    private boolean isSeckill;
    private List<Orders> ordersList;
    private boolean isPickUp;
    private double extraMoney;
    private long travelId;
    private String ordersTravelId;

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public String getOrdersTravelId() {
        return ordersTravelId;
    }

    public void setOrdersTravelId(String ordersTravelId) {
        this.ordersTravelId = ordersTravelId;
    }

    public boolean isPickUp() {
        return isPickUp;
    }

    public void setPickUp(boolean pickUp) {
        isPickUp = pickUp;
    }

    public double getExtraMoney() {
        return extraMoney;
    }

    public void setExtraMoney(double extraMoney) {
        this.extraMoney = extraMoney;
    }

    public List<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    public boolean isSeckill() {
        return isSeckill;
    }

    public void setSeckill(boolean seckill) {
        isSeckill = seckill;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getBookSeats() {
        return bookSeats;
    }

    public void setBookSeats(int bookSeats) {
        this.bookSeats = bookSeats;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getPayTimeout() {
        return payTimeout;
    }

    public void setPayTimeout(int payTimeout) {
        this.payTimeout = payTimeout;
    }

    public NopaySign getNopaySign() {
        return nopaySign;
    }

    public void setNopaySign(NopaySign nopaySign) {
        this.nopaySign = nopaySign;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class NopaySign{
        private String ordersId;
        private String ali;
        private long phone;
        private long createtime;
        private WxMap wxMap;
        private String appid;
        private String noncestr;
        private String packages;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;
        private double price;
        private double discountPrice;
        private double coupon_price;
        private String coupon_id;
        private long starttime;
        private String openid;

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getOrdersId() {
            return ordersId;
        }

        public void setOrdersId(String ordersId) {
            this.ordersId = ordersId;
        }

        public String getAli() {
            return ali;
        }

        public void setAli(String ali) {
            this.ali = ali;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public WxMap getWxMap() {
            return wxMap;
        }

        public void setWxMap(WxMap wxMap) {
            this.wxMap = wxMap;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public double getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(double coupon_price) {
            this.coupon_price = coupon_price;
        }
    }

    public class WxMap{
        private String packages;
        private String coupon_id;
        private String appid;
        private String sign;
        private double discountPrice;
        private double coupon_price;
        private String partnerid;
        private String prepayid;
        private String noncestr;
        private String timestamp;

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(String coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public double getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(double coupon_price) {
            this.coupon_price = coupon_price;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }
    }

    public class Orders{
        private String ordersTravelId;
        private long travelId;

        public long getTravelId() {
            return travelId;
        }

        public void setTravelId(long travelId) {
            this.travelId = travelId;
        }

        public String getOrdersTravelId() {
            return ordersTravelId;
        }

        public void setOrdersTravelId(String ordersTravelId) {
            this.ordersTravelId = ordersTravelId;
        }
    }

}