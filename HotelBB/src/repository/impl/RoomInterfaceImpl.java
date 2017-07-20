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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Constants;

/**
 *
 * @author YuanZhan
 */
public class RoomInterfaceImpl {

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

    public List<String> getRooms(String hotelNameAndID) {
        try {
            List<String> rooms = new ArrayList<>();
            String[] msg = hotelNameAndID.split("_");
            System.out.println("input vaule of hotelname and hotel id" + hotelNameAndID);
            Connection conn = null;
            conn = this.getDBconnection(conn, msg[0]);
            String querystring = "SELECT ROOM_ID FROM room WHERE room.HOTEL_ID = '" + msg[1] + "'";
            System.out.println(querystring);
            PreparedStatement stmt = conn.prepareStatement(querystring);
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                rooms.add(results.getString(1));
            }
            System.out.println("room number: " + rooms.size());
            //close
            results.close();
            stmt.close();
            return rooms;
        } catch (SQLException ex) {
            Logger.getLogger(RoomInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getRoomRate(String roomMsg) {
        try {
            String[] roomMsgArray = roomMsg.split("_");
            Connection conn = null;
            conn = this.getDBconnection(conn, roomMsgArray[0]);
            String querystring = "SELECT rate FROM room WHERE room.room_id = '" + roomMsgArray[1] + "'";
            System.out.println(querystring);
            PreparedStatement stmt = conn.prepareStatement(querystring);
            ResultSet results = stmt.executeQuery();
            String roomRate = "";
            while (results.next()) {
                roomRate = results.getString(1);
            }
            //close
            results.close();
            stmt.close();
            return roomRate;
        } catch (SQLException ex) {
            Logger.getLogger(RoomInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
