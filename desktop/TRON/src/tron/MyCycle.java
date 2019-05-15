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
public class MyCycle extends LightCycle{

    @Override
    int getDirection(Grid grid, int col, int row, int currentDir) {
        if (!grid.isValid(col, row, currentDir)) {
            
        }
        
        if (grid.isOccupied(col, row, currentDir)){
            
        }
        
        return currentDir;
    }
    
}
