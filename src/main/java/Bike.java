/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tespelien
 */
public class Bike {

    //a bike only knows its direction and starting position, otherwise it could cheat
    int direction;
    int[] startingPosition = new int[2];

    Bike(int x, int y) {
        startingPosition[0] = x;
        startingPosition[1] = y;
    }
}
