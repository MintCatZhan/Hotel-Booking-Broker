/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class Server {

    public static void start(String hotelBrand) {
        // TODO Auto-generated method stub
        int hotelCode = 0;

        String hotelName = hotelBrand.toLowerCase();
//      String hotelName = "windsor";
//      String hotelName = "hilton";
//        String hotelName = "regent";
        HashMap<String, Integer> hotelBrands = new HashMap<>();
        hotelBrands.put("hilton", 1);
        hotelBrands.put("windsor", 2);
        hotelBrands.put("regent", 3);

        hotelCode = hotelBrands.get(hotelName);

        if (hotelCode == 0) {
            System.out.println("Server: Please choose the right Server Name!");
        } else {
            int PORT = Constants.PORT + hotelCode;
            System.out.println("The port num for this server:" + PORT);
//int PORT = Constants.PORT + 1; // here for test
            ServerSocket serversocket = null;
            try {
                serversocket = new ServerSocket(PORT);
            } catch (IOException e) {
                System.out.println(e);
                return;
            }
            System.out.println("Server: The Server " + hotelName + " is now running!");
            while (true) {
                Socket incoming = null;
                try {
                    incoming = serversocket.accept();
                } catch (IOException e) {
                    System.out.println(e);
                    continue;
                }
                new SocketHandler(incoming, hotelName).start();
            }
        }
    }
}

class SocketHandler extends Thread {

    Socket incoming;
    ServerHOPP serverhoop;
    BufferedReader serverReader;
    PrintStream serverWriter;
    String hotelName;

    public SocketHandler(Socket incoming, String hotelName) {
        // TODO Auto-generated constructor stub
        this.incoming = incoming;
        this.hotelName = hotelName;
        this.serverhoop = new ServerHOPP(hotelName);
    }

    public void run() {
        try {
            serverReader = new BufferedReader(new InputStreamReader(this.incoming.getInputStream()));
            serverWriter = new PrintStream(this.incoming.getOutputStream());

            String buffer = "";
            boolean isDone = false;
            while (!isDone) {
                buffer = serverReader.readLine();
                switch (buffer) {
                    case Constants.QUERY_FOR_CITIES:
                        this.queryCitysList();
                        break;
                    case Constants.QUERY_HOTELS_BY_CITY:
                        String cityName = "";
                        cityName = serverReader.readLine();
                        this.queryHotelByCityName(cityName);
                        break;

                    case Constants.CHECK_IF_ROOM_AVAILABLE:
                        String msg = "";
                        msg = serverReader.readLine();
                        this.checkIfRoomAvailable(msg);
                        break;

                    case Constants.MAKE_A_BOOKING:
                        String msgForBooking = "";
                        msgForBooking = serverReader.readLine();
                        this.makeABooking(msgForBooking);
                        break;
                    case Constants.FIND_ROOMS:
                        String hotelAndCityForFindRoom = "";
                        hotelAndCityForFindRoom = serverReader.readLine();
                        this.getRoomsOfHotel(hotelAndCityForFindRoom);
                        break;
                        
                    case Constants.GET_ROOM_RATE:
                        String roomMsg = "";
                        roomMsg = serverReader.readLine();
                        this.getRoomRate(roomMsg);
                        break;

                    case Constants.BYE: // need to be added something here
                        incoming.close();
                        serverReader.close();
                        serverWriter.close();
                        break;
                    default:
                        System.out.println("Server: get wrong requests.");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void queryCitysList() {
        System.out.println("Server: The brokerHOPP sent a query city request!");
        List<String> cities = new ArrayList<>();
        cities = this.serverhoop.queryCity();
        if (cities.isEmpty()) {
            System.out.println("No cities in the server!");
        }
//        System.out.println("Server: Cities you have requested!");
        for (String city : cities) {
//            System.out.println(city);
            serverWriter.print(city + Constants.CR_LF);
        }
        serverWriter.print(Constants.END_OF_MESSAGE + Constants.CR_LF);
    }

    // query hotels List in this city
    public void queryHotelByCityName(String cityName) {
        System.out.println("Server: The brokerHOPP sent a query hotel request!" + cityName);
        List<String> hotels = new ArrayList<>();
        hotels = this.serverhoop.queryHotelsByCity(cityName);
        if (hotels.isEmpty()) {
            System.out.println("No hotels in the server!");
        }
//        System.out.println("Server: Cities you have requested!");
        for (String hotel : hotels) {
//            System.out.println(city);
            serverWriter.print(hotel + Constants.CR_LF);
        }
        serverWriter.print(Constants.END_OF_MESSAGE + Constants.CR_LF);
    }

    // check if a room is available
    public void checkIfRoomAvailable(String msg) {
        System.out.println("Server: The brokerHOPP request to check if room available!" + msg);
        String isAvailable;
        boolean isA = this.serverhoop.checkIfRoomAvailable(msg);
        if (isA) {
            System.out.println("true -- > Available");
            isAvailable = Constants.ROOM_IS_AVAILABLE;
        } else {
            System.out.println("false -- >Not Available");
            isAvailable = Constants.ROOM_IS_NOT_AVAILABLE;
        }
        serverWriter.print(isAvailable + Constants.CR_LF);
    }

    // make a booking
    public void makeABooking(String msg) {
        System.out.println("Server: The brokerHOPP request to make a booking!" + msg);
        String isMade;
        if (this.serverhoop.makeABooking(msg)) {
            isMade = Constants.BOOKING_SUCCESSFULLY;
        } else {
            isMade = Constants.BOOKING_NOT_MADE;
        }
        serverWriter.print(isMade + Constants.CR_LF);
    }
    
    public void getRoomsOfHotel(String msg){
        System.out.println("Server: The brokerHOPP request to find rooms of certain hotel in city: " + msg);
        List<String> rooms = new ArrayList<>();
        rooms = this.serverhoop.findRoomsOfHotel(msg);
        if (rooms.isEmpty()) {
            System.out.println("No rooms in this hotelof city!");
        }
        for (String room : rooms) {
            serverWriter.print(room + Constants.CR_LF);
        }
        serverWriter.print(Constants.END_OF_MESSAGE + Constants.CR_LF);
    }
    
    public void getRoomRate(String roomMsg){
        System.out.println("Server: The brokerHOPP request to get room rate: " + roomMsg);
        String rateStr = this.serverhoop.getRoomRate(roomMsg);
        serverWriter.print(rateStr + Constants.CR_LF);
    }
}
