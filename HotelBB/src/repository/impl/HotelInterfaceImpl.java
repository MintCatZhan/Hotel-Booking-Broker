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
public class HotelInterfaceImpl{

    Connection connection;
    String hotelName;

    public HotelInterfaceImpl(String hotelName) {
        getDBconnection(hotelName);
        this.hotelName = hotelName;
    }

    private void getDBconnection(String hotelName) {
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
            connection = DriverManager.getConnection(dbConnection, Constants.DB_USER, Constants.DB_PWD);
            System.out.println("connected to hotel database --> " + hotelName);
        } catch (SQLException e) {
            System.out.println("Fail to Connect to hotel Database");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HotelInterfaceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> queryCityList(String hotelName) {
        // TODO Auto-generated method stub
        if (connection == null) {
            System.out.println("Connection is not established");
            return null;
        } else {
            List<String> cities = new ArrayList<>();
            String querystring = "select distinct CITY from hotel";
            try {
                PreparedStatement statement = connection.prepareStatement(querystring);
                ResultSet results = statement.executeQuery();
                while (results.next()) {
                    cities.add(results.getString(1));
                }
                //close
                results.close();
                statement.close();
                return cities;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    
    public boolean queryHotelsList(String cityName) {
        
        if (connection == null) {
            System.out.println("Connection is not established");
            return false;
        } else {
            boolean isExist = false;
            String querystring = "SELECT hotel.hotel_id FROM hotel WHERE hotel.city = '" + cityName +"'";
            try {
                PreparedStatement statement = connection.prepareStatement(querystring);
                ResultSet results = statement.executeQuery();
                if (Constants.getSizeOfResultSet(results) > 0){
                    System.out.println("This hotel exist in city -->" + cityName);
                    isExist = true;
                }
                //close
                results.close();
                statement.close();
                return isExist;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    // get hotel name
    public String getHotelName(){
        return this.hotelName;
    }
    
    //get hotel id by city and hotelName
    public String getHotelMsg(String msg){
        String hotelMsg = "";
        if (connection == null) {
            System.out.println("Connection is not established");
            return null;
        } else {
            String[] hotelAndCityArray = msg.split("_");
            String querystring = "SELECT hotel.hotel_id FROM hotel WHERE hotel.city = '" + hotelAndCityArray[2] +"'";
            try {
                PreparedStatement statement = connection.prepareStatement(querystring);
                ResultSet results = statement.executeQuery();
                if (results.next()){
                    hotelMsg = hotelAndCityArray[1]+ "_" + results.getString(1);
                    System.out.println("This id of this hotel is -->" + results.getString(1));
                }
                //close
                results.close();
                statement.close();
                return hotelMsg;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    
}
