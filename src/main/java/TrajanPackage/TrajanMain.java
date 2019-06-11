/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrajanPackage;

//import java.sql.*;
import java.util.*;
import javax.servlet.MultipartConfigElement;
import spark.Request;
import static spark.Spark.*;
import TrajanPackage.TrajanLog.*;

/**
 *
 * @author tespelien only for now
 */
public class TrajanMain {

    public final static int LENGTH = 100;
    public final static int BIKES = 2;
    public final static TronGameState STATE = new TronGameState(true);

    private static ArrayList<ServerContext> userContexts;
    //static Connection conn = null;

    //preset game (gameId=0) we will use for testing with a SillyBike (bikeId=0) at (100,100)
    static final Bike[] PreSetBikes = new Bike[]{new SillyBike(0, new Tuple(50, 50)), new SillyBike(1, new Tuple(51, 51))};
    static AbstractGameEngine PreSetGame = new AbstractGameEngine(0, "engineTest", 250, PreSetBikes);

    public static void main(String[] args) {
        char quote = '"';
        staticFiles.location("public/");

        before("*", (req, res) -> {
            //System.out.println("request coming in: " + req.requestMethod() + ":" + req.url());
        });

        //full functionality
        //updateBikeTest();
        get("/updateBikes", "application/json", (req, res) -> updateBikes(req), new JSONRT());
        get("/runGame", "application/json", (req, res) -> runGame(req), new JSONRT());

        //for testing purposes only
        get("/updateBikeTest", "application/json", (req, res) -> updateBikeTest(), new JSONRT());
        post("/runGameTest", "application/json", (req, res) -> runGameTest(req), new JSONRT());

    }

    //TEST
    //testing methods
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

    //send bike position, added trail position of bike
    private static Object[] updateBikes(spark.Request req) {
        try {
            ServerContext ctx = getCtx(req);
            if (ctx.observer == null) {
                return null;
            }

            ArrayList<TronGameLogEntry> list = translateEntryTypes(ctx.observer.getNewItems());

            if (req.queryParams("compact").equals("yes")) {
                // if the client requests it, make a new game state, 
                // we will use it to compact the entries

                TronGameState compactor = new TronGameState(true);
                for (GameLogEntry item : list) {
                    compactor.addEntry(new TronGameLogEntry(item));
                }
                list = compactor.getCompactedEntries();
            }
            // now convert into array to JSON knows what to do
            TronGameLogEntry[] array = new TronGameLogEntry[list.size()];
            return list.toArray(array);

        } catch (Exception e) {
            System.out.println("exception in GU");
            System.out.println(e.getMessage());
            e.printStackTrace(System.out);
        }
        //return null;

        try {
            ArrayList<TronGameLogEntry> log = STATE.getCompactedEntries();
            Object[] result = new Object[4];
            //result be like [bike#, bikes, trails, deaths]
            result[0] = BIKES;

            ArrayList<Object> tempList = new ArrayList<>();
            ArrayList<Object> bikeInfo = new ArrayList<>();
            //info be like {bikeID, (int[] bikePos) [x,y]}
            ArrayList<Object> trailInfo = new ArrayList<>();
            //info be like {bikeid, (int[] trailPos) [x,y]}
            ArrayList<Object> deathInfo = new ArrayList<>();
            //info be like {bikeID} (means bike with that bikeID died)
            int nullID = BIKES + 1;
// I SWITCHED "bikes" to "BIKES" because it was giving an error and it was annoying. Sorry if that wasnt supposed to happen- theo 
            for (int i = 1; i < BIKES + 1; i++) {
                bikeInfo.add(log.get(i));
                //bikeInfo.add(log.get(i).id); //adding id first
                //bikeInfo.add(new int[] {log.get(i).p.x, log.get(i).p.y}); //adding position second
                tempList.add(bikeInfo);
                bikeInfo.clear();
            }
            result[1] = tempList;
            tempList.clear();

            for (int i = BIKES + 1; i < log.size(); i++) {
                while (log.get(i) != null) {
                    trailInfo.add(log.get(i));
                    //trailInfo.add(log.get(i).id); //adding id first
                    //trailInfo.add(new int[] {log.get(i).p.x, log.get(i).p.y}); //adding position second
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
    private static Object[] runGame(spark.Request req) {
        return null;
    }

    private static ServerContext getCtx(Request req) {
        //get a context or make one from a request

        ServerContext ctx = req.session().attribute("context");
        if (ctx == null) {
            ctx = new ServerContext();
            req.session().attribute("context", ctx);
        }
        return ctx;
    }

    public static ArrayList<TronGameLogEntry> translateEntryTypes(ArrayList<GameLogEntry> gleal) {
        ArrayList<TronGameLogEntry> tgleal = new ArrayList<>();//the best naming conventions

        for (GameLogEntry gle : gleal) {
            tgleal.add(new TronGameLogEntry(gle));
        }
        return tgleal;
    }

}
