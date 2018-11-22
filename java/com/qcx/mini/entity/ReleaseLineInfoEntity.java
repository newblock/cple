package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ReleaseLineInfoEntity extends Entity {
    /**
     *
     "startAddress": "中关村创业大厦",
     "start": [116.311171, 40.037122],
     "type": 1,
     "isHaveCreated": true,
     "seats": 1,
     "waypoints": null,
     "redPacketPrice": 1.50,
     "price": 15,
     "lineDesc": "回家",
     "end": [116.20111, 40.050209],
     "startTime": 1525430700000,
     "strategy": 0,
     "endAddress": "中关村创客小镇"
     */

    private String startAddress;
    private double[] start;
    private int type;
    private boolean isHaveCreated;
    private int seats;
    private double redPacketPrice;
    private double price;
    private String lineDesc;
    private String endAddress;
    private long startTime;
    private long lineId;
    private int strategy;
    private double[] end;
    private String[] waypointsAddress;
    private List<double[]> waypoints;
    private boolean isAutoCreate;

    public boolean isAutoCreate() {
        return isAutoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        isAutoCreate = autoCreate;
    }

    public String[] getWaypointsAddress() {
        return waypointsAddress;
    }

    public void setWaypointsAddress(String[] waypointsAddress) {
        this.waypointsAddress = waypointsAddress;
    }

    public List<double[]> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<double[]> waypoints) {
        this.waypoints = waypoints;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public double[] getStart() {
        return start;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHaveCreated() {
        return isHaveCreated;
    }

    public void setHaveCreated(boolean haveCreated) {
        isHaveCreated = haveCreated;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getRedPacketPrice() {
        return redPacketPrice;
    }

    public void setRedPacketPrice(double redPacketPrice) {
        this.redPacketPrice = redPacketPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLineDesc() {
        return lineDesc;
    }

    public void setLineDesc(String lineDesc) {
        this.lineDesc = lineDesc;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public double[] getEnd() {
        return end;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }
}
