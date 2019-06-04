package org.eastsideprep.enginePackage;

import eastsideprep.org.troncommon.TronLogInterface;
import eastsideprep.org.troncommon.AbstractGameInterface;
import eastsideprep.org.troncommon.Tuple;
import org.eastsideprep.trongamelog.*;
import java.util.ArrayList;
import java.util.Random;

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
    public int GameId;
    private int[][] board;
    private final ArrayList<BikeContainer> bikes;
    private TronLogInterface gameLog;

    public AbstractGameEngine(int id, int size, Bike[] bikeArr) {
        this.GameId = id;
        this.bikes = new ArrayList<>();
        for (Bike b : bikeArr) {
            bikes.add(new BikeContainer(b, new Tuple(0, 0), maxSpeed));//placing the bikes at 0,0
        }
        board = new int[size][size];
    }

    //each square on the board will have one of 3 ints:
    //  0 for empty square
    //  1 for bike
    //  2 for wall
    @Override
    public void init(TronLogInterface tl) {
        this.gameLog = tl;
        gameLog.Setup(size);
        //create 4 bikes at random positions
        Random rand = new Random();

        for (BikeContainer bc : bikes) {
            int x = rand.nextInt(249) + 1;//random int between 1 and 250 inclusive
            int y = rand.nextInt(249) + 1;
            int vel = rand.nextInt(maxSpeed);

            //place the given bikes at random coords on the board and with a random velocity up to maxSpeed
            bc.direction = rand.nextInt(3);//four directions: 0,1,2,3
            board[x][y] = 1;
            gameLog.UpdatePosition(bc.bike.bikeId, new Tuple(x, y));
        }
    }

    public int[][] getGrid() {
        return this.board;
    }

    private void update() {
        //for each bike:
        //  take directions from container
        //  add directions to each position
        //  update trails
        //  check if this bike is dead
        //      if so kill it AND its trail

        for (BikeContainer b : bikes) {
            int dir = b.bike.direction;
            Tuple pos = b.currentPosition;

            //add a trail at that position
            b.trail.add(pos);
            board[pos.x][pos.y] = 2;

            //update postions using direction
            if (dir == 0) {
                pos.x--;
            } else if (dir == 1) {
                pos.y++;
            } else if (dir == 2) {
                pos.x++;
            } else if (dir == 3) {
                pos.y--;
            }
            board[pos.x][pos.y] = 1;
            gameLog.UpdatePosition(b.bike.bikeId, pos);

            //kill
            if (pos.x == 0 || pos.x == 251 || pos.y == 0 || pos.y == 251 || board[pos.x][pos.y] == 2) {
                gameLog.KillBike(b.bike.bikeId);
                killBike(b);
            }
        }
    }

    private void killBike(BikeContainer bc) {
        bikes.remove(bc);
    }

    //TODO: send win logs to Faye
    public Tuple[] run(int n) {
        int size = this.size;
        Bike[] testBikes = new Bike[bikes.size()];
        for (int i = 0; i < testBikes.length; i++) {
            testBikes[i] = bikes.get(i).bike;
        }
        int numBikes = testBikes.length;
        Tuple[] scoreboard = new Tuple[numBikes];
        for (int i = 0; i < scoreboard.length; i++) {
            scoreboard[i] = new Tuple(i, 0);
        }
        for (int i = 0; i < n; i++) {//n complete games
            int winner;//winner's id

            AbstractGameEngine currentGame = new AbstractGameEngine(i, Math.max(250, size), testBikes);//dont want the game board to be too small
            while (currentGame.bikes.size() > 1) {
                currentGame.update();
            }
            //there is a winner!
            winner = currentGame.bikes.get(0).bike.bikeId;//there is only one bike left in the arraylist
            //the tuples are (id, times won)
            for (Tuple tuple : scoreboard) {
                if (tuple.x == winner) {
                    scoreboard[winner].y++;
                }
            }
        }
        gameLog.runResults(testBikes);
        return scoreboard;
        
    }

}
