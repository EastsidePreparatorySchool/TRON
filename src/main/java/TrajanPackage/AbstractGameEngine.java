package TrajanPackage;

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
    private final int size = 42;//the edge will be a wall / border
    private final int maxSpeed = 1;

//IMPORTANT OBJECTS
    public String name = "Unnamed";//change this in the constructor if you wish
    public int GameId;
    //private int[][] board;
    public Grid board = new Grid(size, size);//this is a complex object that contains a int[][] grid
    private ArrayList<BikeContainer> bikes;
    public int numStartingBikes;
    //private TronLogInterface gameLog;

    public AbstractGameEngine(int id, int size, Bike[] bikeArr) {
        System.out.println("making an engine without a name");
        this.GameId = id;
        this.bikes = new ArrayList<>();
        for (Bike b : bikeArr) {
            bikes.add(new BikeContainer(b, new Tuple(0, 0), maxSpeed));//placing the bikes at 0,0
        }
        numStartingBikes = bikes.size();
        board = new Grid(size, size);

    }
//not every game needs a name

    public AbstractGameEngine(int id, String name, int size, Bike[] bikeArr) {
        this.GameId = id;
        this.name = name;
        this.bikes = new ArrayList<>();
        for (Bike b : bikeArr) {
            bikes.add(new BikeContainer(b, new Tuple(0, 0), maxSpeed));//placing the bikes at 0,0 for now
        }
        numStartingBikes = bikes.size();
        board = new Grid(size, size);
    }

    //each square on the board will have one of 3 ints:
    //  0 for empty square
    //  1 for bike
    //  2 for wall
    @Override
    public void init(/*TronLogInterface tl*/) {
        //this.gameLog = tl;
        //gameLog.Setup(size);
        //create 4 bikes at random positions
        Random rand = new Random();

        //walls:
        int[][] grid = new int[size][size];
        for (int i = 0; i < grid.length; i++) {//first and last column
            grid[0][i] = 2;
            grid[size - 1][i] = 2;
        }
        for (int i = 0; i < grid[0].length; i++) {//top and bottom row
            grid[i][0] = 2;
            grid[i][size - 1] = 2;
        }
        board.grid = grid;
        for (BikeContainer bc : bikes) {

            int x = rand.nextInt(5) + 18;//Placing the bikes towards the center
            int y = rand.nextInt(5) + 18;
            int vel = rand.nextInt(maxSpeed);

            //place the given bikes at random coords on the board and with a random velocity up to maxSpeed
            bc.direction = rand.nextInt(3);//four directions: 0,1,2,3
            bc.currentPosition = new Tuple(x, y);
            System.out.println("Placed a bike at " + bc.currentPosition.toString());
            board.grid[x][y] = 1;
            //gameLog.UpdatePosition(bc.bike.bikeId, new Tuple(x, y));
        }
    }

    public int[][] getGrid() {
        return this.board.grid;
    }

    public void update() {
        //for each bike:
        //  take directions from container
        //  add directions to each position
        //  update trails
        //  check if this bike is dead
        //      if so kill it AND its trail
        if (bikes.size() == 1) {
            System.out.println("Game over, bike " + bikes.get(0).id + " won!");
        } else if (bikes.size() > 1) {
            for (BikeContainer b : bikes) {

                Bike bike = b.bike;
                Tuple pos = b.currentPosition;

                int dir = bike.getDirection(board, pos.x, pos.y, bike.direction);
                //System.out.println("Direction: " + dir);

                //add a trail at that position
                b.trail.add(pos);
                board.grid[pos.x][pos.y] = 3;

                switch (dir) {
                    case 0:
                        if (pos.x > 1) {//only let it move if its a legal move
                            pos.x--;
                        }
                        break;
                    case 1:
                        if (pos.y < size - 2) {
                            pos.y++;
                        }
                        break;
                    case 2:
                        if (pos.x < size - 2) {
                            pos.x++;
                        }
                        break;
                    case 3:
                        if (pos.y > 1) {
                            pos.y--;
                        }
                        break;
                }
                System.out.println(pos.toString());
                //if you are now on the edge/in a trail, you're dead
                int square = board.grid[pos.x][pos.y];
                if (pos.x == 0 || pos.x == size - 1 || pos.y == 0 || pos.y == size - 1 || square == 2 || square == 3) {

                    System.out.println("Oops! " + b.id + " crashed!");
                    killBike(b);
                }
                board.grid[pos.x][pos.y] = 1;
                System.out.println("Moved bike " + b.id + " to position " + pos.toString());
            }
        }
    }

    private void killBike(BikeContainer toKill) {
        //gameLog.KillBike(bc.bike.bikeId);

        for (BikeContainer b : bikes) {
            if (b.id == toKill.id) {

                bikes.remove(b);
                System.out.println("Removing bike from array...");
            }
        }
        for (Tuple pos : toKill.trail) {//clean the board back up, remove the dead trails
            board.grid[pos.x][pos.y] = 0;
        }
        System.out.println("Killed bike " + toKill.id + " at position " + toKill.currentPosition.toString());
    }

    public Tuple[] run(int n) {
        System.out.println(this.name);
        int size = this.size;
        Bike[] testBikes = new Bike[numStartingBikes];
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

            AbstractGameEngine currentGame = new AbstractGameEngine(i, size, testBikes);
            while (currentGame.bikes.size() > 1) {//sometimes a bike will die and the length of the arraylist will decrease
                //System.out.println("run5");
                currentGame.update();
            }

            //there is a winner!
            winner = currentGame.bikes.get(0).bike.bikeId;//there is only one bike left in the arraylist
            System.out.println("The winner is " + winner);
            //the tuples are (id, times won)
            for (Tuple tuple : scoreboard) {
                System.out.println(tuple.toString());
                if (tuple.x == winner) {
                    scoreboard[winner].y++;
                }
            }
        }
        //gameLog.runResults(testBikes);

        return scoreboard;
    }

}
