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
import org.eastsideprep.enginePackage.Bike;
import org.eastsideprep.trongamelog.TronGameLogEntry;
import org.eastsideprep.bikes.*;
import eastsideprep.org.troncommon.*;
import javax.servlet.MultipartConfigElement;
import spark.Request;
import static spark.Spark.*;

/**
 *
 * @author tespelien and other less cool boios
 */
public class main {

    public final static int LENGTH = 252;
    public final static int BIKES = 4;
    public static ArrayList<AbstractGameEngine> runningGames;//pointers to running games
    //public final static TronGameState STATE = new TronGameState(true);
    //static Connection conn = null;

    //preset game (gameId=0) we will use for testing with a SillyBike (bikeId=0) at (100,100)
    static final Bike[] PreSetBikes = new Bike[]{new SillyBike(0, new Tuple(50, 50)), new SillyBike(1, new Tuple(51, 51))};
    static AbstractGameEngine PreSetGame = new AbstractGameEngine(0, "engineTest", 250, PreSetBikes);

    public static void main(String[] args) {
        char quote = '"';
        staticFiles.location("public/");

        before("*", (req, res) -> {
            System.out.println("request coming in: " + req.requestMethod() + ":" + req.url());
        });
        //We don't need SQL for testing!
        //connect();

        //full functionality
        //updateBikeTest();
        //get("/updateBikes", "application/json", (req, res) -> updateBikes(req), new JSONRT());
        //post("/createGame", "application/json", (req, res) -> newGame(req));
        //get("/getGames", "application/json", (req, res) -> getGamesTable(req), new JSONRT());
        //get("/initializeBikes", "application/json", (req, res) -> initializeBikes(), new JSONRT());
        //get("/runGame", "application/json", (req, res) -> runGame(req), new JSONRT());
        //for testing purposes only
        get("/updateBikes", "application/json", (req, res) -> updateBikes(req), new JSONRT());
        get("/getBoard", "application/json", (req, res) -> getBoard(req), new JSONRT());
        post("/runGame", "application/json", (req, res) -> runGame(req), new JSONRT());
        //giveMeTheValue("GameID", "games", "GameName= " + quote + "Gametest" + quote);

    }

//Trajan's methods (work better...)
    //testing methods
    private static Object[] runGame(Request req) {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
        req.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        String type = req.queryParams("type");
        System.out.println("This test is of type " + type);
        int num = Integer.parseInt(req.queryParams("num"));
        System.out.println("Number of simulations: " + num);

        try {
            AbstractGameEngine testGame = PreSetGame;//see comments at the top to see params

            Tuple[] rawTestResults = testGame.run(num);//runs 1 full game

            String[] nicerResults = new String[testGame.numStartingBikes];
            System.out.println("nicerResults length: " + nicerResults.length);
            for (int i = 0; i < rawTestResults.length; i++) {
                Tuple t = rawTestResults[i];
                int bikeId = t.x;
                int numWins = t.y;
                nicerResults[i] = ("Bike " + bikeId + " win " + numWins + " times! \n");
                System.out.println(nicerResults[i]);
            }
            return nicerResults;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("something crashed and this hElPfUl error message is ~~not~~ going to help you :joy:");
        }
        return new String[]{"oof"};
    }

    private static Object[] getBoard(spark.Request req) {
        int bikeId = Integer.parseInt(req.queryParams("bikeId"));
        int[][] board = runningGames.get(bikeId).board.grid;
        synchronized (board) {
            return board;//return the whole table oof
        }
    }

    private static Object[] updateBikes(Request req) {
        //eventually I'll put in the updates instead of the full table
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
