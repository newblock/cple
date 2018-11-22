package com.qcx.mini.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class PersonalLineEntity extends Entity {
    /**
     *
     "lineId": 50311503451893,
     "phone": 18712855639,
     "start": [116.326086, 39.967807],
     "startAddress": "UME国际影城(华星店)",
     "end": [116.315142, 39.886886],
     "endAddress": "六里桥东(地铁站)",
     "startTime": "13:45",
     "strategy": 0,
     "seats": 2,
     "price": 30,
     "lineDesc": "",
     "autoCreate": true,
     "waypoints": null,
     "createTime": 1525319434519,
     "updateTime": 1525319434519,
     "type": 1,
     "autoDays": [1, 2, 3, 4, 5, 6, 0]
     */

    private long lineId;
    private long phone;
    private double[] start;
    private String startAddress;
    private double[] end;
    private String endAddress;
    private String startTime;
    private int strategy;
    private int seats;
    private double price;
    private String lineDesc;
    private boolean autoCreate;
    private long createTime;
    private long updateTime;
    private int type;
    private List<Integer> autoDays;
    private boolean isChecked;
    private int status;
    private List<double[]> waypoints;
    private List<String> waypointsAddress;

    public List<double[]> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<double[]> waypoints) {
        this.waypoints = waypoints;
    }

    public List<String> getWaypointsAddress() {
        return waypointsAddress;
    }

    public void setWaypointsAddress(List<String> waypointsAddress) {
        this.waypointsAddress = waypointsAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public double[] getStart() {
        return start;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public double[] getEnd() {
        return end;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
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

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getAutoDays() {
        return autoDays;
    }

    public void setAutoDays(List<Integer> autoDays) {
        this.autoDays = autoDays;
    }
}
