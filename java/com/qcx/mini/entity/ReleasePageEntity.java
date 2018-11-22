package com.qcx.mini.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.help.Tip;
import com.qcx.mini.ConstantValue;
import com.qcx.mini.activity.ReleaseTravel_2Activity;
import com.qcx.mini.utils.CommonUtil;
import com.qcx.mini.utils.DateUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/8/13.
 * 发布行程、添加常用路线页传递对象
 */

public class ReleasePageEntity implements Parcelable{

    private int type;
    private int travelType;

    private Tip start;
    private Tip end ;

    private int seats;
    private double price;
    private long startTime;
    private int strategy;
    private double redPacketPrice;
    private long lineId;
    private ArrayList<Tip> wayPoints;

    private ReleasePageEntity(){}

    private ReleasePageEntity(Parcel in) {
        setType(in.readInt());
        setTravelType(in.readInt());
        setStart((Tip) in.readParcelable(Tip.class.getClassLoader()));
        setEnd((Tip)in.readParcelable(Tip.class.getClassLoader()));
        setSeats(in.readInt());
        setPrice(in.readDouble());
        setStartTime(in.readLong());
        setStrategy(in.readInt());
        setRedPacketPrice(in.readDouble());
        setLineId(in.readLong());
        in.readArrayList(Tip.class.getClassLoader());
    }

    public static final Creator<ReleasePageEntity> CREATOR = new Creator<ReleasePageEntity>() {
        @Override
        public ReleasePageEntity createFromParcel(Parcel in) {
            return new ReleasePageEntity(in);
        }

        @Override
        public ReleasePageEntity[] newArray(int size) {
            return new ReleasePageEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(travelType);
        dest.writeParcelable(start,PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeParcelable(end,PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeInt(seats);
        dest.writeDouble(price);
        dest.writeLong(startTime);
        dest.writeInt(strategy);
        dest.writeDouble(redPacketPrice);
        dest.writeLong(lineId);
        dest.writeList(wayPoints);
    }

    public String toString(){
        StringBuilder builder=new StringBuilder();
        builder.append("type=");
        builder.append(type);
        builder.append("\r\n travelType=");
        builder.append(travelType);
        builder.append("\r\n start=");
        if(start!=null) {
            builder.append(start.toString());
        }

        builder.append("\r\n end=");
        if (end != null) {
            builder.append(end.toString());
        }
        builder.append("\r\n seats=");
        builder.append(seats);
        builder.append("\r\n price=");
        builder.append(price);
        builder.append("\r\n startTime=");
        builder.append(startTime);
        builder.append("\r\n strategy=");
        builder.append(strategy);
        builder.append("\r\n redPacketPrice=");
        builder.append(redPacketPrice);
        builder.append("\r\n lineId=");
        builder.append(lineId);
        builder.append("\r\n wayPoints=");
        if(wayPoints!=null){
            for (int i=0;i<wayPoints.size();i++){
                builder.append(wayPoints.get(i).toString());
                builder.append("  ");
            }
        }
        builder.append(lineId);

        return builder.toString();
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTravelType() {
        return travelType;
    }

    public void setTravelType(int travelType) {
        this.travelType = travelType;
    }

    public Tip getStart() {
        return start;
    }

    public void setStart(Tip start) {
        this.start = start;
    }

    public Tip getEnd() {
        return end;
    }

    public void setEnd(Tip end) {
        this.end = end;
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

    public double getRedPacketPrice() {
        return redPacketPrice;
    }

    public void setRedPacketPrice(double redPacketPrice) {
        this.redPacketPrice = redPacketPrice;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public ArrayList<Tip> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(ArrayList<Tip> wayPoints) {
        this.wayPoints = wayPoints;
    }


    public static class Factory{

        public static ReleasePageEntity getPageData(PersonalLineEntity line){
            ReleasePageEntity data=new ReleasePageEntity();
            if(line!=null){

                data.setType(ReleaseTravel_2Activity.TYPE_LINE);
                data.setTravelType(line.getType());
                data.setStart(CommonUtil.getTip(line.getStart(),line.getStartAddress()));
                data.setEnd(CommonUtil.getTip(line.getEnd(),line.getEndAddress()));
                data.setSeats(line.getSeats());
                data.setPrice(line.getPrice());
                data.setStartTime(DateUtil.getTimeFromString(line.getStartTime(),"HH:mm"));
                data.setStrategy(line.getStrategy());
                if(line.getWaypoints()!=null){
                    ArrayList<Tip> tips=new ArrayList<>();
                    for(int i=0;i<line.getWaypoints().size();i++){
                        if(line.getWaypointsAddress()!=null&&line.getWaypointsAddress().size()==line.getWaypoints().size()){
                            tips.add(CommonUtil.getTip(line.getWaypoints().get(i),line.getWaypointsAddress().get(i)));
                        }
                    }
                    data.setWayPoints(tips);
                }
                data.setLineId(line.getLineId());

            }
            return data;
        }

        /**
         * 获取默认跳转参数(默认值 type=TYPE_RELEASE,travelType=PASSENGER)
         * @return 默认的跳转参数
         */
        public static ReleasePageEntity emptyData(){
            ReleasePageEntity data=new ReleasePageEntity();
            data.setType(ReleaseTravel_2Activity.TYPE_RELEASE);
            data.setTravelType(ConstantValue.TravelType.PASSENGER);
            data.setStart(null);
            data.setEnd(null);
            data.setSeats(0);
            data.setPrice(0);
            data.setStartTime(0);
            data.setStrategy(-1);
            data.setRedPacketPrice(0);
            data.setLineId(-1);
            return data;
        }

        public static ReleasePageEntity releasePassengerData(){
            return emptyData();
        }

        public static ReleasePageEntity releaseDriverData(){
            ReleasePageEntity data=emptyData();
            data.setType(ReleaseTravel_2Activity.TYPE_RELEASE);
            data.setTravelType(ConstantValue.TravelType.DRIVER);
            return data;
        }

        public static ReleasePageEntity linePassengerData(){
            ReleasePageEntity data=emptyData();
            data.setType(ReleaseTravel_2Activity.TYPE_LINE);
            data.setTravelType(ConstantValue.TravelType.PASSENGER);
            return data;
        }

        public static ReleasePageEntity lineDriverData(){
            ReleasePageEntity data=emptyData();
            data.setType(ReleaseTravel_2Activity.TYPE_LINE);
            data.setTravelType(ConstantValue.TravelType.DRIVER);
            return data;
        }
    }
}
