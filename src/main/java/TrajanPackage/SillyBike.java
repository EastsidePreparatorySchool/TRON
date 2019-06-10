/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrajanPackage;

import org.eastsideprep.bikes.*;
import eastsideprep.org.troncommon.Tuple;
import java.util.Random;
import org.eastsideprep.enginePackage.*;

/**
 *
 * @author pkavounas
 */
public class SillyBike extends Bike {

    public SillyBike(int id, Tuple p) {
        super(id, p);
    }

    @Override
//BIKE GOES IN DIRECTION OF UNOCCUPIED SPACE
    public int getDirection(Grid grid, int col, int row, int currentDir) {
        if (grid.isOccupied(col, row, 1) && grid.isOccupied(col, row, 2) && grid.isOccupied(col, row, 3)) {
            return Bike.UP;
        }
        if (grid.isOccupied(col, row, 0) && grid.isOccupied(col, row, 1) && grid.isOccupied(col, row, 3)) {
            return Bike.DOWN;
        }
        if (grid.isOccupied(col, row, 0) && grid.isOccupied(col, row, 1) && grid.isOccupied(col, row, 2)) {
            return Bike.LEFT;
        }
        if (grid.isOccupied(col, row, 0) && grid.isOccupied(col, row, 2) && grid.isOccupied(col, row, 3)) {
            return Bike.RIGHT;
        }

//BIKE PICKS RANDOM DIRECTION IF SPACE AHEAD, LEFT, AND RIGHT OF BIKE ARE OPEN
        if (!grid.isOccupied(col, row, 0) && !grid.isOccupied(col, row, 1) && !grid.isOccupied(col, row, 3)) {
            int rand1[] = new int[3];
            rand1[0] = Bike.UP;
            rand1[1] = Bike.RIGHT;
            rand1[2] = Bike.LEFT;

            int randDir1 = new Random().nextInt(rand1.length);
            return rand1[randDir1];

        }
        if (!grid.isOccupied(col, row, 0) && !grid.isOccupied(col, row, 1) && !grid.isOccupied(col, row, 2)) {
            int rand2[] = new int[3];
            rand2[0] = Bike.UP;
            rand2[1] = Bike.RIGHT;
            rand2[2] = Bike.DOWN;

            int randDir2 = new Random().nextInt(rand2.length);
            return rand2[randDir2];
        }
        if (!grid.isOccupied(col, row, 1) && !grid.isOccupied(col, row, 2) && !grid.isOccupied(col, row, 3)) {
            int rand3[] = new int[3];
            rand3[0] = Bike.RIGHT;
            rand3[1] = Bike.DOWN;
            rand3[2] = Bike.LEFT;

            int randDir3 = new Random().nextInt(rand3.length);
            return rand3[randDir3];
        }
        if (!grid.isOccupied(col, row, 0) && !grid.isOccupied(col, row, 2) && !grid.isOccupied(col, row, 3)) {
            int rand4[] = new int[3];
            rand4[0] = Bike.UP;
            rand4[1] = Bike.DOWN;
            rand4[2] = Bike.LEFT;

            int randDir4 = new Random().nextInt(rand4.length);
            return rand4[randDir4];
        }

        return Bike.UP;
    }

}
