/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.bikes;

import eastsideprep.org.troncommon.Tuple;
import org.eastsideprep.enginePackage.Grid;
import org.eastsideprep.enginePackage.*;

/**
 *
 * @author pkavounas
 */
public class BasicBike extends Bike {

    public BasicBike(int s, Tuple p) {
        super(s, p);
    }
     @Override

    public int getDirection(Grid grid, int col, int row, int currentDir) {
        
        //BIKE GOES IN DIRECTION OF UNOCCUPIED SPACE
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
        

        return Bike.UP;
    }
}
