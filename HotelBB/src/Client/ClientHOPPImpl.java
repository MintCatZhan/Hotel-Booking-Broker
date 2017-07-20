/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

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
public class ClientHOPPImpl{

    public static void main(String[] args) {
    }

    private Socket clientSocket;
    private InetAddress addr;
    private BufferedReader reader;
    private PrintStream writer;

    public ClientHOPPImpl() {
        try {
            addr = InetAddress.getLocalHost();
            clientSocket = new Socket(addr, Constants.PORT);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintStream(clientSocket.getOutputStream());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientHOPPImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientHOPPImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public List<String> queryCity() {
        writer.print(Constants.QUERY_FOR_CITIES + Constants.CR_LF);
        List<String> cities = new ArrayList<>();
        String str;
        try {
            while (true) {
                str = reader.readLine();
                if (str.equals(Constants.END_OF_MESSAGE)) {
                    break;
                }
                cities.add(str);
            }
            return cities;
        } catch (IOException e) {
            return null;
        }
    }

    
    public List<String> queryHotelsByCity(String cityName) {
        writer.print(Constants.QUERY_HOTELS_BY_CITY + Constants.CR_LF);
        writer.print(cityName + Constants.CR_LF);
        List<String> hotels = new ArrayList<>();
        String str;
        try {
            while (true) {
                str = reader.readLine();
                if (str.equals(Constants.END_OF_MESSAGE)) {
                    break;
                }
                hotels.add(str);
            }
            return hotels;
        } catch (IOException e) {
            return null;
        }
    }

    // roomNum means the room id
    public boolean checkIfRoomAvailable(String msg) {
        boolean isAvailable = false;
        writer.print(Constants.CHECK_IF_ROOM_AVAILABLE + Constants.CR_LF);
        writer.print(msg + Constants.CR_LF);
        String str;
        try {
            str = reader.readLine();
            if (str.equals(Constants.ROOM_IS_AVAILABLE)) {
                isAvailable = true;
                System.out.println("true -- > Available");
            }
        } catch (IOException e) {
            return false;
        }
        return isAvailable;
    }

    
    public boolean makeABooking(String msg) {
        writer.print(Constants.MAKE_A_BOOKING + Constants.CR_LF);
        writer.print(msg + Constants.CR_LF);
        String str;
        try {
            str = reader.readLine();
            if (str.equals(Constants.BOOKING_SUCCESSFULLY)) {
                return true;
            }
        } catch (IOException e) {
        }
        return false;
    }

    public List<String> getRooms(String hotelAndCity){
        writer.print(Constants.FIND_ROOMS + Constants.CR_LF);
        writer.print(hotelAndCity + Constants.CR_LF);
        List<String> rooms = new ArrayList<>();
        String str;
        try {
            while (true) {
                str = reader.readLine();
                if (str.equals(Constants.END_OF_MESSAGE)) {
                    break;
                }
                rooms.add(str);
            }
            return rooms;
        } catch (IOException e) {
            return null;
        }
    }
    
    // get room rate by room id
    public String getRoomRate(String roomID){
        writer.print(Constants.GET_ROOM_RATE + Constants.CR_LF);
        writer.print(roomID + Constants.CR_LF);
     
        String str;
        try{
            String rate = reader.readLine();
            return rate;
        }catch(IOException e){
            return null;
        }
    }
}
