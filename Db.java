package com.example.test3;

import java.sql.*;
public class Db {
    static String user ="root";
    static String pass="hammer15";
    public Db(){}
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/IESCO", user , pass);
    }
    public static void connect () {
        Connection myconn = null;
        Statement mystmt = null;
        ResultSet myRs = null;
        try {
            myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", user , pass);
            System.out.println("Connection Successful");
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        finally {
            if (myRs != null){
                try {
                    myRs.close();
                } catch (SQLException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }} }
}