
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
public class AbstractGame {

    private final int size = 250;
    private int[][] board = new int[size][size];
    //each square on the board will have one of 3 ints:
    //  0 for empty square
    //  1 for bike
    //  2 for wall

    private void init() {
        //create 4 bikes at random positions
        Random rand = new Random();
        int x, y;
        x = rand.nextInt(250);
        y = rand.nextInt(250);
        Bike red = new Bike(x, y)
        );
        board[x][y] = 1;
        x = rand.nextInt(250);
        y = rand.nextInt(250);
        Bike blue = new Bike(x, y)
        );
        board[x][y] = 1;
        x = rand.nextInt(250);
        y = rand.nextInt(250);
        Bike green = new Bike(x, y)
        );
        board[x][y] = 1;
        
        

    }

    private void update() {

        //for each bike:
        //  take directions from container
        //  add directions to each position
        //  update trails
        //  check if this bike is dead
        //      if so kill it AND its trail
    }
}
