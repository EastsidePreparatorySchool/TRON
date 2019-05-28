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
public abstract class LightCycle {
    static final int UP = 0;
    static final int RIGHT = 1; 
    static final int DOWN = 2;
    static final int LEFT = 3;
    
    // you must override this:
    abstract int getDirection(Grid grid, int col, int row, int currentDir); 

    void noteOtherCycle(Color co, int col, int row, int dir){
        // intentionally left blank. 
        // Override it if you want to do something with it!
    }
}
