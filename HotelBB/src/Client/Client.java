/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class Client {

    protected BufferedReader console;
    protected ClientHOPPImpl clientHI;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Client client = new Client();
        client.loop();
    }

    public Client() {
        clientHI = null;
        try {
            clientHI = new ClientHOPPImpl();
        } catch (Exception e) {
            System.out.println("Eroor: " + e.getMessage());
            System.exit(1);
        }
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    private void loop() {

        boolean isDone = false;
        while (!isDone) {
            this.showMainMenu();
            String command = null;
            try {
                command = console.readLine();
                switch (command) {
                    case Constants.QUERY_FOR_CITIES:
                        this.getAllCities();

                        break;
                    case Constants.QUERY_HOTELS_BY_CITY:
                        String cityName = "";
                        cityName = console.readLine();
                        this.requestForHotelsInGivenCity(cityName);
                        break;
                    case Constants.CHECK_IF_ROOM_AVAILABLE:
                        System.out.println("client request to check availbility");
                        String roomMsg = "";
                        roomMsg = console.readLine();
                        this.requestForVacancyByGivenDates(roomMsg);
                        break;

                    case Constants.MAKE_A_BOOKING:
                        System.out.println("client request to make a booking");
                        String msgForBking = "";
                        msgForBking = console.readLine();
                        this.makeABooking(msgForBking);
                        break;

                    case Constants.FIND_ROOMS:
                        System.out.println("client request to make a booking");
                        String cityForFindRoom = "";
                        cityForFindRoom = console.readLine();
                        this.makeABooking(cityForFindRoom);
                        break;

                    case Constants.BYE:
                        isDone = true;
                        break;
                    default:
                        System.out.println("Wrong command");
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // 找到所有有关的城市
    public void getAllCities() {
        List<String> cities = this.clientHI.queryCity();
        System.out.println("city list length: " + cities.size());
        if (!cities.isEmpty()) {
            for (int i = 1; i <= cities.size(); i++) {
                System.out.println(i + ". " + cities.get(i - 1));
            }
        } else {
            System.out.println("No result.");
        }
    }

    public String[] getAllCitiesArray() {
        List<String> cities = this.clientHI.queryCity();
        System.out.println("city list length: " + cities.size());
        String[] citiesArray = new String[Constants.HOTEL_NUM];
        if (!cities.isEmpty()) {
            for (int i = 0; i < cities.size(); i++) {
                citiesArray[i] = cities.get(i);
            }
        }
        return citiesArray;
    }

    public void requestForHotelsInGivenCity(String cityName) {
        List<String> hotels = this.clientHI.queryHotelsByCity(cityName);
        System.out.println("Hotel list length: " + hotels.size());
        if (!hotels.isEmpty()) {
            for (int i = 1; i <= hotels.size(); i++) {
                String rate = "";
                switch (hotels.get(i - 1).toLowerCase()) {
                    case "hilton":
                        rate = "450";
                        break;
                    case "chervon":
                        rate = "300";
                        break;
                    case "regent":
                        rate = "400";
                        break;
                    case "windsor":
                        rate = "350";
                        break;
                    default:
                        break;
                }
                System.out.println(i + ". " + hotels.get(i - 1) + " $" + rate);
            }
        } else {
            System.out.println("No result.");
        }
    }

    // return the list of hotels in this certain city
    public String[] getHotelsListInCity(String cityName) {
        List<String> hotels = this.clientHI.queryHotelsByCity(cityName);
        String[] result = new String[Constants.HOTEL_NUM];
        System.out.println("Hotel list length: " + hotels.size());

        if (!hotels.isEmpty()) {
            for (int i = 0; i < hotels.size(); i++) {
                result[i] = hotels.get(i);
            }
            return result;
        } else {
            System.out.println("No result.");
            return result;
        }
    }

    public String[] getRooms(String hotelAndCity) {
        List<String> rooms = this.clientHI.getRooms(hotelAndCity);
        String[] result = new String[Constants.MAX_ROOM_NUM];
        System.out.println("Room list length: " + rooms.size());

        if (!rooms.isEmpty()) {
            for (int i = 0; i < rooms.size(); i++) {
                result[i] = rooms.get(i);
            }
            return result;
        } else {
            System.out.println("No result");
            return result;
        }
    }

    public void requestForVacancyByGivenDates(String msg) {
        boolean isAvailable = this.clientHI.checkIfRoomAvailable(msg);

        if (isAvailable) {

            System.out.println("Available");
        } else {
            System.out.println("Not Available");
        }
    }

    public boolean checkIfRoomAvailable(String msg) {
        return this.clientHI.checkIfRoomAvailable(msg);
    }

    
    // get room rate
    public String getRoomRate(String roomID) {
        String rate = this.clientHI.getRoomRate(roomID);

        return rate;
    }
    
    // make a booking
    public void makeABooking(String msg) {
        boolean isMade = this.clientHI.makeABooking(msg);
        if (isMade) {
            System.out.println(Constants.BOOKING_SUCCESSFULLY);
        } else {
            System.out.println(Constants.BOOKING_NOT_MADE);
        }
    }

    // make a booking
    public boolean makeABookingWithFeedback(String msg) {
        boolean isMade = this.clientHI.makeABooking(msg);
        if (isMade) {
            System.out.println(Constants.BOOKING_SUCCESSFULLY);
            return true;
        } else {
            System.out.println(Constants.BOOKING_NOT_MADE);
            return false;
        }
    }

    public void showMainMenu() {
        System.out.println("Your option: ");
    }
}
