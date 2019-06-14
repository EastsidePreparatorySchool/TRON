/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.tron;

import com.google.gson.Gson;
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

    public final static int LENGTH = 100;
    public final static int BIKES = 2;
    public final static TronGameState STATE = new TronGameState(true);
    static Connection conn = null;

    //preset game (gameId=0) we will use for testing with a SillyBike (bikeId=0) at (100,100)
    static final Bike[] PreSetBikes = new Bike[]{new SillyBike(0, new Tuple(50, 50)), new SillyBike(1, new Tuple(51, 51))};
    static AbstractGameEngine PreSetGame = new AbstractGameEngine(0, "engineTest", 250, PreSetBikes);

    public static void main(String[] args) throws InterruptedException{
        char quote = '"';
        staticFiles.location("public/");

        before("*", (req, res) -> {
            //System.out.println("request coming in: " + req.requestMethod() + ":" + req.url());
        });
        //We don't need SQL for testing!
        connect();

        //full functionality
        //updateBikeTest();
        get("/updateBikes", "application/json", (req, res) -> updateBikes(req), new JSONRT());
        post("/createGame", "application/json", (req, res) -> newGameTest(req), new JSONRT());
        get("/getGames", "application/json", (req, res) -> getGamesTable(req), new JSONRT());
        get("/initializeBikes", "application/json", (req, res) -> initializeBikes(), new JSONRT());
        get("/runGame", "application/json", (req, res) -> runGame(req), new JSONRT());
        //for testing purposes only
        get("/updateBikeTest", "application/json", (req, res) -> updateBikeTest(), new JSONRT());
        post("/runGameTest", "application/json", (req, res) -> runGameTest(req), new JSONRT());
        //giveMeTheValue("GameID","games","GameName= " +quote + "Gametest"+ quote);
        System.out.println("teeeeeestttt " + giveMeTheBikeArray("Gametest"));
        
        logTest();
    }

