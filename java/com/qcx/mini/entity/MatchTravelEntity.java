package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class MatchTravelEntity extends Entity {
    private DriverAndTravelEntity selfTravel;
    private MatchTravel matchTravelDrivers;
    private MatchTravel matchTravelPassengers;
    private int status;

    public MatchTravel getMatchTravelPassengers() {
        return matchTravelPassengers;
    }

    public void setMatchTravelPassengers(MatchTravel matchTravelPassengers) {
        this.matchTravelPassengers = matchTravelPassengers;
    }

    public DriverAndTravelEntity getSelfTravel() {
        return selfTravel;
    }

    public void setSelfTravel(DriverAndTravelEntity selfTravel) {
        this.selfTravel = selfTravel;
    }

    public MatchTravel getMatchTravelDrivers() {
        return matchTravelDrivers;
    }

    public void setMatchTravelDrivers(MatchTravel matchTravelDrivers) {
        this.matchTravelDrivers = matchTravelDrivers;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class MatchTravel{
        private List<DriverAndTravelEntity> matchTravelPassengers;
        private List<DriverAndTravelEntity> travelResults;
        private int matchTravelNum;

        public int getMatchTravelNum() {
            return matchTravelNum;
        }

        public void setMatchTravelNum(int matchTravelNum) {
            this.matchTravelNum = matchTravelNum;
        }

        public List<DriverAndTravelEntity> getTravelResults() {
            return travelResults;
        }

        public void setTravelResults(List<DriverAndTravelEntity> travelResults) {
            this.travelResults = travelResults;
        }

        public List<DriverAndTravelEntity> getMatchTravelPassengers() {
            return matchTravelPassengers;
        }

        public void setMatchTravelPassengers(List<DriverAndTravelEntity> matchTravelPassengers) {
            this.matchTravelPassengers = matchTravelPassengers;
        }
    }
}
