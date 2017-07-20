/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.List;
import repository.impl.*;

/**
 *
 * @author YuanZhan
 */
public class ServerHOPP {

    private String hotelName;
    private RoomInterfaceImpl rii;
    private BookingIntefaceImpl bii;
    private HotelInterfaceImpl hii;
    private CustomerInterfaceImpl cii;

    public static void main(String[] args) {
    }

    public ServerHOPP(String hotelName) {
        rii = new RoomInterfaceImpl();
        bii = new BookingIntefaceImpl();
        hii = new HotelInterfaceImpl(hotelName);
        cii = new CustomerInterfaceImpl();
        this.hotelName = hotelName;
    }

    public List<String> queryCity() {
        // TODO Auto-generated method stub
        System.out.println("ServerHOPP Running! Querying the CityList from table hotel-->" + this.hotelName);
        List<String> cities = hii.queryCityList(this.hotelName);
        return cities;
    }

    public List<String> queryHotelsByCity(String cityName){
        System.out.println("ServerHOPP Running! Querying the hotels from all hotels table " + cityName);
        List<String> hotels = new ArrayList<>();
        if (hii.queryHotelsList(cityName)){
            System.out.println(this.hii.getHotelName() + " is exist in "+cityName);
            hotels.add(this.hii.getHotelName());
        } else {
            System.out.println(this.hii.getHotelName() + " is NOT exist in " + cityName);
        }
        return hotels;
    }
    
    public boolean checkIfRoomAvailable(String msg){
        System.out.println("ServerHOPP Running! Checking if room available " + msg);
        boolean isAvailable = bii.checkIfRoomAvailable(msg);
        return isAvailable;
    }

    public boolean makeABooking(String msg){
        System.out.println("ServerHOPP Running! Make a booking " + msg);
        boolean isMade = bii.makeABooking(msg);
        return isMade;
    }
    
    public List<String> findRoomsOfHotel(String hotelAndCityForFindRoom){
        System.out.println("ServerHOPP Running! Querying the rooms of certain hotel " + this.getHotelMsg(hotelAndCityForFindRoom));
        List<String> rooms = rii.getRooms(this.getHotelMsg(hotelAndCityForFindRoom));
        return rooms;
    }
    
    public String getRoomRate(String roomMsg){
        System.out.println("ServerHOPP Running! get room rate " + roomMsg);
        String rateStr = rii.getRoomRate(roomMsg);
        return rateStr;
    }
    
    // get hotel ID
    private String getHotelMsg(String msg){
        String hotelID = "";
        hotelID = this.hii.getHotelMsg(msg);
        return hotelID;
    }
}
