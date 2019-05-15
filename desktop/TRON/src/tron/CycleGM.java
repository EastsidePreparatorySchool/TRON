/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

/**
 *
 * @author gmein
 */
public class CycleGM extends LightCycle{

    @Override
    int getDirection(Grid grid, int col, int row, int currentDir) {
        // strategy: avoid the edge of board and other lines
        return LightCycle.DOWN;
    }
    
}
