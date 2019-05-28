/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.enginePackage;

import java.lang.reflect.Field;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author gmein
 */
public class Grid extends GridPane {

    StackPane[][] grid;
    boolean[][] occupied;
    int cols;
    int rows;

    Grid(int columns, int rows) {
        this.cols = columns;
        this.rows = rows;
        this.grid = new StackPane[this.cols][this.rows];
        this.occupied = new boolean[this.cols][this.rows];

        this.setHgap(2.0);
        this.setVgap(2.0);
        this.setMinSize(cols * 5 + (cols - 1) * 2, rows * 5 + (rows - 1) * 2);
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                StackPane sp = new StackPane();
                Rectangle r = new Rectangle(5, 5);
                r.setFill(Color.LIGHTBLUE);
                sp.getChildren().add(r);
                this.add(sp, col, row);
                grid[col][row] = sp;
            }
        }
        this.layout();
    }

    int nextCol(int col, int dir) {
        switch (dir) {
            case LightCycle.LEFT:
                return col - 1;
            case LightCycle.RIGHT:
                return col + 1;
        }
        return col;
    }

    int nextRow(int row, int dir) {
        switch (dir) {
            case LightCycle.UP:
                return row - 1;
            case LightCycle.DOWN:
                return row + 1;
        }
        return row;
    }

    boolean isValid(int col, int row) {
        return col >= 0 && col < cols & row >= 0 && row < rows;
    }

    boolean isValid(int col, int row, int dir) {
        col = nextCol(col, dir);
        row = nextRow(row, dir);
        return isValid(col, row);
    }

    boolean isOccupied(int col, int row, int dir) {
        if (!isValid(col, row, dir)) {
            return true;
        }
        col = nextCol(col, dir);
        row = nextRow(row, dir);
        return occupied[col][row];
    }

    boolean isOccupied(int col, int row) {
        if (!isValid(col, row)) {
            return true;
        }
        return occupied[col][row];
    }

    void set(int col, int row, Color c) {
        Rectangle r = (Rectangle) grid[col][row].getChildren().get(0);
        r.setFill(c);
        this.occupied[col][row] = true;
    }
}