//TEST
    //testing methods
    public static String[] newGameTest(Request req){
        String params = req.queryParams("gameNameAndBikes");
        String info[]=params.split("/");
        String gameName=info[0];
        String bikes=info[1];
        System.out.println(info[1]+"-----");
        String bikeList[]=bikes.split(":");
        System.out.println(gameName + "GAME NAME");
        
        for (int i=0; i<bikeList.length;i++){
         System.out.println(bikeList[i] +"LENGTHTHTHTHTHT4949494494949");   
        }
        System.out.println(bikeList.length+"LENGTHTHTHTHTHT");
    newGame(gameName,bikeList);
       return bikeList;
    }
    
    public static void logTest() throws InterruptedException  {
        Bike[] testbikes = new Bike[]{new SillyBike(0, new Tuple(5, 5)), new SillyBike(1, new Tuple(10, 10))};
        AbstractGameEngine testgame = new AbstractGameEngine(1, "logTest", 15, testbikes);

        testgame.run();
    }

    private static Object[] runGameTest(Request req) {
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

    public static List<Integer> giveMeTheBikeArray(String gameName) {
        List<Integer> bikeList = new ArrayList<Integer>();
        char quote = '"';
        int gameID = giveMeTheValue("GameID", "games", "GameName= " + quote + gameName + quote);
        System.out.println("GAME ID: " + gameID);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select BikeClassID from gamesbikes where GameId=" + gameID + ";"); // select everything in the table

            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                int i = 1;
                int iarr = 0;
                bikeList.add(iarr, rs.getInt(i));
                System.out.println("TEST: " + rs.getString(i) + "|" + i);
                i++;
                iarr++;

            }
            return bikeList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static int giveMeTheValue(String row, String table, String where) {
        System.out.println("select " + row + " from " + table + " where " + where + ";" +"in the FUNCTION GIEV ME THE VALUE");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select " + row + " from " + table + " where " + where + ";");

            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println(rs.getString(1)+"in the FUNCTION GIEV ME THE VALUE");
            System.out.println(rs.getInt(1)+"in the FUNCTION GIEV ME THE VALUE");
            
            return rs.getInt(1);
            //System.out.println(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return 1;
    }

    private static Object[] updateBikeTest() {
        Object[] testArr = new Object[2];
        ArrayList<Object> tes = new ArrayList<>();
        ArrayList<Object> tes2 = new ArrayList<>();

        tes.add("test1");
        tes.add(true);
        tes.add(new int[]{50, 30});
        tes.add(new int[]{50, -20});

        testArr[0] = tes;

        tes2.add("test2");
        tes2.add(false);
        tes2.add(new int[]{-40, 30});
        tes2.add(new int[]{30, 30});

        testArr[1] = tes2;

        return testArr;
    }
    //End of testing methods

    //Regular methods:    
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

    private static String newGame(String gameName, String[] bikeList) {

        
        //this function will take a list of bikes in a string formated in this format - bike1|bike2|bike3|bike4|
        char quote = '"';
        
        String sqlGame = "INSERT INTO " + quote + "games" + quote + " (NumBikes, GameName) VALUES (" + bikeList.length  + ", " + quote + gameName + quote + ");";
        System.out.println(sqlGame);

        try {
            PreparedStatement sqlcmdGame = conn.prepareStatement(sqlGame);
            sqlcmdGame.execute();

           // GameID = sqlcmdGame.getGeneratedKeys().getInt(1);

          
        } catch (Exception e) {
            System.out.println(e);
        }
       int gameID = giveMeTheValue("GameID", "games", "GameName= " + quote + gameName + quote);
       System.out.println(gameID+"========");
       for (int i = 0; i < bikeList.length; i++) {
        int bikeID = giveMeTheValue("BikeClassId", "bikeclasses", "Name= " + quote + bikeList[i] + quote);
         String sqlBikeClass = "INSERT INTO " + quote + "gamesbikes" + quote + " (GameID, BikeClassID) VALUES (" + gameID + ", " + quote + bikeID + quote + ");";
           System.out.println(sqlBikeClass);

            try {
                PreparedStatement sqlcmdBike = conn.prepareStatement(sqlBikeClass);
                sqlcmdBike.execute();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return null;
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

    private static ServerContext getCtx(spark.Request req) {
        HashMap<String, ServerContext> ctxMap = req.session().attribute("ServerContexts");
        if (req.session().isNew() || ctxMap == null) {
            ctxMap = new HashMap<>();
            req.session().attribute("ServerContexts", ctxMap);
        }

        String client = req.queryParams("clientID");
        ServerContext ctx = ctxMap.get(client);
        if (ctx == null) {
            ctx = new ServerContext();
            ctx.clientSubID = client;
            ctxMap.put(client, ctx);
        }
//I COMENTED THIS ONE OUT BECAUSE IT WAS GIVING AN ERROR AND IT WAS ANNOYING
        // blow up stale contexts

        // if (ctx.observer != null && ctx.observer.isStale()) { 
        //     ctx.observer = null;
//            return null;
        //  }
        //if (ctx.observer != null && ctx.observer.isStaxle()) {
        //ctx.observer = null;
//            return null;
        //}
        req.session().maxInactiveInterval(60); // kill this session afte 60 seconds of inactivity
        return ctx;
    }

//smol update stuff
//send bike position, added trail position of bike
    private static Object[] updateBikes(spark.Request req) {
        try {
            ServerContext ctx = getCtx(req);
            if (ctx.observer == null) {
                return null;
            }

            ArrayList<GameLogEntry> list = ctx.observer.getNewItems();
            ArrayList<Object> done = new ArrayList<>();

            if (req.queryParams("compact").equals("yes")) {
                // if the client requests it, make a new game state, 
                // we will use it to compact the entries

                TronGameState compactor = new TronGameState(true);
                for (GameLogEntry item : list) {
                    compactor.addEntry(item);
                }
                list = compactor.getCompactedEntries();

                // now convert into array to JSON knows what to do
                TronGameLogEntry[] array = new TronGameLogEntry[list.size()];
                return list.toArray(array);

            } else {
                for (int i = 0; i < list.size(); i++) {

                    TronGameLogEntry entry = (TronGameLogEntry) list.get(i);
                    Object[] result = new Object[3];

                    switch (entry.entryType) {
                        case 1:
                            result[0] = "POSUPDATE";
                            result[1] = entry.id;
                            result[2] = new int[]{entry.p.x, entry.p.y};
                        case 2:
                            result[0] = "DEATH";
                            result[1] = entry.id;
                        default:
                            break;
                    }
                    done.add(result);
                    System.out.println("bikeupdate: " + result[0] + " " + result[1] + " " + result[2]);
                }
                Object[] array = new Object[list.size()];
                return done.toArray(array);
            }
        } catch (Exception e) {
            System.out.println("exception in bikeUpdate");
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        }
        return new Object[]{"idk what happened oof,, bikeUpdate failed"};
    }

    //TODO: get bikes from request and use the .run() method in AbstractGameEngine.java
    private static Object[] runGame(spark.Request req) {
        return null;
    }

}