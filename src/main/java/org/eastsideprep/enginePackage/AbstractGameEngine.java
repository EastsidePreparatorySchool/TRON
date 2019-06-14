package org.eastsideprep.enginePackage;

import eastsideprep.org.troncommon.TronLogInterface;
import eastsideprep.org.troncommon.AbstractGameInterface;
import eastsideprep.org.troncommon.Tuple;
import java.util.ArrayList;
import org.eastsideprep.trongamelog.TronGameLog;
import org.eastsideprep.trongamelog.TronGameState;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tespelien
 */
public class AbstractGameEngine implements AbstractGameInterface {

//GAME CONSTANTS
    private final int size = 252;//the edge will be a wall / border
    private final int maxSpeed = 1;

//IMPORTANT OBJECTS
    public String name = "Unnamed";//change this in the constructor if you wish
    public int GameId;
    //private int[][] board;
    private final Grid board;//this is a complex object that contains a int[][] grid
    private final ArrayList<BikeContainer> bikes;
    public int numStartingBikes;
    private final TronGameLog gameLog = new TronGameLog(new TronGameState(true));

    public AbstractGameEngine(int id, int size, Bike[] bikeArr) {
        this.GameId = id;
        this.bikes = new ArrayList<>();
        for (Bike b : bikeArr) {
            bikes.add(new BikeContainer(b, new Tuple(0, 0), maxSpeed));//placing the bikes at 0,0
        }
        numStartingBikes = bikes.size();
        board = new Grid(size, size);
    }

    public AbstractGameEngine(int id, String name, int size, Bike[] bikeArr) {
        this.GameId = id;
        this.name = name;
        this.bikes = new ArrayList<>();
        for (Bike b : bikeArr) {
            bikes.add(new BikeContainer(b, b.startingPosition, maxSpeed)); //placing the bikes at 0,0
        }
        numStartingBikes = bikes.size();
        board = new Grid(size, size);

        System.out.println(numStartingBikes + " bikes: " + bikes.get(0).toString() + " " + bikes.get(1).toString());
    }

    //each square on the board will have one of 3 ints:
    //  0 for empty square
    //  1 for bike
    //  2 for wall
    @Override
    public void init(TronLogInterface tl) {
//        //this.gameLog = tl;
//        //gameLog.Setup(size);
//        //create 4 bikes at random positions
//        Random rand = new Random();
//
//        for (BikeContainer bc : bikes) {
//            int x = rand.nextInt(249) + 1;//random int between 1 and 250 inclusive
//            int y = rand.nextInt(249) + 1;
//            int vel = rand.nextInt(maxSpeed);
//
//            //place the given bikes at random coords on the board and with a random velocity up to maxSpeed
//            bc.direction = rand.nextInt(3);//four directions: 0,1,2,3
//            bc.pos = new Tuple(x, y);
//            board.grid[x][y] = 1;
//            //gameLog.UpdatePosition(bc.bike.bikeId, new Tuple(x, y));
//        }
    }

    public void printGrid() {
        int[][] g = this.board.grid;

        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                System.out.print(g[i][j]);
            }
            System.out.println();
        }
    }

    private void update() {
        //for each bike:
        //  take directions from container
        //  add directions to each position
        //  update trails
        //  check if this bike is dead
        //  if so kill it AND its trail

        for (BikeContainer b : bikes) {

            Bike bike = b.bike;
            Tuple pos = b.pos;

            int dir = bike.getDirection(board, pos.x, pos.y, bike.direction);
            System.out.println(dir);
            
            b.trail.add(pos);
            board.grid[pos.x][pos.y] = 2;
            
            //directions + movement
            switch (dir) {
                case 0:
                    pos.x--;
                    break;
                case 1:
                    pos.y++;
                    break;
                case 2:
                    pos.x++;
                    break;
                case 3:
                    pos.y--;
                    break;
                default:
                    System.out.println("game update err: didn't move");
                    break;
            }

            //no mercy
            if (pos.x == 0 || pos.x == size - 1 || pos.y == 0 || pos.y == size - 1 || board.grid[pos.x][pos.y] == 2) {
                System.out.println("killed bike#: " + b.id);
                killBike(b);
            } else {
                board.grid[pos.x][pos.y] = 1;
                System.out.print(b.toString() + " ");
            }
        }
        
        System.out.println();
        printGrid();
    }

    private void killBike(BikeContainer bc) {
        gameLog.KillBike(bc.bike.bikeId);
        bc.kill();
        bikes.remove(bc);
    }

    @Override
    public void run() throws InterruptedException {
        System.out.println("running " + this.name + "...");

        int turns = 0;
        while (bikes.size() > 1) {
            Thread.sleep(50);
            turns++;
            System.out.print("turn " + turns + ": ");
            update();
        }

        System.out.println("The winner is: " + bikes.get(0).bike.bikeId + " after " + turns + " turns.");
    }

    @Override
    public Tuple[] run(int n) { //this is so sketchy what????
        System.out.println("running " + this.name + "...");
        int tsize = this.size;
        Bike[] testBikes = new Bike[numStartingBikes];
        //System.out.println("run1");
        for (int i = 0; i < testBikes.length; i++) {
            testBikes[i] = bikes.get(i).bike;
        }
        int numBikes = testBikes.length;
        Tuple[] scoreboard = new Tuple[numBikes];
        //System.out.println("run2");
        for (int i = 0; i < scoreboard.length; i++) {
            scoreboard[i] = new Tuple(i, 0);
        }
        //System.out.println("run3");
        for (int i = 0; i < n; i++) {//n complete games
            int winner;//winner's id
            int turns = 0;
            //System.out.println("run4");
            AbstractGameEngine currentGame = new AbstractGameEngine(i, Math.max(250, tsize), testBikes);//dont want the game board to be too small
            while (currentGame.bikes.size() > 1) {//sometimes a bike will die and the length of the arraylist will decrease
                //System.out.println("run5");
                currentGame.update();
                turns++;
                //System.out.println("Turns ran " + turns);
                //System.out.println("run6");
            }
            // System.out.println("run7");
            //there is a winner!
            winner = currentGame.bikes.get(0).bike.bikeId;//there is only one bike left in the arraylist
            System.out.println("The winner is " + winner);
            //the tuples are (id, times won)
            for (Tuple tuple : scoreboard) {
                System.out.println(tuple.toString());
                if (tuple.x == winner) {
                    scoreboard[winner].y++;
                    //System.out.println(tuple.toString());
                }
            }
            //System.out.println("run8");
        }
        //gameLog.runResults(testBikes);

        return scoreboard;
    }

}
