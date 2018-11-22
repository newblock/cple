package com.qcx.mini.message;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by zsp on 2017/9/18.
 */

@MessageTag(value = "QU:travel", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class TravelMessage extends MessageContent {

    private String startAddress;//行程起点
    private String endAddress;//行程终点
    private long startTime;//行程开始时间
    private String travelID;//行程ID
    private int travelType;//行程类型
    private String userID;//消息发送者的phone



    /**
     * 发消息时调用，将自定义消息对象序列化为消息数据:
     * 首先将消息属性封装成json，再将json转换成byte数组
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("startAddress", getStartAddress());
            jsonObject.put("endAddress", getEndAddress());
            jsonObject.put("startTime", getStartTime());
            jsonObject.put("travelID", getTravelID());
            jsonObject.put("userID", getUserID());
            jsonObject.put("travelType", getTravelType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private TravelMessage() {

    }

    /**
     * 将收到的消息进行解析，byte -> json,再将json中的内容取出赋值给消息属性
     */
    public TravelMessage(byte[] data) {
        String jsonString = null;

        try {
            jsonString = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("startAddress"))
                setStartAddress(jsonObject.optString("startAddress"));
            if (jsonObject.has("endAddress"))
                setEndAddress(jsonObject.optString("endAddress"));
            if (jsonObject.has("startTime"))
                setStartTime(jsonObject.optLong("startTime"));
            if (jsonObject.has("travelID"))
                setTravelID(jsonObject.optString("travelID"));
            if (jsonObject.has("userID"))
                setUserID(jsonObject.optString("userID"));
            if (jsonObject.has("travelType"))
                setTravelType(jsonObject.optInt("travelType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param in 通过初始化传入的Parcel，为消息属性赋值
     */
    private TravelMessage(Parcel in) {
        setStartAddress(ParcelUtils.readFromParcel(in));
        setEndAddress(ParcelUtils.readFromParcel(in));
        setStartTime(ParcelUtils.readLongFromParcel(in));
        setTravelID(ParcelUtils.readFromParcel(in));
        setUserID(ParcelUtils.readFromParcel(in));
        setTravelType(ParcelUtils.readIntFromParcel(in));
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理
     */
    public static final Creator<TravelMessage> CREATOR = new Creator<TravelMessage>() {
        @Override
        public TravelMessage createFromParcel(Parcel source) {
            return new TravelMessage(source);
        }

        @Override
        public TravelMessage[] newArray(int size) {
            return new TravelMessage[size];
        }
    };

    /**
     * 描述了包含在Parcelable对象排列信息中的特殊对象的类型
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入到外部提供的Parcel中
     * @param dest 对象被写入的Parcel
     * @param flags 对象如何被写入的附加标志
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest,startAddress);
        ParcelUtils.writeToParcel(dest,endAddress);
        ParcelUtils.writeToParcel(dest,startTime);
        ParcelUtils.writeToParcel(dest,travelID);
        ParcelUtils.writeToParcel(dest,userID);
        ParcelUtils.writeToParcel(dest,travelType);
    }

    public static TravelMessage obtain(@NonNull String startAddress, @NonNull String endAddress, long startTime, @NonNull String travelID, String userID,int travelType) {
        TravelMessage travelMessage = new TravelMessage();
        travelMessage.setStartAddress(startAddress);
        travelMessage.setEndAddress(endAddress);
        travelMessage.setStartTime(startTime);
        travelMessage.setTravelID(travelID);
        travelMessage.setUserID(userID);
        travelMessage.setTravelType(travelType);
        return travelMessage;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTravelID() {
        return travelID;
    }

    public void setTravelID(String travelID) {
        this.travelID = travelID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getTravelType() {
        return travelType;
    }

    public void setTravelType(int travelType) {
        this.travelType = travelType;
    }
}
