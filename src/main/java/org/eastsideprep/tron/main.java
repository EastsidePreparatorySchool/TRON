/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import java.sql.*;
import org.eastsideprep.enginePackage.AbstractGameEngine;
import org.eastsideprep.gamelog.GameLogEntry;
import org.eastsideprep.trongamelog.TronGameState;
import java.util.*;
import org.eastsideprep.enginePackage.Bike;
import static spark.Spark.*;

/**
 *
 * @author tho
 */
public class main {

    public final static int LENGTH = 100;
    public final static int BIKES = 2;
    public final static TronGameState STATE = new TronGameState(true);
    static Connection conn = null;

    public static void main(String[] args) {
        connect();
        staticFiles.location("/public/");
        updateBikeTest();
        get("/updateBikes", "application/json", (req, res) -> updateBikes(), new JSONRT());
        post("/createGame", "application/json", (req, res) -> newGame(req));
        get("/getGames", "application/json", (req, res) -> getGamesTable(req), new JSONRT());
        get("/initializeBikes", "application/json", (req, res) -> initializeBikes(), new JSONRT());
        get("/runFullGame ", "application/json", (req, res) -> runFullGame(req), new JSONRT());

        //test
        get("/updateBikeTest ", "application/json", (req, res) -> updateBikeTest(), new JSONRT());
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
        String[] bikeIDList = req.queryParams("idBikeList").split("|");
        String gameName = req.queryParams("gameName");
        System.out.println(gameName + "=================");
        //this function will take a list of bikes in a string formated in this format - bike1|bike2|bike3|bike4|
        char quote = '"';
        int GameID = 0;
        String sqlGame = "INSERT INTO" + quote + "games" + quote + "(NumBikes, GameName) VALUES (" + bikeIDList.length + quote + ", " + quote + gameName + quote + ");";
        System.out.println(sqlGame);

        try {
            PreparedStatement sqlcmdGame = conn.prepareStatement(sqlGame);
            sqlcmdGame.execute();
             GameID = sqlcmdGame.getGeneratedKeys().getInt(1);
           
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
            Object[] result = new Object[4];
            //result be like [bike#, bikes, trails, deaths]
            int bikes = BIKES;
            result[0] = bikes;

            ArrayList<Object> tempList = new ArrayList<>();
            ArrayList<Object> bikeInfo = new ArrayList<>();
            //info be like {bikeID, (int[] bikePos) [x,y]}
            ArrayList<Object> trailInfo = new ArrayList<>();
            //info be like {bikeid, (int[] trailPos) [x,y]}
            ArrayList<Object> deathInfo = new ArrayList<>();
            //info be like {bikeID} (means bike with that bikeID died)
            int nullID = bikes + 1;

            for (int i = 1; i < bikes + 1; i++) {
                bikeInfo.add(log.get(i));
                //bikeInfo.add(log.get(i).id); //adding id first
                //bikeInfo.add(new int[] {log.get(i+1).p.x, log.get(i+1).p.y}); //adding position second
                tempList.add(bikeInfo);
                bikeInfo.clear();
            }
            result[1] = tempList;
            tempList.clear();

            for (int i = bikes + 1; i < log.size(); i++) {
                while (log.get(i) != null) {
                    trailInfo.add(log.get(i));
                    //trailInfo.add(log.get(i).id); //adding id first
                    //trailInfo.add(new int[] {log.get(i+1).p.x, log.get(i+1).p.y}); //adding position second
                    tempList.add(trailInfo);
                    trailInfo.clear();
                }
                nullID = i;
                break;
            }
            result[2] = tempList;
            tempList.clear();

            for (int i = nullID; i < log.size(); i++) {
                deathInfo.add(log.get(i));
                //deathInfo.add(log.get(i).id);
            }
            result[3] = tempList;
            tempList.clear();

            return result;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //TODO: get bikes from request and use the .run() method in AbstractGameEngine.java
    private static Object[] runFullGame(spark.Request req) {
        return null;
    }

//TEST
    private static Object[] updateBikeTest() {
        Object[] testArr = new Object[2];
        ArrayList<Object> tes = new ArrayList<>();

        tes.add("test1");
        tes.add(true);
        tes.add(new int[]{50, 30});
        tes.add(new int[]{50, -20});

        testArr[0] = tes;

        tes.clear();
        tes.add("test2");
        tes.add(false);
        tes.add(new int[]{-40, 30});
        tes.add(new int[]{30, 30});

        testArr[1] = tes;

        return testArr;
    }
}
