package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/8/23.
 *
 */

public class DriverAuthInfo extends Entity {
    private DriverInfo feedback;
    private int status;

    public DriverInfo getFeedback() {
        return feedback;
    }

    public void setFeedback(DriverInfo feedback) {
        this.feedback = feedback;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class DriverInfo{
        /**
         *
         "carNumber": "京A58456",
         "msg": null,
         "realName": "张**",
         "drivingLicensePictureMain": "https://t1.driver.quchuxing.com.cn/resources/pictures/1534754832562.jpg",
         "car": "vf hfd",
         "carOwner": null,
         "idCard": "510************614",
         "driverLicencePictureMain": "https://t1.driver.quchuxing.com.cn/resources/pictures/1534754831928.jpg",
         "licenseDate": null,
         "registeDate": null
         */
        private String carNumber;
        private String msg;
        private String realName;
        private String drivingLicensePictureMain;
        private String car;
        private String carOwner;
        private String idCard;
        private String driverLicencePictureMain;
        private String licenseDate;
        private String registeDate;

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getDrivingLicensePictureMain() {
            return drivingLicensePictureMain;
        }

        public void setDrivingLicensePictureMain(String drivingLicensePictureMain) {
            this.drivingLicensePictureMain = drivingLicensePictureMain;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getCarOwner() {
            return carOwner;
        }

        public void setCarOwner(String carOwner) {
            this.carOwner = carOwner;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getDriverLicencePictureMain() {
            return driverLicencePictureMain;
        }

        public void setDriverLicencePictureMain(String driverLicencePictureMain) {
            this.driverLicencePictureMain = driverLicencePictureMain;
        }

        public String getLicenseDate() {
            return licenseDate;
        }

        public void setLicenseDate(String licenseDate) {
            this.licenseDate = licenseDate;
        }

        public String getRegisteDate() {
            return registeDate;
        }

        public void setRegisteDate(String registeDate) {
            this.registeDate = registeDate;
        }
    }
}
