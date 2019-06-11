package TrajanPackage;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tespelien
 */
public abstract class Bike {

    //a bike only knows its direction and starting position, otherwise it could cheat
    int bikeId;
    int direction;
    Tuple startingPosition;

    public Bike(int id, Tuple p) {
        this.bikeId = id;
        this.startingPosition = p;
    }
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    // you must override this:
    public abstract int getDirection(Grid grid, int col, int row, int currentDir);

    void noteOtherCycle(int col, int row, int dir) {
        // intentionally left blank. 
        // Override it if you want to do something with it!
    }
}
