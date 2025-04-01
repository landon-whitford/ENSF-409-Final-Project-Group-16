package edu.ucalgary.oop;

import java.sql.*;

public class DataBaseConnector {

    public static Connection createConnection(String databaseName){

        StringBuilder dBPath = new StringBuilder("jdbc:postgresql://localhost/");
        dBPath.append(databaseName);
        Connection dBConnect = null;

        try{
            dBConnect = DriverManager.getConnection(dBPath.toString(),"oop","ucalgary");
        } catch (SQLException e) {
            System.out.println("Query could not be completed: " + e.getMessage());
        }

        return dBConnect;
    }

}
