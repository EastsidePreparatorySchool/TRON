/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;
/**
 *
 * @author subif
 */
public class CycleSLMG extends LightCycle {
    @Override
    int getDirection(Grid grid, int col, int row, int currentDir) {
        switch (currentDir) {
            case 0:
                
                if (!grid.isOccupied(col, row, 1)) {
                    return 1;
                }else if(!grid.isOccupied(col, row, 0)){
                    return 0;
                }else if(!grid.isOccupied(col, row, 3)){
                    return 3;
                }else{
                    return 2;
                }
            case 1:
                
                
                 if (!grid.isOccupied(col, row, 1)) {
                     //System.out.println(currentDir);
                    return 1;
                }else if(!grid.isOccupied(col, row, 2)){
                     System.out.println("bob");
                    return 2;
                }else if(!grid.isOccupied(col, row, 0)){
                    return 0;
                }else{
                     System.out.println("bill");
                    return 3;
                }
                
                
            case 2:
                if (!grid.isOccupied(col, row, 2)) {
                    return 2;
                }else if(!grid.isOccupied(col, row, 3)){
                    return 3;
                }else if(!grid.isOccupied(col, row, 1)){
                    return 1;
                }else{
                    return 0;
                }
                
            case 3:
                if (!grid.isOccupied(col, row, 3)) {
                    return 3;
                }else if(!grid.isOccupied(col, row, 0)){
                    return 0;
                }else if(!grid.isOccupied(col, row, 2)){
                    return 2;
                }else{
                    return 1;
                }
            default:
                return 0;
        }
     
    }
}

