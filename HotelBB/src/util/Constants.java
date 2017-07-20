/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import repository.impl.HotelInterfaceImpl;

/**
 *
 * @author YuanZhan
 */
public class Constants {

    public static final int PORT = 22221;
    public static final String CR_LF = "\r\n";
    public static final String DB_CONNECTION = ""; // what is the java derby connectionname? it changes every time

    public static final String DB_USER = "hbb";
    public static final String DB_PWD = "hbb";
    public static final int SERVER_NUM = 3;
    public static final String QUIT = "quit";
    public static final int HOTEL_NUM = 3;
    public static final int MAX_ROOM_NUM = 10;

    // Protocols
    public static final String END_OF_MESSAGE = "END";
    public static final String BYE = "bye";
    public static final String QUERY_FOR_CITIES = "query_cities_list";
    public static final String QUERY_HOTELS_BY_CITY = "query_hotels_of_city";
    public static final String CHECK_IF_ROOM_AVAILABLE = "check_if_room_available";
    public static final String MAKE_A_BOOKING = "make_a_booking";
    public static final String FIND_ROOMS = "find_rooms";
    public static final String GET_ROOM_RATE = "get_room_rate";
    
    public static final String BOOKING_SUCCESSFULLY = "Booking successfully";
    public static final String BOOKING_NOT_MADE = "Booking not made";
    public static final String ROOM_IS_AVAILABLE = "Available";
    public static final String ROOM_IS_NOT_AVAILABLE = "Not Available";

    public static int getSizeOfResultSet(ResultSet rs) {
        try {
            int size = 0;
            while (rs.next()) {
                size++;
            }
            return size;
        } catch (SQLException ex) {
            Logger.getLogger(HotelInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1; // if error occurs
        }
    }

    public static int convertMMtoNum(String month) {
        int monthNum = 0;
        switch (month) {
            case "January":
                monthNum = 1;
                break;
            case "February":
                monthNum = 2;
                break;
            case "March":
                monthNum = 3;
                break;
            case "April":
                monthNum = 4;
                break;
            case "May":
                monthNum = 5;
                break;
            case "June":
                monthNum = 6;
                break;
            case "July":
                monthNum = 7;
                break;
            case "August":
                monthNum = 8;
                break;
            case "September":
                monthNum = 9;
                break;
            case "October":
                monthNum = 10;
                break;
            case "November":
                monthNum = 11;
                break;
            case "December":
                monthNum = 12;
                break;
        }
        return monthNum;
    }

    public static boolean checkIfisCreditCardNumber(String ccn) {
        // ccn start with no-zero, its length is 16
        Pattern testPattern = Pattern.compile("^[1-9][0-9]{15}");
        Matcher teststring = testPattern.matcher(ccn);
        return teststring.matches();
    }
    
    public static boolean checkIfisEmailAddr(String addr){
        Pattern testPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher teststring = testPattern.matcher(addr);
        return teststring.matches();
    }
}
