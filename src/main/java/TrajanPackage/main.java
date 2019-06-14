/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrajanPackage;

import java.sql.*;
import java.util.*;

import javax.servlet.MultipartConfigElement;
import spark.Request;
import static spark.Spark.*;

/**
 *
 * @author tespelien and other less cool boios
 */
public class main {

    public final static int LENGTH = 42;
    public final static int BIKES = 4;
    private static ArrayList<AbstractGameEngine> runningGames = new ArrayList<>();//pointers to running games
    //public final static TronGameState STATE = new TronGameState(true);
    //static Connection conn = null;

    //preset game (gameId=0) we will use for testing with a SillyBike (bikeId=0) at (100,100)
    private static Bike b1 = new SillyBike(0, new Tuple(50, 50));
    private static Bike b2 = new SillyBike(0, new Tuple(51, 51));
    private static final Bike[] PreSetBikes = new Bike[]{b1, b2};
    static AbstractGameEngine PreSetGame = new AbstractGameEngine(0, "engineTest", 250, PreSetBikes);

    public static void main(String[] args) {

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
        get("/startGame", (req, res) -> startGame());
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
        System.out.println("getting whole board...");
        //int gameId = Integer.parseInt(req.queryParams("gameId"));
        int gameId = 0;//only 1 game at the moment
        System.out.println("This game is called " + (runningGames.get(gameId).name));//pls dont be null...
        return runningGames.get(gameId).board.grid;
    }

    private static String startGame() {
        System.out.println("starting a new game...");
        try {
            AbstractGameEngine age = new AbstractGameEngine(0, 250, PreSetBikes);//no name constructor is better
            age.init();
            age.name = "coolname";
            runningGames.add(age);
            System.out.println("list of running games: " + runningGames.toString());
            System.out.println("added a game called: " + runningGames.get(0).name);
        } catch (Exception e) {
            System.out.println("error starting game: " + e);
        }
        return "Trajan was here";
    }

    private static Object[] updateBikes(Request req) {
        //eventually I'll put in the updates instead of the full table
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
