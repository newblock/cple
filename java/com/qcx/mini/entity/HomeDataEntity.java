package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/8/13.
 */

public class HomeDataEntity extends Entity {
    private Driver driver;
    private Passenger passenger;
    private int status;

    public Driver getDriver() {
        return driver;
    }

    public HomeDataEntity setDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public HomeDataEntity setPassenger(Passenger passenger) {
        this.passenger = passenger;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public HomeDataEntity setStatus(int status) {
        this.status = status;
        return this;
    }

    public static class Driver {
        private List<PersonalLineEntity> driverLine;
        private List<DriverAndTravelEntity> driverTravel;

        public List<PersonalLineEntity> getDriverLine() {
            return driverLine;
        }

        public Driver setDriverLine(List<PersonalLineEntity> driverLine) {
            this.driverLine = driverLine;
            return this;
        }

        public List<DriverAndTravelEntity> getDriverTravel() {
            return driverTravel;
        }

        public void setDriverTravel(List<DriverAndTravelEntity> driverTravel) {
            this.driverTravel = driverTravel;
        }
    }

    public static class Passenger {
        private List<PersonalLineEntity> passengerLine;
        private List<DriverAndTravelEntity> passengerTravel;

        public List<PersonalLineEntity> getPassengerLine() {
            return passengerLine;
        }

        public void setPassengerLine(List<PersonalLineEntity> passengerLine) {
            this.passengerLine = passengerLine;
        }

        public List<DriverAndTravelEntity> getPassengerTravel() {
            return passengerTravel;
        }

        public void setPassengerTravel(List<DriverAndTravelEntity> passengerTravel) {
            this.passengerTravel = passengerTravel;
        }
    }
}
