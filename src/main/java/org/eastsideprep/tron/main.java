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
        connect();
        staticFiles.location("/public/");

        get("/getGrid", "application/json", (req, res) -> getGrid(), new JSONRT());
        get("/updateBikes", "application/json", (req, res) -> updateBikes(), new JSONRT());
        post("/createGame", (req, res) -> newGame(req));
        get("/getGames", "application/json", (req, res) -> getGames(), new JSONRT());

    }
     public static void connect() {
        try {
            // db parameters
            String url = "jdbc:sqlite:tron.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //big bulky update stuff
    public static int[][] getGrid(spark.Request req) {
        //sends grid with all ocupied spaces marked by the following scheme
        //  0 for empty square
        //  1 for bike
        //  2 for wall
        try {
            //GET grid
            int gameId = Integer.parseInt(req.queryParams("id"));
            for (AbstractGameEngine age : RunningAbstractGameEngines) {
                if (age.AbstractGameEngineId == gameId){
                    return age.getGrid();
                }                
            }
            return null;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String newGame(spark.Request req) {
        String bikeNames = req.queryParams("bikeListID");
        String[] bikeIDList = bikeNames.split("|");
        String gameName=req.queryParams("gameName");
        System.out.println(gameName+"=================");
        //this function will take a list of bikes in a string formated in this format - bike1|bike2|bike3|bike4|
        char quote = '"';
        int lastBikeID = 0; //here i need something that will return the last bike id so i can add it 
        int lastGameID = 0; //same comment 
       
        String sqlGame = "INSERT INTO" + quote + "games" + quote + "(GameId, NumBikes, GameName) VALUES (" + Integer.toString(lastGameID + 1) + ", " + quote + bikeIDList.length + quote + ", " + quote + gameName + quote +");";
        System.out.println(sqlGame);
        try {
            PreparedStatement sqlcmdGame = conn.prepareStatement(sqlGame);
            sqlcmdGame.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
         for (int i = 0; i > bikeIDList.length; i++) {

            lastBikeID = lastBikeID + 1;
            String sqlBikeClass = "INSERT INTO" + quote + "gameBike" + quote + "(GameID, BikeClassID) VALUES (" + Integer.toString(lastGameID +1) + ", " + quote + bikeIDList[i] + quote + ");";
            System.out.println(sqlBikeClass);

            try {
                PreparedStatement sqlcmdBike = conn.prepareStatement(sqlBikeClass);
                sqlcmdBike.execute();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    return req.session().id();
    }

    public static Object[][] getGames() {

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from bikeclasses"); // select everything in the table

            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
             System.out.println(numberOfColumns);
            for (int i = 1; i <= numberOfColumns; i++) {
                System.out.println(rsmd.getColumnName(i) + ",  " + rsmd.getColumnTypeName(i)); // prints column name and type

            }

            Object[][] res = new Object[LENGTH][numberOfColumns];
            int count = 0;

            while (rs.next() && count < LENGTH) {
                for (int j = 1; j <= numberOfColumns; j++) {
                    res[count][j - 1] = rs.getString(j);
                    System.out.println(rs.getString(j) + "======================");
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
