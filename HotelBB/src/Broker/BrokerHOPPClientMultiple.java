/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Broker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class BrokerHOPPClientMultiple {

    public static void main(String[] args) {
    }

    private final BrokerHOPPClient[] brokerhopps;

    public BrokerHOPPClientMultiple() {
        brokerhopps = new BrokerHOPPClient[Constants.HOTEL_NUM]; // for test  here is 1
        for (int i = 0; i < Constants.HOTEL_NUM; i++) {
            brokerhopps[i] = new BrokerHOPPClient(i + 1);
            System.out.println("done");
        }
    }

    public List<String> queryCity() {
        // TODO Auto-generated method stub
        List<String> cities = new ArrayList<>();
        for (int i = 0; i < Constants.HOTEL_NUM; i++) {
            cities.addAll(brokerhopps[i].queryCity());
        }
//        cities.addAll(brokerhopps[0].queryCity());
        // delete duplicated records
        HashSet<String> hash = new HashSet<>(cities);
        cities.clear();
        cities.addAll(hash);
        return cities;
    }

    public List<String> queryHotelByCity(String cityName) {
        List<String> hotels = new ArrayList<>();
        for (int i = 0; i < Constants.HOTEL_NUM; ++i) {
            hotels.addAll(brokerhopps[i].queryHotelByCity(cityName));
        }
//        hotels.addAll(brokerhopps[0].queryHotelByCity(cityName));
        // delete duplicated records
        HashSet<String> hash = new HashSet<>(hotels);
        hotels.clear();
        hotels.addAll(hash);
        return hotels;
    }

    // check if room available
    public boolean checkIfRoomAvailable(String msg) {
        System.out.println("broker received : " + msg);
        boolean isAvailable = false;

        //cityName + "_|_" + hotelName + "_|_" + checkInDate + "_|_" + checkOutDate + "_|_" + roomID
        String[] msgArray = msg.split("_");
        switch (msgArray[1].toLowerCase()) {
            case "hilton":
                isAvailable = brokerhopps[0].checkIfRoomAvailable(msg);
                break;
            case "windsor":
                isAvailable = brokerhopps[1].checkIfRoomAvailable(msg);
                break;
            case "regent":
                isAvailable = brokerhopps[2].checkIfRoomAvailable(msg);
                break;
            default:
                break;
        }
        return isAvailable;
    }
    
    // make a booking
    //hotelName + "_|_" + custID + "_|_" + fname + "_|_" + lname + "_|_" + email
   //+ "_|_" + ccNum + "_|_" + roomID + "_|_" + checkInDate + "_|_" + checkOutDate;
    public boolean makeABooking(String msg){
        System.out.println("broker received : " + msg);
        boolean isMade = false;
        //hotelName + "_|_" + custID + "_|_" + fname + "_|_" + lname + "_|_" + email
        //+ "_|_" + ccNum + "_|_" + roomID + "_|_" + checkInDate + "_|_" + checkOutDate;
        String[] msgArray = msg.split("_");
        switch (msgArray[0].toLowerCase()) {
            case "hilton":
                isMade = brokerhopps[0].makeABooking(msg);
                break;
            case "windsor":
                isMade = brokerhopps[1].makeABooking(msg);
                break;
            case "regent":
                isMade = brokerhopps[2].makeABooking(msg);
                break;
            default:
                break;
        }
        return isMade;
    }
    
    // get room ids
    public List<String> getRooms(String hotelAndCityForFindRoom){
        System.out.println("broker received : " + hotelAndCityForFindRoom);
        List<String> rooms = new ArrayList<>();
        
        String[] msgArray = hotelAndCityForFindRoom.split("_");
        switch (msgArray[0].toLowerCase()) {
            case "hilton":
                rooms.addAll(brokerhopps[0].getRooms("hilton_" + hotelAndCityForFindRoom));
                break;
            case "windsor":
                rooms.addAll(brokerhopps[1].getRooms("windsor_" + hotelAndCityForFindRoom));
                break;
            case "regent":
                rooms.addAll(brokerhopps[2].getRooms("regent_" + hotelAndCityForFindRoom));
                break;
            default:
                break;
        }
//        cities.addAll(brokerhopps[0].queryCity());
        // delete duplicated records
        HashSet<String> hash = new HashSet<>(rooms);
        rooms.clear();
        rooms.addAll(hash);
        return rooms;
    }
    
    // get room rate
    public String getRoomRate(String msg){ // msg is : hotelName_roomid
        System.out.println("broker received : " + msg);
        String rateStr = "";
        String[] msgArray = msg.split("_");
        switch (msgArray[0].toLowerCase()) {
            case "hilton":
                rateStr = brokerhopps[0].getRoomRate(msg);
                break;
            case "windsor":
                rateStr = brokerhopps[1].getRoomRate(msg);
                break;
            case "regent":
                rateStr = brokerhopps[2].getRoomRate(msg);
                break;
            default:
                break;
        }
        return rateStr;
    }
}
