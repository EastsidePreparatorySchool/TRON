/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.enginePackage;

import eastsideprep.org.troncommon.*;
import org.eastsideprep.*;

/**
 *
 * @author gmein
 */
public class Grid {

    int[][] grid;
    int rows = grid.length;
    int cols = grid[0].length;
    

    Grid(int columns, int rows) {
        this.cols = columns;
        this.rows = rows;
    }

    Tuple nextPosition(int row, int col, int dir) {
        switch (dir) {
            case LightCycle.LEFT:
                return new Tuple(row, col - 1);
            case LightCycle.RIGHT:
                return new Tuple(row, col + 1);
            case LightCycle.UP:
                return new Tuple(row - 1, col);
            case LightCycle.DOWN:
                return new Tuple(row + 1, col);
        }
        return null;
    }

    boolean isValid(int col, int row) {
        return col >= 0 && col < cols & row >= 0 && row < rows;
    }
    
    boolean isValid(int col, int row, int dir){
        Tuple pos = nextPosition(col, row, dir);
        return (pos.x>cols || pos.x<0 || pos.y>rows || pos.y<0);
        
    }
    boolean isOccupied(int col, int row, int dir) {
        if (!isValid(col, row, dir)) {
            return true;
        }
        Tuple pos = nextPosition(col, row, dir);
        
        return grid[pos.x][pos.y]==0;
    }

    boolean isOccupied(int col, int row) {
        if (!isValid(col, row)) {
            return true;
        }
        return (grid[col, row]==0);
    }
    

}
