package com.qcx.mini.share;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Administrator on 2018/1/31.
 */

public class ShareTravelEntity implements Parcelable {
    private double[] start;
    private double[] end;
    private String startAddress;
    private String endAddress;
    private double price;
    private String startTime;
    private String seatsNum;
    private String icon;
    private String name;
    private long travelId;
    private int travelType;

    private String car;
    private int surplusSeats;
    private String age;
    private int sex;
    private String waypoints;

    public ShareTravelEntity(){}

    public String toString(){
        StringBuilder str=new StringBuilder();
        if(start==null){
            str.append("start==null\n");
        }else {
            for(int i=0;i<start.length;i++){
                str.append("start[").append(i).append("]=").append(start[i]).append("\n");
            }
        }

        if(end==null){
            str.append("end==null\n");
        }else {
            for(int i=0;i<end.length;i++){
                str.append("end["+i+"]="+end[i]+"\n");
            }
        }
        str.append("startAddress=").append(startAddress).append("\n");
        str.append("endAddress=").append(endAddress).append("\n");
        str.append("price=").append(price).append("\n");
        str.append("startTime=").append(startTime).append("\n");
        str.append("seatsNum=").append(seatsNum).append("\n");
        str.append("icon=").append(icon).append("\n");
        str.append("name=").append(name).append("\n");
        str.append("travelId=").append(travelId).append("\n");
        str.append("travelType=").append(travelType).append("\n");
        str.append("car=").append(car).append("\n");
        str.append("surplusSeats=").append(surplusSeats).append("\n");
        str.append("age=").append(age).append("\n");
        str.append("sex=").append(sex).append("\n");
        str.append("waypoints=").append(waypoints).append("\n");
        return str.toString();
    }


    public static final Parcelable.Creator<ShareTravelEntity> CREATOR = new Parcelable.Creator<ShareTravelEntity>() {

        @Override
        public ShareTravelEntity createFromParcel(Parcel source) {
            return new ShareTravelEntity(source);
        }

        @Override
        public ShareTravelEntity[] newArray(int size) {
            return new ShareTravelEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (start == null || start.length == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(start.length);
            dest.writeDoubleArray(start);
        }

        if (end == null || end.length == 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(end.length);
            dest.writeDoubleArray(end);
        }
        dest.writeString(startAddress);
        dest.writeString(endAddress);
        dest.writeDouble(price);
        dest.writeString(startTime);
        dest.writeString(seatsNum);
        dest.writeString(icon);
        dest.writeString(name);
        dest.writeLong(travelId);
        dest.writeInt(travelType);
        dest.writeString(car);
        dest.writeInt(surplusSeats);
        dest.writeString(age);
        dest.writeInt(sex);
        dest.writeString(waypoints);

    }
    public ShareTravelEntity(Parcel in) {
        int length1 = in.readInt();
        if (length1 > 0) {
            start = new double[length1];
            in.readDoubleArray(start);
        }
        int length2 = in.readInt();
        if (length2 > 0) {
            end = new double[length2];
            in.readDoubleArray(end);
        }

        startAddress = in.readString();
        endAddress = in.readString();
        price = in.readDouble();
        startTime = in.readString();
        seatsNum = in.readString();
        icon = in.readString();
        name = in.readString();
        travelId = in.readLong();
        travelType = in.readInt();
        car=in.readString();
        surplusSeats=in.readInt();
        age=in.readString();
        sex=in.readInt();
        waypoints=in.readString();
    }

    public String getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(String waypoints) {
        this.waypoints = waypoints;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public double[] getStart() {
        return start;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public double[] getEnd() {
        return end;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSeatsNum() {
        return seatsNum;
    }

    public void setSeatsNum(String seatsNum) {
        this.seatsNum = seatsNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public int getTravelType() {
        return travelType;
    }

    public void setTravelType(int travelType) {
        this.travelType = travelType;
    }

    public int getSurplusSeats() {
        return surplusSeats;
    }

    public void setSurplusSeats(int surplusSeats) {
        this.surplusSeats = surplusSeats;
    }
}
