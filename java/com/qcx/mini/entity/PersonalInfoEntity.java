package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PersonalInfoEntity extends Entity {
    private int isAttention;
    private long OtherPhone;
    private int isSelf;
    private int status;
    private PersonalEntity personalInfo;
    private GroupEntity Group;

    public int getIsAttention() {
        return isAttention;
    }

    public void setIsAttention(int isAttention) {
        this.isAttention = isAttention;
    }

    public long getOtherPhone() {
        return OtherPhone;
    }

    public void setOtherPhone(long otherPhone) {
        OtherPhone = otherPhone;
    }

    public int getIsSelf() {
        return isSelf;
    }

    public void setIsSelf(int isSelf) {
        this.isSelf = isSelf;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PersonalEntity getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalEntity personalInfo) {
        this.personalInfo = personalInfo;
    }

    public GroupEntity getGroup() {
        return Group;
    }

    public void setGroup(GroupEntity group) {
        Group = group;
    }

    public static class PersonalEntity extends Entity{
        /**
         *
         "goNum": 28,
         "lastTimeOnline": "21小时前",
         "authenStatus": 0,
         "moneyCard": -168.01,
         "nickName": "ZSPing",
         "sex": 1,
         "orderNum": 4,
         "picture": "https://t1.driver.quchuxing.com.cn/resources/pictures/1523584253545.jpg",
         "carNumber": "京A***56",
         "QC": 0.0,
         "car": "vf hfd",
         "departureNum": 24,
         "attention": 9,
         "company": null,
         "position": null,
         "job": "额",
         "age": "90后",
         "beCare": 7,
         "status": 4,
         "zmxy_status": 1
         */
        private int goNum;
        private String lastTimeOnline;
        private int authenStatus;
        private double moneyCard;
        private String nickName;
        private int sex;
        private int orderNum;
        private String picture;
        private String carNumber;
        private double QC;
        private String car;
        private int departureNum;
        private String company;
        private String position;
        private String job;
        private String age;
        private int attention;

        public int getGoNum() {
            return goNum;
        }

        public void setGoNum(int goNum) {
            this.goNum = goNum;
        }

        public double getMoneyCard() {
            return moneyCard;
        }

        public void setMoneyCard(double moneyCard) {
            this.moneyCard = moneyCard;
        }

        public double getQC() {
            return QC;
        }

        public void setQC(double QC) {
            this.QC = QC;
        }

        public int getAttention() {
            return attention;
        }

        public void setAttention(int attention) {
            this.attention = attention;
        }

        public String getLastTimeOnline() {
            return lastTimeOnline;
        }

        public void setLastTimeOnline(String lastTimeOnline) {
            this.lastTimeOnline = lastTimeOnline;
        }

        public int getAuthenStatus() {
            return authenStatus;
        }

        public void setAuthenStatus(int authenStatus) {
            this.authenStatus = authenStatus;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public int getDepartureNum() {
            return departureNum;
        }

        public void setDepartureNum(int departureNum) {
            this.departureNum = departureNum;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public int getBeCare() {
            return beCare;
        }

        public void setBeCare(int beCare) {
            this.beCare = beCare;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getZmxy_status() {
            return zmxy_status;
        }

        public void setZmxy_status(int zmxy_status) {
            this.zmxy_status = zmxy_status;
        }

        private int beCare;
        private int status;
        private int zmxy_status;

    }

    public static class GroupEntity extends Entity{

    }
}
