/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eastsideprep.enginePackage;

import javafx.scene.paint.Color;

/**
 *
 * @author gmein
 */
public class CycleContainer {

    LightCycle cycle;
    int col;
    int row;
    int direction;
    boolean alive = true;
    Color color;

    CycleContainer(LightCycle c, Color co, int col, int row, int dir) {
        this.cycle = c;
        this.color = co;
        this.alive = true;
        this.col = col;
        this.row = row;
        this.direction = dir;
        System.out.println(Util.nameFromColor(this.color) + ": " + this.cycle.getClass().getName());
    }

    void kill() {
        this.alive = false;
    }

    boolean isAlive() {
        return this.alive;

    }

    void move(Grid g) {
        this.direction = cycle.getDirection(g, col, row, direction);
        if (this.direction < 0 || this.direction > 3) {
            this.kill();
            return;
        }
        if (!g.isValid(this.col, this.row, this.direction)
                || g.isOccupied(this.col, this.row, this.direction)) {
            this.kill();
            return;
        }

        this.col = g.nextCol(this.col, this.direction);
        this.row = g.nextRow(this.row, this.direction);
        g.set(this.col, this.row, this.color);
    }

    void mark(Grid grid) {
        grid.set(this.col, this.row, this.color);

    }
}
