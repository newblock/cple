package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/1/30.
 */

public class DriverDetailEntity extends Entity {
    private Driver driver;
    private int status;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Driver{
        /**
         * 	"authenStatus": 0,
         "location_home": [116.261538, 39.75105],
         "zmxy": 718,
         "bycarsTimes": 28,
         "driveTimes": 21,
         "car": "",
         "zmxy_url": "http://www.quchuxing.com/safety/sesameCredit.html?score=718&IDcard=5107****6614&name=å¼ æå¹³",
         "company": null,
         "zmxy_status": 1,
         "secretName": 0,
         "moneyCard": 168.0,
         "ordersTimes": 13,
         "nickName": "254",
         "sex": 1,
         "zmxy_name": "张思平",
         "addr_company": "a(新燕莎店)",
         "location_company": [116.410181, 39.910036],
         "addr_home": "鹅房村",
         "picture": "https://t1.driver.quchuxing.com.cn/resources/pictures/1517222046584.jpg",
         "phone": 18712855639,
         "carnumber": "",
         "name": "254",
         "position": null,
         "job": "额",
         "age": "90后",
         "status": 0
         */
        private int authenStatus;
        private double[] location_home;
        private int zmxy;
        private int bycarsTimes;
        private int driveTimes;
        private String car;
        private String zmxy_url;
        private String company;
        private int zmxy_status;
        private int secretName;
        private double moneyCard;
        private int ordersTimes;
        private String nickName;
        private int sex;
        private String zmxy_name;
        private String addr_company;
        private double[] location_company;
        private String addr_home;
        private String picture;
        private long phone;
        private String carnumber;
        private String name;
        private int status;
        private String age;

        public int getAuthenStatus() {
            return authenStatus;
        }

        public void setAuthenStatus(int authenStatus) {
            this.authenStatus = authenStatus;
        }

        public double[] getLocation_home() {
            return location_home;
        }

        public void setLocation_home(double[] location_home) {
            this.location_home = location_home;
        }

        public int getZmxy() {
            return zmxy;
        }

        public void setZmxy(int zmxy) {
            this.zmxy = zmxy;
        }

        public int getBycarsTimes() {
            return bycarsTimes;
        }

        public void setBycarsTimes(int bycarsTimes) {
            this.bycarsTimes = bycarsTimes;
        }

        public int getDriveTimes() {
            return driveTimes;
        }

        public void setDriveTimes(int driveTimes) {
            this.driveTimes = driveTimes;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getZmxy_url() {
            return zmxy_url;
        }

        public void setZmxy_url(String zmxy_url) {
            this.zmxy_url = zmxy_url;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getZmxy_status() {
            return zmxy_status;
        }

        public void setZmxy_status(int zmxy_status) {
            this.zmxy_status = zmxy_status;
        }

        public int getSecretName() {
            return secretName;
        }

        public void setSecretName(int secretName) {
            this.secretName = secretName;
        }

        public double getMoneyCard() {
            return moneyCard;
        }

        public void setMoneyCard(double moneyCard) {
            this.moneyCard = moneyCard;
        }

        public int getOrdersTimes() {
            return ordersTimes;
        }

        public void setOrdersTimes(int ordersTimes) {
            this.ordersTimes = ordersTimes;
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

        public String getZmxy_name() {
            return zmxy_name;
        }

        public void setZmxy_name(String zmxy_name) {
            this.zmxy_name = zmxy_name;
        }

        public String getAddr_company() {
            return addr_company;
        }

        public void setAddr_company(String addr_company) {
            this.addr_company = addr_company;
        }

        public double[] getLocation_company() {
            return location_company;
        }

        public void setLocation_company(double[] location_company) {
            this.location_company = location_company;
        }

        public String getAddr_home() {
            return addr_home;
        }

        public void setAddr_home(String addr_home) {
            this.addr_home = addr_home;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public long getPhone() {
            return phone;
        }

        public void setPhone(long phone) {
            this.phone = phone;
        }

        public String getCarnumber() {
            return carnumber;
        }

        public void setCarnumber(String carnumber) {
            this.carnumber = carnumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
