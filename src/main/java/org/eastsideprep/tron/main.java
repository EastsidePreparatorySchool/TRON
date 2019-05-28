/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import java.sql.*;
import static spark.Spark.*;

/**
 *
 * @author tho
 */
public class main {

    public final static int LENGTH = 100;

    static Connection conn = null;

    public static void main(String[] args) {
        staticFiles.location("/public/");

      
        get("/getGrid", "application/json", (req, res) -> getGrid(), new JSONRT());
        get("/updateBikes", "application/json", (req, res) -> updateBikes(), new JSONRT());
    }

    //big bulky update stuff
    //sends grid with all ocupied spaces marked
    public static Object[][] getGrid() {

        try {
            //GET grid
            return null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void newGame(spark.Request req) {
        String bikeNames = req.queryParams("bikeName");
        String[] bikeList = bikeNames.split("|");

        for (int i = 0; i > bikeList.length; i++) {

        }

    }

    public static Object[][] getGames() {

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from games"); // select everything in the table

            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.println(rsmd.getColumnName(i) + ",  " + rsmd.getColumnTypeName(i)); // prints column name and type

            }

            Object[][] res = new Object[LENGTH][numberOfColumns];
            int count = 0;

            while (rs.next() && count < LENGTH) {
                for (int j = 1; j <= numberOfColumns; j++) {
                    res[count][j - 1] = rs.getString(j);
                }
                count++;
            }
            return res;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }



//smol update stuff
//send bike position, added trail position of bike
public static Object updateBikes() {
        try {
            //GET updated move

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
