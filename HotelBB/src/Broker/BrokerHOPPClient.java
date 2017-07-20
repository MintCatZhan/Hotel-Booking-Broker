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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class BrokerHOPPClient {

    private int brokerHOPPPort;
    private InetAddress brokerAddr = null;
    private Socket brokerSocket = null;
    private BufferedReader brokerReader = null;
    private PrintStream brokerWriter = null;

    public BrokerHOPPClient(int serverNO) {
        this.brokerHOPPPort = Constants.PORT + serverNO;
        System.out.println("this port num is" + brokerHOPPPort);
        try {
            brokerAddr = InetAddress.getLocalHost();
            brokerSocket = new Socket(brokerAddr, brokerHOPPPort);
            brokerReader = new BufferedReader(new InputStreamReader(brokerSocket.getInputStream()));
            brokerWriter = new PrintStream(brokerSocket.getOutputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("BrokerHOPP: UnKnownHost!");
        } catch (IOException ex) {
            Logger.getLogger(BrokerHOPPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> queryCity() {
        List<String> cities = new ArrayList<>();
        brokerWriter.print(Constants.QUERY_FOR_CITIES + Constants.CR_LF);
        try {
            String buffer;
            while (true) {
                buffer = brokerReader.readLine();
                if (buffer.equals(Constants.END_OF_MESSAGE)) {
                    break;
                }
                cities.add(buffer);
            }
            return cities;
        } catch (IOException e) {
            return null;
        }
    }
    
    public List<String> getRooms(String hotelAndCityForFindRoom){
        List<String> rooms = new ArrayList<>();
        brokerWriter.print(Constants.FIND_ROOMS + Constants.CR_LF);
        brokerWriter.print(hotelAndCityForFindRoom + Constants.CR_LF);
        try{
            String buffer;
            while (true) {
                buffer = brokerReader.readLine();
                if (buffer.equals(Constants.END_OF_MESSAGE)) {
                    break;
                }
                rooms.add(buffer);
            }
            return rooms;
        } catch (IOException e) {
            return null;
        }
    }
    
    // get room rate
    public String getRoomRate(String msg){
        brokerWriter.print(Constants.GET_ROOM_RATE + Constants.CR_LF);
        brokerWriter.print(msg + Constants.CR_LF);
        try{
            String rateStr = brokerReader.readLine();
            return rateStr;
        }catch(IOException e){
            return null;
        }
    }

    // query hotels of the certain city
    public List<String> queryHotelByCity(String cityName) {
        List<String> hotels = new ArrayList<>();
        brokerWriter.print(Constants.QUERY_HOTELS_BY_CITY + Constants.CR_LF);
        brokerWriter.print(cityName + Constants.CR_LF);
        try {
            String buffer;
            while (true) {
                buffer = brokerReader.readLine();
                if (buffer.equals(Constants.END_OF_MESSAGE)) {
                    break;
                }
                hotels.add(buffer);
            }
            return hotels;
        } catch (IOException e) {
            return null;
        }
    }

    //check if room available
    public boolean checkIfRoomAvailable(String msg) {
        try {
            brokerWriter.print(Constants.CHECK_IF_ROOM_AVAILABLE + Constants.CR_LF);
            brokerWriter.print(msg + Constants.CR_LF);
            String buffer;
            buffer = brokerReader.readLine();
            System.out.println(buffer);
            if (buffer.equals(Constants.ROOM_IS_AVAILABLE)){
                System.out.println("true -- > Available");
                return true;
            } else {
                System.out.println("false -- > Not Available");
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(BrokerHOPPClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    // make a booking
    public boolean makeABooking(String msg){
        try {
            brokerWriter.print(Constants.MAKE_A_BOOKING + Constants.CR_LF);
            brokerWriter.print(msg + Constants.CR_LF);
            String buffer;
            buffer = brokerReader.readLine();
            return buffer.equals(Constants.BOOKING_SUCCESSFULLY);
        } catch (IOException ex) {
            Logger.getLogger(BrokerHOPPClient.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
