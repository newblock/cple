package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/1/19.
 */

public class PayInfoEntity extends Entity {
    //{"ordersId":"151633250361313693100472","ticketPrice":5.0,"wxapp":{},"creatTime":1516332503613,
    // "bookSeats":1,"buyingSafety":false,"travelId":11817493208588,

    // "wxmap":{"appid":"wxb4188a08e56b21a0","noncestr":"1128235098","package":"Sign=WXPay",
    // "partnerid":"1437430302","prepayid":"wx201801191128233b26dc67070306522004","sign":
    // "566C12815E17992DE86F45A27C42A317","timestamp":"1516332503"},
    //
    // "status":200,
    // "ali":"Y2hhcnNldD11dGYtOCZiaXpfY29udGVudD0lN0IlMjJ0aW1lb3V0X2V4cHJlc3MlMjIlM0ElMjIzbSUyMiUyQyUyMnByb2R1Y3RfY29kZSUyMiUzQSUyMlFVSUNLX01TRUNVUklUWV9QQVklMjIlMkMlMjJ0b3RhbF9hbW91bnQlMjIlM0ElMjI1LjAlMjIlMkMlMjJzdWJqZWN0JTIyJTNBJTIyJUU1JTg3JUJBJUU4JUExJThDJUU4JUFFJUEyJUU1JThEJTk1JTIyJTJDJTIyYm9keSUyMiUzQSUyMiUyMiUyQyUyMm91dF90cmFkZV9ubyUyMiUzQSUyMjE1MTYzMzI1MDM2MTMxMzY5MzEwMDQ3MiUyMiU3RCZtZXRob2Q9YWxpcGF5LnRyYWRlLmFwcC5wYXkmbm90aWZ5X3VybD1odHRwJTNBJTJGJTJGNDcuOTIuODQuNjUlM0EyMDAwMSUyRnBheSUyRmFsaXBheSUyRm5vdGlmeSZhcHBfaWQ9MjAxNzAxMTcwNTE1NTI0NiZzaWduX3R5cGU9UlNBJnZlcnNpb249MS4wJnRpbWVzdGFtcD0yMDE4LTAxLTE5KzExJTNBMjglM0EyMyZzaWduPXJQZk5rcDlmbFhlbGJKNVBqZnBNcG51VEdrTmY2SkJVc3RGUnBQcW1ZMzNHdnJBR2JzWXAwS2MwWXdPVWF2dWpQSVVMT0JCR0s1d3FtMTl4JTJGTnpXWWJPUG9NajZjanIlMkZFaXFaSkRkOWMzOTk5aEp5SHZJWU5menhVYTZSbXBhNk5QJTJGQnhTZ1ZRJTJCN1VHJTJCRFBCUjlZJTJCUmFlWjIlMkZLV0Y4a1ZLJTJGSWRkOUxNV0klM0Q="}

    private String ordersId;
    private String ticketPrice;
    private long creatTime;
    private int bookSeats;
    private int status;
    private boolean buyingSafety;
    private long travelId;

    private Wxmap wxmap;
    private String ali;
    private String ordersTravelId;

    public String getOrdersTravelId() {
        return ordersTravelId;
    }

    public void setOrdersTravelId(String ordersTravelId) {
        this.ordersTravelId = ordersTravelId;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public int getBookSeats() {
        return bookSeats;
    }

    public void setBookSeats(int bookSeats) {
        this.bookSeats = bookSeats;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isBuyingSafety() {
        return buyingSafety;
    }

    public void setBuyingSafety(boolean buyingSafety) {
        this.buyingSafety = buyingSafety;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public Wxmap getWxmap() {
        return wxmap;
    }

    public void setWxmap(Wxmap wxmap) {
        this.wxmap = wxmap;
    }

    public String getAli() {
        return ali;
    }

    public void setAli(String ali) {
        this.ali = ali;
    }

    public class Wxmap{
        private String appid;
        private String noncestr;
        private String packages;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

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
    }

}
