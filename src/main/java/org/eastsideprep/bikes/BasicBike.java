/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.bikes;

import java.util.Random;
import org.eastsideprep.enginePackage.Grid;
import org.eastsideprep.enginePackage.LightCycle;

/**
 *
 * @author pkavounas
 */
public class BasicBike extends LightCycle {
     @Override

    public int getDirection(Grid grid, int col, int row, int currentDir) {
        
        //BIKE GOES IN DIRECTION OF UNOCCUPIED SPACE
        if (grid.isOccupied(col, row, 1) && grid.isOccupied(col, row, 2) && grid.isOccupied(col, row, 3)) {
            return LightCycle.UP;
        }
        if (grid.isOccupied(col, row, 0) && grid.isOccupied(col, row, 1) && grid.isOccupied(col, row, 3)) {
            return LightCycle.DOWN;
        }
        if (grid.isOccupied(col, row, 0) && grid.isOccupied(col, row, 1) && grid.isOccupied(col, row, 2)) {
            return LightCycle.LEFT;
        }
        if (grid.isOccupied(col, row, 0) && grid.isOccupied(col, row, 2) && grid.isOccupied(col, row, 3)) {
            return LightCycle.RIGHT;
        }
        

        return LightCycle.UP;
    }
}
