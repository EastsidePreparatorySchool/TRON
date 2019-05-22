
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
public class AbstractGameEngine {
//GAME CONSTANTS

    private final int size = 252;//the edge will be a wall / border
    private final int maxSpeed = 2;

//IMPORTANT OBJECTS
    private int[][] board = new int[size][size];
    private final ArrayList<BikeContainer> bikes = new ArrayList<>();

    //each square on the board will have one of 3 ints:
    //  0 for empty square
    //  1 for bike
    //  2 for wall
    private void init() {
        //create 4 bikes at random positions
        Random rand = new Random();

        for (int i = 0; i < bikes.size(); i++) {

            int x = rand.nextInt(249) + 1;//random int between 1 and 250 inclusive
            int y = rand.nextInt(249) + 1;
            int v = rand.nextInt(maxSpeed);
            //make a new bike with random coords on the board, id from 0-3 and with a random velocity up to maxSpeed
            Bike b = new Bike(i, new Position(x, y));
            b.direction = rand.nextInt(3);//four directions: 0,1,2,3
            bikes.set(i, new BikeContainer(b, new Position(x, y), v));
            board[x][y] = 1;

        }

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
            Position pos = b.currentPosition;

            //
            b.trail.add(pos);
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
//
//            //kill
            if (pos.x == 0 || pos.x == 251 || pos.y == 0 || pos.y == 251) {
                killBike(b);
            }
        }
    }

    void killBike(BikeContainer bc) {
        bikes.remove(bc);
    }

}