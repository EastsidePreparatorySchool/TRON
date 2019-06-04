/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import java.sql.*;
import org.eastsideprep.enginePackage.*;
import org.eastsideprep.gamelog.GameLogEntry;
import org.eastsideprep.trongamelog.TronGameState;
import java.util.*;
import org.eastsideprep.bikes.*;
import eastsideprep.org.troncommon.*;
import static spark.Spark.*;

/**
 *
 * @author tespelien and other less cool boios
 */
public class main {

    public final static int LENGTH = 100;
    public final static TronGameState STATE = new TronGameState(true);
    static Connection conn = null;

    //preset game (gameId=0) we will use for testing with a SillyBike (bikeId=0) at (100,100)
    AbstractGameEngine PreSetGame = new AbstractGameEngine(0, 250, new Bike[]{new SillyBike(0, new Tuple(100, 100))});

    public static void main(String[] args) {
        connect();
        staticFiles.location("/public/");

        get("/updateBikes", "application/json", (req, res) -> updateBikes(), new JSONRT());
        post("/createGame", "application/json", (req, res) -> newGame(req));
        get("/getGames", "application/json", (req, res) -> getGamesTable(req), new JSONRT());
        get("/initializeBikes", "application/json", (req, res) -> initializeBikes(), new JSONRT());
        get("/runFullGame ", "application/json", (req, res) -> runFullGame(req), new JSONRT());
    }

    private static void connect() {
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

    private static String newGame(spark.Request req) {
        String bikeNames = req.queryParams("bikeListID");
        String[] bikeIDList = bikeNames.split("|");
        String gameName = req.queryParams("gameName");
        System.out.println(gameName + "=================");
        //this function will take a list of bikes in a string formated in this format - bike1|bike2|bike3|bike4|
        char quote = '"';

        String sqlGame = "INSERT INTO" + quote + "games" + quote + "(GameId, NumBikes, GameName) VALUES (" + Integer.toString(GameID) + ", " + quote + bikeIDList.length + quote + ", " + quote + gameName + quote + ");";
        System.out.println(sqlGame);
        try {
            PreparedStatement sqlcmdGame = conn.prepareStatement(sqlGame);
            sqlcmdGame.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        for (int i = 0; i < bikeIDList.length; i++) {

            String sqlBikeClass = "INSERT INTO" + quote + "gameBike" + quote + "(GameID, BikeClassID) VALUES (" + Integer.toString(GameID) + ", " + quote + bikeIDList[i] + quote + ");";
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

    private static Object[][] getGamesTable(spark.Request req) {
        String table = req.queryParams("tableName");

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from " + table + ";"); // select everything in the table

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

//list of bikeos and such
    private static Object[] initializeBikes() {
        try {
            ArrayList<GameLogEntry> log = STATE.getCompactedEntries();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

//smol update stuff
//send bike position, added trail position of bike
    private static Object[] updateBikes() {
        try {
            ArrayList<GameLogEntry> log = STATE.getCompactedEntries();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //TODO: get bikes from request and use the .run() method in AbstractGameEngine.java
    private static Object[] runFullGame(spark.Request req) {
        return null;
    }
}
