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

    Tuple nextPosition(Tuple pos, int dir) {
        int col = pos.x;
        int row = pos.y;
        switch (dir) {
            case LightCycle.LEFT:
                return new Tuple(col - 1, row);
            case LightCycle.RIGHT:
                return new Tuple(col + 1, row);
            case LightCycle.UP:
                return new Tuple(col, row + 1);
            case LightCycle.DOWN:
                return new Tuple(col, row - 1);
        }
        return null;
    }

    boolean isValid(Tuple pos) {
        int col = pos.x;
        int row = pos.y;
        return col >= 0 && col < cols & row >= 0 && row < rows;
    }

    boolean isValid(Tuple pos, int dir) {
        Tuple nextPos = nextPosition(pos, dir);
        return (nextPos.x > cols || nextPos.x < 0 || nextPos.y > rows || nextPos.y < 0);
    }

    //these are the methods you should use to implement bikes
    //it is recommended to use your direction for anything more than the most basic bikes
    public boolean isOccupied(int col, int row) {
        if (!isValid(new Tuple(col, row))) {
            return true;
        }
        return grid[col][row] == 0;
    }

    public boolean isOccupied(int col, int row, int dir) {
        Tuple pos = nextPosition(new Tuple(col, row), dir);

        if (!isValid(pos)) {
            return true;
        }

        return grid[pos.x][pos.y] == 0;
    }

    //you can either use integer col and row or Tuple position as parameters
    public boolean isOccupied(Tuple pos) {
        if (!isValid(pos)) {
            return true;
        }
        return grid[pos.x][pos.y] == 0;
    }

    public boolean isOccupied(Tuple pos, int dir) {
        Tuple nextPos = nextPosition(pos, dir);

        if (!isValid(nextPos)) {
            return true;
        }

        return grid[nextPos.x][nextPos.y] == 0;
    }
}
