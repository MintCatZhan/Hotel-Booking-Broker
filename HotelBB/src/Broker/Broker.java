/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Broker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class Broker {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            return;
        }
        System.out.println("BROKER RUNNING");
        while (true) {
            Socket incoming = null;
            try {
                incoming = serverSocket.accept();
            } catch (IOException e) {
                //e.printStackTrace();
                return;
            }
            new ClientSocketHandler(incoming).start();
        }
    }
}

class ClientSocketHandler extends Thread {

    Socket incoming;
    BufferedReader reader;
    PrintStream writer;

    BrokerHOPPClientMultiple brokerHOPPCM;

    ClientSocketHandler(Socket incoming) {
        this.incoming = incoming;
        brokerHOPPCM = new BrokerHOPPClientMultiple();
    }

    public void run() {
        System.out.println("Broker Running");
        String buffer = "";

        try {
            reader = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            writer = new PrintStream(incoming.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        Boolean isDone = false;
        while (!isDone) {
            try {
                buffer = reader.readLine();

                switch (buffer) {
                    case Constants.QUERY_FOR_CITIES:
                        this.queryForCitiesByBroker();
                        break;
                    case Constants.QUERY_HOTELS_BY_CITY:
                        String cityName = "";
                        cityName = reader.readLine();
                        this.queryHotelsByCity(cityName);
                        break;

                    case Constants.CHECK_IF_ROOM_AVAILABLE:
                        String msg = "";
                        msg = reader.readLine();
                        this.checkIfRoomAvailable(msg);
                        break;

                    case Constants.MAKE_A_BOOKING:
                        String msgForBooking = "";
                        msgForBooking = reader.readLine();
                        this.makeABooking(msgForBooking);
                        break;

                    case Constants.FIND_ROOMS:
                        String hotelAndCityForFindRoom = "";
                        hotelAndCityForFindRoom = reader.readLine();
                        this.getRooms(hotelAndCityForFindRoom);
                        break;
                       
                    case Constants.GET_ROOM_RATE:
                        String roomID = "";
                        roomID = reader.readLine();
                        this.getRoomRate(roomID);
                        break;

                    case Constants.BYE:
                        reader.close();
                        writer.close();
                        incoming.close();
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void queryForCitiesByBroker() {
        System.out.println("Broker: Request the Cities serviced by the system!");
        List<String> cities = brokerHOPPCM.queryCity();
        if (cities.isEmpty()) {
            System.out.println("Broker: NO CITIES");
        }
        for (String city : cities) {
            writer.print(city + Constants.CR_LF);
        }
        writer.print(Constants.END_OF_MESSAGE + Constants.CR_LF);
    }

    // query for hotels of certain city by broker
    public void queryHotelsByCity(String cityName) {
        System.out.println("Broker: Request the Hotels of certain city!" + cityName);
        List<String> hotels = brokerHOPPCM.queryHotelByCity(cityName);
        if (hotels.isEmpty()) {
            System.out.println("Broker: NO Hotels");
        }
        for (String city : hotels) {
            writer.print(city + Constants.CR_LF);
        }
        writer.print(Constants.END_OF_MESSAGE + Constants.CR_LF);
    }

    // check if a given room is available
    public void checkIfRoomAvailable(String msg) {
        System.out.println("Broker: Request to check if room is available!" + msg);
        String isAvailable = Constants.ROOM_IS_NOT_AVAILABLE;
        if (brokerHOPPCM.checkIfRoomAvailable(msg)) {
            isAvailable = Constants.ROOM_IS_AVAILABLE;
        }
        writer.print(isAvailable + Constants.CR_LF);
    }

    //make a booking
    public void makeABooking(String msg) {
        System.out.println("Broker: Request to make a booking!" + msg);
        String isMade = Constants.BOOKING_NOT_MADE;
        if (brokerHOPPCM.makeABooking(msg)) {
            isMade = Constants.BOOKING_SUCCESSFULLY;
        }
        writer.print(isMade + Constants.CR_LF);
    }
    
    //get rooms id
    public void getRooms(String hotelAndCityForFindRoom){
        System.out.println("Broker: Request to find rooms' ids!" + hotelAndCityForFindRoom);
        List<String> rooms = brokerHOPPCM.getRooms(hotelAndCityForFindRoom);
        if (rooms.isEmpty()) {
            System.out.println("Broker: NO Rooms");
        }
        for (String room : rooms) {
            writer.print(room + Constants.CR_LF);
        }
        writer.print(Constants.END_OF_MESSAGE + Constants.CR_LF);
    }
    
    // get room rate
    public void getRoomRate(String msg){
        System.out.println("Broker: Request to get room rate!" + msg);
        String rateStr = brokerHOPPCM.getRoomRate(msg);
        writer.print(rateStr + Constants.CR_LF);
    }
}
