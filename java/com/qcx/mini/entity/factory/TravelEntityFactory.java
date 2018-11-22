package com.qcx.mini.entity.factory;

import com.qcx.mini.ConstantValue;
import com.qcx.mini.entity.TravelDetail_DriverEntity;
import com.qcx.mini.entity.TravelDetail_PassengerEntity;
import com.qcx.mini.entity.TravelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */

public class TravelEntityFactory {

    public static TravelEntity creatTravel(TravelDetail_DriverEntity driverTravel){
        if(driverTravel==null){
            return null;
        }
        TravelDetail_DriverEntity.Travel dTravel=driverTravel.getTravel();
        if(dTravel==null){
            return null;
        }
        TravelEntity travel=new TravelEntity();
        travel.setTravelId(dTravel.getTravelId());
        travel.setTravelPrice(dTravel.getTravelPrice());
        travel.setSurplusSeats(dTravel.getSurplusSeats());
        if(driverTravel.getPassengers()!=null){
            List<String> headPictures=new ArrayList<>();
            for(int i=0;i<driverTravel.getPassengers().size();i++){
                if(driverTravel.getPassengers().get(i)!=null){
                    for(int j=0;j<driverTravel.getPassengers().get(i).getBookSeats();j++){
                        headPictures.add(driverTravel.getPassengers().get(i).getPicture());
                    }
                }
            }
            travel.setHeadPictures(headPictures);
        }
        travel.setSeats(dTravel.getSeats());
        travel.setStatus(dTravel.getTravelStatus());
        travel.setStartTime(dTravel.getStartTime());
        travel.setType(ConstantValue.TravelType.DRIVER);
        travel.setEndAddress(dTravel.getEndAddress());
        travel.setStartAddress(dTravel.getStartAddress());
        travel.setEnd(dTravel.getEnd());
        travel.setStart(dTravel.getStart());


        return travel;
    }

    public static TravelEntity creatTravel(TravelDetail_PassengerEntity passengerTravel){
        if(passengerTravel==null){
            return null;
        }
        TravelDetail_PassengerEntity.Travel pTravel=passengerTravel.getTravel();
        if(pTravel==null){
            return null;
        }
        TravelEntity travel=new TravelEntity();
        travel.setOrdersId(pTravel.getOrdersId());
        travel.setTravelPrice(pTravel.getPrice());
        travel.setSurplusSeats(pTravel.getBookSeats());
        travel.setSeats(pTravel.getBookSeats());
        travel.setStatus(pTravel.getOrdersStatus());
        travel.setStartTime(pTravel.getStartTime());
        travel.setType(ConstantValue.TravelType.PASSENGER);
        travel.setEndAddress(pTravel.getDriverEndAddress());
        travel.setStartAddress(pTravel.getDriverStartAddress());
        travel.setEnd(pTravel.getDriverEnd());
        travel.setStart(pTravel.getDriverStart());
        return travel;
    }

}
