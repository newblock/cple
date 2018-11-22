package com.qcx.mini.entity;

/**
 * Created by Administrator on 2018/2/1.
 */

public class HomeAndCommpanyEntity extends Entity{
    private Address driver;
    private int status;

    public Address getDriver() {
        return driver;
    }

    public void setDriver(Address driver) {
        this.driver = driver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Address{
        private String addr_company;
        private double[] location_company;
        private String addr_home;
        private double[] location_home;

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

        public double[] getLocation_home() {
            return location_home;
        }

        public void setLocation_home(double[] location_home) {
            this.location_home = location_home;
        }
    }

}
