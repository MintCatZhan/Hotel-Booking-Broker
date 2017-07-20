/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class BookingIntefaceImpl {

//    Connection bkConn;
    private Connection getDBconnection(Connection conn, String hotelName) {
        try {
            String dbConnection = "";
            Class.forName("org.apache.derby.jdbc.ClientDriver").getInterfaces();
//            System.out.println("Class.forname  done");
            switch (hotelName) {
                case "hilton":
                    dbConnection = "jdbc:derby://localhost:1527/HiltonDB";
                    break;
                case "chevron":
                    dbConnection = "jdbc:derby://localhost:1527/ChevronDB";
                    break;
                case "regent":
                    dbConnection = "jdbc:derby://localhost:1527/RegentDB";
                    break;
                case "windsor":
                    dbConnection = "jdbc:derby://localhost:1527/WindsorDB";
                    break;
            }
            System.out.println("getconnection");
            conn = DriverManager.getConnection(dbConnection, Constants.DB_USER, Constants.DB_PWD);
            System.out.println("connected to hotel database --> " + hotelName);
            return conn;
        } catch (SQLException e) {
            System.out.println("Fail to Connect to hotel Database");
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HotelInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean checkIfRoomAvailable(String msg) {
        // msg syntax: cityName + "_|_" + hotelName + "_|_" + checkInDate + "_|_" + checkOutDate + "_|_" + roomID
        String[] msgArray = msg.split("_");

        try {
            Connection conn = null;
            conn = this.getDBconnection(conn, msgArray[1]);
            String inDate = msgArray[2];
            String outDate = msgArray[3];
            String roomID = msgArray[4];
            String querystring = "SELECT room_id FROM  booking"
                    + " WHERE (room_id = '" + roomID + "')"
                    + " AND (room_id IN ("
                    + "SELECT room_id FROM booking WHERE"
                    + " ((check_in_date >= '" + inDate
                    + "' AND check_in_date < '" + outDate
                    + "' OR (check_out_date >= '" + inDate
                    + "' AND check_out_date < '" + outDate
                    + "')"
                    + ")"
                    + ")"
                    + ")"
                    + ")";

            System.out.println(querystring);
            PreparedStatement statement = conn.prepareStatement(querystring);
            ResultSet results = statement.executeQuery();
            int size = Constants.getSizeOfResultSet(results);

            results.close();
            statement.close();
            conn.close();

            if (size == 0) {
                System.out.println("impl true -- > Available");
            }

            return size == 0;

        } catch (SQLException ex) {
            Logger.getLogger(BookingIntefaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean makeABooking(String msg) {
        String[] msgArray = msg.split("_");
//        boolean isBooked = false;
        try {
            Connection conn = null;
            conn = this.getDBconnection(conn, msgArray[0]);
            String custID = msgArray[1];
            String fname = msgArray[2];
            String lname = msgArray[3];
            String email = msgArray[4];
            String ccNum = msgArray[5];
            String roomID = msgArray[6];
            String inDate = msgArray[7];
            String outDate = msgArray[8];
            String bookingID = msgArray[9];

            // check if the customer is existing in the customer table
            String checkCustSQL = "SELECT CUSTOMER_ID FROM customer WHERE CUSTOMER_ID = '" + custID + "'";
            System.out.println("Check if customer exists --> " + checkCustSQL);
            PreparedStatement checkCustStmt = conn.prepareStatement(checkCustSQL);
            ResultSet custResult = checkCustStmt.executeQuery();
            int size = Constants.getSizeOfResultSet(custResult);
            System.out.println("size of result -->" + size);
            // if no record found, insert this record, else, do nothing
            if (size == 0) {
                String insertSQLForCust = "INSERT INTO customer VALUES('"
                        + custID + "','"
                        + fname + "','"
                        + lname + "','"
                        + email + "','"
                        + ccNum + "')";
                System.out.println(insertSQLForCust);

                Statement custStmt = conn.createStatement();
                custStmt.executeUpdate(insertSQLForCust);
                System.out.println("Customer Record has been added");
                custStmt.close();
            }

            String insertSQLForBking = "INSERT INTO booking VALUES ("
                    + bookingID + ",'"
                    + custID + "','"
                    + roomID + "','"
                    + inDate + "','"
                    + outDate
                    + "')";
            System.out.println(insertSQLForBking);

            Statement bkingStmt = conn.createStatement();
            bkingStmt.executeUpdate(insertSQLForBking);
            System.out.println("Booking Record has been added");

            bkingStmt.close();
            conn.close();

            return true;
//            isBooked = true;

        } catch (SQLException ex) {
            Logger.getLogger(BookingIntefaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
